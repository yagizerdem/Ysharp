package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.lexer.Cursor;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.parser.Parser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Loader {

    private final GraphNode root;
    private final Hashtable<String, GraphNode> parsedModules;
    private final Set<String> visiting = new HashSet<>();

    public Loader(Parser.Program program, String mainModulePath) {
        this.root = new GraphNode(program, mainModulePath);
        this.parsedModules = new Hashtable<>();
    }

    private static class GraphNode {
        public final Parser.Program program;
        public final String modulePath;
        public final List<GraphNode> neighbors;
        public List<String> exports;
        public Environment env;

        public GraphNode(Parser.Program program,
                         String modulePath,
                         List<GraphNode> neighbors){
            this.program = program;
            this.modulePath = modulePath;
            this.neighbors = neighbors;
            exports = new ArrayList<>();
        }

        public GraphNode(Parser.Program program,
                         String modulePath){
            this.program = program;
            this.modulePath = modulePath;
            this.neighbors = new ArrayList<>();
            exports = new ArrayList<>();
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof GraphNode)) return false;
            return this.modulePath.equals(((GraphNode)obj).modulePath);
        }


        @Override
        public int hashCode() {
            return modulePath.hashCode();
        }

    }

    public Hashtable<String, Variable> loadEnv() throws Exception {
        List<String> importPaths = this.root.program.
                useDeclaration.stream().map(m -> this.resolvePath(Paths.get(this.root.modulePath).getParent().toString(), m)).toList();

        importPaths.forEach(this::ensurePathExist);

        for(String curModulePath : importPaths) {
            GraphNode newNode = buildDepGraph(curModulePath);
            if(newNode != null) {
                this.root.neighbors.add(newNode);
            }
        }

        List<GraphNode> sorted = topologicalSort();

        for(int i = 0; i < sorted.size(); i++) {
            GraphNode curNode = sorted.get(i);

            if(curNode.equals(this.root)) continue;

            Interpreter interpreter = new Interpreter();

            if(!curNode.neighbors.isEmpty()) {
                for(GraphNode neighbor : curNode.neighbors) {
                    neighbor.exports.forEach(e -> {
                        if(!interpreter.global.existsAt(0, e)) {
                            interpreter.global.define(e, neighbor.env.getAt(0, e));
                        }
                    });
                }
            }

            interpreter.interpret(curNode.program.program);
            if(interpreter.hadErrors()) {
                StdIO.printStdErr(interpreter.errors);
                System.exit(1);
            }

            curNode.exports = interpreter.exports;
            curNode.env = interpreter.global; // only global env can be exported
        }

        Hashtable<String, Variable> exportRegistry = new Hashtable<>();

        if(!this.root.neighbors.isEmpty()) {
            for(GraphNode neighbor : this.root.neighbors) {
                neighbor.exports.forEach(e -> {
                    exportRegistry.put(e, neighbor.env.getValue(e));
                });
            }
        }

        return exportRegistry;
    }

    private GraphNode buildDepGraph(String curModulePath)  throws Exception {
        if(parsedModules.containsKey(curModulePath)) {
            return parsedModules.get(curModulePath);
        }

        if (visiting.contains(curModulePath)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    "Circular dependency detected: " + curModulePath
            );
        }

        visiting.add(curModulePath);

        Interpreter interpreter = new Interpreter();
        Registery.register(interpreter);

        String moduleContent = new String(Files.readAllBytes(Paths.get(curModulePath)));

        Preprocess preprocess = new Preprocess();
        List<Cursor.Pchar> buf = preprocess.process(moduleContent);
        if(preprocess.hadErrors()){
            StdIO.printStdErr(preprocess.errors);
            return null;
        }

        Lexer lexer = new Lexer(buf);
        var stream = lexer.scanTokens();
        if(lexer.hadErrors()) {
            StdIO.printStdErr(lexer.errors);
            return null;
        }

        Parser parser = new Parser(stream);
        Parser.Program program = parser.parse();
        if(parser.hadErrors()) {
            StdIO.printStdErr(parser.errors);
            return null;
        }


        List<String> importPaths = program.
                useDeclaration.stream().map(useDeclaration -> this.resolvePath(Paths.get(curModulePath).getParent().toString(), useDeclaration)).toList();

        GraphNode node = new GraphNode(program, curModulePath);

        for(String importPath : importPaths) {
            node.neighbors.add(buildDepGraph(importPath));
        }

        parsedModules.put(curModulePath, node);

        visiting.remove(curModulePath);

        return node;
    }

    private String resolvePath(String curModulePath, String moduleImport) {
        Path path = Paths.get(curModulePath);
        return path.resolve(moduleImport).normalize().toString();
    }

    private void ensurePathExist(String path) {
        Path p = Paths.get(path);

        if (!Files.exists(p)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    "Path does not exist: " + path
            );
        }
    }

    private List<GraphNode> topologicalSort() {
        List<GraphNode> sorted = new ArrayList<>();
        topologicalSortRecursive(this.root, sorted);
        return sorted;
    }

    private void topologicalSortRecursive(GraphNode curNode, List<GraphNode> list) {
        if(curNode.neighbors.isEmpty()) {
            if(!list.contains(curNode)) {
                list.add(curNode);
            }
            return;
        }
        for(GraphNode n : curNode.neighbors) {
            topologicalSortRecursive(n, list);
        }
        if(!list.contains(curNode)) {
            list.add(curNode);
        }
    }

}
