package ysharp.treewalk.evaluator.Native.Concurrency.Threading.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Concurrency.Threading.yThread;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;

public class CurrentFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        Thread current = Thread.currentThread();

        yThread.yThreadInstance instance = new yThread.yThreadInstance();
        instance.setJavaThread(current);

        return new Variable.Variant(instance);
    }

    @Override
    public String getFnName() {
        return "current";
    }
}