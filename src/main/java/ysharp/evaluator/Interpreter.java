package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.evaluator.Native.Collections.Array.yArray;
import ysharp.evaluator.Native.Collections.HashMap.yHashMap;
import ysharp.evaluator.Native.Range;
import ysharp.evaluator.Native.function.binding.BoundNativeFunction;
import ysharp.lexer.Token;
import ysharp.parser.Expr;
import ysharp.parser.Stmt;

import java.util.*;

public class Interpreter implements
        Expr.Visitor<Variable.Variant>,
        Stmt.Visitor {

    public Environment global;
    public Environment curEnv;
    public Map<Expr, Integer> locals;
    public List<YsharpError> errors;
    public List<String> exports;

    public boolean hadErrors() {
        return !errors.isEmpty();
    }

    public void resolve(Expr expr, int depth) {
        locals.put(expr, depth);
    }

    public Interpreter() {
        this.global = new Environment();
        this.curEnv = global;
        this.errors = new ArrayList<>();
        this.exports = new ArrayList<>();
        this.locals = new HashMap<>();
    }

    public void defineGlobal(String key, Variable variable) throws Exception {
        try {
            this.global.define(key, variable);
        }catch (YsharpError err) {
            throw new Exception("[Programmatic error] defining natives should not throw error");
        }
    }

    public void interpret(List<Stmt> statements) {
        try {
            for (Stmt stmt : statements) {
                execute(stmt);
            }
        } catch (YsharpError err) {
            this.errors.add(err);
        }
    }

    public Variable.Variant evaluate(Expr expr) {
        return expr.accept(this);
    }

    public Variable.Variant evaluate(Expr expr, Environment newEnv) {
        Environment previous = this.curEnv;
        try {
            this.curEnv = newEnv;
            return expr.accept(this);
        }
        finally {
            this.curEnv = previous;
        }
    }

    public Interpreter copy() {
        Interpreter newInterpreter = new Interpreter();
        newInterpreter.curEnv = this.curEnv;
        newInterpreter.global = this.global;
        newInterpreter.locals = this.locals;
        newInterpreter.exports = this.exports;

        return newInterpreter;
    }

    public void execute(Stmt stmt){
        if (Thread.currentThread().isInterrupted()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    "Thread interrupted."
            );
        }

        stmt.accept(this);
    }

    public void executeBlock(Stmt.BlockStmt blockStmt,
                             Environment newEnv) {
        Environment previous = this.curEnv;
        try {
            this.curEnv = newEnv;

            for (Stmt stmt : blockStmt.stmtList) {
                execute(stmt);
            }

        }
        finally {
            this.curEnv = previous;
        }
    }

    private void requireIntegerOperands(Variable.Variant left,
                                        Variable.Variant right,
                                        Token op) throws YsharpError {

        if (!left.isInt() || !right.isInt()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    op.line,
                    "Operator '" + op.lexeme +
                            "' requires integer operands. Found '" +
                            left.getType() + "' and '" +
                            right.getType() + "'."
            );
        }
    }

    private void requireNumberOperands(Variable.Variant left,
                                       Variable.Variant right,
                                       Token op) throws YsharpError {

        if (!left.isNumber() || !right.isNumber()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    op.line,
                    "Operator '" + op.lexeme +
                            "' requires numeric operands. Found '" +
                            left.getType() + "' and '" +
                            right.getType() + "'."
            );
        }
    }

    // expr visitor
    @Override
    public Variable.Variant visitBinaryExpr(Expr.BinaryExpr expr) {
        switch (expr.op.type) {
            case PLUS -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                if(left.canImplicitlyConvertNumber() && right.canImplicitlyConvertNumber()) {
                    if(left.isInt() && right.isInt()) return new Variable.Variant(left.asInt() + right.asInt());
                    double sum = left.implicitlyConvertNumber() + right.implicitlyConvertNumber();
                    return new Variable.Variant(sum);
                }

                if (left.isString() && right.isNumber()) {

                    if (right.isInt()) {
                        return new Variable.Variant(
                                new yString.yStringInstance(left.asString() + right.asInt())
                        );
                    } else {
                        return new Variable.Variant(
                                new yString.yStringInstance(left.asString() + right.asNumber())
                        );
                    }
                }

                if (left.isNumber() && right.isString()) {

                    if (left.isInt()) {
                        return new Variable.Variant(
                                new yString.yStringInstance(left.asInt() + right.asString())
                        );
                    } else {
                        return new Variable.Variant(
                                new yString.yStringInstance(left.asNumber() + right.asString())
                        );
                    }
                }

                if (left.isString() && right.isString()) {
                    return new Variable.Variant(
                            new yString.yStringInstance(left.asString() + right.asString())
                    );
                }

                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Operator '+' cannot be applied to types '"
                                + left.getType() + "' and '" + right.getType() + "'."
                );
            }
            case MINUS -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireNumberOperands(left, right, expr.op);
                if(left.isInt() && right.isInt()) return new Variable.Variant(left.asInt() - right.asInt());
                double diff = left.implicitlyConvertNumber() - right.implicitlyConvertNumber();
                return new Variable.Variant(diff);
            }
            case MULTIPLY -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireNumberOperands(left, right, expr.op);
                if(left.isInt() && right.isInt()) return new Variable.Variant(left.asInt() * right.asInt());
                double mul = left.asNumber() * right.asNumber();
                return new Variable.Variant(mul);
            }
            case DIVIDE -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                if(right.asNumber() == 0)
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            expr.op.line,
                            "Division by zero."
                    );
                requireNumberOperands(left, right, expr.op);
                if(left.isInt() && right.isInt()) return new Variable.Variant(left.asInt() / right.asInt());
                double div = left.asNumber() / right.asNumber();
                return new Variable.Variant(div);
            }
            case MODULO -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireNumberOperands(left, right, expr.op);
                if (left.isInt() && right.isInt()) {
                        return new Variable.Variant(
                                left.asInt() % right.asInt()
                        );
                    }
                double mod = left.asNumber() % right.asNumber();
                return new Variable.Variant(mod);
            }
            case BITWISE_AND -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireIntegerOperands(left, right, expr.op);
                int result = left.asInt() & right.asInt();
                return new Variable.Variant(result);
            }
            case BITWISE_OR -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireIntegerOperands(left, right, expr.op);
                int result = left.asInt() | right.asInt();
                return new Variable.Variant(result);
            }
            case BITWISE_XOR -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireIntegerOperands(left, right, expr.op);
                int result = left.asInt() ^ right.asInt();
                return new Variable.Variant(result);
            }
            case LEFT_SHIFT -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireIntegerOperands(left, right, expr.op);
                int result = left.asInt() << right.asInt();
                return new Variable.Variant(result);
            }
            case RIGHT_SHIFT -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireIntegerOperands(left, right, expr.op);
                int result = left.asInt() >> right.asInt();
                return new Variable.Variant(result);
            }
            case GREATER_THAN -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireNumberOperands(left, right, expr.op);
                return new Variable.Variant(left.implicitlyConvertNumber() > right.implicitlyConvertNumber());
            }
            case GREATER_OR_EQUAL -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireNumberOperands(left, right, expr.op);
                return new Variable.Variant(left.implicitlyConvertNumber() >= right.implicitlyConvertNumber());

            }
            case LESS_THAN -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireNumberOperands(left, right, expr.op);
                return new Variable.Variant(left.implicitlyConvertNumber() < right.implicitlyConvertNumber());
            }
            case LESS_OR_EQUAL -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireNumberOperands(left, right, expr.op);
                    return new Variable.Variant(left.implicitlyConvertNumber() <= right.implicitlyConvertNumber());
            }
            case EQUAL_EQUAL -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                return new Variable.Variant(left.equals(right));
            }
            case BANG_EQUAL -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                return new Variable.Variant(!left.equals(right));
            }
            case DOUBLE_QUESTION_MARK -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                return left.value != null ?  left : right;
            }
            default -> {
                throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line, "unsupported op");
            }
        }
    }

    @Override
    public Variable.Variant visitUnaryExpr(Expr.UnaryExpr expr) {
        switch (expr.op.type) {
            case PLUS -> {
                Variable.Variant var = this.evaluate(expr.expr);
                if(!var.isNumber()) {
                    throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                            expr.op.line,
                            "Operand must be a number.");
                }
                return var;
            }
            case MINUS -> {
                Variable.Variant var = this.evaluate(expr.expr);
                if(var.isInt()) {
                    return new Variable.Variant(var.asInt() * -1);
                }
                if(var.isDouble()) {
                    return new Variable.Variant(var.asDouble() * -1);
                }

                throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Operand must be a number.");
            }
            case BANG -> {
                Variable.Variant var = this.evaluate(expr.expr);
                return new Variable.Variant(!var.isTruthy());
            }
            case BITWISE_NOT -> {
                Variable.Variant var = this.evaluate(expr.expr);
                if(var.isInt()) {
                    return new Variable.Variant(~var.asInt());
                }

                throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Operand must be a int.");

            }
            case PLUS_PLUS -> {

                if (!(expr.expr instanceof Expr.VariableExpr ||
                        expr.expr instanceof Expr.GetExpr)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.SEMANTIC,
                            expr.op.line,
                            "Operand of '" + expr.op.lexeme + "' must be a variable."
                    );
                }

                Variable.Variant var = this.evaluate(expr.expr);
                if(!var.isNumber()) {
                    throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                            expr.op.line,
                            "Operand must be a number.");
                }

                var oldValue = var.value;

                if (var.isInt()) {
                    var.value = var.asInt() + 1;
                } else {
                    var.value = var.asDouble() + 1;
                }
                return new Variable.Variant(oldValue);
            }
            case MINUS_MINUS -> {

                if (!(expr.expr instanceof Expr.VariableExpr ||
                        expr.expr instanceof Expr.GetExpr)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.SEMANTIC,
                            expr.op.line,
                            "Operand of '" + expr.op.lexeme + "' must be a variable."
                    );
                }

                Variable.Variant var = this.evaluate(expr.expr);
                if(!var.isNumber()) {
                    throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                            expr.op.line,
                            "Operand must be a number.");
                }

                var oldValue = var.value;

                if (var.isInt()) {
                    var.value = var.asInt() - 1;
                } else {
                    var.value = var.asDouble() - 1;
                }
                return new Variable.Variant(oldValue);
            }
        }

        throw new YsharpError(
                YsharpError.YsharpErrorType.PROCESS,
                expr.op.line,
                "Unknown unary operator '" + expr.op.lexeme + "'."
        );
    }

    @Override
    public Variable.Variant visitTernaryExpr(Expr.TernaryExpr expr) {
        Variable.Variant condition = evaluate(expr.condition);

        if (condition.isTruthy()) {
            return evaluate(expr.thenBranch);
        } else {
            return evaluate(expr.elseBranch);
        }
    }

    @Override
    public Variable.Variant visitPostfixExpr(Expr.PostfixExpr expr) {

        if (!(expr.operand instanceof Expr.VariableExpr ||
                expr.operand instanceof Expr.GetExpr) ) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.SEMANTIC,
                    expr.op.line,
                    "Operand of '" + expr.op.lexeme + "' must be a variable."
            );
        }

        Variable.Variant var = this.evaluate(expr.operand);

        if (!var.isNumber()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.op.line,
                    "Operand must be a number."
            );
        }
        Variable.Variant oldValue = new Variable.Variant(var.value);

        switch (expr.op.type) {

            case PLUS_PLUS -> {
                if (var.isInt()) {
                    var.value = var.asInt() + 1;
                } else {
                    var.value = var.asDouble() + 1;
                }
            }

            case MINUS_MINUS -> {
                if (var.isInt()) {
                    var.value = var.asInt() - 1;
                } else {
                    var.value = var.asDouble() - 1;
                }
            }

            default -> throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.op.line,
                    "Unknown postfix operator '" + expr.op.lexeme + "'."
            );
        }
        return oldValue;
    }

    @Override
    public Variable.Variant visitAssignmentExpr(Expr.AssignmentExpr expr) {
        if(expr.target instanceof Expr.VariableExpr) {
            Token lvalue = ((Expr.VariableExpr) expr.target).name;
            Variable.Variant right = this.evaluate(expr.value);

            Integer distance = this.locals.get(expr.target);

            Variable identifier;

            if (distance != null) {
                identifier = this.curEnv.getAt(distance, lvalue.lexeme);
            } else {
                identifier = this.curEnv.getValue(lvalue);
            }
            if(identifier.isConst) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Cannot assign to constant variable '" + lvalue.lexeme + "'."
                );
            }

            if(!Interpreter.typeChecker(identifier, right)) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Type mismatch: cannot assign value of type '" +
                                right.getType() +
                                "' to variable '" +
                                lvalue.lexeme +
                                "' of type '" +
                                identifier.getType() +
                                "'."
                );
            }

            switch (expr.op.type) {
                case ASSIGN -> {
                    Variable.Variant assigned;

                    if (right.isRuntimeObject()) {
                        assigned = right;
                    } else {
                        assigned = new Variable.Variant(right.value);
                    }

                    this.curEnv.assign(lvalue, assigned);
                    return assigned;
                }
                case PLUS_ASSIGN -> {
                    Variable.Variant left = identifier.value;
                    Variable.Variant result;
                    if(left.isInt() && right.isInt())
                        result = new Variable.Variant(left.asInt() + right.asInt());
                    else if(left.isNumber() && right.isNumber())
                        result = new Variable.Variant(left.asNumber() + right.asNumber());
                    else if(left.isString() && right.isString())
                        result = new Variable.Variant(new yString.yStringInstance(left.asString() + right.asString()));
                    else if(left.isNumber() && right.isString())
                        result = new Variable.Variant(new yString.yStringInstance((left.isInt() ? left.asInt().toString() : left.asNumber().toString()) + right.asString()));
                    else if(left.isString() && right.isNumber())
                        result = new Variable.Variant(new yString.yStringInstance(left.asString() + (right.isInt() ? right.asInt().toString() : right.asNumber().toString())));

                    else
                        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                -1,
                                "Operator '+=' requires numeric or string operands.");

                    curEnv.assign(lvalue, result);
                    return result;
                }
                case MINUS_ASSIGN -> {
                    Variable.Variant left = identifier.value;
                    requireNumberOperands(left, right, expr.op);
                    Variable.Variant result;
                    if(left.isInt() && right.isInt())
                        result = new Variable.Variant(left.asInt() - right.asInt());
                    else
                        result = new Variable.Variant(left.asNumber() - right.asNumber());

                    curEnv.assign(lvalue, result);
                    return result;
                }
                case MULTIPLY_ASSIGN -> {
                    Variable.Variant left = identifier.value;
                    requireNumberOperands(left, right, expr.op);
                    Variable.Variant result;
                    if(left.isInt() && right.isInt())
                        result = new Variable.Variant(left.asInt() * right.asInt());
                    else
                        result = new Variable.Variant(left.asNumber() * right.asNumber());

                    curEnv.assign(lvalue, result);
                    return result;
                }
                case DIVIDE_ASSIGN -> {
                    Variable.Variant left = identifier.value;
                    requireNumberOperands(left, right, expr.op);
                    if(right.asNumber() == 0)
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                expr.op.line,
                                "Division by zero."
                        );
                    Variable.Variant result;
                    if(left.isInt() && right.isInt())
                        result = new Variable.Variant(left.asInt() / right.asInt());
                    else
                        result = new Variable.Variant(left.asNumber() / right.asNumber());

                    curEnv.assign(lvalue, result);
                    return result;
                }
                case MODULO_ASSIGN -> {
                    Variable.Variant left = identifier.value;
                    requireNumberOperands(left, right, expr.op);
                    Variable.Variant result;
                    if(left.isInt() && right.isInt())
                        result = new Variable.Variant(left.asInt() % right.asInt());
                    else
                        result = new Variable.Variant(left.asNumber() % right.asNumber());

                    curEnv.assign(lvalue, result);
                    return result;
                }
                case LEFT_SHIFT_ASSIGN -> {
                    Variable.Variant left = identifier.value;
                    requireIntegerOperands(left, right, expr.op);
                    Variable.Variant result;
                    result = new Variable.Variant(left.asInt() << right.asInt());
                    curEnv.assign(lvalue, result);
                    return result;
                }
                case RIGHT_SHIFT_ASSIGN -> {
                    Variable.Variant left = identifier.value;
                    requireIntegerOperands(left, right, expr.op);
                    Variable.Variant result;
                    result = new Variable.Variant(left.asInt() >> right.asInt());
                    curEnv.assign(lvalue, result);
                    return result;
                }
                case BITWISE_AND_ASSIGN -> {
                    Variable.Variant left = identifier.value;
                    requireIntegerOperands(left, right, expr.op);
                    Variable.Variant result;
                    result = new Variable.Variant(left.asInt() & right.asInt());
                    curEnv.assign(lvalue, result);
                    return result;
                }
                case BITWISE_OR_ASSIGN -> {
                    Variable.Variant left = identifier.value;
                    requireIntegerOperands(left, right, expr.op);
                    Variable.Variant result;
                    result = new Variable.Variant(left.asInt() | right.asInt());
                    curEnv.assign(lvalue, result);
                    return result;
                }
                case BITWISE_XOR_ASSIGN -> {
                    Variable.Variant left = identifier.value;
                    requireIntegerOperands(left, right, expr.op);
                    Variable.Variant result;
                    result = new Variable.Variant(left.asInt() ^ right.asInt());
                    curEnv.assign(lvalue, result);
                    return result;
                }
                default -> {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            expr.op.line,
                            "Unsupported assignment operator '" + expr.op.lexeme + "'."
                    );
                }
            }
        }
        else {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.op.line,
                    "Invalid assignment target. Left-hand side of assignment must be a variable."
            );
        }
    }

    @Override
    public Variable.Variant visitLogicalExpr(Expr.LogicalExpr expr) {
        if (expr.op.type == Token.TokenType.LOGICAL_AND) {
            Variable.Variant left = evaluate(expr.left);
            if (!left.isTruthy()) {
                return new Variable.Variant(false); // short-circuit
            }
            Variable.Variant right = evaluate(expr.right);
            return new Variable.Variant(right.isTruthy());
        }

        if (expr.op.type == Token.TokenType.LOGICAL_OR) {
            Variable.Variant left = evaluate(expr.left);
            if (left.isTruthy()) {
                return new Variable.Variant(true); // short-circuit
            }
            Variable.Variant right = evaluate(expr.right);
            return new Variable.Variant(right.isTruthy());
        }

        // throw error
        return new Variable.Variant(null);
    }

    @Override
    public Variable.Variant visitGroupingExpr(Expr.GroupingExpr expr) {
        return this.evaluate(expr.expression);
    }

    @Override
    public Variable.Variant visitGetExpr(Expr.GetExpr expr) {

        Variable.Variant object = evaluate(expr.object);
        boolean isStatic = object.isClass();

        if (!object.isRuntimeObject()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.name.line,
                    "Only objects have properties."
            );
        }

        RuntimeObject instance = object.asRuntimeObject();

        Variable field = instance.get(expr.name.lexeme);

        if (field == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.name.line,
                    "Undefined property '" + expr.name.lexeme + "'."
            );
        }

        // class itself does not need this keyword only insntaces need this keyword
        if(!isStatic) {
            if (field.value.isNativeFunction()) {
                Function.NativeFunction fn = field.value.asNativeFunction();

                BoundNativeFunction bound =
                        new BoundNativeFunction(fn, instance, "this");

                return new Variable.Variant(bound);
            }

            if (field.value.isFunctionOverload()) {
                Function.FunctionOverload fn = field.value.asFunctionOverload();

                BoundNativeFunction bound =
                        new BoundNativeFunction(fn, instance, "this");

                return new Variable.Variant(bound);
            }
        }


        return field.value;
    }

    @Override
    public Variable.Variant visitSetExpr(Expr.SetExpr expr) {
        Variable.Variant variant = this.evaluate(expr.object);
        if(!(variant.isClass() || variant.isClassInstance())) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.name.line,
                    "Only objects and class instances can have fields assigned."
            );
        }

        Variable var = variant.asRuntimeObject().assign(expr.name.lexeme, this.evaluate(expr.value));
        return var.value;
    }

    @Override
    public Variable.Variant visitCallExpr(Expr.CallExpr expr) {
        Variable.Variant calee = this.evaluate(expr.callee);

        if(calee.isNull() && expr.callee instanceof Expr.GetExpr &&
                ((Expr.GetExpr) expr.callee).isOptional) {
            return new Variable.Variant(null);
        }

        if(!calee.isCallable()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.leftParen.line,
                    "Attempted to call a non-callable value of type '"
                            + calee.getType() + "'."
            );
        }

        List<Variable.Variant> args = new ArrayList<>();
        for(Expr expr_ : expr.arguments) {
            args.add(evaluate(expr_));
        }

        if(calee.isFunctionOverload()) {
            calee = calee.asFunctionOverload().getFunction(args.size());
        }

        if(!calee.isFunctionLike()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.leftParen.line,
                    "Value of type '" + calee.getType() + "' cannot be invoked like a function."
            );
        }

        return calee.asCallable().call(this, args);
    }

    @Override
    public Variable.Variant visitLiteralExpr(Expr.LiteralExpr expr) {

        Object lit = expr.token.literal;

        if (lit == null)
            return new Variable.Variant(null);

        if (lit instanceof Token.Literal.Int i)
            return new Variable.Variant(i.value());

        if (lit instanceof Token.Literal.Double d)
            return new Variable.Variant(d.value());

        if (lit instanceof Token.Literal.Bool b)
            return new Variable.Variant(b.value());

        if (lit instanceof Token.Literal.Chr c)
            return new Variable.Variant(c.value());

        if (lit instanceof Token.Literal.Str s) {
            yString.yStringInstance object = new yString.yStringInstance(s.value());
            return new Variable.Variant(object);
        }

        if (lit instanceof Token.Literal.Null n) {
            return new Variable.Variant(null);
        }

        throw new IllegalStateException(
                "Unknown literal type: " + expr.token.literal
        );
    }

    @Override
    public Variable.Variant visitVariableExpr(Expr.VariableExpr expr) {

        Integer distance = this.locals.get(expr);

        if (distance != null) {
            return ((Variable)this.curEnv.getAt(distance, expr.name.lexeme)).value;
        } else {
            return ((Variable)this.curEnv.getValue(expr.name)).value;
        }
    }

    @Override
    public Variable.Variant visitArrayInitializerExpr(Expr.ArrayInitializerExpr expr) {
        ArrayList<Variable.Variant> data = new ArrayList<>();
        for(int i = 0; i < expr.elements.size(); i++) {
            data.add(evaluate(expr.elements.get(i)));
        }
        yArray.yArrayInstance y_array = new yArray.yArrayInstance(data);

        return new Variable.Variant(y_array);
    }

    @Override
    public Variable.Variant visitMapInitializerExpr(Expr.MapInitializerExpr expr) {
        HashMap<Variable.Variant, Variable.Variant> hashMap = new HashMap<>();
        for(Expr.MapInitializerExpr.Entry entry : expr.entries) {
            if(entry.key.literal  instanceof Token.Literal.Str) {
                String key = ((Token.Literal.Str) entry.key.literal).value();
                yString.yStringInstance stringObj = new yString.yStringInstance(key);
                hashMap.put(new Variable.Variant(stringObj), evaluate(entry.value));
            }
            else {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        expr.leftCurlyBrace.line,
                        "HashMap initializer shortcut only support string keys.");
            }
        }

        return new Variable.Variant(new yHashMap.yHashMapInstance(hashMap));
    }

    @Override
    public Variable.Variant visitLambdaExpr(Expr.LambdaExpr expr) {
        Function.LambdaObject lambdaObject = new Function.LambdaObject(expr, curEnv);
        return new Variable.Variant(lambdaObject);
    }

    @Override
    public Variable.Variant visitNexExpr(Expr.NewExpr expr) {

        Variable.Variant callee = evaluate(expr.qualifiedName);

        int errorLine = -1;
        if (expr.qualifiedName instanceof Expr.CallExpr callExpr) {
            errorLine = callExpr.leftParen.line;
        } else if (expr.qualifiedName instanceof Expr.GetExpr getExpr) {
            errorLine = getExpr.name.line;
        }

        if (!callee.isCallable()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    errorLine,
                    "Attempted to call a non-callable value of type '" +
                            callee.getType() + "'."
            );
        }

        if (!callee.isClass()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    errorLine,
                    "Attempted to instantiate a non-class value of type '" +
                            callee.getType() + "'."
            );
        }

        List<Variable.Variant> args = new ArrayList<>();
        for (Expr arg : expr.arguments) {
            args.add(evaluate(arg));
        }

        return callee.asCallable().call(this, args);
    }

    @Override
    public Variable.Variant visitSuperCallExpr(Expr.SuperCallExpr expr) {
        Variable thisVar = curEnv.getValue("this");
        Variable superVar = curEnv.getValue("super");

        if (thisVar == null || !thisVar.value.isClassInstance()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.leftParen.line,
                    "super() can only be used inside class constructor."
            );
        }

        if (superVar == null || !superVar.value.isClass()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.leftParen.line,
                    "Class has no superclass."
            );
        }

        yClass.ClassObjectInstance instance =
                thisVar.value.asClassInstance();

        yClass.ClassObject superConstructor = superVar.value.asClass();
        List<Variable.Variant> superArgs = new ArrayList<>();
        for(Expr expr_ : expr.arguments) {
            superArgs.add(this.evaluate(expr_));
        }

        Variable.Variant superInstanceVariant = superConstructor.call(this, superArgs);
        if(!superInstanceVariant.isClassInstance()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.leftParen.line,
                    "Superclass constructor did not return a valid class instance."
            );
        }

        yClass.ClassObjectInstance superInstance = superInstanceVariant.asClassInstance();

        for(var field : superInstance.fields.entrySet()) {
            if (instance.get(field.getKey()) == null) {
                instance.set(field.getKey(), field.getValue());
            }
        }

        return new Variable.Variant(null);
    }

    @Override
    public Variable.Variant visitRangeExpr(Expr.RangeExpr expr) {
        Variable.Variant startVar = this.evaluate(expr.start);
        Variable.Variant endVar = this.evaluate(expr.end);

        if(!startVar.isInt()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.operator.line,
                    "Range start must be an 'int', but found: " + startVar.getType()
            );
        }

        if(!endVar.isInt()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.operator.line,
                    "Range end must be an 'int', but found: " + endVar.getType()
            );
        }

        return new Variable.Variant(new Range.RangeValue(startVar.asInt(), endVar.asInt()));
    }

    // stmt visitor


    @Override
    public void visitPrintStmt(Stmt.PrintStmt stmt) {
        Variable.Variant value = evaluate(stmt.expr);
        if(value == null || value.isNull()) System.out.print("null");
        else System.out.print(value.toString());
    }

    @Override
    public void visitPrintlnStmt(Stmt.PrintlnStmt stmt) {
        Variable.Variant value = evaluate(stmt.expr);
        if(value == null || value.isNull()) System.out.println("null");
        else System.out.println(value.toString());
    }

    @Override
    public void visitBlockStmt(Stmt.BlockStmt stmt) {
        executeBlock(stmt, new Environment(curEnv));
    }

    @Override
    public void visitIfStmt(Stmt.IfStmt stmt) {
        Variable.Variant condition= this.evaluate(stmt.condition);
        if(condition.isTruthy()) {
            this.execute(stmt.then);
            return;
        }

        for(int i = 0; i < stmt.elifStmtList.size(); i++) {
            condition = this.evaluate(stmt.elifStmtList.get(i).condition);
            if(condition.isTruthy()) {
                this.execute(stmt.elifStmtList.get(i).then);
                return;
            }
        }

        if(stmt.else_ != null) {
            this.execute(stmt.else_);
        }
    }

    @Override
    public void visitWhileStmt(Stmt.WhileStmt stmt) {
        while (evaluate(stmt.condition).isTruthy()) {
            try {
                this.execute(stmt.stmt);
            } catch (Signal.ContinueSignal c) {
                continue;
            } catch (Signal.BreakSignal b) {
                break;
            }
        }
    }

    @Override
    public void visitExprStmt(Stmt.ExprStmt stmt) {
        this.evaluate(stmt.expr);
    }

    @Override
    public void visitForStmt(Stmt.ForStmt stmt) {

        Environment previous = this.curEnv;
        this.curEnv = new Environment(previous);

        try {

            if (stmt.initializer != null) {
                stmt.initializer.accept(this);
            }

            while (stmt.condition == null ||
                    evaluate(stmt.condition).isTruthy()) {

                try {
                    stmt.body.accept(this);
                } catch (Signal.ContinueSignal c) {
                    // continue loop
                } catch (Signal.BreakSignal b) {
                    break;
                }

                if (stmt.increment != null) {
                    evaluate(stmt.increment);
                }
            }

        } finally {
            this.curEnv = previous;
        }
    }

    @Override
    public void visitForInStmt(Stmt.ForInStmt stmt) {
        Environment previous = this.curEnv;
        this.curEnv = new Environment(previous);

        try {

            String typeName = stmt.declaration.type == null ? "any" : stmt.declaration.type.lexeme;
            if (!typeName.equals("int") && !typeName.equals("any") && !typeName.equals("number")) {
                throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, -1,
                        "For-in loop variable : " + stmt.declaration.identifier  + " must be 'int' or 'number' for a range, but found: " + typeName);
            }

            if (stmt.declaration.initializer != null) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        stmt.declaration.identifier.line,
                        "Variable '" + stmt.declaration.identifier.lexeme +
                                "' in for-in loop cannot have an initial value. It will be assigned values from the range."
                );
            }

            Variable.Variant range = this.evaluate(stmt.iterable);
            if(!(range.value instanceof Range.RangeValue)) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        stmt.declaration.identifier.line,
                        "The expression after 'in' must result in a range (e.g., 1..10), but found: " +
                                (range.value == null ? "null" : range.value.getClass().getSimpleName())
                );
            }

            Range.RangeValue rangeValue = (Range.RangeValue) range.value;

            Variable iterVar =  new Variable(
                    new Variable.Variant(null),
                    false,
                    typeName
            );

            this.curEnv.define(stmt.declaration.identifier.lexeme, iterVar);
            int start = rangeValue.start; // inclusive
            int end = rangeValue.end; // inclusive

            iterVar.value = new Variable.Variant(start);

            while (iterVar.value.asInt() <= end) {

                try {
                    this.execute(stmt.body);
                } catch (Signal.ContinueSignal c) {
                    // continue loop
                } catch (Signal.BreakSignal b) {
                    break;
                }

                iterVar.value = new Variable.Variant(iterVar.value.asInt() + 1);

            }

        } finally {
            this.curEnv = previous;
        }
    }

    @Override
    public void visitForEachStmt(Stmt.ForEachStmt stmt) {
        Environment previous = this.curEnv;
        this.curEnv = new Environment(previous);

        try {
            stmt.declaration.accept(this);

            Variable.Variant iterableVariant =
                    stmt.iterable.accept(this);

            if(!iterableVariant.isClassLike()) {
                throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                        -1, "iterable should be calss");
            }

            RuntimeObject iterable = iterableVariant.asRuntimeObject();
            Variable iterFnVar = iterable.get("iter");

            if(iterFnVar == null || !iterFnVar.value.isNativeFunction()) {
                throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, -1 ,
                        "iter should be function that returns iterator object to use foreach loop");
            }

            Function iterFn = new BoundNativeFunction(iterFnVar.value.asNativeFunction(),
                    iterable
                    ,"this");

            Variable.Variant iteratorVariant =  iterFn.call(this, new ArrayList<>());

            if(iteratorVariant == null || !iteratorVariant.isClassLike()) {
                throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, -1 ,
                        "iterator should be class");
            }

            RuntimeObject iterator = iteratorVariant.asRuntimeObject();

            Variable getNextFnVar = iterator.get("getNext");

            if(getNextFnVar == null || !getNextFnVar.value.isNativeFunction()) {
                throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, -1 ,
                        "getNext should be function that returns iterator object to use foreach loop");
            }

            Function getNextFn= new BoundNativeFunction(getNextFnVar.value.asNativeFunction(),
                    iterator,
                    "this");

            while (true) {
                Variable.Variant nextVariant = getNextFn.call(this, new ArrayList<>());

                if(nextVariant.isNull()) break;

                String typeName = stmt.declaration.type == null ? "any" :
                        stmt.declaration.type.lexeme;

                if(!Interpreter.typeChecker(typeName, nextVariant)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            stmt.declaration.type.line,
                            "Type mismatch. Cannot assign value of type '" +
                                    nextVariant.getType() +
                                    "' to variable '" +
                                    stmt.declaration.identifier.lexeme +
                                    "' of type '" +
                                    stmt.declaration.type.lexeme + "'."
                    );
                }

                this.curEnv.assign(stmt.declaration.identifier, nextVariant);

                try {
                    stmt.body.accept(this);
                } catch (Signal.ContinueSignal c) {
                    // continue loop
                } catch (Signal.BreakSignal b) {
                    break;
                }
            }

        }
        finally {
            this.curEnv = previous;
        }

    }

    @Override
    public void visitBreakStmt(Stmt.BreakStmt stmt) throws Signal.BreakSignal {
        throw new Signal.BreakSignal();
    }

    @Override
    public void visitContinueStmt(Stmt.ContinueStmt stmt) throws Signal.ContinueSignal {
        throw new Signal.ContinueSignal();
    }

    @Override
    public void visitReturnStmt(Stmt.ReturnStmt stmt) {
        Variable.Variant value = this.evaluate(stmt.expr);
        throw new Signal.ReturnSignal(value);
    }

    @Override
    public void visitSwitchStmt(Stmt.SwitchStmt stmt) {

        Variable.Variant switchValue = evaluate(stmt.condition);

        boolean executing = false;

        try {

            for (Stmt.SwitchStmt.CaseClause caseClause : stmt.cases) {

                if (!executing) {
                    Variable.Variant caseValue = evaluate(caseClause.matchExpr);

                    if (switchValue.equals(caseValue)) {
                        executing = true;
                    }
                }

                if (executing) {
                    execute(caseClause.block);
                }
            }

            // default case
            if (!executing && stmt.defaultClause != null) {
                execute(stmt.defaultClause);
            }

        } catch (Signal.BreakSignal signal) {
            // break Fallthrough
        }
    }

    @Override
    public void visitThrowStmt(Stmt.ThrowStmt stmt) {
        Variable.Variant variant = this.evaluate(stmt.expr);
        throw new Signal.ThrowSignal(variant);
    }

    @Override
    public void visitTryStmt(Stmt.TryStmt stmt) {

        try {
            this.execute(stmt.tryBlock);
        }catch (Signal.ThrowSignal sig) {
            Environment oldEnv = this.curEnv;
            try {
                Environment newEnv = new Environment(curEnv);
                newEnv.define(stmt.errIdentifier.lexeme, new Variable(sig.value, true, "any"));
                this.curEnv = newEnv;
                this.execute(stmt.catchBlock);
            }
            finally {
                this.curEnv = oldEnv;
            }

        }
        finally {

            if(stmt.finallyBlock != null) {
                this.execute(stmt.finallyBlock);
            }
        }
    }

    // declaration visitor

    @Override
    public void visitVarDeclaration(Stmt.VarDeclaration stmt) {
        Variable.Variant value = stmt.initializer != null ?
                this.evaluate(stmt.initializer) : null;

        String typeTag = stmt.type != null ? stmt.type.lexeme : "any";

        if (value != null && !Interpreter.typeChecker(typeTag, value)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    stmt.identifier.line,
                    "Type mismatch. Cannot assign value of type '" +
                            value.getType() +
                            "' to variable '" +
                            stmt.identifier.lexeme +
                            "' of type '" +
                            typeTag + "'."
            );
        }

        Variable var = new Variable(
                value == null ? new Variable.Variant(null) :new Variable.Variant(value.value),
                false,
                typeTag,
                true);


        Variable existing = this.curEnv.getAtOrDefault(0, stmt.identifier.lexeme);
        if(existing != null && existing.enableRedeclare) {
            this.curEnv.removeAt(0, stmt.identifier.lexeme);
        }

        this.curEnv.define(stmt.identifier.lexeme, var);

        if(stmt.isExported) {
            this.exports.add(stmt.identifier.lexeme);
        }
    }

    @Override
    public void visitLetDeclaration(Stmt.LetDeclaration stmt) {
        Variable.Variant value = stmt.initializer != null ?
                this.evaluate(stmt.initializer) : null;

        String typeTag = stmt.type != null ? stmt.type.lexeme : "any";

        if (value != null && !Interpreter.typeChecker(typeTag, value)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    stmt.identifier.line,
                    "Type mismatch. Cannot assign value of type '" +
                            value.getType() +
                            "' to variable '" +
                            stmt.identifier.lexeme +
                            "' of type '" +
                            typeTag + "'."
            );
        }

        Variable var = new Variable(
                value == null ? new Variable.Variant(null) :new Variable.Variant(value.value),
                false,
                typeTag,
                false);

        this.curEnv.define(stmt.identifier.lexeme, var);

        if(stmt.isExported) {
            this.exports.add(stmt.identifier.lexeme);
        }
    }

    @Override
    public void visitFunctionDeclaration(Stmt.FunctionDeclaration stmt) {
        Function.FunctionObject funObj =
                new Function.FunctionObject(
                        stmt,
                        curEnv);

        int argSize = Function.getArgCount(funObj);


        String name = funObj.declaration.name.lexeme;
        if(curEnv.exists(name)) {
            Variable.Variant prevFn = curEnv.getValue(name).value;
            int prevArgSize = Function.getArgCount(prevFn.asCallable());

            if(prevFn.isFunctionOverload()) {
                Function.FunctionOverload overload = prevFn.asFunctionOverload();
                overload.addFunction(new Variable.Variant(funObj), argSize, stmt.isExported);
            }
            else if(prevFn.isFunctionLike()){
                boolean isPrevExported =  this.exports.contains(stmt.name.lexeme);
                this.curEnv.remove(name);
                Function.FunctionOverload overload = new Function.FunctionOverload(name);
                overload.addFunction(new Variable.Variant(funObj), argSize, stmt.isExported);
                overload.addFunction(prevFn, prevArgSize, isPrevExported);
                Variable var = new Variable(new Variable.Variant(overload),
                        false,
                        "function");
                this.curEnv.define(funObj.declaration.name.lexeme,  var);
            }
            else {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        -1,
                        "Variable '" +
                                name +
                                "' is already defined in this scope."
                );
            }

        }else {
            Variable var = new Variable(new Variable.Variant(funObj),
                    false,
                    "function");
            this.curEnv.define(funObj.declaration.name.lexeme,  var);
        }


        // if function or functionOverload is exported mark its name to exported map
        //if functionOverload is exported mark exported to overload objects map as well
        if(stmt.isExported && !this.exports.contains(stmt.name.lexeme)) {
            this.exports.add(stmt.name.lexeme);
        }
    }

    @Override
    public void visitConstDeclaration(Stmt.ConstDeclaration stmt) {
        Variable.Variant value = this.evaluate(stmt.initializer);

        String typeTag = stmt.type != null ? stmt.type.lexeme : "any";

        if (value != null && !Interpreter.typeChecker(typeTag, value)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    stmt.identifier.line,
                    "Type mismatch. Cannot assign value of type '" +
                            value.getType() +
                            "' to variable '" +
                            stmt.identifier.lexeme +
                            "' of type '" +
                            typeTag + "'."
            );
        }

        Variable var = new Variable(
                value == null ? new Variable.Variant(null) :new Variable.Variant(value.value),
                true,
                typeTag,
                false);

        this.curEnv.define(stmt.identifier.lexeme, var);

        if(stmt.isExported) {
            this.exports.add(stmt.identifier.lexeme);
        }
    }

    @Override
    public void visitClassDeclaration(Stmt.ClassDeclaration stmt) {

        if(stmt.methods.stream().filter(m -> m.name.lexeme.equals("constructor")).count() > 1) {
            throw  new YsharpError(YsharpError.YsharpErrorType.SYNTAX, -1,
                    "class : " +
                    stmt.name.lexeme + " cannot have more than one constructor.");
        }

        Stmt.ClassDeclaration.Method constructorFn =
                stmt.methods.stream()
                        .filter(m -> m.name.lexeme.equals("constructor"))
                        .findFirst()
                        .orElse(null);

        List<Stmt.ClassDeclaration.Method> staticMethods =
                    stmt.methods.stream().filter(m -> !m.name.lexeme.equals("constructor") &&
                            m.isStatic).toList();

        List<Stmt.ClassDeclaration.Method> instanceMethods =
                stmt.methods.stream().filter(m -> !m.name.lexeme.equals("constructor") &&
                        !m.isStatic).toList();

        List<Stmt.ClassDeclaration.Property> staticProperty =
                stmt.properties.stream().filter(m -> m.isStatic).toList();

        List<Stmt.ClassDeclaration.Property> instanceProperty =
                stmt.properties.stream().filter(m -> !m.isStatic).toList();


        // class constructor object
        yClass.ClassObject  klass = new yClass.ClassObject() {

            @Override
            public boolean isSealed() {
                return stmt.isSealed;
            }

            @Override
            public String getClassName() {
                return stmt.name.lexeme;
            }

            @Override
            public String toString() {
                return "<class:" + stmt.name.lexeme + ">";
            }

            @Override
            public int arity() {
                if(constructorFn == null) return 0;
                return constructorFn.params.size();
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                this.requireArity(
                        arguments,
                        this.arity(),
                        "Constructor called with incorrect number of arguments. Expected " + this.arity() + "."
                );

                yClass.ClassObjectInstance instance = new yClass.ClassObjectInstance() {
                    @Override
                    public boolean isTruthy() {
                        return true;
                    }

                    @Override
                    public String getType() {
                        return stmt.name.lexeme;
                    }

                    @Override
                    public String toString() {
                        return "<instance:" + stmt.name.lexeme + ">";
                    }
                };

                instance.prototype = this.InstancePrototype;


                // initialize fields from super class
                if(stmt.superName != null) {
                    // if super call explicitly override it must be first statement
                    Stmt.BlockStmt body = (Stmt.BlockStmt)constructorFn.body;
                    boolean explicitSuper = false;
                    for(Stmt stmt_ : body.stmtList) {
                        if(stmt_ instanceof Stmt.ExprStmt && ((Stmt.ExprStmt) stmt_).expr instanceof Expr.SuperCallExpr) {
                            explicitSuper = true;
                            break;
                        }
                    }

                    // super must be first
                    if(explicitSuper) {
                        Stmt stmt_ = body.stmtList.getFirst();
                        if(!(stmt_ instanceof Stmt.ExprStmt && ((Stmt.ExprStmt) stmt_).expr instanceof Expr.SuperCallExpr)) {
                            throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                                    -1,
                                    "super() must be the first statement in the constructor.");
                        }

                    }
                    if (!explicitSuper) {
                        // implicit super: call parent constructor with no args now
                        Variable parentClassVar = interpreter.curEnv.getValue(stmt.superName);
                        yClass.ClassObject parentClass = parentClassVar.value.asClass();

                        Variable.Variant superInstanceVariant =
                                parentClass.call(interpreter, new ArrayList<>());

                        yClass.ClassObjectInstance superInstance =
                                superInstanceVariant.asClassInstance();

                        for (var field : superInstance.fields.entrySet()) {
                            instance.set(field.getKey(), field.getValue());
                        }
                    }
                }

                // add instance properties of super class to child class

                for(var prop :   instanceProperty) {
                    instance.set(prop.name.lexeme,
                            new Variable(
                                    prop.initializer != null ?
                                            new Variable.Variant(interpreter.evaluate(prop.initializer).value):
                                            new Variable.Variant(null),
                                    prop.isConst,
                                    prop.type == null ? "any" :  prop.type.lexeme));
                }

                if(constructorFn != null) {
                    Environment newEnv = new Environment(curEnv);

                    // constructor parameters
                    for(int i = 0 ; i < this.arity(); i++) {
                        newEnv.define(constructorFn.params.get(i).name.lexeme, new Variable(
                                arguments.get(i),
                                true,
                                constructorFn.params.get(i).type == null ? "any" : constructorFn.params.get(i).type.lexeme
                        ));
                    }

                    // bind this
                    newEnv.define(
                            "this",
                            new Variable(
                                    new Variable.Variant(instance),
                                    true,
                                    stmt.name.lexeme
                            )
                    );

                    // bind super
                    if(stmt.superName != null) {

                        Variable parentClassVar = interpreter.curEnv.getValue(stmt.superName);
                        yClass.ClassObject parentClass = parentClassVar.value.asClass();

                        newEnv.define(
                                "super",
                                new Variable(
                                        new Variable.Variant(parentClass),
                                        true,
                                        stmt.superName.lexeme
                                )
                        );
                    }

                    interpreter.executeBlock(
                            (Stmt.BlockStmt) constructorFn.body,
                            newEnv
                    );
                }

                return new Variable.Variant(instance);
            }

            @Override
            public String getType() {
                return stmt.name.lexeme;
            }
        };

        klass.prototype = yClass.ClassPrototype;

        klass.superClassName = stmt.superName; // allowed to be null

        klass.closure = this.curEnv;


        // static methods reside in class itself
        HashMap<String, List<Function.NativeFunction>> staticMethodsFn = new HashMap<>();
        for(Stmt.ClassDeclaration.Method method : staticMethods) {
            if(staticMethodsFn.containsKey(method.name.lexeme)) {
                staticMethodsFn.get(method.name.lexeme).add(methodToNativeFn(method, klass.closure));
            }
            else {
                staticMethodsFn.computeIfAbsent(method.name.lexeme, k -> new ArrayList<>())
                        .add(methodToNativeFn(method, klass.closure ));
            }
        }

        for(String key: staticMethodsFn.keySet()) {
            List<Function.NativeFunction> list = staticMethodsFn.get(key);
            if(list.size() == 1) {
                klass.set(key, new Variable(
                        new Variable.Variant(list.get(0)),
                        true,
                        "function"
                ));
            }
            else {
                Function.FunctionOverload overload  = new Function.FunctionOverload(key);
                for(Function.NativeFunction nf : list) {
                    overload.addFunction(new Variable.Variant(nf), Function.getArgCount(nf));
                }
                klass.set(key, new Variable(
                        new Variable.Variant(overload),
                        true,
                        "function"
                ));
            }
        }
        //

        // static props reside in class constructor itself
        for(Stmt.ClassDeclaration.Property prop : staticProperty) {

            if(prop.isConst && prop.initializer == null) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SEMANTIC,
                        prop.name.line,
                        "Constant field '" + prop.name.lexeme + "' must be initialized."
                );
            }

            klass.set(prop.name.lexeme, new Variable(
                    prop.initializer == null ? new Variable.Variant(null) : new Variable.Variant(this.evaluate(prop.initializer).value),
                    prop.isConst,
                    prop.type == null ? "any" : prop.type.lexeme
            ));
        }

        klass.InstancePrototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__" + stmt.name.lexeme + "__";
            }

            @Override
            public String toString() {
                return "<prototype:" + stmt.name.lexeme + ">";
            }
        };

        // instance methods reside in instance prototype
        HashMap<String, List<Function.NativeFunction>> instanceMethodsFn = new HashMap<>();
        for(Stmt.ClassDeclaration.Method method : instanceMethods) {
            if(instanceMethodsFn.containsKey(method.name.lexeme)) {
                instanceMethodsFn.get(method.name.lexeme).add(methodToNativeFn(method, klass.closure));
            }
            else {
                instanceMethodsFn.computeIfAbsent(method.name.lexeme, k -> new ArrayList<>())
                        .add(methodToNativeFn(method, klass.closure));
            }
        }

        for(String key: instanceMethodsFn.keySet()) {
            List<Function.NativeFunction> list = instanceMethodsFn.get(key);
            if(list.size() == 1) {
                klass.InstancePrototype.set(key, new Variable(
                        new Variable.Variant(list.get(0)),
                        true,
                        "function"
                ));
            }
            else {
                Function.FunctionOverload overload  = new Function.FunctionOverload(key);
                for(Function.NativeFunction nf : list) {
                    overload.addFunction(new Variable.Variant(nf), Function.getArgCount(nf));
                }
                klass.InstancePrototype.set(key, new Variable(
                        new Variable.Variant(overload),
                        true,
                        "function"
                ));
            }
        }
        //

        if(constructorFn != null) {
            klass.constructor = methodToNativeFn(constructorFn, klass.closure);
            klass.InstancePrototype .set(constructorFn.name.lexeme, new Variable(
                    new Variable.Variant(methodToNativeFn(constructorFn, klass.closure)),
                    true,
                    "function"
            ));
        }

        if(stmt.superName != null) {
            Variable.Variant variant = this.curEnv.getValue(stmt.superName).value;
            if (!variant.isClass()) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        stmt.superName.line,
                        "Class '" + stmt.name.lexeme +
                                "' cannot extend non-class value '" +
                                stmt.superName.lexeme + "'."
                );
            }

            yClass.ClassObject superClass = variant.asClass();
            if(superClass.isSealed()) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        stmt.superName.line,
                        "Class '" + stmt.name.lexeme +
                                "' cannot extend sealed class '" +
                                stmt.superName.lexeme + "'."
                );
            }

            klass.InstancePrototype.prototype =  superClass.InstancePrototype;
        }
        else {
            klass.InstancePrototype.prototype = yClass.ClassPrototype; // root prototype
        }

        curEnv.define(klass.getClassName(), new Variable(new Variable.Variant(klass), true, klass.getClassName()));

        if(stmt.isExported) {
            this.exports.add(stmt.name.lexeme);
        }
    }

    private Function.NativeFunction methodToNativeFn(Stmt.ClassDeclaration.Method method,
                                                     Environment closure){

        return new Function.NativeFunction() {
            @Override
            public int arity() {
                return method.params.size();
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                try {

                    if (arguments.size() != method.params.size()) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                method.name.line,
                                method.name.lexeme + " Expected " + method.params.size() +
                                        " arguments but got " + arguments.size()
                        );
                    }


                    // this binding came from call expression but classes are using closures , so that when i create new env with closure this
                    // env loose this binding , bind this keyword to new env manually !!!
                    Environment newEnv = new Environment(closure);
                    Variable thisVar = interpreter.curEnv.getValueOrDefault("this");
                    if(thisVar != null) newEnv.define("this", thisVar); // class static methods do not need this binding

                    for(int i = 0; i < method.params.size(); i++) {

                        Stmt.ClassDeclaration.Method.Param param = method.params.get(i);
                        Variable.Variant arg = arguments.get(i);

                        String typeTag = param.type != null ? param.type.lexeme : "any";

                        if (!Interpreter.typeChecker(typeTag, arg)) {
                            throw new YsharpError(
                                    YsharpError.YsharpErrorType.PROCESS,
                                    method.name.line,
                                    "Parameter '" + param.name.lexeme +
                                            "' type mismatch. Expected '" +
                                            typeTag + "' but got '" +
                                            arg.getType() + "'."
                            );
                        }


                        newEnv.define(method.params.get(i).name.lexeme, new Variable(
                                arguments.get(i),
                                true,
                                typeTag
                        ));
                    }

                    interpreter.executeBlock((Stmt.BlockStmt) method.body, newEnv);
                }catch (Signal.ReturnSignal sig) {
                    String expectedType = "any";

                    if (method.returnType != null) {
                        expectedType = method.returnType.lexeme;
                    }

                    if (!Interpreter.typeChecker(expectedType, sig.value)) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                method.name.line,
                                "Return type mismatch. Expected '" +
                                        expectedType + "' but got '" +
                                        sig.value.getType() + "'."
                        );
                    }

                    return sig.value;
                }

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return method.name.lexeme;
            }

            @Override
            public String toString() {
                return "<function:" + method.name.lexeme + ">";
            }
        };

    }

    public static boolean typeChecker(Variable variable, Variable.Variant variant) {

        if(variant.isNull()) return true;

        String type = variable.typeTag;

        switch (type) {

            case "int":
                return variant.isInt();

            case "double":
                return variant.isDouble();

            case "number":
                return variant.isNumber();

            case "bool":
                return variant.isBoolean();

            case "char":
                return variant.isChar();

            case "any":
                return true;

            default:
                return variable.getType().equals(variant.getType());
        }
    }

    public static boolean typeChecker(String type, Variable.Variant variant) {
        if(variant.isNull()) return true;

        switch (type) {

            case "int":
                return variant.isInt();

            case "double":
                return variant.isDouble();

            case "number":
                return variant.isNumber();

            case "bool":
                return variant.isBoolean();

            case "char":
                return variant.isChar();

            case "any":
                return true;

            default:
                return type.equals(variant.getType());
        }
    }
}




