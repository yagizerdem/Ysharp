package ysharp.treewalk.evaluator.Native.IO.File.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.nio.file.*;
import java.util.List;
import ysharp.treewalk.evaluator.Native.IO.yIO;

public class WriteFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 2;
    }


    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {
        try {
            requireArity(arguments, arity(), getFnName());

            String path = requireString(arguments.get(0), getFnName(), 1);
            String content = requireString(arguments.get(1), getFnName(), 2);

            Path targetPath = yIO.resolvePath(interpreter, path);

            Files.writeString(
                    targetPath,
                    content,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );

            return new Variable.Variant(null);
        }
        catch (AccessDeniedException ex) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "File.write: access denied: " + ex.getMessage()
            );
        }
        catch (NoSuchFileException ex) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "File.write: parent directory does not exist: " + ex.getMessage()
            );
        }
        catch (Exception ex) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "File.write: " + ex.getMessage()
            );
        }
    }

    @Override
    public String getFnName() {
        return "write";
    }
}