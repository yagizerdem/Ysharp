package ysharp.treewalk.evaluator.Native.Concurrency.Threading.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Concurrency.Threading.yThread;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class JoinFn extends Function.NativeFunction {
    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
        try {
            yThread.yThreadInstance thread = yThread.requireThreadThis(interpreter);
            thread.getJavaThread().join();
        }catch (InterruptedException interruptedException) {
            throw new YsharpException(YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    interruptedException.getMessage());
        }

        return new Variable.Variant(null);
    }

    @Override
    public String getFnName() {
        return "join";
    }
}
