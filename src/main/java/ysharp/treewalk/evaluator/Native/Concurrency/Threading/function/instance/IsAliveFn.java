package ysharp.treewalk.evaluator.Native.Concurrency.Threading.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Concurrency.Threading.yThread;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class IsAliveFn extends Function.NativeFunction {
    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
        yThread.yThreadInstance thread = yThread.requireThreadThis(interpreter);
        boolean flag = thread.getJavaThread().isAlive();

        return new Variable.Variant(flag);
    }

    @Override
    public String getFnName() {
        return "isAlive";
    }
}
