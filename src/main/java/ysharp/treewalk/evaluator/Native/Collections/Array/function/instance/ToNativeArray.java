package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpError;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class ToNativeArray extends Function.NativeFunction implements Callable {

    @Override
    public int arity() { return 0; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());
        ArrayList<Variable.Variant> list = array.data;

        Object[] nativeArray = new Object[list.size()];
        for(int i = 0; i < list.size(); i++) {
            nativeArray[i] =  list.get(i).asJavaNative();
        }

        return new Variable.Variant(nativeArray);
    }

    @Override
    public String getFnName() { return "toNativeArray"; }
}
