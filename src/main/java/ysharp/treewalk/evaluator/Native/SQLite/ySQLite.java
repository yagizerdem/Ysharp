package ysharp.treewalk.evaluator.Native.SQLite;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.JavaObjectWrapper;
import ysharp.treewalk.evaluator.Native.SQLite.Connection.yConnection;
import ysharp.treewalk.evaluator.Native.SQLite.PreparedStatement.yPreparedStatement;
import ysharp.treewalk.evaluator.Native.SQLite.ResultSet.yResultSet;
import ysharp.treewalk.evaluator.Native.SQLite.Statement.yStatement;
import ysharp.treewalk.evaluator.Native.SQLite.function.statix.*;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yClass;

import java.util.List;

public class ySQLite {

    static {
        // register convertor types
        JavaObjectWrapper.RegisterConvertorTypes(java.sql.Statement.class, yStatement.yStatementInstance.class);
        JavaObjectWrapper.RegisterConvertorTypes(java.sql.PreparedStatement.class, yPreparedStatement.yPreparedStatementInstance.class);
        JavaObjectWrapper.RegisterConvertorTypes(java.sql.ResultSet.class, yResultSet.yResultSetInstance.class);
    }

    public static class SQLiteClass extends yClass.SealedClassObject {

        SQLiteClass() {
            this.prototype = yClass.ClassPrototype;

            this.RegisterNativeFn(new ConnectFn());

            this.RegisterClass(new yConnection.yConnectionClass());
            this.RegisterClass(new yStatement.yStatementClass());
            this.RegisterClass(new yPreparedStatement.yPreparedStatementClass());
            this.RegisterClass(new yResultSet.yResultSetClass());
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpException {

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "cannot take instance of SQLite class"
            );
        }

        @Override
        public String getClassName() {
            return "SQLite";
        }

        @Override
        public String getType() {
            return "_SQLite_";
        }

        @Override
        public String toString() {
            return "<class:SQLite>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        SQLiteClass ctor = new SQLiteClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(
                variant,
                true,
                "function"
        );

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}