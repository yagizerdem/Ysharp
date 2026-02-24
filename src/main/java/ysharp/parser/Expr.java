package ysharp.parser;


import ysharp.YsharpError;
import ysharp.lexer.Token;

import java.util.List;

abstract public class Expr {

    public interface Visitor<R> {

        R visitBinaryExpr(BinaryExpr expr);
        R visitUnaryExpr(UnaryExpr expr);
        R visitTernaryExpr(TernaryExpr expr);
        R visitPostfixExpr(PostfixExpr expr);
        R visitAssignmentExpr(AssignmentExpr expr);
        R visitLogicalExpr(LogicalExpr expr);
        R visitGroupingExpr(GroupingExpr expr);
        R visitGetExpr(GetExpr expr);
        R visitSetExpr(SetExpr expr);
        R visitCallExpr(CallExpr expr);
        R visitLiteralExpr(LiteralExpr expr);
        R visitVariableExpr(VariableExpr expr);
        R visitArrayInitializerExpr(ArrayInitializerExpr expr);
        R visitMapInitializerExpr(MapInitializerExpr expr);
        R visitLambdaExpr(LambdaExpr expr);
    }

    public abstract <R> R accept(Visitor<R> visitor);

    public static final class BinaryExpr extends Expr {

        public final Expr left;
        public final Token op;
        public final Expr right;

        BinaryExpr(Expr left, Token op, Expr right) {
            this.left = left;
            this.op = op;
            this.right = right;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    public static final class UnaryExpr extends Expr {

        public final Token op;
        public final Expr expr;

        UnaryExpr(Token op, Expr expr) {
            this.op = op;
            this.expr = expr;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    public  static final class TernaryExpr extends Expr {

        public final Expr condition;
        public final Expr thenBranch;
        public final Expr elseBranch;

        TernaryExpr(Expr condition, Expr thenBranch, Expr elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitTernaryExpr(this);
        }
    }

    public static final class PostfixExpr extends Expr {

        public final Expr operand;
        public final Token op;

        PostfixExpr(Expr operand, Token op) {
            this.operand = operand;
            this.op = op;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitPostfixExpr(this);
        }
    }

    public  static final class AssignmentExpr extends Expr {

        public final Expr target;
        public final Token op;
        public final Expr value;

        AssignmentExpr(Expr target, Token op, Expr value) {
            this.target = target;
            this.op = op;
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignmentExpr(this);
        }
    }

    public static final class LogicalExpr extends Expr {

        public final Expr left;
        public final Token op;
        public final Expr right;

        LogicalExpr(Expr left, Token op, Expr right) {
            this.left = left;
            this.op = op;
            this.right = right;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalExpr(this);
        }
    }

    public  static final class GroupingExpr extends Expr {

        public final Expr expression;

        GroupingExpr(Expr expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
    }

    public static class GetExpr extends Expr {
        public final Expr object;
        public final Token name;

        GetExpr(Expr object, Token name) {
            this.object = object;
            this.name = name;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGetExpr(this);
        }
    }

    public static class SetExpr extends Expr {
        public final Expr object;
        public final Token name;
        public final Expr value;

        SetExpr(Expr object, Token name, Expr value) {
            this.object = object;
            this.name = name;
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitSetExpr(this);
        }
    }

    public static class CallExpr extends Expr {
        public final Expr callee;
        public final List<Expr> arguments;
        public final Token leftParen;

        CallExpr(Expr callee,
                 List<Expr> arguments,
                 Token leftParen) {
            this.callee = callee;
            this.arguments = arguments;
            this.leftParen = leftParen;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }
    }

    public static final class LiteralExpr extends Expr {

        public final Token token;

        LiteralExpr(Token token) {
            this.token = token;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }

    public static final class VariableExpr extends Expr {

        public final Token name;

        VariableExpr(Token name) {
            this.name = name;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }
    }

    public static final class ArrayInitializerExpr extends Expr {

        public final List<Expr> elements;

        ArrayInitializerExpr(List<Expr> elements) {
            this.elements = elements;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitArrayInitializerExpr(this);
        }
    }

    public static final class MapInitializerExpr extends Expr {

        static final class Entry {
            public final Token key;
            public final Expr value;

            Entry(Token key, Expr value) {
                this.key = key;
                this.value = value;
            }
        }

        final List<Entry> entries;

        MapInitializerExpr(List<Entry> entries) {
            this.entries = entries;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitMapInitializerExpr(this);
        }
    }

    public static final class LambdaExpr extends Expr {
        public final List<Expr.LambdaExpr.Param> params;
        public final Token returnType; // nullable
        public final Stmt body;
        public final Expr expr;
        public final Token leftParen;

        public LambdaExpr(List<Expr.LambdaExpr.Param> params,
                            Token returnType,
                          Stmt body,
                          Token leftParen) {
            this.params = params;
            this.returnType = returnType;
            this.body = body;
            this.expr = null;
            this.leftParen = leftParen;
        }

        public LambdaExpr(List<Expr.LambdaExpr.Param> params,
                          Token returnType,
                          Expr expr,
                          Token leftParen) {
            this.params = params;
            this.returnType = returnType;
            this.expr = expr;
            this.body = null;
            this.leftParen = leftParen;
        }

        public static class Param {

            public final Token name;
            public final Token type;

            public Param(Token name, Token type) {
                this.name = name;
                this.type = type;
            }
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLambdaExpr(this);
        }
    }

}
