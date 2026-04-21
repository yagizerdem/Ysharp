package ysharp.treewalk.evaluator.Native.function.core;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Callable;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.Collections.yVector;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.lexer.Lexer;
import ysharp.treewalk.lexer.Preprocess;
import ysharp.treewalk.lexer.Token;
import ysharp.treewalk.parser.Expr;
import ysharp.treewalk.parser.Parser;
import ysharp.treewalk.parser.Stmt;

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

    }
}