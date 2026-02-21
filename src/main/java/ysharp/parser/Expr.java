package ysharp.parser;


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
    }

    public abstract <R> R accept(Visitor<R> visitor);

    static final class BinaryExpr extends Expr {

        final Expr left;
        final Token op;
        final Expr right;

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

    static final class UnaryExpr extends Expr {

        final Token op;
        final Expr expr;

        UnaryExpr(Token op, Expr expr) {
            this.op = op;
            this.expr = expr;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    static final class TernaryExpr extends Expr {

        final Expr condition;
        final Expr thenBranch;
        final Expr elseBranch;

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

    static final class PostfixExpr extends Expr {

        final Expr operand;
        final Token op;

        PostfixExpr(Expr operand, Token op) {
            this.operand = operand;
            this.op = op;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitPostfixExpr(this);
        }
    }

    static final class AssignmentExpr extends Expr {

        final Expr target;
        final Token op;
        final Expr value;

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

    static final class LogicalExpr extends Expr {

        final Expr left;
        final Token op;
        final Expr right;

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

    static final class GroupingExpr extends Expr {

        final Expr expression;

        GroupingExpr(Expr expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
    }

    static class GetExpr extends Expr {
        final Expr object;
        final Token name;

        GetExpr(Expr object, Token name) {
            this.object = object;
            this.name = name;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGetExpr(this);
        }
    }

    static class SetExpr extends Expr {
        final Expr object;
        final Token name;
        final Expr value;

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

    static class CallExpr extends Expr {
        final Expr callee;
        final List<Expr> arguments;

        CallExpr(Expr callee, List<Expr> arguments) {
            this.callee = callee;
            this.arguments = arguments;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }
    }

    static final class LiteralExpr extends Expr {

        final Token token;

        LiteralExpr(Token token) {
            this.token = token;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }

    static final class VariableExpr extends Expr {

        final Token name;

        VariableExpr(Token name) {
            this.name = name;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }
    }

    static final class ArrayInitializerExpr extends Expr {

        final List<Expr> elements;

        ArrayInitializerExpr(List<Expr> elements) {
            this.elements = elements;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitArrayInitializerExpr(this);
        }
    }

    static final class MapInitializerExpr extends Expr {

        static final class Entry {
            final Token key;
            final Expr value;

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

}
