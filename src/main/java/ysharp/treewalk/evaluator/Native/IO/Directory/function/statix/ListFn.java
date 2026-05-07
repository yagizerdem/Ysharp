package ysharp.treewalk.evaluator.Native.IO.Directory.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.IO.yIO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ListFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(
            Interpreter interpreter,
            List<Variable.Variant> arguments
    ) throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        String pathText = requireString(arguments.getFirst(), getFnName(), 1);
        Path targetPath = yIO.resolvePath(interpreter, pathText);

        if (!Files.isDirectory(targetPath)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Path is not a directory: " + pathText
            );
        }

        try {
            List<String> result = new ArrayList<>();

            try (Stream<Path> stream = Files.list(targetPath)) {
                stream.forEach(path -> result.add(path.getFileName().toString()));
            }

            return new Variable.Variant(JavaObjectWrapper.wrap(result));

        } catch (Exception e) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Failed to list directory '" + pathText + "': " + e.getMessage()
            );
        }
    }

    @Override
    public String getFnName() {
        return "list";
    }
}