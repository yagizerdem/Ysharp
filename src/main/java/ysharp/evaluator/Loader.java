package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.lexer.Cursor;
import ysharp.lexer.Lexer;
import ysharp.lexer.Preprocess;
import ysharp.parser.Parser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private final Parser.Program mainProgram;

    private final GraphNode root;


    public Loader(Parser.Program program, String mainModulePath) {
        this.mainProgram = program;
        this.root = new GraphNode(program, mainModulePath);
    }

    private static class GraphNode {
        public final Parser.Program program;
        public final String modulePath;
        public final List<GraphNode> neighbors;

        public GraphNode(Parser.Program program,
                         String modulePath,
                         List<GraphNode> neighbors){
            this.program = program;
            this.modulePath = modulePath;
            this.neighbors = neighbors;
        }

        public GraphNode(Parser.Program program,
                         String modulePath){
            this.program = program;
            this.modulePath = modulePath;
            this.neighbors = new ArrayList<>();
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

    public void loadEnv() throws Exception {
        List<String> importPaths = this.root.program.
                useDeclaration.stream().map(m -> this.resolvePath(Paths.get(this.root.modulePath).getParent().toString(), m)).toList();

        importPaths.forEach(this::ensurePathExist);

        for(String curModulePath : importPaths) {
            this.root.neighbors.add(buildDepGraph(curModulePath));
        }

        List<GraphNode> sorted = topologicalSort();

        int a = 10;
    }

    private GraphNode buildDepGraph(String curModulePath)  throws Exception {
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
