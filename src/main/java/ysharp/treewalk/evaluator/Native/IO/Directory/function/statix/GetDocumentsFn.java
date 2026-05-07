package ysharp.treewalk.evaluator.Native.IO.Directory.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.nio.file.Path;
import java.util.List;

public class GetDocumentsFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(
            Interpreter interpreter,
            List<Variable.Variant> arguments
    ) throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        String home = System.getProperty("user.home");
        return new Variable.Variant(Path.of(home, "Documents").toString());
    }

    @Override
    public String getFnName() {
        return "getDocuments";
    }
}