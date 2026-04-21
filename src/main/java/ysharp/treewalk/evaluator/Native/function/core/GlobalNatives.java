package ysharp.treewalk.evaluator.Native.function.core;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.yVector;
import ysharp.treewalk.evaluator.Native.Util.Type.Converter;
import ysharp.treewalk.evaluator.Native.Util.Type.Type;
import ysharp.treewalk.lexer.Lexer;
import ysharp.treewalk.lexer.Preprocess;
import ysharp.treewalk.lexer.Token;
import ysharp.treewalk.parser.Expr;
import ysharp.treewalk.parser.Parser;
import ysharp.treewalk.parser.Stmt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class GlobalNatives extends Function.NativeFunction {

    // now() -> epoch milliseconds
    public static class Now extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 0, getFnName());
            return new Variable.Variant(System.currentTimeMillis());
        }

        @Override public int arity() { return 0; }
        @Override public String getFnName() { return "now"; }
    }

    // sleep(ms)
    public static class Sleep extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());
            long ms = (long) requireNumber(args.getFirst(), getFnName(), 1);

            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "sleep interrupted"
                );
            }

            return new Variable.Variant(null);
        }

        @Override public int arity() { return 1; }
        @Override public String getFnName() { return "sleep"; }
    }

    // callable(object)
    public static class CallableFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            Variable.Variant value = args.get(0);

            boolean result = false;

            // callable check
            if (value.value instanceof Callable) {
                result = true;
            }

            return new Variable.Variant(result);
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "callable"; }
    }

    // chr(codepoint)
    public static class ChrFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            long code = (long) requireNumber(args.getFirst(), getFnName(), 1);

            if (code < 0 || code > 0x10FFFF) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "chr() codepoint out of range (0..0x10FFFF)"
                );
            }

            String result = new String(Character.toChars((int) code));

            return new Variable.Variant(result);
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "chr"; }
    }

    // eval(string)
    public static class EvalFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            String code =  requireString(args.getFirst(), getFnName(), 1);

            try {
                Lexer lexer = new Lexer(Preprocess.removeComments(Preprocess.mergeContinuation(code)));
                List<Token> tokens = lexer.scanTokens();

                Parser parser = new Parser(tokens);
                List<Expr> exprs = parser.parseExprGrammer();

                if (parser.hadErrors()) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
                            -1,
                            parser.errors.stream()
                                    .map(Object::toString)
                                    .collect(Collectors.joining("\n"))
                    );
                }

                if(exprs.size() != 1) {
                    throw  new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1
                            , "only single expression is allowed in eval method");
                }

                Expr expr =  exprs.getFirst();

                Interpreter interpreter = new Interpreter();

                Variable.Variant result = interpreter.evaluate(expr);

                return result != null ? result : new Variable.Variant(null);

            } catch (Exception e) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        0,
                        "eval failed: " + e.getMessage()
                );
            }
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "eval"; }
    }

    // abs(number)
    public static class AbsFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            double value = requireNumber(args.getFirst(), getFnName(), 1);

            double result = Math.abs(value);

            return new Variable.Variant(result);
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "abs"; }
    }

    // all(iterable)
    public static class AllFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            Object collection = args.getFirst().value;

            // vector is one kind of base iterable prototype
            if(collection instanceof yVector.IVector) {
                List<Variable.Variant> data =  ((yVector.IVector)collection).getData();

                boolean flag = true;
                for(Variable.Variant var : data) {
                    if (!flag) break;
                    flag = var.isTruthy();
                }

                return new Variable.Variant(flag);
            }

            throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1,
                    "any function accept only built in collections that has iterator protocol implemented such like arrays");
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "all"; }
    }

    // any(iterable)
    public static class AnyFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            Object collection = args.getFirst().value;

            // vector is one kind of base iterable prototype
            if(collection instanceof yVector.IVector) {
                List<Variable.Variant> data =  ((yVector.IVector)collection).getData();

                boolean flag = false;
                for(Variable.Variant var : data) {
                    if (flag) break;
                    flag = var.isTruthy();
                }

                return new Variable.Variant(flag);
            }

            throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1,
                    "any function accept only built in collections that has iterator protocol implemented such like arrays");
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "any"; }
    }

    // bin(int)
    public static class BinFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            int num = requireInt(args.getFirst(), getFnName(), 1);

            String binaryString = "0b" + Integer.toBinaryString(num);

            if(num < 0) {
                binaryString = "-" + binaryString;
            }

            return new Variable.Variant(binaryString);
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "bin"; }
    }

    // bool(variable)
    public static class BoolFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            Variable.Variant var = args.getFirst();

            return new Variable.Variant(var.isTruthy());
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "bool"; }
    }

    public static class ExecFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            String code = requireString(args.getFirst(), getFnName(), 1);


            try {
                Lexer lexer = new Lexer(Preprocess.removeComments(Preprocess.mergeContinuation(code)));
                List<Token> tokens = lexer.scanTokens();

                if (lexer.hadErrors()) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
                            -1,
                            lexer.errors.stream()
                                    .map(Object::toString)
                                    .collect(Collectors.joining("\n"))
                    );
                }


                Parser parser = new Parser(tokens);
                Parser.Program program = parser.parse();

                if (parser.hadErrors()) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
                            -1,
                            parser.errors.stream()
                                    .map(Object::toString)
                                    .collect(Collectors.joining("\n"))
                    );
                }

                if(!program.useDeclaration.isEmpty())  {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
                            -1,
                            "exec function cannot load modules with use statement");
                }


                List<Stmt> stmts = program.program;


                for(Stmt stmt : stmts) {
                    i.execute(stmt);
                }

                new Variable.Variant(null);

            } catch (Exception e) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        0,
                        "eval failed: " + e.getMessage()
                );
            }


            return new Variable.Variant(null);
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "exec"; }
    }

    // double()
    public static class DoubleFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            Variable.Variant v = args.getFirst();

            if (v.value == null) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "double() cannot convert null"
                );
            }

            Object val = v.value;

            // number zaten double gibi davranıyor
            if (val instanceof Number n) {
                return new Variable.Variant(n.doubleValue());
            }

            if (val instanceof Boolean b) {
                return new Variable.Variant(b ? 1.0 : 0.0);
            }

            if (val instanceof String s) {
                try {
                    return new Variable.Variant(Double.parseDouble(s));
                } catch (NumberFormatException e) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
                            -1,
                            "double() invalid string: '" + s + "'"
                    );
                }
            }

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "double() cannot convert type: " + val.getClass().getSimpleName()
            );
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "double"; }
    }

    public static class IntFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            Variable.Variant v = args.getFirst();

            if (v.value == null) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "int() cannot convert null"
                );
            }

            Object val = v.value;

            // number
            if (val instanceof Number n) {
                return new Variable.Variant((int) n.doubleValue()); // truncate
            }

            // boolean
            if (val instanceof Boolean b) {
                return new Variable.Variant(b ? 1 : 0);
            }

            // string
            if (val instanceof yString.yStringInstance yStr) {
                String s = yStr.data;
                try {
                    if (s.contains(".") || s.contains("e") || s.contains("E")) {
                        throw new NumberFormatException();
                    }

                    return new Variable.Variant(Integer.parseInt(s));
                } catch (NumberFormatException e) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
                            -1,
                            "int() invalid string: '" + s + "'"
                    );
                }
            }

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "int() cannot convert type: " + val.getClass().getSimpleName()
            );
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "int"; }
    }


    // hash(value)
    public static class HashFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            Object val = args.getFirst().value;

            if (val == null) {
                return new Variable.Variant(0);
            }

            if (val instanceof Boolean b) {
                return new Variable.Variant(b ? 1 : 0);
            }

            if (val instanceof Number n) {
                return new Variable.Variant(Double.hashCode(n.doubleValue()));
            }

            if(val instanceof String n) {
                return new Variable.Variant(n.hashCode());
            }

            if(val instanceof Character c) {
                return new Variable.Variant(Character.hashCode(c));
            }


            return new Variable.Variant(System.identityHashCode(val));
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "hash"; }
    }


    // id(value)
    public static class IdFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, 1, getFnName());

            Object val = args.getFirst().value;

            if (val == null) {
                return new Variable.Variant(0);
            }

            return new Variable.Variant(System.identityHashCode(val));
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "id"; }
    }


    // isInstance(value)
    public static class IsInstanceFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

                requireArity(args, arity(), getFnName());

                Variable.Variant var = args.getFirst();

                return new Variable.Variant(var.isClassInstance());
        }

        @Override public int arity() { return 1; }
        @Override public String getFnName() { return "isInstance"; }
    }

    // isClass(value)
    public static class IsClassFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, arity(), getFnName());

            Variable.Variant var = args.getFirst();

            return new Variable.Variant(var.isClass());
        }

        @Override public int arity() { return 1; }
        @Override public String getFnName() { return "isClass"; }
    }

    // input()
    public static class InputFn extends GlobalNatives {

        private static final BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            if (args.size() > 1) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "input() takes at most 1 argument"
                );
            }

            if (!args.isEmpty()) {
                String prompt = requireString(args.get(0), getFnName(), 1);
                System.out.print(prompt);
                System.out.flush();
            }

            try {
                String line = reader.readLine();

                if (line == null) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
                            -1,
                            "EOFError: input stream closed"
                    );
                }

                return new Variable.Variant(line);

            } catch (IOException e) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "input() failed: " + e.getMessage()
                );
            }
        }

        @Override public int arity() { return -1; }
        @Override public String getFnName() { return "input"; }
    }


    // len()
    public static class LenFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

                requireArity(args, arity(), getFnName());
                Variable.Variant variant = args.getFirst();

                Object data = variant.value;

                if(data instanceof yVector.IVector vec) {
                    return new Variable.Variant(vec.getData().size());
                }

                throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1,
                        "len function mus take single vector type collection as parameter");
        }

        @Override public int arity() { return 1; }
        @Override public String getFnName() { return "len"; }
    }


    // max()
    public static class MaxFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            if (args.isEmpty()) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "max() expected at least 1 argument"
                );
            }

            double max;

            // iterable
            if (args.size() == 1 && args.getFirst().value instanceof yVector.IVector vec) {

                List<Variable.Variant> data = vec.getData();

                if (data.isEmpty()) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
                            -1,
                            "max() arg is an empty sequence"
                    );
                }

                max = requireNumber(data.getFirst(), getFnName(), 1);

                for (Variable.Variant v : data) {
                    double val = requireNumber(v, getFnName(), 1);
                    if (val > max) {
                        max = val;
                    }
                }

                return new Variable.Variant(max);
            }

            // variadic
            max = requireNumber(args.getFirst(), getFnName(), 1);

            for (Variable.Variant v : args) {
                double val = requireNumber(v, getFnName(), 1);
                if (val > max) {
                    max = val;
                }
            }

            return new Variable.Variant(max);
        }

        @Override public int arity() { return -1; }
        @Override public String getFnName() { return "max"; }
    }


    // min()
    public static class MinFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            if (args.isEmpty()) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "min() expected at least 1 argument"
                );
            }

            double min;

            // iterable
            if (args.size() == 1 && args.getFirst().value instanceof yVector.IVector vec) {

                List<Variable.Variant> data = vec.getData();

                if (data.isEmpty()) {
                    throw new YsharpException(
                            YsharpException.YsharpErrorType.PROCESS,
                            -1,
                            "min() arg is an empty sequence"
                    );
                }

                min = requireNumber(data.getFirst(), getFnName(), 1);

                for (Variable.Variant v : data) {
                    double val = requireNumber(v, getFnName(), 1);
                    if (val < min) {
                        min = val;
                    }
                }

                return new Variable.Variant(min);
            }

            // variadic
            min = requireNumber(args.getFirst(), getFnName(), 1);

            for (Variable.Variant v : args) {
                double val = requireNumber(v, getFnName(), 1);
                if (val < min) {
                    min = val;
                }
            }

            return new Variable.Variant(min);
        }

        @Override public int arity() { return -1; }
        @Override public String getFnName() { return "min"; }
    }


    // str()
    public static class StrFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            requireArity(args, arity(), getFnName());

            Variable.Variant var = args.getFirst();

            if(var.isInt()) return new Variable.Variant(new yString.yStringInstance(String.valueOf(var.asInt())));
            if(var.isDouble()) return new Variable.Variant(new yString.yStringInstance(String.valueOf(var.asDouble())));
            if(var.isNull()) return new Variable.Variant("null");
            if(var.isChar()) return new Variable.Variant(new yString.yStringInstance(String.valueOf(var.asCharacter())));
            if(var.isBoolean()) return new Variable.Variant(new yString.yStringInstance(String.valueOf(var.asBoolean())));

            return new Variable.Variant(new yString.yStringInstance(var.value.toString()));

        }

        @Override public int arity() { return 1; }
        @Override public String getFnName() { return "str"; }
    }


    // round()
    public static class RoundFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            if (args.size() != 1) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "round() takes exactly 1 argument"
                );
            }

            double value = requireNumber(args.get(0), getFnName(), 1);

            return new Variable.Variant((double) Math.round(value));
        }

        @Override public int arity() { return 1; }

        @Override public String getFnName() { return "round"; }
    }

    // sum()
    public static class SumFn extends GlobalNatives {

        @Override
        public Variable.Variant call(Interpreter i, List<Variable.Variant> args)
                throws YsharpException {

            if (args.isEmpty()) {
                return new Variable.Variant(0.0);
            }

            double sum = 0.0;

            // iterable case
            if (args.size() == 1 && args.getFirst().value instanceof yVector.IVector vec) {

                List<Variable.Variant> data = vec.getData();

                for (Variable.Variant v : data) {
                    sum += requireNumber(v, getFnName(), 1);
                }

                return new Variable.Variant(sum);
            }

            // variadic case
            for (Variable.Variant v : args) {
                sum += requireNumber(v, getFnName(), 1);
            }

            return new Variable.Variant(sum);
        }

        @Override public int arity() { return -1; }

        @Override public String getFnName() { return "sum"; }
    }


    public static void  Register(Interpreter interpreter) {
        Now nowFn = new Now();
        interpreter.global.define(nowFn.getFnName(),
                new Variable(new Variable.Variant(nowFn), true, nowFn.getType()));

        Sleep sleepFn = new Sleep();
        interpreter.global.define(sleepFn.getFnName(),
                new Variable(new Variable.Variant(sleepFn), true, sleepFn.getType()));

        CallableFn callableFn = new CallableFn();
        interpreter.global.define(callableFn.getFnName(),
                new Variable(new Variable.Variant(callableFn), true, callableFn.getType()));

        ChrFn chrFn = new ChrFn();
        interpreter.global.define(chrFn.getFnName(),
                new Variable(new Variable.Variant(chrFn), true, chrFn.getType()));


        EvalFn evalFn = new EvalFn();
        interpreter.global.define(evalFn.getFnName(),
                new Variable(new Variable.Variant(evalFn), true, evalFn.getType()));

        AbsFn absFn = new AbsFn();
        interpreter.global.define(absFn.getFnName(),
                new Variable(new Variable.Variant(absFn), true, absFn.getType()));


        AllFn allFn = new AllFn();
        interpreter.global.define(allFn.getFnName(),
                new Variable(new Variable.Variant(allFn), true, allFn.getType()));


        AnyFn anyFn = new AnyFn();
        interpreter.global.define(anyFn.getFnName(),
                new Variable(new Variable.Variant(anyFn), true, anyFn.getType()));


        BinFn binFn = new BinFn();
        interpreter.global.define(binFn.getFnName(),
                new Variable(new Variable.Variant(binFn), true, binFn.getType()));


        BoolFn boolFn = new BoolFn();
        interpreter.global.define(boolFn.getFnName(),
                new Variable(new Variable.Variant(boolFn), true, boolFn.getType()));


        DoubleFn doubleFn = new DoubleFn();
        interpreter.global.define(doubleFn.getFnName(),
                new Variable(new Variable.Variant(doubleFn), true, doubleFn.getType()));


        IntFn intFn = new IntFn();
        interpreter.global.define(intFn.getFnName(),
                new Variable(new Variable.Variant(intFn), true, intFn.getType()));

        HashFn hashFn = new HashFn();
        interpreter.global.define(hashFn.getFnName(),
                new Variable(new Variable.Variant(hashFn), true, hashFn.getType()));

        IdFn idFn = new IdFn();
        interpreter.global.define(idFn.getFnName(),
                new Variable(new Variable.Variant(idFn), true, idFn.getType()));


        InputFn inputFn = new InputFn();
        interpreter.global.define(inputFn.getFnName(),
                new Variable(new Variable.Variant(inputFn), true, inputFn.getType()));

        IsInstanceFn isInstanceFn = new IsInstanceFn();
        interpreter.global.define(isInstanceFn.getFnName(),
                new Variable(new Variable.Variant(isInstanceFn), true, isInstanceFn.getType()));

        IsClassFn isClassFn = new IsClassFn();
        interpreter.global.define(isClassFn.getFnName(),
                new Variable(new Variable.Variant(isClassFn), true, isClassFn.getType()));

        LenFn lenFn = new LenFn();
        interpreter.global.define(lenFn.getFnName(),
                new Variable(new Variable.Variant(lenFn), true, lenFn.getType()));


        MaxFn maxFn = new MaxFn();
        interpreter.global.define(maxFn.getFnName(),
                new Variable(new Variable.Variant(maxFn), true, maxFn.getType()));


        MinFn minFn = new MinFn();
        interpreter.global.define(minFn.getFnName(),
                new Variable(new Variable.Variant(minFn), true, minFn.getType()));

        StrFn strFn = new StrFn();
        interpreter.global.define(strFn.getFnName(),
                new Variable(new Variable.Variant(strFn), true, strFn.getType()));

        RoundFn roundFn = new RoundFn();
        interpreter.global.define(roundFn.getFnName(),
                new Variable(new Variable.Variant(roundFn), true, roundFn.getType()));

        SumFn sumFn = new SumFn();
        interpreter.global.define(sumFn.getFnName(),
                new Variable(new Variable.Variant(sumFn), true, sumFn.getType()));


//        ExecFn execFn = new ExecFn();
//        interpreter.global.define(execFn.getFnName(),
//                new Variable(new Variable.Variant(execFn), true, execFn.getType()));

    }
}