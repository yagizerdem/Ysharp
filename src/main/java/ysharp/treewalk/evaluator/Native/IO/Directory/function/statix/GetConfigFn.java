package ysharp.treewalk.evaluator.Native.IO.Directory.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.nio.file.Path;
import java.util.List;

public class GetConfigFn extends Function.NativeFunction implements Callable {

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
            path = Path.of(home, "Library", "Preferences").toString();

        } else {
            String xdgConfigHome = System.getenv("XDG_CONFIG_HOME");

            if (xdgConfigHome != null && !xdgConfigHome.isBlank()) {
                path = xdgConfigHome;
            } else {
                path = Path.of(home, ".config").toString();
            }
        }

        return new Variable.Variant(path);
    }

    @Override
    public String getFnName() {
        return "getConfig";
    }
}