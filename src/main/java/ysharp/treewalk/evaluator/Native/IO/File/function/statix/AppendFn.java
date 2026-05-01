package ysharp.treewalk.evaluator.Native.IO.File.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import ysharp.treewalk.evaluator.Native.IO.yIO;

public class AppendFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        String pathText = requireString(arguments.get(0), getFnName(), 1);
        String content = requireString(arguments.get(1), getFnName(), 2);

        Path targetPath = yIO.resolvePath(interpreter, pathText);

        try {
            Files.writeString(
                    targetPath,
                    content,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND,
                    StandardOpenOption.WRITE
            );

            return new Variable.Variant(null);
        }
        catch (AccessDeniedException ex) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "File.append: access denied: " + targetPath
            );
        }
        catch (NoSuchFileException ex) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "File.append: parent directory does not exist: " + targetPath
            );
        }
        catch (Exception ex) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "File.append: " + ex.getMessage()
            );
        }
    }

    @Override
    public String getFnName() {
        return "append";
    }
}