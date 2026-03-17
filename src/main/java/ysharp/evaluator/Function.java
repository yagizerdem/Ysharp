package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.parser.Expr;
import ysharp.parser.Stmt;

import java.util.HashMap;
import java.util.List;

public abstract class Function extends RuntimeObject implements Callable {

    public static int getArgCount(Callable fn) {
        if(fn instanceof FunctionObject) {
            FunctionObject fnObj = (FunctionObject) fn;
            return fnObj.declaration.params.size();
        }
        else if(fn instanceof NativeFunction) {
            NativeFunction nativeFn = (NativeFunction) fn;
            return nativeFn.arity();
        }
        else if(fn instanceof LambdaObject) {
            LambdaObject lambda = (LambdaObject) fn;
            return lambda.lambdaExpr.params.size();
        }

        return 0;
    }

    public static int getArgCount(Function fn) {
        if(fn instanceof FunctionObject) {
            FunctionObject fnObj = (FunctionObject) fn;
            return fnObj.declaration.params.size();
        }
        else if(fn instanceof NativeFunction) {
            NativeFunction nativeFn = (NativeFunction) fn;
            return nativeFn.arity();
        }
        else if(fn instanceof LambdaObject) {
            LambdaObject lambda = (LambdaObject) fn;
            return lambda.lambdaExpr.params.size();
        }

        return 0;
    }

    // function prototype chain is closed
    public static class FunctionObject extends Function {
        public final Stmt.FunctionDeclaration declaration;
        private Environment closure;


        public FunctionObject(Stmt.FunctionDeclaration declaration,
                              Environment closure) {
            this.declaration = declaration;
            this.closure = closure;
            this.prototype = null;
        }


        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "function";
        }

        @Override
        public String toString() {
            return "<function:" + this.declaration.name.lexeme + ">" ;
        }

        @Override
        public int arity() {
            return this.declaration.params.size();
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            Environment newEnv = new Environment(this.closure);

            if (arguments.size() != declaration.params.size()) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        declaration.name.line,
                        "Expected " + declaration.params.size() +
                                " arguments but got " + arguments.size()
                );
            }

            for (int i = 0; i < declaration.params.size(); i++) {

                Stmt.FunctionDeclaration.Param param =
                        declaration.params.get(i);

                Variable.Variant arg = arguments.get(i);

                String typeTag = "any";
                if (param.type != null) {
                    typeTag = param.type.lexeme;
                }

                if(!Interpreter.typeChecker(typeTag, arg)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            declaration.name.line,
                            "Parameter : " + declaration.params.get(i).name.lexeme + " type mismatch. Expected '" +
                                    typeTag + "' but got '" +
                                    arg.getType() + "'."
                    );
                }

                Variable newVar = new Variable(arg, true, typeTag);

                newEnv.define(param.name.lexeme, newVar);
            }

            try {
                interpreter.executeBlock(
                        (Stmt.BlockStmt)declaration.body,
                        newEnv
                );
            } catch (Signal.ReturnSignal returnValue) {

                String expectedType = "any";

                if (declaration.returnType != null) {
                    expectedType = declaration.returnType.lexeme;
                }

                if (!Interpreter.typeChecker(expectedType, returnValue.value)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            declaration.name.line,
                            "Return type mismatch. Expected '" +
                                    expectedType + "' but got '" +
                                    returnValue.value.getType() + "'."
                    );
                }

                return returnValue.value;
            }

            return new Variable.Variant(null);
        }
    }

    public static class LambdaObject extends Function {
        public final Expr.LambdaExpr lambdaExpr;
        private Environment closure;

        public LambdaObject(Expr.LambdaExpr  lambdaExpr,
                            Environment closure) {
            this.lambdaExpr = lambdaExpr;
            this.closure = closure;
            this.prototype = null;
        }


        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "function";
        }

        @Override
        public String toString() {
            return "<lambda>" ;
        }

        @Override
        public int arity() {
            return this.lambdaExpr.params.size();
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            Environment newEnv = new Environment(this.closure);

            if (arguments.size() != lambdaExpr.params.size()) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        lambdaExpr.leftParen.line,
                        "Expected " + lambdaExpr.params.size() +
                                " arguments but got " + arguments.size()
                );
            }

            for (int i = 0; i < lambdaExpr.params.size(); i++) {

                Expr.LambdaExpr.Param param =
                        lambdaExpr.params.get(i);

                Variable.Variant arg = arguments.get(i);

                String typeTag = null;
                if (param.type != null) {
                    typeTag = param.type.lexeme;
                }

                Variable newVar = new Variable(arg, true, typeTag);

                newEnv.define(param.name.lexeme, newVar);
            }

            try {

                if(lambdaExpr.body != null) {
                    interpreter.executeBlock(
                            (Stmt.BlockStmt)lambdaExpr.body,
                            newEnv
                    );
                }

                if(lambdaExpr.expr != null) {
                    return interpreter.evaluate(
                            lambdaExpr.expr,
                            newEnv
                    );
                }


            } catch (Signal.ReturnSignal returnValue) {
                return returnValue.value;
            }

            return new Variable.Variant(null);
        }
    }

    public static abstract class NativeFunction extends Function {

        public abstract String getFnName();

        public NativeFunction(){
            this.prototype = null;
        }

        @Override
        public String toString() {
            return "<function:" + this.getFnName() + ">" ;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "function";
        }
    }

    public static class FunctionOverload extends Function {
        public final String name;
        public final HashMap<Integer, Variable.Variant> dispatcher =
                new HashMap<>();

        public FunctionOverload(String name) {
            this.name = name;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "function";
        }

        @Override
        public String toString() {
            return "<function:" + this.name + ">" ;
        }

        @Override
        public int arity() {
            return -1;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

            int argCount = arguments.size();

            if (this.dispatcher.containsKey(argCount)) {
                return this.dispatcher
                        .get(argCount)
                        .asCallable()
                        .call(interpreter, arguments);
            }

            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    0,
                    "No overload found for function '" + name +
                            "' with " + argCount + " argument(s)."
            );
        }

        public void addFunction(Variable.Variant fn, int argSize) {
            if(this.dispatcher.containsKey(argSize)) {
                throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,-1,
                        "Variable '" +
                        this.name +
                        "' is already defined in this scope.");
            }
            this.dispatcher.put(argSize, fn);
        }

        public Variable.Variant getFunction(int argSize) {
            if (this.dispatcher.containsKey(argSize)) {
                return this.dispatcher.get(argSize);
            }

            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    "Function '" + this.name + "' does not have an overload that accepts " + argSize +
                            (argSize <= 1 ? " argument." : " arguments." )
            );

        }
    }

}
