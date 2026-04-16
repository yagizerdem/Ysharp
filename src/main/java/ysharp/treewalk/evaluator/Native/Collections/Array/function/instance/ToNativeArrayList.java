package ysharp.treewalk.evaluator.Native.Collections.Array.function.instance;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Array.yArray;
import ysharp.treewalk.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class ToNativeArrayList extends Function.NativeFunction implements Callable {

    @Override
    public int arity() { return 0; }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

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
