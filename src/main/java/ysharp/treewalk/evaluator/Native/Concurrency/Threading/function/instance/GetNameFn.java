package ysharp.treewalk.evaluator.Native.Concurrency.Threading.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Concurrency.Threading.yThread;
import ysharp.treewalk.evaluator.yString;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class GetNameFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        yThread.yThreadInstance thread = yThread.requireThreadThis(interpreter);

        Thread jt = thread.getJavaThread();

        if (jt == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Thread is not initialized."
            );
        }

        return new Variable.Variant(new yString.yStringInstance(jt.getName()));
    }

    @Override
    public String getFnName() {
        return "getName";
    }
}