package ysharp.evaluator.Native.Collections.LinkedHashMap.function.instance;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.LinkedHashMap.yLinkedHashMap;
import ysharp.evaluator.Variable;

import java.util.ArrayList;
import java.util.List;

public class ValuesFn extends Function.NativeFunction {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpError {

        yLinkedHashMap.yLinkedHashMapInstance lhm = yLinkedHashMap.requireLinkedHashMapThis(interpreter);

        ArrayList<Variable.Variant> list = new ArrayList<>(lhm.data.values());

        yArray.yArrayInstance array = new yArray.yArrayInstance(list);

        return new Variable.Variant(array);
    }

    @Override
    public String getFnName() {
        return "values";
    }
}
