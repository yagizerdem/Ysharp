package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.lexer.Token;
import ysharp.parser.Expr;
import ysharp.parser.Stmt;
import ysharp.parser.TypeTag;

import java.util.ArrayList;
import java.util.List;

public class Interpreter implements
        Expr.Visitor<Variable.Variant>,
        Stmt.Visitor {

    private Environment global;
    private Environment curEnv;

    public Interpreter() {
        this.global = new Environment();
        this.curEnv = global;
    }

    public void defineGlobal(String key, Variable variable) throws Exception {
        try {
            this.global.define(key, variable);
        }catch (YsharpError err) {
            throw new Exception("[Programmatic error] defining natives should not throw error");
        }
    }

    public Variable.Variant evaluate(Expr expr) {
        return expr.accept(this);
    }

    public void execute(Stmt stmt){
        stmt.accept(this);
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
        try {

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

        }catch (YsharpError err) {
            int a = 120;
        }
        return new Variable.Variant(null);
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
        try {
            if(expr.target instanceof Expr.VariableExpr) {
                Token lvalue = ((Expr.VariableExpr) expr.target).name;
                Variable.Variant right = this.evaluate(expr.value);

                switch (expr.op.type) {
                    case Token.TokenType.ASSIGN ->  {
                        this.curEnv.assign(lvalue, right);
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
                }
            }
            else {
                // throw error
            }
        }   catch (YsharpError err) {
            // throw error
        }

        return null;
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
        return null;
    }

    @Override
    public Variable.Variant visitSetExpr(Expr.SetExpr expr) {
        return null;
    }

    @Override
    public Variable.Variant visitCallExpr(Expr.CallExpr expr) {
        Variable.Variant calee = this.evaluate(expr.callee);

        if(!calee.isCallable()) {
            // throw error
            return null;
        }

        List<Variable.Variant> args = new ArrayList<>();
        for(Expr expr_ : expr.arguments) {
            args.add(evaluate(expr_));
        }

        try {
            return calee.asCallable().call(this, args);
        }catch (YsharpError err) {
            // throw error
        }

        return null;
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

        if (lit instanceof Token.Literal.Str s)
            return new Variable.Variant(s.value());

        // throw error here
        return  new  Variable.Variant(null);
    }

    @Override
    public Variable.Variant visitVariableExpr(Expr.VariableExpr expr) {
        try {
           return ((Variable)this.curEnv.getValue(expr.name)).value;
        }catch (YsharpError err) {
            // throw error
        }
        return null;
    }

    @Override
    public Variable.Variant visitArrayInitializerExpr(Expr.ArrayInitializerExpr expr) {
        return null;
    }

    @Override
    public Variable.Variant visitMapInitializerExpr(Expr.MapInitializerExpr expr) {
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
        for(int i = 0; i < stmt.stmtList.size(); i++) {
            this.execute(stmt.stmtList.get(i));
        }
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
        try {
            Variable.Variant value = stmt.initializer != null ?
                    this.evaluate(stmt.initializer) : null;

            String typeTag = stmt.type != null ? stmt.type.lexeme : "any";

            Variable var = new Variable(
                    value,
                    false,
                    TypeTag.fromString(typeTag));

            this.curEnv.define(stmt.identifier.lexeme, var);
        } catch (YsharpError err) {
            // throw error
        }
    }

    @Override
    public void visitFunctionDeclaration(Stmt.FunctionDeclaration stmt) {

    }
}
