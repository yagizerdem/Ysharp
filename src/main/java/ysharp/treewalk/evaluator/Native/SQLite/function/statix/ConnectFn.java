package ysharp.treewalk.evaluator.Native.SQLite.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Function;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.IO.yIO;
import ysharp.treewalk.evaluator.Native.SQLite.Connection.yConnection;
import ysharp.treewalk.evaluator.Variable;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ConnectFn  extends Function.NativeFunction {
    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {

        requireArity(arguments, 1, getFnName());

        String dbPath = requireString(arguments.getFirst(), getFnName(), 1);
        Path absolutePath = yIO.resolvePath(interpreter, dbPath);

        String uri = "jdbc:sqlite:" + absolutePath;
        try {
            Connection conn = DriverManager.getConnection(uri);
            return new Variable.Variant(new yConnection.yConnectionInstance(conn));
        } catch (SQLException e) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    e.getMessage()
            );
        }
    }

    @Override
    public String getFnName() {
        return "connect";
    }
}
