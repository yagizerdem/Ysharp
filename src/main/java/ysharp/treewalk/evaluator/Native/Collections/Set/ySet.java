package ysharp.treewalk.evaluator.Native.Collections.Set;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.Array.function.instance.SetFn;
import ysharp.treewalk.evaluator.Native.Collections.Set.instance.*;

import java.util.*;

public class ySet {

    public static ySetInstance requireSetThis(Interpreter interpreter) {

        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method called without valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof ySetInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method can only be called on Set objects."
            );
        }

        return (ySetInstance) obj;
    }

    public static RuntimeObject ySet_Instance_Prototype;

    static {

        ySet_Instance_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() { return true; }

            @Override
            public String getType() { return "__Set__"; }

            @Override
            public String toString() {
                return "<prototype:Set>";
            }
        };
        ySet_Instance_Prototype.prototype = yClass.ClassPrototype;

        // set.toString()
        ySet_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        // set.add("data")
        ySet_Instance_Prototype.RegisterNativeFn(new AddFn());
        // set.remove("data")
        ySet_Instance_Prototype.RegisterNativeFn(new SetFn());
        // set.contains("data")
        ySet_Instance_Prototype.RegisterNativeFn(new ContainsFn());
        // set.clear()
        ySet_Instance_Prototype.RegisterNativeFn(new ClearFn());
        // set.size()
        ySet_Instance_Prototype.RegisterNativeFn(new SizeFn());
        // set.clone()
        ySet_Instance_Prototype.RegisterNativeFn(new CloneFn());
        // set.union(other)
        ySet_Instance_Prototype.RegisterNativeFn(new UnionFn());
        // set.intersection(other)
        ySet_Instance_Prototype.RegisterNativeFn(new IntersectionFn());
        // set.difference(other)
        ySet_Instance_Prototype.RegisterNativeFn(new DifferenceFn());
        // set.isSubsetOf(other)
        ySet_Instance_Prototype.RegisterNativeFn(new IsSubsetFn());
        // set.empty()
        ySet_Instance_Prototype.RegisterNativeFn(new EmptyFn());
        // set.isSupersetOf(other)
        ySet_Instance_Prototype.RegisterNativeFn(new IsSupersetFn());
        // set.equals(other)
        ySet_Instance_Prototype.RegisterNativeFn(new EqualsFn());
    }


    public static class ySetInstance extends yClass.ClassObjectInstance {

        public final HashSet<Variable.Variant> data;

        public ySetInstance() {
            this.data = new HashSet<>();
            this.prototype = ySet_Instance_Prototype;
        }

        public ySetInstance(HashSet<Variable.Variant> data) {
            this.data = data;
            this.prototype = ySet_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() { return true; }

        @Override
        public String getType() { return "Set"; }

        @Override
        public String toString() {
            return "<instance:Set>";
        }
    }

    public static class ySetClass extends yClass.SealedClassObject {

        @Override
        public int arity() { return 0; }

        @Override
        public Variable.Variant call(
                Interpreter interpreter,
                List<Variable.Variant> arguments)
                throws YsharpException {

            return new Variable.Variant(new ySetInstance());
        }

        @Override
        public String getClassName() {
            return "Set";
        }

        @Override
        public String getType() {
            return "Set";
        }

        @Override
        public String toString() {
            return "<class:Set>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        ySetClass ctor = new ySetClass();

        interpreter.defineGlobal(
                ctor.getClassName(),
                new Variable(
                        new Variable.Variant(ctor),
                        false,
                        ctor.getType()));
    }
}