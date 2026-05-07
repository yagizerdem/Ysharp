package ysharp.treewalk.evaluator.Native.IO.Directory.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.nio.file.Path;
import java.util.List;

public class GetAppDataFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Variable.Variant call(
            Interpreter interpreter,
            List<Variable.Variant> arguments
    ) throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        String os = System.getProperty("os.name").toLowerCase();
        String home = System.getProperty("user.home");

        String path;

        if (os.contains("win")) {
            path = System.getenv("APPDATA");

            if (path == null || path.isBlank()) {
                path = Path.of(home, "AppData", "Roaming").toString();
            }

        } else if (os.contains("mac")) {
            path = Path.of(home, "Library", "Application Support").toString();

        } else {
            String xdgDataHome = System.getenv("XDG_DATA_HOME");

            if (xdgDataHome != null && !xdgDataHome.isBlank()) {
                path = xdgDataHome;
            } else {
                path = Path.of(home, ".local", "share").toString();
            }
        }

        return new Variable.Variant(path);
    }

    @Override
    public String getFnName() {
        return "getAppData";
    }
}