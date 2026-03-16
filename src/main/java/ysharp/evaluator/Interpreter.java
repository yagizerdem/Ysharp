package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.evaluator.Native.Collections.yArray;
import ysharp.evaluator.Native.Collections.yHashTable;
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

                if (!(expr.expr instanceof Expr.VariableExpr)) {
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

                if (!(expr.expr instanceof Expr.VariableExpr)) {
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

        if (!(expr.operand instanceof Expr.VariableExpr)) {
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
                case ASSIGN ->  {
                    this.curEnv.assign(lvalue, right);
                    return right;
                }
                case PLUS_ASSIGN -> {
                    Variable.Variant left = identifier.value;
                    requireNumberOperands(left, right, expr.op);
                        Variable.Variant result;
                        if(left.isInt() && right.isInt())
                            result = new Variable.Variant(left.asInt() + right.asInt());
                        else
                            result = new Variable.Variant(left.asNumber() + right.asNumber());

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

        if (field.value.isNativeFunction()) {
            Function.NativeFunction fn = field.value.asNativeFunction();

            BoundNativeFunction bound =
                    new BoundNativeFunction(fn, instance, "this");

            return new Variable.Variant(bound);
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
        Hashtable<Variable.Variant, Variable.Variant> hashTable = new Hashtable<>();
        for(Expr.MapInitializerExpr.Entry entry : expr.entries) {
            if(entry.key.literal  instanceof Token.Literal.Str) {
                String key = ((Token.Literal.Str) entry.key.literal).value();
                yString.yStringInstance stringObj = new yString.yStringInstance(key);
                hashTable.put(new Variable.Variant(stringObj), evaluate(entry.value));
            }
            else {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        expr.leftCurlyBrace.line,
                        "HashTable initializer shortcut only support string keys.");
            }
        }

        return new Variable.Variant(new yHashTable.yMapInstance(hashTable));
    }

    @Override
    public Variable.Variant visitLambdaExpr(Expr.LambdaExpr expr) {
        Function.LambdaObject lambdaObject = new Function.LambdaObject(expr, curEnv);
        return new Variable.Variant(lambdaObject);
    }

    @Override
    public Variable.Variant visitNexExpr(Expr.NewExpr expr) {

        Variable.Variant callee = evaluate(expr.qualifiedName);

        if (!callee.isCallable()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    ((Expr.CallExpr)expr.qualifiedName).leftParen.line,
                    "Attempted to call a non-callable value of type '" +
                            callee.getType() + "'."
            );
        }

        if (!callee.isClass()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    ((Expr.CallExpr)expr.qualifiedName).leftParen.line,
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
            instance.set(field.getKey(), field.getValue());
        }

        return new Variable.Variant(null);
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
                value,
                false,
                typeTag);

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
                        curEnv); // take recursive deep copy to handle closures

        Variable var = new Variable(new Variable.Variant(funObj),
                false,
                "function");

        this.curEnv.define(funObj.declaration.name.lexeme,  var);

        if(stmt.isExported) {
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
                value,
                true,
                typeTag);

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

                for(var prop :   instanceProperty) {
                    instance.set(prop.name.lexeme,
                            new Variable(
                                    prop.initializer != null ?
                                            interpreter.evaluate(prop.initializer):
                                            new Variable.Variant(null),
                                                    prop.isConst,
                                                    prop.type == null ? "any" :  prop.type.lexeme));
                }

                // add instance properties of super class to child class

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

        // static methods reside in class constructor itself
        for(Stmt.ClassDeclaration.Method method : staticMethods) {
            klass.set(method.name.lexeme, new Variable(
                    new Variable.Variant(methodToNativeFn(method)),
                    true,
                    "function"
            ));
        }

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
                    prop.initializer == null ? new Variable.Variant(null) : this.evaluate(prop.initializer),
                    prop.isConst,
                    prop.type.lexeme
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
        for(Stmt.ClassDeclaration.Method method : instanceMethods) {
            klass.InstancePrototype .set(method.name.lexeme, new Variable(
                    new Variable.Variant(methodToNativeFn(method)),
                    true,
                    "function"
            ));
        }

        if(constructorFn != null) {
            klass.constructor = methodToNativeFn(constructorFn);
            klass.InstancePrototype .set(constructorFn.name.lexeme, new Variable(
                    new Variable.Variant(methodToNativeFn(constructorFn)),
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

    private Function.NativeFunction methodToNativeFn(Stmt.ClassDeclaration.Method method){

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

                    Environment newEnv = new Environment(curEnv);
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
                                method.params.get(i).type.lexeme
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
