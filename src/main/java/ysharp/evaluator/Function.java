package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.parser.Expr;
import ysharp.parser.Stmt;

import java.util.List;

public abstract class Function extends RuntimeObject implements Callable {

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
            return "<fn:" + this.declaration.name.lexeme + ">" ;
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

                Variable newVar = new Variable(arg, true, typeTag);

                newEnv.define(param.name.lexeme, newVar);
            }

            try {
                interpreter.executeBlock(
                        (Stmt.BlockStmt)declaration.body,
                        newEnv
                );
            } catch (Signal.ReturnSignal returnValue) {
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
            return "lambda";
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
            return "<fn:" + this.getFnName() + ">" ;
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

}
