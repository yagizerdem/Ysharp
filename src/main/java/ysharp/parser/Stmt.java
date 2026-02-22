package ysharp.parser;

import ysharp.lexer.Token;

import java.util.List;

public abstract class Stmt {

    public abstract void accept(Stmt.Visitor visitor);


    public interface Visitor {

        void visitPrintStmt(Stmt.PrintStmt stmt);
        void visitPrintlnStmt(Stmt.PrintlnStmt stmt);
        void visitBlockStmt(Stmt.BlockStmt stmt);
        void visitIfStmt(Stmt.IfStmt stmt);
        void visitWhileStmt(Stmt.WhileStmt stmt);
        void visitExprStmt(Stmt.ExprStmt stmt);
        void visitForStmt(Stmt.ForStmt stmt);

        void visitVarDeclaration(Stmt.VarDeclaration stmt);
    }


    // stmt

    public static class PrintStmt extends Stmt {
        public final Expr expr;

        PrintStmt(Expr expr){
            this.expr = expr;
        }

        @Override
        public void accept(Stmt.Visitor visitor) {
            visitor.visitPrintStmt(this);
        }
    }

    public static class PrintlnStmt extends Stmt {
        public final Expr expr;

        PrintlnStmt(Expr expr){
            this.expr = expr;
        }

        @Override
        public void accept(Stmt.Visitor visitor) {
            visitor.visitPrintlnStmt(this);
        }
    }

    public static class BlockStmt extends Stmt {
        public final List<Stmt> stmtList;

        BlockStmt(List<Stmt> stmtList){
            this.stmtList = stmtList;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visitBlockStmt(this);
        }
    }

    public static class IfStmt extends Stmt {
        public Expr condition;
        public Stmt then;
        public Stmt else_;
        public List<ElifStmt> elifStmtList;

        IfStmt(Expr condition,
               Stmt then,
               Stmt else_,
               List<ElifStmt> elifStmtList) {

            this.condition = condition;
            this.then = then;
            this.else_ = else_;
            this.elifStmtList = elifStmtList;
        }

        public static class ElifStmt {
            public final Expr condition;
            public final Stmt then;

            ElifStmt(Expr condition, Stmt then){
                this.condition = condition;
                this.then = then;
            }
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visitIfStmt(this);
        }
    }

    public static class WhileStmt extends Stmt {
        public final Expr condition;
        public final Stmt stmt;

        WhileStmt(Expr condition, Stmt stmt){
            this.condition = condition;
            this.stmt = stmt;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visitWhileStmt(this);
        }
    }

    public static class ExprStmt extends Stmt {
        public final Expr expr;

        public ExprStmt(Expr expr){
            this.expr = expr;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visitExprStmt(this);
        }
    }

    public static class ForStmt extends Stmt {

        public final Stmt initializer;
        public final Expr condition;
        public final Expr increment;
        public final Stmt body;

        public ForStmt(Stmt initializer,
                       Expr condition,
                       Expr increment,
                       Stmt body) {
            this.initializer = initializer;
            this.condition = condition;
            this.increment = increment;
            this.body = body;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visitForStmt(this);
        }
    }

    // declaration

    public static class VarDeclaration extends Stmt {
        public final Token identifier;
        public final Token typeTag;
        public final Expr initializer;

        public VarDeclaration(
                Token identifier,
                Token typeTag,
                Expr initializer
        ) {
            this.identifier = identifier;
            this.typeTag = typeTag;
            this.initializer = initializer;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visitVarDeclaration(this);
        }
    }

}
