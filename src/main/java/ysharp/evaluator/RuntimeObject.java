package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.parser.Expr;
import ysharp.parser.Stmt;
import ysharp.parser.TypeTag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RuntimeObject {

    protected Map<String, Variable> fields = new HashMap<>();
    // function native function and lambda do not need prototype chain, their behaviour is fixed
    protected RuntimeObject prototype;

    public void set(String name, Variable value) {
        fields.put(name, value);
    }

    public Variable get(String name) {
        if (fields.containsKey(name)) {
            return fields.get(name);
        }

        if (prototype != null) {
            return prototype.get(name);
        }

        return null;
    }

    public void setPrototype(RuntimeObject proto) {
        this.prototype = proto;
    }

    public RuntimeObject getPrototype() {
        return prototype;
    }

    public abstract boolean isTruthy();

    public abstract String getType();


    public static class StringObject extends RuntimeObject {
        final String data;

        public StringObject(String data){
            this.data = data;
        }

        @Override
        public boolean isTruthy() {
            return  !this.data.isEmpty();
        }

        @Override
        public String getType() {
            return "string";
        }

        @Override
        public String toString() {
            return this.data;
        }
    }

    public static class FunctionObject extends RuntimeObject implements Callable {
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

                TypeTag typeTag = null;
                if (param.type != null) {
                    typeTag = TypeTag.fromString(param.type.lexeme);
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

    public static class LambdaObject extends RuntimeObject implements Callable {
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

                TypeTag typeTag = null;
                if (param.type != null) {
                    typeTag = TypeTag.fromString(param.type.lexeme);
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


    public static class ClassObject extends RuntimeObject {

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "class";
        }

        @Override
        public String toString() {
            return "class";
        }
    }

}
