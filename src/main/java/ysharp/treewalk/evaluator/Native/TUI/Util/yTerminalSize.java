package ysharp.treewalk.evaluator.Native.TUI.Util;

import com.googlecode.lanterna.TerminalSize;
import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.util.List;

public class yTerminalSize {

    // helper
    public static yTerminalSizeInstance requireTerminalSizeThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yTerminalSizeInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'TerminalSize' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return (yTerminalSizeInstance) obj;
    }

    public static yTerminalSize.yTerminalSizeInstance requireTerminalSize (Variable.Variant v,
                                                 String fn,
                                                 int index) throws YsharpException {

        if (!v.isRuntimeObject()) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a object."
            );
        }

        RuntimeObject obj = v.asRuntimeObject();

        if(!(obj instanceof yTerminalSize.yTerminalSizeInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    fn + " argument " + index + " must be a TerminalSize object."
            );
        }

        return (yTerminalSize.yTerminalSizeInstance) obj;
    }

    public static RuntimeObject yTerminalSize_Instance_Prototype;

    static {
        yTerminalSize_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__TerminalSize__";
            }

            @Override
            public String toString() {
                return "<prototype:TerminalSize>";
            }
        };

        yTerminalSize_Instance_Prototype.prototype = yClass.ClassPrototype;


        // terminalSize.equals(obj)
        class EqualsFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());
                yTerminalSizeInstance self = requireTerminalSizeThis(interpreter, getFnName());

                Variable.Variant otherVar = arguments.getFirst();
                yTerminalSizeInstance otherTerminalSize = requireTerminalSize(otherVar, getFnName(),1);

                return new Variable.Variant(self.terminalSize.equals(otherTerminalSize.terminalSize));
            }

            @Override
            public String getFnName() {
                return "equals";
            }
        }

        EqualsFn equals = new EqualsFn();
        yTerminalSize_Instance_Prototype.set(
                equals.getFnName(),
                new Variable(new Variable.Variant(equals), true, "function")
        );

        // terminalSize.getColumns()
        class GetColumnsFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());
                yTerminalSizeInstance self = requireTerminalSizeThis(interpreter, getFnName());

                return new Variable.Variant(self.terminalSize.getColumns());
            }

            @Override
            public String getFnName() {
                return "getColumns";
            }

        }

        GetColumnsFn getColumns = new GetColumnsFn();
        yTerminalSize_Instance_Prototype.set(
                getColumns.getFnName(),
                new Variable(new Variable.Variant(getColumns), true, "function")
        );


        // terminalSize.getRows()
        class GetRowsFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());
                yTerminalSizeInstance self = requireTerminalSizeThis(interpreter, getFnName());

                return new Variable.Variant(self.terminalSize.getRows());
            }

            @Override
            public String getFnName() {
                return "getRows";
            }
        }

        GetRowsFn getRows = new GetRowsFn();
        yTerminalSize_Instance_Prototype.set(
                getRows.getFnName(),
                new Variable(new Variable.Variant(getRows), true, "function")
        );

        // terminalSize.hashCode()
        class HashCodeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());
                yTerminalSizeInstance self = requireTerminalSizeThis(interpreter, getFnName());

                return new Variable.Variant(self.terminalSize.hashCode());
            }

            @Override
            public String getFnName() {
                return "hashCode";
            }
        }

        HashCodeFn hashCode = new HashCodeFn();
        yTerminalSize_Instance_Prototype.set(
                hashCode.getFnName(),
                new Variable(new Variable.Variant(hashCode), true, "function")
        );

        // terminalSize.max()
        class MaxFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());

                yTerminalSizeInstance self = requireTerminalSizeThis(interpreter, getFnName());
                yTerminalSizeInstance other = requireTerminalSize(arguments.getFirst(), getFnName(), 1);


                return new Variable.Variant(self.terminalSize.max(other.terminalSize));
            }

            @Override
            public String getFnName() {
                return "max";
            }
        }

        MaxFn max = new MaxFn();
        yTerminalSize_Instance_Prototype.set(
                max.getFnName(),
                new Variable(new Variable.Variant(max), true, "function")
        );


        // terminalSize.min()
        class MinFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());

                yTerminalSizeInstance self = requireTerminalSizeThis(interpreter, getFnName());
                yTerminalSizeInstance other = requireTerminalSize(arguments.getFirst(), getFnName(), 1);

                return new Variable.Variant(self.terminalSize.min(other.terminalSize));
            }

            @Override
            public String getFnName() {
                return "min";
            }
        }

        MinFn min = new MinFn();
        yTerminalSize_Instance_Prototype.set(
                min.getFnName(),
                new Variable(new Variable.Variant(min), true, "function")
        );


        // terminalSize.toString()
        class ToStringFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());

                yTerminalSizeInstance self = requireTerminalSizeThis(interpreter, getFnName());

                return new Variable.Variant(
                        new yString.yStringInstance(self.terminalSize.toString())
                );
            }

            @Override
            public String getFnName() {
                return "toString";
            }
        }

        ToStringFn toString = new ToStringFn();
        yTerminalSize_Instance_Prototype.set(
                toString.getFnName(),
                new Variable(new Variable.Variant(toString), true, "function")
        );


        // terminalSize.with()
        class WithFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());

                yTerminalSizeInstance self = requireTerminalSizeThis(interpreter, getFnName());
                yTerminalSizeInstance other = requireTerminalSize(arguments.getFirst(), getFnName(), 1);

                return new Variable.Variant(self.terminalSize.with(other.terminalSize));
            }

            @Override
            public String getFnName() {
                return "with";
            }
        }

        WithFn with = new WithFn();
        yTerminalSize_Instance_Prototype.set(
                with.getFnName(),
                new Variable(new Variable.Variant(with), true, "function")
        );


        // terminalSize.withColumns()
        class WithColumnsFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());

                yTerminalSizeInstance self = requireTerminalSizeThis(interpreter, getFnName());
                int columns = requireInt(arguments.getFirst(), getFnName(), 1);

                return new Variable.Variant(
                        self.terminalSize.withColumns(columns)
                );
            }

            @Override
            public String getFnName() {
                return "withColumns";
            }
        }

        WithColumnsFn withColumns = new WithColumnsFn();
        yTerminalSize_Instance_Prototype.set(
                withColumns.getFnName(),
                new Variable(new Variable.Variant(withColumns), true, "function")
        );


        // terminalSize.withRelativeColumns()
        class WithRelativeColumnsFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());

                yTerminalSizeInstance self = requireTerminalSizeThis(interpreter, getFnName());
                int delta = requireInt(arguments.getFirst(), getFnName(), 1);

                return new Variable.Variant(
                        self.terminalSize.withRelativeColumns(delta)
                );
            }

            @Override
            public String getFnName() {
                return "withRelativeColumns";
            }
        }

        WithRelativeColumnsFn withRelativeColumns = new WithRelativeColumnsFn();
        yTerminalSize_Instance_Prototype.set(
                withRelativeColumns.getFnName(),
                new Variable(new Variable.Variant(withRelativeColumns), true, "function")
        );


        // terminalSize.withRelativeRows()
        class WithRelativeRowsFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());

                yTerminalSizeInstance self = requireTerminalSizeThis(interpreter, getFnName());
                int delta = requireInt(arguments.getFirst(), getFnName(), 1);

                return new Variable.Variant(
                        self.terminalSize.withRelativeRows(delta)
                );
            }

            @Override
            public String getFnName() {
                return "withRelativeRows";
            }
        }

        WithRelativeRowsFn withRelativeRows = new WithRelativeRowsFn();
        yTerminalSize_Instance_Prototype.set(
                withRelativeRows.getFnName(),
                new Variable(new Variable.Variant(withRelativeRows), true, "function")
        );



        // terminalSize.withRows()
        class WithRowsFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                requireArity(arguments, arity(), getFnName());

                yTerminalSizeInstance self = requireTerminalSizeThis(interpreter, getFnName());
                int rows = requireInt(arguments.getFirst(), getFnName(), 1);

                return new Variable.Variant(
                        self.terminalSize.withRows(rows)
                );
            }

            @Override
            public String getFnName() {
                return "withRows";
            }
        }

        WithRowsFn withRows = new WithRowsFn();
        yTerminalSize_Instance_Prototype.set(
                withRows.getFnName(),
                new Variable(new Variable.Variant(withRows), true, "function")
        );


        class WithRelativeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return -1; // 1 or 2 params
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpException {

                yTerminalSizeInstance self = requireTerminalSizeThis(interpreter, getFnName());

                if (arguments.size() == 2) {
                    int deltaCols = requireInt(arguments.get(0), getFnName(), 1);
                    int deltaRows = requireInt(arguments.get(1), getFnName(), 2);

                    return new Variable.Variant(
                            self.terminalSize.withRelative(deltaCols, deltaRows)
                    );
                }

                if (arguments.size() == 1) {
                    // withRelative(TerminalSize)
                    yTerminalSizeInstance delta = requireTerminalSize(arguments.getFirst(), getFnName(), 1);

                    return new Variable.Variant(
                            self.terminalSize.withRelative(delta.terminalSize)
                    );
                }

                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        0,
                        "'withRelative' expects 1 or 2 arguments."
                );
            }

            @Override
            public String getFnName() {
                return "withRelative";
            }
        }

        WithRelativeFn withRelative = new WithRelativeFn();
        yTerminalSize_Instance_Prototype.set(
                withRelative.getFnName(),
                new Variable(new Variable.Variant(withRelative), true, "function")
        );
    }

    public static class yTerminalSizeInstance extends yClass.ClassObjectInstance {

        public TerminalSize terminalSize;

        public yTerminalSizeInstance(int cols, int rows) {
            this.terminalSize = new TerminalSize(cols, rows);
            this.prototype = yTerminalSize_Instance_Prototype;
        }

        public yTerminalSizeInstance(TerminalSize terminalSize) {
            this.terminalSize = terminalSize;
            this.prototype = yTerminalSize_Instance_Prototype;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "TerminalSize";
        }

        @Override
        public String toString() {
            return "<instance:TerminalSize>";
        }
    }


    public static class yTerminalSizeClass extends yClass.SealedClassObject {

        @Override
        public String getType() {
            return "TerminalSize";
        }

        @Override
        public String toString() {
            return "<class:TerminalSize>";
        }

        @Override
        public String getClassName() {
            return "TerminalSize";
        }

        @Override
        public int arity() {
            return 2; // col row
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

            requireArity(arguments, arity(), getClassName());

            int cols = requireInt(arguments.getFirst(), getClassName(), 1);
            int rows = requireInt(arguments.get(1), getClassName(), 2);

            yTerminalSizeInstance instance = new yTerminalSizeInstance(cols, rows);
            return new Variable.Variant(instance);
        }
    }

}
