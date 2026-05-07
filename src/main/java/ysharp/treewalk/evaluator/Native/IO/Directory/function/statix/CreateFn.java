package ysharp.treewalk.evaluator.Native.IO.Directory.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.IO.yIO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CreateFn extends Function.NativeFunction implements Callable {

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

        try {
            Files.createDirectory(targetPath);
            return new Variable.Variant(null);

        } catch (Exception e) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Failed to create directory '" + pathText + "': " + e.getMessage()
            );
        }
    }

    @Override
    public String getFnName() {
        return "create";
    }
}