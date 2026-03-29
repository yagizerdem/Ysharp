package ysharp.evaluator.Native.Collections.Array.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Callable;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class ToNativeArrayList extends Function.NativeFunction implements Callable {

    @Override
    public int arity() { return 0; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        requireArity(arguments, arity(), getFnName());
        yArray.yArrayInstance array = yArray.requireArrayThis(interpreter, getFnName());
        ArrayList<Variable.Variant> list = array.data;

        ArrayList<Object> nativeArrayList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            nativeArrayList.add(list.get(i).asJavaNative());
        }

        return new Variable.Variant(nativeArrayList);
    }

    @Override
    public String getFnName() { return "toNativeArrayList"; }
}
