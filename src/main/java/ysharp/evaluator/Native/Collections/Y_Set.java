package ysharp.evaluator.Native.Collections;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.evaluator.Native.function.binding.BoundNativeFunction;
import ysharp.parser.TypeTag;

import java.util.*;

public class Y_Set {

    private static Y_SetObject requireSetThis(Interpreter interpreter) {

        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method called without valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof Y_SetObject)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "Method can only be called on Set objects."
            );
        }

        return (Y_SetObject) obj;
    }

    public static RuntimeObject Y_Set_Prototype;

    static {

        Y_Set_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() { return true; }

            @Override
            public String getType() { return "set_prototype"; }
        };

        // set.toString()
        class ToStringFn extends Function.NativeFunction {

            @Override
            public int arity() { return 0; }

            @Override
            public Variable.Variant call(
                    Interpreter interpreter,
                    List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_SetObject set = requireSetThis(interpreter);

                StringBuilder builder = new StringBuilder();
                builder.append("{ ");

                int i = 0;
                for (Variable.Variant element : set.data) {

                    if (element.value instanceof RuntimeObject) {
                        Variable toStringFn =
                                ((RuntimeObject) element.value).get("toString");

                        if (toStringFn != null &&
                                toStringFn.value.isNativeFunction()) {

                            BoundNativeFunction bound =
                                    new BoundNativeFunction(
                                            toStringFn.value.asNativeFunction(),
                                            element.asRuntimeObject(),
                                            "this");

                            builder.append(bound.call(
                                    interpreter,
                                    new ArrayList<>()));
                        } else {
                            builder.append("<object>");
                        }
                    } else {
                        builder.append(element.value);
                    }

                    if (i++ != set.data.size() - 1) {
                        builder.append(", ");
                    }
                }

                builder.append(" }");
                return new Variable.Variant(builder.toString());
            }

            @Override
            public String getFnName() { return "toString"; }
        }

        ToStringFn toString = new ToStringFn();
        Variable toStringVar = new Variable(
                new Variable.Variant(toString),
                true,
                TypeTag.OBJECT);
        Y_Set_Prototype.set(toString.getFnName(), toStringVar);

        // set.add("data")
        class AddFn extends Function.NativeFunction {

            @Override
            public int arity() { return 1; }

            @Override
            public Variable.Variant call(
                    Interpreter interpreter,
                    List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_SetObject set = requireSetThis(interpreter);
                set.data.add(arguments.get(0));

                return new Variable.Variant(set.data.size());
            }

            @Override
            public String getFnName() { return "add"; }
        }
        AddFn add = new AddFn();
        Variable addVar = new Variable(
                new Variable.Variant(add),
                true,
                TypeTag.OBJECT
        );
        Y_Set_Prototype.set(add.getFnName(), addVar);

        // set.remove("data")
        class RemoveFn extends Function.NativeFunction {

            @Override
            public int arity() { return 1; }

            @Override
            public Variable.Variant call(
                    Interpreter interpreter,
                    List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_SetObject set = requireSetThis(interpreter);

                boolean removed = set.data.remove(arguments.get(0));
                return new Variable.Variant(removed);
            }

            @Override
            public String getFnName() { return "remove"; }
        }

        RemoveFn remove = new RemoveFn();
        Variable removeVar = new Variable(
                new Variable.Variant(remove),
                true,
                TypeTag.OBJECT
        );
        Y_Set_Prototype.set(remove.getFnName(),removeVar);

        // set.contains("data")
        class ContainsFn extends Function.NativeFunction {

            @Override
            public int arity() { return 1; }

            @Override
            public Variable.Variant call(
                    Interpreter interpreter,
                    List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_SetObject set = requireSetThis(interpreter);
                return new Variable.Variant(
                        set.data.contains(arguments.get(0)));
            }

            @Override
            public String getFnName() { return "contains"; }
        }

        ContainsFn contains = new ContainsFn();
        Variable containsVar = new Variable(
                new Variable.Variant(contains),
                true,
                TypeTag.OBJECT
        );
        Y_Set_Prototype.set(contains.getFnName(), containsVar);

        // set.clear()
        class ClearFn extends Function.NativeFunction {

            @Override
            public int arity() { return 0; }

            @Override
            public Variable.Variant call(
                    Interpreter interpreter,
                    List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_SetObject set = requireSetThis(interpreter);
                set.data.clear();

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() { return "clear"; }
        }

        ClearFn clear = new ClearFn();
        Variable clearVar = new Variable(
                new Variable.Variant(clear),
                true,
                TypeTag.OBJECT
        );
        Y_Set_Prototype.set(clear.getFnName(), clearVar);

        // set.size()
        class SizeFn extends Function.NativeFunction {

            @Override
            public int arity() { return 0; }

            @Override
            public Variable.Variant call(
                    Interpreter interpreter,
                    List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_SetObject set = requireSetThis(interpreter);
                return new Variable.Variant(set.data.size());
            }

            @Override
            public String getFnName() { return "size"; }
        }

        SizeFn size = new SizeFn();
        Variable sizeVar = new Variable(
                new Variable.Variant(size),
                true,
                TypeTag.OBJECT
        );
        Y_Set_Prototype.set(size.getFnName(), sizeVar);

        // set.clone()
        class CloneFn extends Function.NativeFunction {

            @Override
            public int arity() { return 0; }

            @Override
            public Variable.Variant call(
                    Interpreter interpreter,
                    List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_SetObject set = requireSetThis(interpreter);

                // shallow copy
                HashSet<Variable.Variant> clonedData =
                        new HashSet<>(set.data);

                Y_SetObject newSet =
                        new Y_SetObject(clonedData);

                return new Variable.Variant(newSet);
            }

            @Override
            public String getFnName() { return "clone"; }
        }

        CloneFn clone = new CloneFn();
        Variable cloneVar = new Variable(
                new Variable.Variant(clone),
                true,
                TypeTag.OBJECT
        );
        Y_Set_Prototype.set(clone.getFnName(), cloneVar);

        // set.union(other)
        class UnionFn extends Function.NativeFunction {

            @Override
            public int arity() { return 1; }

            @Override
            public Variable.Variant call(
                    Interpreter interpreter,
                    List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_SetObject set = requireSetThis(interpreter);

                Variable.Variant otherVar = arguments.get(0);

                if (!otherVar.isRuntimeObject() ||
                        !(otherVar.asRuntimeObject() instanceof Y_SetObject)) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'union' argument must be a Set."
                    );
                }

                Y_SetObject other =
                        (Y_SetObject) otherVar.asRuntimeObject();

                HashSet<Variable.Variant> newData =
                        new HashSet<>(set.data);

                newData.addAll(other.data);

                return new Variable.Variant(
                        new Y_SetObject(newData)
                );
            }

            @Override
            public String getFnName() { return "union"; }
        }

        UnionFn union = new UnionFn();
        Variable unionVar = new Variable(
                new Variable.Variant(union),
                true,
                TypeTag.OBJECT
        );
        Y_Set_Prototype.set(union.getFnName(), unionVar);

        // set.intersection(other)
        class IntersectionFn extends Function.NativeFunction {

            @Override
            public int arity() { return 1; }

            @Override
            public Variable.Variant call(
                    Interpreter interpreter,
                    List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_SetObject set = requireSetThis(interpreter);

                Variable.Variant otherVar = arguments.get(0);

                if (!otherVar.isRuntimeObject() ||
                        !(otherVar.asRuntimeObject() instanceof Y_SetObject)) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'intersection' argument must be a Set."
                    );
                }

                Y_SetObject other =
                        (Y_SetObject) otherVar.asRuntimeObject();

                HashSet<Variable.Variant> newData =
                        new HashSet<>(set.data);

                newData.retainAll(other.data);

                return new Variable.Variant(
                        new Y_SetObject(newData)
                );
            }

            @Override
            public String getFnName() { return "intersection"; }
        }

        IntersectionFn intersection = new IntersectionFn();
        Variable intersectionVar = new Variable(
                new Variable.Variant(intersection),
                true,
                TypeTag.OBJECT
        );
        Y_Set_Prototype.set(intersection.getFnName(), intersectionVar);

        // set.difference(other)
        class DifferenceFn extends Function.NativeFunction {

            @Override
            public int arity() { return 1; }

            @Override
            public Variable.Variant call(
                    Interpreter interpreter,
                    List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_SetObject set = requireSetThis(interpreter);

                Variable.Variant otherVar = arguments.get(0);

                if (!otherVar.isRuntimeObject() ||
                        !(otherVar.asRuntimeObject() instanceof Y_SetObject)) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'difference' argument must be a Set."
                    );
                }

                Y_SetObject other =
                        (Y_SetObject) otherVar.asRuntimeObject();

                HashSet<Variable.Variant> newData =
                        new HashSet<>(set.data);

                newData.removeAll(other.data);

                return new Variable.Variant(
                        new Y_SetObject(newData)
                );
            }

            @Override
            public String getFnName() { return "difference"; }
        }

        DifferenceFn difference = new DifferenceFn();
        Variable differenceVar = new Variable(
                new Variable.Variant(difference),
                true,
                TypeTag.OBJECT
        );
        Y_Set_Prototype.set(difference.getFnName(), differenceVar);

        // set.isSubsetOf(other)
        class IsSubsetFn extends Function.NativeFunction {

            @Override
            public int arity() { return 1; }

            @Override
            public Variable.Variant call(
                    Interpreter interpreter,
                    List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_SetObject set = requireSetThis(interpreter);

                Variable.Variant otherVar = arguments.get(0);

                if (!otherVar.isRuntimeObject() ||
                        !(otherVar.asRuntimeObject() instanceof Y_SetObject)) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'isSubsetOf' argument must be a Set."
                    );
                }

                Y_SetObject other =
                        (Y_SetObject) otherVar.asRuntimeObject();

                boolean result =
                        other.data.containsAll(set.data);

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() { return "isSubsetOf"; }
        }

        IsSubsetFn isSubset = new IsSubsetFn();
        Variable isSubsetVar = new Variable(
                new Variable.Variant(isSubset),
                true,
                TypeTag.OBJECT
        );
        Y_Set_Prototype.set(isSubset.getFnName(), isSubsetVar);

        // set.empty()
        class EmptyFn extends Function.NativeFunction {

            @Override
            public int arity() { return 0; }

            @Override
            public Variable.Variant call(
                    Interpreter interpreter,
                    List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_SetObject set = requireSetThis(interpreter);
                return new Variable.Variant(set.data.isEmpty());
            }

            @Override
            public String getFnName() { return "empty"; }
        }

        EmptyFn empty = new EmptyFn();
        Variable emptyVar = new Variable(
                new Variable.Variant(empty),
                true,
                TypeTag.OBJECT
        );
        Y_Set_Prototype.set(empty.getFnName(), emptyVar);

        // set.isSupersetOf(other)
        class IsSupersetFn extends Function.NativeFunction {

            @Override
            public int arity() { return 1; }

            @Override
            public Variable.Variant call(
                    Interpreter interpreter,
                    List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_SetObject set = requireSetThis(interpreter);

                Variable.Variant otherVar = arguments.get(0);

                if (!otherVar.isRuntimeObject() ||
                        !(otherVar.asRuntimeObject() instanceof Y_SetObject)) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'isSupersetOf' argument must be a Set."
                    );
                }

                Y_SetObject other =
                        (Y_SetObject) otherVar.asRuntimeObject();

                boolean result =
                        set.data.containsAll(other.data);

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() { return "isSupersetOf"; }
        }

        IsSupersetFn isSuperset = new IsSupersetFn();
        Variable isSupersetVar = new Variable(
                new Variable.Variant(isSuperset),
                true,
                TypeTag.OBJECT
        );
        Y_Set_Prototype.set(isSuperset.getFnName(), isSupersetVar);

        // set.equals(other)
        class EqualsFn extends Function.NativeFunction {

            @Override
            public int arity() { return 1; }

            @Override
            public Variable.Variant call(
                    Interpreter interpreter,
                    List<Variable.Variant> arguments)
                    throws YsharpError {

                Y_SetObject set = requireSetThis(interpreter);

                Variable.Variant otherVar = arguments.get(0);

                if (!otherVar.isRuntimeObject() ||
                        !(otherVar.asRuntimeObject() instanceof Y_SetObject)) {

                    return new Variable.Variant(false);
                }

                Y_SetObject other =
                        (Y_SetObject) otherVar.asRuntimeObject();

                if (set.data.size() != other.data.size()) {
                    return new Variable.Variant(false);
                }

                boolean result =
                        set.data.containsAll(other.data);

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() { return "equals"; }
        }

        EqualsFn equals = new EqualsFn();
        Variable equalsVar = new Variable(
                new Variable.Variant(equals),
                true,
                TypeTag.OBJECT
        );
        Y_Set_Prototype.set(equals.getFnName(), equalsVar);
    }


    public static class Y_SetObject extends RuntimeObject {

        private final HashSet<Variable.Variant> data;

        public Y_SetObject() {
            this.data = new HashSet<>();
            this.prototype = Y_Set_Prototype;
        }

        public Y_SetObject(HashSet<Variable.Variant> data) {
            this.data = data;
            this.prototype = Y_Set_Prototype;
        }

        @Override
        public boolean isTruthy() { return true; }

        @Override
        public String getType() { return "Set"; }

        @Override
        public String toString() { return "<class:set>"; }
    }

    public static class Y_SetInit extends Function.NativeFunction {

        @Override
        public int arity() { return 0; }

        @Override
        public Variable.Variant call(
                Interpreter interpreter,
                List<Variable.Variant> arguments)
                throws YsharpError {

            return new Variable.Variant(new Y_SetObject());
        }

        @Override
        public String getFnName() { return "Set"; }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        Y_SetInit ctor = new Y_SetInit();

        interpreter.defineGlobal(
                ctor.getFnName(),
                new Variable(
                        new Variable.Variant(ctor),
                        false,
                        TypeTag.OBJECT));
    }
}