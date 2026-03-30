package ysharp.evaluator.Native.Collections.Stack.function.instance;

import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.Native.Collections.Stack.yStack;
import ysharp.evaluator.Variable;

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
