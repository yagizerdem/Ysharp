package ysharp.treewalk.evaluator.Native.Collections.Stack;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.Stack.function.instance.*;
import ysharp.treewalk.evaluator.Native.Collections.yVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class yStack {

    // helper
    public static yStack.yStackInstance requireStackThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yStackInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'stack' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return  (yStackInstance) obj;
    }

    public static RuntimeObject yStack_Instance_Prototype;

    static {
        yStack_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__Stack__";
            }

            @Override
            public String toString() {
                return "<prototype:Stack>";
            }
        };
        yStack_Instance_Prototype.prototype = yVector.Vector_Instance_Prototype;

        // stack.toString()
        yStack_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // stack.add(value)
        yStack_Instance_Prototype.RegisterNativeFn(new AddFn(), Arrays.asList("push")); // push alias
        // stack.pop()
        yStack_Instance_Prototype.RegisterNativeFn(new PopFn());
        // stack.peek()
        yStack_Instance_Prototype.RegisterNativeFn(new PeekFn(), Arrays.asList("top"));
        // stack.empty()
        yStack_Instance_Prototype.RegisterNativeFn(new EmptyFn(), Arrays.asList("isEmpty"));
        // stack.search(element)
        yStack_Instance_Prototype.RegisterNativeFn(new SearchFn());
        // stack.clear()
        yStack_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // stack.clone()
        yStack_Instance_Prototype.RegisterNativeFn(new CloneFn());
        // stack.contains(value)
        yStack_Instance_Prototype.RegisterNativeFn(new ContainsFn());
        // stack.reverse()
        yStack_Instance_Prototype.RegisterNativeFn(new ReverseFn());
        // stack.size()
        yStack_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // stack.toArray()
        yStack_Instance_Prototype.RegisterNativeFn(new ToArrayFn(), Arrays.asList("toList"));
        // stack.asQueryable()
        yStack_Instance_Prototype.RegisterNativeFn(new AsQueryableFn());
        // stack.peekOrNull()
        yStack_Instance_Prototype.RegisterNativeFn(new PeekOrNullFn());
        // stack.addAll()
        yStack_Instance_Prototype.RegisterNativeFn(new AddAllFn());
    }

    public static class yStackInstance extends yClass.ClassObjectInstance implements yVector.IVector {

        public final Stack<Variable.Variant> data;

        public yStackInstance(Stack<Variable.Variant> data)  {
            this.data = data;
            this.prototype = yStack_Instance_Prototype;
        }

        public yStackInstance() {
            this.data = new Stack<>();
            this.prototype = yStack_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Stack";
        }

        @Override
        public String toString() {
            return "<instance:Stack>";
        }

        @Override
        public List<Variable.Variant> getData() {
            return new ArrayList<>(this.data);
        }

        @Override
        public Object getNativeJavaObject() {
            Stack<Object> nativeStack = new Stack<>();
            for(int i = 0; i < this.data.size(); i++) {
                nativeStack.add(i, this.data.get(i).asJavaNative());
            }
            return nativeStack;
        }
    }

    public static class yStackClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
            Stack<Variable.Variant> value = new Stack<>();
            yStack.yStackInstance newStack = new yStack.yStackInstance(value);

            return new Variable.Variant(newStack);
        }

        @Override
        public String getClassName() {
            return "Stack";
        }

        @Override
        public String getType() {
            return "Stack";
        }

        @Override
        public String toString() {
            return "<class:Stack>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yStack.yStackClass stackCtor = new yStack.yStackClass();
        Variable.Variant variant = new Variable.Variant(stackCtor);
        Variable var = new Variable(variant, false, stackCtor.getType());
        interpreter.defineGlobal(stackCtor.getClassName(), var);
    }
}
