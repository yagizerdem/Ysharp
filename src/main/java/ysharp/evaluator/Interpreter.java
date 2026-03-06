package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.evaluator.Native.Collections.Y_Array;
import ysharp.evaluator.Native.Collections.Y_HashTable;
import ysharp.evaluator.Native.function.binding.BoundNativeFunction;
import ysharp.lexer.Token;
import ysharp.parser.Expr;
import ysharp.parser.Stmt;
import ysharp.parser.TypeTag;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

public class Interpreter implements
        Expr.Visitor<Variable.Variant>,
        Stmt.Visitor {

    public Environment global;
    public Environment curEnv;
    public List<YsharpError> errors;

    public boolean hadErrors() {
        return !errors.isEmpty();
    }

    public Interpreter() {
        this.global = new Environment();
        this.curEnv = global;
        errors = new ArrayList<>();
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

    // expr visitor
    @Override
    public Variable.Variant visitBinaryExpr(Expr.BinaryExpr expr) {
        switch (expr.op.type) {
            case Token.TokenType.PLUS -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                if(left.canImplicitlyConvertNumber() && right.canImplicitlyConvertNumber()) {
                    if(left.isInt() && right.isInt()) return new Variable.Variant(left.asInt() + right.asInt());
                    double sum = left.implicitlyConvertNumber() + right.implicitlyConvertNumber();
                    return new Variable.Variant(sum);
                }

                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Operator '+' cannot be applied to types '"
                                + left.getType() + "' and '" + right.getType() + "'."
                );
            }
            case Token.TokenType.MINUS -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);

                if(left.canImplicitlyConvertNumber() && right.canImplicitlyConvertNumber()) {
                    if(left.isInt() && right.isInt()) return new Variable.Variant(left.asInt() - right.asInt());
                    double diff = left.implicitlyConvertNumber() - right.implicitlyConvertNumber();
                    return new Variable.Variant(diff);
                }

                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Operator '-' cannot be applied to types '"
                                + left.getType() + "' and '" + right.getType() + "'."
                );
            }
            case Token.TokenType.MULTIPLY -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                if(left.canImplicitlyConvertNumber() && right.canImplicitlyConvertNumber()) {
                    if(left.isInt() && right.isInt()) return new Variable.Variant(left.asInt() * right.asInt());

                    double mul = left.implicitlyConvertNumber() * right.implicitlyConvertNumber();
                    return new Variable.Variant(mul);
                }

                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Operator '*' cannot be applied to types '"
                                + left.getType() + "' and '" + right.getType() + "'."
                );
            }
            case Token.TokenType.DIVIDE -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                if(left.canImplicitlyConvertNumber() && right.canImplicitlyConvertNumber()) {
                    if(left.isInt() && right.isInt()) return new Variable.Variant(left.asInt() / right.asInt());
                    double div = left.implicitlyConvertNumber() / right.implicitlyConvertNumber();
                    return new Variable.Variant(div);
                }

                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Operator '/' cannot be applied to types '"
                                + left.getType() + "' and '" + right.getType() + "'."
                );
            }
            case Token.TokenType.MODULO -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                if(left.canImplicitlyConvertNumber() && right.canImplicitlyConvertNumber()) {
                    if (left.isInt() && right.isInt()) {
                        return new Variable.Variant(
                                left.asInt() % right.asInt()
                        );
                    }
                    double mod = left.implicitlyConvertNumber() % right.implicitlyConvertNumber();
                    return new Variable.Variant(mod);
                }

                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Operator '%' cannot be applied to types '"
                                + left.getType() + "' and '" + right.getType() + "'."
                );
            }
            case Token.TokenType.BITWISE_AND -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireIntegerOperands(left, right, expr.op);
                int result = left.asInt() & right.asInt();
                return new Variable.Variant(result);
            }
            case Token.TokenType.BITWISE_OR -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireIntegerOperands(left, right, expr.op);
                int result = left.asInt() | right.asInt();
                return new Variable.Variant(result);
            }
            case Token.TokenType.BITWISE_XOR -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireIntegerOperands(left, right, expr.op);
                int result = left.asInt() ^ right.asInt();
                return new Variable.Variant(result);
            }
            case Token.TokenType.LEFT_SHIFT -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireIntegerOperands(left, right, expr.op);
                int result = left.asInt() << right.asInt();
                return new Variable.Variant(result);
            }
            case Token.TokenType.RIGHT_SHIFT -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                requireIntegerOperands(left, right, expr.op);
                int result = left.asInt() >> right.asInt();
                return new Variable.Variant(result);
            }
            case Token.TokenType.GREATER_THAN -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                if(left.canImplicitlyConvertNumber() && right.canImplicitlyConvertNumber()) {
                    return new Variable.Variant(
                            left.implicitlyConvertNumber() >
                                    right.implicitlyConvertNumber()
                    );
                }

                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Operator '>' requires numeric operands. Found '" +
                                left.getType() + "' and '" +
                                right.getType() + "'."
                );
            }
            case Token.TokenType.GREATER_OR_EQUAL -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                if(left.canImplicitlyConvertNumber() && right.canImplicitlyConvertNumber()) {
                    return new Variable.Variant(
                            left.implicitlyConvertNumber() >=
                                    right.implicitlyConvertNumber()
                    );
                }

                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Operator '>=' requires numeric operands. Found '" +
                                left.getType() + "' and '" +
                                right.getType() + "'."
                );
            }
            case Token.TokenType.LESS_THAN -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                if(left.canImplicitlyConvertNumber() && right.canImplicitlyConvertNumber()) {
                    return new Variable.Variant(
                            left.implicitlyConvertNumber() <
                                    right.implicitlyConvertNumber()
                    );
                }

                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Operator '<' requires numeric operands. Found '" +
                                left.getType() + "' and '" +
                                right.getType() + "'."
                );
            }
            case Token.TokenType.LESS_OR_EQUAL -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                if(left.canImplicitlyConvertNumber() && right.canImplicitlyConvertNumber()) {
                    return new Variable.Variant(
                            left.implicitlyConvertNumber() <=
                                    right.implicitlyConvertNumber()
                    );
                }

                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Operator '<=' requires numeric operands. Found '" +
                                left.getType() + "' and '" +
                                right.getType() + "'."
                );
            }
            case Token.TokenType.EQUAL_EQUAL -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                return new Variable.Variant(
                        left.equals(right)
                );
            }
            case Token.TokenType.BANG_EQUAL -> {
                Variable.Variant left = evaluate(expr.left);
                Variable.Variant right = evaluate(expr.right);
                return new Variable.Variant(
                        !left.equals(right)
                );
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
            case Token.TokenType.PLUS -> {
                Variable.Variant var = this.evaluate(expr.expr);
                if(!var.isNumber()) {
                    // throw error
                    return null;
                }
                return var;
            }
            case Token.TokenType.MINUS -> {
                Variable.Variant var = this.evaluate(expr.expr);
                if(var.isInt()) {
                    return new Variable.Variant(var.asInt() * -1);
                }
                if(var.isDouble()) {
                    return new Variable.Variant(var.asDouble() * -1);
                }

                // throw error

                return null;
            }
            case Token.TokenType.BANG -> {
                Variable.Variant var = this.evaluate(expr.expr);
                return new Variable.Variant(!var.isTruthy());
            }
            case Token.TokenType.BITWISE_NOT -> {
                Variable.Variant var = this.evaluate(expr.expr);
                if(var.isInt()) {
                    return new Variable.Variant(~var.asInt());
                }
                // throw error
                return  null;
            }
        }

        return null;
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
        return null;
    }

    @Override
    public Variable.Variant visitAssignmentExpr(Expr.AssignmentExpr expr) {
        if(expr.target instanceof Expr.VariableExpr) {
            Token lvalue = ((Expr.VariableExpr) expr.target).name;
            Variable.Variant right = this.evaluate(expr.value);

            Variable identifier = this.curEnv.getValue(lvalue);
            if(identifier.isConst) {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        expr.op.line,
                        "Cannot assign to constant variable '" + lvalue.lexeme + "'."
                );
            }

            switch (expr.op.type) {
                case Token.TokenType.ASSIGN ->  {
                    this.curEnv.assign(lvalue, right);
                    return right;
                }
                case Token.TokenType.PLUS_ASSIGN -> {
                    Variable left = this.curEnv.getValue(lvalue);

                    if(left.value.isNumber() && right.isNumber()) {
                        Variable.Variant result;
                        if(left.value.isInt() && right.isInt())
                            result = new Variable.Variant(left.value.asInt() + right.asInt());
                        else
                            result = new Variable.Variant(left.value.asDouble() + right.asDouble());

                        curEnv.assign(lvalue, result);
                        return result;
                    }

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            expr.op.line,
                            "Operator '+=' cannot be applied to types '"
                                    + left.value.getType() + "' and '" + right.getType() + "'."
                    );
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
                    new BoundNativeFunction(fn, instance);

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
            Y_String.Y_StringObject object = new Y_String.Y_StringObject(s.value());
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
        return ((Variable)this.curEnv.getValue(expr.name)).value;
    }

    @Override
    public Variable.Variant visitArrayInitializerExpr(Expr.ArrayInitializerExpr expr) {
        ArrayList<Variable.Variant> data = new ArrayList<>();
        for(int i = 0; i < expr.elements.size(); i++) {
            data.add(evaluate(expr.elements.get(i)));
        }
        Y_Array.Y_ArrayObject y_array = new Y_Array.Y_ArrayObject(data);

        return new Variable.Variant(y_array);
    }

    @Override
    public Variable.Variant visitMapInitializerExpr(Expr.MapInitializerExpr expr) {
        Hashtable<Variable.Variant, Variable.Variant> hashTable = new Hashtable<>();
        for(Expr.MapInitializerExpr.Entry entry : expr.entries) {
            if(entry.key.literal  instanceof Token.Literal.Str) {
                String key = ((Token.Literal.Str) entry.key.literal).value();
                Y_String.Y_StringObject stringObj = new Y_String.Y_StringObject(key);
                hashTable.put(new Variable.Variant(stringObj), evaluate(entry.value));
            }
            else {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.SYNTAX,
                        expr.leftCurlyBrace.line,
                        "HashTable initializer shortcut only support string keys.");
            }
        }

        return new Variable.Variant(new Y_HashTable.Y_MapObject(hashTable));
    }

    @Override
    public Variable.Variant visitLambdaExpr(Expr.LambdaExpr expr) {
        Function.LambdaObject lambdaObject = new Function.LambdaObject(expr, curEnv);
        return new Variable.Variant(lambdaObject);
    }

    @Override
    public Variable.Variant visitNexExpr(Expr.NewExpr expr) {
        Variable.Variant calee = this.curEnv.getValue(expr.name).value;

        if(!calee.isCallable()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.name.line,
                    "Attempted to call a non-callable value of type '"
                            + calee.getType() + "'."
            );
        }

        if(!calee.isClass()) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    expr.name.line,
                    "Attempted to instantiate a non-class value of type '"
                            + calee.getType() + "'."
            );
        }


        List<Variable.Variant> args = new ArrayList<>();
        for(Expr expr_ : expr.arguments) {
            args.add(evaluate(expr_));
        }

        return calee.asCallable().call(this, args);
    }

    @Override
    public Variable.Variant visitSuperCallExpr(Expr.SuperCallExpr expr) {
        return null;
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

    // declaration visitor

    @Override
    public void visitVarDeclaration(Stmt.VarDeclaration stmt) {
        Variable.Variant value = stmt.initializer != null ?
                this.evaluate(stmt.initializer) : null;

        String typeTag = stmt.type != null ? stmt.type.lexeme : "any";

        Variable var = new Variable(
                value,
                false,
                TypeTag.fromString(typeTag));

        this.curEnv.define(stmt.identifier.lexeme, var);
    }

    @Override
    public void visitFunctionDeclaration(Stmt.FunctionDeclaration stmt) {
        Function.FunctionObject funObj =
                new Function.FunctionObject(
                        stmt,
                        curEnv); // take recursive deep copy to handle closures

        Variable var = new Variable(new Variable.Variant(funObj),
                false,
                TypeTag.OBJECT);

        this.curEnv.define(funObj.declaration.name.lexeme,  var);
    }

    @Override
    public void visitConstDeclaration(Stmt.ConstDeclaration stmt) {
        Variable.Variant value = this.evaluate(stmt.initializer);

        String typeTag = stmt.type != null ? stmt.type.lexeme : "any";

        Variable var = new Variable(
                value,
                true,
                TypeTag.fromString(typeTag));

        this.curEnv.define(stmt.identifier.lexeme, var);
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


        Y_Class.ClassObject  klass = new Y_Class.ClassObject() {
            @Override
            public boolean isSealed() {
                return stmt.isSealed;
            }

            @Override
            public String getClassName() {
                return stmt.name.lexeme;
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

                Y_Class.ClassObjectInstance instance = new Y_Class.ClassObjectInstance() {
                    @Override
                    public boolean isTruthy() {
                        return true;
                    }

                    @Override
                    public String getType() {
                        return stmt.name.lexeme;
                    }
                };

                // instance props reside in instance itself
                for(Stmt.ClassDeclaration.Property prop : instanceProperty) {

                    if(prop.isConst && prop.initializer == null) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.SEMANTIC,
                                prop.name.line,
                                "Constant field '" + prop.name.lexeme + "' must be initialized."
                        );
                    }

                    instance.set(prop.name.lexeme, new Variable(
                            prop.initializer == null ? new Variable.Variant(null) : interpreter.evaluate(prop.initializer),
                            prop.isConst,
                            prop.type == null ? TypeTag.ANY : TypeTag.fromString(prop.type.lexeme)
                    ));
                }

                instance.prototype = this.InstancePrototype;


                return new Variable.Variant(instance);
            }

            @Override
            public String getType() {
                return stmt.name.lexeme;
            }
        };


        // static methods reside in class constructor itself
        for(Stmt.ClassDeclaration.Method method : staticMethods) {
            klass.set(method.name.lexeme, new Variable(
                    new Variable.Variant(methodToNativeFn(method)),
                    true,
                    TypeTag.OBJECT
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
                    prop.type == null ? TypeTag.ANY : TypeTag.fromString(prop.type.lexeme)
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
        };

        // instance methods reside in instance prototype
        for(Stmt.ClassDeclaration.Method method : instanceMethods) {
            klass.InstancePrototype .set(method.name.lexeme, new Variable(
                    new Variable.Variant(methodToNativeFn(method)),
                    true,
                    TypeTag.OBJECT
            ));
        }

        if(constructorFn != null) {
            klass.constructor = methodToNativeFn(constructorFn);
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

            Y_Class.ClassObject superClass = variant.asClass();
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
            klass.InstancePrototype.prototype = Y_Class.ClassPrototype; // root prototype
        }

        curEnv.define(klass.getClassName(), new Variable(new Variable.Variant(klass), true, TypeTag.OBJECT));
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
                    Environment newEnv = new Environment(curEnv);
                    for(int i = 0; i < method.params.size(); i++) {
                        newEnv.define(method.params.get(i).name.lexeme, new Variable(
                                arguments.get(i),
                                true,
                                method.params.get(i).type == null ? TypeTag.ANY : TypeTag.fromString(method.params.get(i).type.lexeme)
                        ));
                    }

                    interpreter.executeBlock((Stmt.BlockStmt) method.body, newEnv);
                }catch (Signal.ReturnSignal sig) {
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

}
