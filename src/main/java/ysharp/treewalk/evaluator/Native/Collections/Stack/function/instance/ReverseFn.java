package ysharp.treewalk.evaluator.Native.Collections.Stack.function.instance;

import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.Stack.yStack;
import ysharp.treewalk.evaluator.Variable;

import java.util.List;
import java.util.Stack;

public class ReverseFn extends Function.NativeFunction {

    @Override
    public int arity() { return 0; }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args) {
        yStack.yStackInstance stack = yStack.requireStackThis(interpreter, getFnName());

        Stack<Variable.Variant> reversed = new Stack<>();
        for (Variable.Variant v : stack.data) {
            reversed.addFirst(v);
        }

        return new Variable.Variant(new yStack.yStackInstance(reversed));
    }

    @Override
    public String getFnName() { return "reverse"; }
}
