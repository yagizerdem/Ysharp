package ysharp.treewalk.evaluator.Native.IO.File.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.IO.yIO;

import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

public class ReadFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {
        requireArity(arguments, arity(), getFnName());

        String pathText = requireString(arguments.getFirst(), getFnName(), 1);
        Path targetPath = yIO.resolvePath(interpreter, pathText);

        try {
            String content = Files.readString(targetPath);
            return new Variable.Variant(new yString.yStringInstance(content));
        }
        catch (NoSuchFileException ex) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "File.read: file not found: " + targetPath
            );
        }
        catch (AccessDeniedException ex) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "File.read: access denied: " + targetPath
            );
        }
        catch (Exception ex) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "File.read: " + ex.getMessage()
            );
        }
    }

    @Override
    public String getFnName() {
        return "read";
    }
}