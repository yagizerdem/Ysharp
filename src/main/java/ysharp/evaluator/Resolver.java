package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.lexer.Token;
import ysharp.parser.Expr;
import ysharp.parser.Stmt;

import java.util.*;

public class Resolver implements Expr.Visitor<Void> ,
        Stmt.Visitor {
    private final Interpreter interpreter;
    private final Stack<Map<String, Boolean>> scopes = new Stack<>();
    public final List<YsharpError> errors = new ArrayList<>();

    public boolean hadErrors (){
        return !errors.isEmpty();
    }

    public Resolver(Interpreter interpreter) {
        this.interpreter = interpreter;
    }


    private enum FunctionType {
        NONE,
        FUNCTION,
        NATIVE_FUNCTION,
        CONSTRUCTOR_NATIVE_FUNCTION,
        LAMBDA,
    }


    private enum ClassType {
        NONE,
        CLASS,
    }

    public void resolve(List<Stmt> statements) {
        for (Stmt statement : statements) {
            if(statement == null) continue;
            try {
                resolve(statement);
            }catch (YsharpError err) {
                errors.add(err);
            }
        }
    }

    private void resolve(Stmt stmt) {
        if(stmt == null) return;
        stmt.accept(this);
    }

    private void resolve(Expr expr) {
        if(expr == null) return;
        expr.accept(this);
    }

    private void beginScope() {
        scopes.push(new HashMap<>());
    }

    private void endScope() {
        scopes.pop();
    }

    private void resolveLocal(Expr expr, Token name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name.lexeme)) {
                interpreter.resolve(expr, scopes.size() - 1 - i);
                return;
            }
        }
    }

    private void declare(Token name) {
        if (scopes.isEmpty()) return;

        Map<String, Boolean> scope = scopes.peek();
        if (scope.containsKey(name.lexeme)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    "Already a variable with this name in this scope.");
        }

        scope.put(name.lexeme, false);
    }

    private void define(Token name) {
        if (scopes.isEmpty()) return;
        scopes.peek().put(name.lexeme, true);
    }

    // expr visitor
    @Override
    public Void visitBinaryExpr(Expr.BinaryExpr expr) {
        resolve(expr.left);
        resolve(expr.right);
        return null;
    }

    @Override
    public Void visitUnaryExpr(Expr.UnaryExpr expr) {
        resolve(expr.expr);
        return null;
    }

    @Override
    public Void visitTernaryExpr(Expr.TernaryExpr expr) {
        resolve(expr.condition);
        resolve(expr.thenBranch);
        resolve(expr.elseBranch);
        return null;
    }

    @Override
    public Void visitPostfixExpr(Expr.PostfixExpr expr) {
        resolve(expr.operand);
        return null;
    }

    @Override
    public Void visitAssignmentExpr(Expr.AssignmentExpr expr) {
        resolve(expr.value);
        resolve(expr.target);
        return null;
    }

    @Override
    public Void visitLogicalExpr(Expr.LogicalExpr expr) {
        resolve(expr.left);
        resolve(expr.right);
        return null;
    }

    @Override
    public Void visitGroupingExpr(Expr.GroupingExpr expr) {
        resolve(expr.expression);
        return null;
    }

    @Override
    public Void visitGetExpr(Expr.GetExpr expr) {
        resolve(expr.object);
        return null;
    }

    @Override
    public Void visitSetExpr(Expr.SetExpr expr) {
        resolve(expr.value);
        resolve(expr.object);
        return null;
    }

    @Override
    public Void visitCallExpr(Expr.CallExpr expr) {
        resolve(expr.callee);
        for(Expr arg : expr.arguments) {
            resolve(arg);
        }
        return null;
    }

    @Override
    public Void visitSuperCallExpr(Expr.SuperCallExpr expr) {
        for(Expr arg : expr.arguments) {
            resolve(arg);
        }
        return null;
    }

    @Override
    public Void visitLiteralExpr(Expr.LiteralExpr expr) {
        return null;
    }

    @Override
    public Void visitVariableExpr(Expr.VariableExpr expr) {
        if (!scopes.isEmpty() &&
                scopes.peek().get(expr.name.lexeme) == Boolean.FALSE) {
            throw new YsharpError(YsharpError.YsharpErrorType.PROCESS,
                    -1 ,
                    "Can't read local variable in its own initializer.");
        }

        resolveLocal(expr, expr.name);
        return null;
    }

    @Override
    public Void visitArrayInitializerExpr(Expr.ArrayInitializerExpr expr) {
        for(Expr item : expr.elements) {
            resolve(item);
        }
        return null;
    }

    @Override
    public Void visitMapInitializerExpr(Expr.MapInitializerExpr expr) {
        for(var entry : expr.entries) {
            resolve(entry.value);
        }
        return null;
    }

    @Override
    public Void visitLambdaExpr(Expr.LambdaExpr expr) {
        beginScope();
        for(var param : expr.params)  {
            declare(param.name);
            define(param.name);
        }
        resolve(expr.expr);
        if(expr.body != null) {
            Stmt.BlockStmt block = (Stmt.BlockStmt ) expr.body;
            resolve(block.stmtList);
        }
        endScope();
        return null;
    }

    @Override
    public Void visitNexExpr(Expr.NewExpr expr) {
        resolve(expr.qualifiedName);
        for(var arg : expr.arguments) {
            resolve(arg);
        }
        return null;
    }

    @Override
    public Void visitRangeExpr(Expr.RangeExpr expr) {
        resolve(expr.start);
        resolve(expr.end);
        return null;
    }

    @Override
    public Void visitPipeExpr(Expr.PipeExpr expr) {
        resolve(expr.left);
        resolve(expr.right);
        return null;
    }

    // stmt visitor
    @Override
    public void visitBlockStmt(Stmt.BlockStmt stmt) {
        beginScope();
        resolve(stmt.stmtList);
        endScope();
    }

    @Override
    public void visitPrintStmt(Stmt.PrintStmt stmt) {
        resolve(stmt.expr);
    }

    @Override
    public void visitPrintlnStmt(Stmt.PrintlnStmt stmt) {
        resolve(stmt.expr);
    }

    @Override
    public void visitIfStmt(Stmt.IfStmt stmt) {
        resolve(stmt.condition);
        resolve(stmt.then);
        for(var elifStmt : stmt.elifStmtList) {
            resolve(elifStmt.condition);
            resolve(elifStmt.then);
        }
        resolve(stmt.else_);
    }

    @Override
    public void visitWhileStmt(Stmt.WhileStmt stmt) {
        resolve(stmt.condition);
        resolve(stmt.stmt);
    }

    @Override
    public void visitExprStmt(Stmt.ExprStmt stmt) {
        resolve(stmt.expr);
    }

    @Override
    public void visitForStmt(Stmt.ForStmt stmt) {
        beginScope();
        resolve(stmt.initializer);
        resolve(stmt.condition);
        resolve(stmt.increment);
        resolve(stmt.body);
        endScope();
    }

    @Override
    public void visitForInStmt(Stmt.ForInStmt stmt) {
        beginScope();
        resolve(stmt.declaration);
        resolve(stmt.iterable);
        resolve(stmt.body);
        endScope();
    }

    @Override
    public void visitForEachStmt(Stmt.ForEachStmt stmt) {
        beginScope();
        resolve(stmt.declaration);
        resolve(stmt.iterable);
        resolve(stmt.body);
        endScope();
    }

    @Override
    public void visitBreakStmt(Stmt.BreakStmt stmt) {
        return;
    }

    @Override
    public void visitContinueStmt(Stmt.ContinueStmt stmt) {
        return;
    }

    @Override
    public void visitReturnStmt(Stmt.ReturnStmt stmt) {
        resolve(stmt.expr);
    }

    @Override
    public void visitSwitchStmt(Stmt.SwitchStmt stmt) {
        resolve(stmt.condition);
        for(var case_ : stmt.cases) {
            resolve(case_.matchExpr);
            resolve(case_.block);
        }
        resolve(stmt.defaultClause);
    }

    @Override
    public void visitThrowStmt(Stmt.ThrowStmt stmt) {
        resolve(stmt.expr);
    }

    @Override
    public void visitTryStmt(Stmt.TryStmt stmt) {
        resolve(stmt.tryBlock);
        resolve(stmt.catchBlock);
        resolve(stmt.finallyBlock);
    }

    @Override
    public void visitVarDeclaration(Stmt.VarDeclaration stmt) {
        declare(stmt.identifier);
        resolve(stmt.initializer);
        define(stmt.identifier);
    }

    @Override
    public void visitLetDeclaration(Stmt.LetDeclaration stmt) {
        declare(stmt.identifier);
        resolve(stmt.initializer);
        define(stmt.identifier);
    }

    @Override
    public void visitFunctionDeclaration(Stmt.FunctionDeclaration stmt) {
        declare(stmt.name);
        define(stmt.name);
        beginScope();
        for(var arg : stmt.params) {
            declare(arg.name);
            define(arg.name);
        }
        Stmt.BlockStmt block = (Stmt.BlockStmt )stmt.body;
        resolve(block.stmtList);

        endScope();
    }

    @Override
    public void visitConstDeclaration(Stmt.ConstDeclaration stmt) {
        declare(stmt.identifier);
        resolve(stmt.initializer);
        define(stmt.identifier);
    }

    @Override
    public void visitClassDeclaration(Stmt.ClassDeclaration stmt) {
        declare(stmt.name);
        define(stmt.name);
        beginScope();

        for(var prop : stmt.properties) {
            declare(prop.name);
            define(prop.name);
            resolve(prop.initializer);
        }

        for(var method : stmt.methods) {
            beginScope();
            for(var arg : method.params) {
                declare(arg.name);
                define(arg.name);
            }
            if(method.body instanceof Stmt.BlockStmt) {
                Stmt.BlockStmt block = (Stmt.BlockStmt) method.body;
                for(int i = 0 ; i < block.stmtList.size(); i++) resolve(block.stmtList.get(i));
            }
            else {
                // normally this should happen but i put this as fallback , idk maybe i shouldn't !!
                resolve(method.body);
            }
            endScope();
        }

        endScope();
    }

}
