package ysharp.parser;

import ysharp.lexer.Token;

import javax.print.attribute.standard.Finishings;
import java.security.PublicKey;
import java.security.spec.DSAPublicKeySpec;
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
        void visitBreakStmt(Stmt.BreakStmt stmt);
        void visitContinueStmt(Stmt.ContinueStmt stmt);
        void visitReturnStmt(Stmt.ReturnStmt stmt);
        void visitSwitchStmt(Stmt.SwitchStmt stmt);
        void visitThrowStmt(Stmt.ThrowStmt stmt);
        void visitTryStmt(Stmt.TryStmt stmt);

        void visitVarDeclaration(Stmt.VarDeclaration stmt);
        void visitFunctionDeclaration(Stmt.FunctionDeclaration stmt);
        void visitConstDeclaration(Stmt.ConstDeclaration stmt);
        void visitClassDeclaration(Stmt.ClassDeclaration stmt);
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

    public static class BreakStmt extends Stmt {

        @Override
        public void accept(Visitor visitor) {
            visitor.visitBreakStmt(this);
        }
    }

    public static class ContinueStmt extends Stmt {

        @Override
        public void accept(Visitor visitor) {
            visitor.visitContinueStmt(this);
        }
    }

    public static class ReturnStmt extends Stmt {
        public final Expr expr;

        public ReturnStmt(Expr expr){
            this.expr = expr;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visitReturnStmt(this);
        }
    }

    public static class SwitchStmt extends Stmt {

        public final Expr condition;
        public final List<CaseClause> cases;
        public final Stmt defaultClause;

        public static class CaseClause {

            public final Expr matchExpr;
            public final Stmt block;

            public CaseClause(Expr matchExpr, Stmt block) {
                this.matchExpr = matchExpr;
                this.block = block;
            }
        }

        public SwitchStmt(Expr condition,
                          List<CaseClause> cases,
                          Stmt defaultClause) {
            this.condition = condition;
            this.cases = cases;
            this.defaultClause = defaultClause;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visitSwitchStmt(this);
        }
    }

    public static class ThrowStmt extends Stmt {

        public final Token throwToken;
        public final Expr expr;

        public ThrowStmt(Token throwToken,
                         Expr expr) {
            this.throwToken = throwToken;
            this.expr =expr;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visitThrowStmt(this);
        }
    }

    public static class TryStmt extends Stmt {

        public final Token tryToken;
        public final Stmt tryBlock;
        public final Token errIdentifier;
        public final Stmt catchBlock;
        public final Stmt finallyBlock;

        public TryStmt(Token tryToken,
                       Stmt tryBlock,
                       Token errIdentifier,
                       Stmt catchBlock,
                       Stmt finallyBlock) {

            this.tryToken = tryToken;
            this.tryBlock = tryBlock;
            this.errIdentifier = errIdentifier;
            this.catchBlock = catchBlock;
            this.finallyBlock = finallyBlock;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visitTryStmt(this);
        }
    }

    // declaration

    public static class VarDeclaration extends Stmt {
        public final Token identifier;
        public final Token type;
        public final Expr initializer;
        public final boolean isExported;

        public VarDeclaration(
                Token identifier,
                Token type,
                Expr initializer,
                boolean isExported
        ) {
            this.identifier = identifier;
            this.type = type;
            this.initializer = initializer;
            this.isExported = isExported;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visitVarDeclaration(this);
        }
    }

    public static class ConstDeclaration extends Stmt {
        public final Token identifier;
        public final Token type;
        public final Expr initializer;
        public final boolean isExported;

        public ConstDeclaration(
                Token identifier,
                Token type,
                Expr initializer,
                boolean isExported
        ) {
            this.identifier = identifier;
            this.type = type;
            this.initializer = initializer;
            this.isExported = isExported;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visitConstDeclaration(this);
        }
    }

    public static class FunctionDeclaration extends Stmt {

        public final Token name;
        public final List<Param> params;
        public final Token returnType; // nullable
        public final Stmt body;
        public final boolean isExported;

        public FunctionDeclaration(Token name,
                            List<Param> params,
                            Token returnType,
                            Stmt body,
                            boolean isExported) {
            this.name = name;
            this.params = params;
            this.returnType = returnType;
            this.body = body;
            this.isExported = isExported;
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
        public void accept(Visitor visitor) {
            visitor.visitFunctionDeclaration(this);
        }
    }

    public static class ClassDeclaration extends Stmt {

        public final Token name;
        public final Token superName;
        public final List<Method> methods;
        public final List<Property> properties;
        public final boolean isSealed;
        public final boolean isExported;

        public ClassDeclaration (Token name,
                                Token superName,
                                List<Method> methods,
                                List<Property> properties,
                                 boolean isSealed,
                                 boolean isExported) {
                this.name = name;
                this.superName = superName;
                this.methods = methods;
                this.properties = properties;
                this.isSealed = isSealed;
                this.isExported = isExported;
        }

        public static class Method {

            public final Token name;
            public final List<Param> params;
            public final Token returnType; // nullable
            public final Stmt body;
            public final boolean isStatic;
            public final boolean isConstructor;

            public Method(Token name,
                          List<ClassDeclaration.Method.Param> params,
                           Token returnType,
                          Stmt body,
                          boolean isStatic,
                          boolean isConstructor) {
                this.name = name;
                this.params = params;
                this.returnType = returnType;
                this.body = body;
                this.isStatic = isStatic;
                this.isConstructor = isConstructor;
            }

            public static class Param {

                public final Token name;
                public final Token type;

                public Param(Token name, Token type) {
                    this.name = name;
                    this.type = type;
                }
            }

        }

        public static class Property {

            public final Token name;
            public final Token type;
            public final Expr initializer;
            public final boolean isConst;
            public final  boolean isStatic;

            public Property(Token name,
                            Token type,
                            Expr initializer,
                            boolean isConst,
                            boolean isStatic) {
                this.name = name;
                this.type = type;
                this.initializer = initializer;
                this.isConst = isConst;
                this.isStatic = isStatic;
            }
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visitClassDeclaration(this);
        }
    }

}
