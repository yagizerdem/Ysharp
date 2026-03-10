package ysharp.evaluator;

import ysharp.SD;
import ysharp.parser.Parser;

import javax.swing.*;
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

    }

    public void loadEnv() {
        List<String> importPaths = this.root.program.
                useDeclaration.stream().map(this::resolvePath).toList();




    }


    private String resolvePath(String moduleImport) {
        Path path = Paths.get(this.root.modulePath);
        return path.resolve(moduleImport).normalize().toString();
    }

}
