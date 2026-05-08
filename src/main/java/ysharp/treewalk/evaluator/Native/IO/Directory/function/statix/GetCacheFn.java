package ysharp.treewalk.evaluator.Native.IO.Directory.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.nio.file.Path;
import java.util.List;

public class GetCacheFn extends Function.NativeFunction implements Callable {

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
            path = System.getenv("LOCALAPPDATA");

            if (path == null || path.isBlank()) {
                path = Path.of(home, "AppData", "Local").toString();
            }

        } else if (os.contains("mac")) {
            path = Path.of(home, "Library", "Caches").toString();

        } else {
            String xdgCacheHome = System.getenv("XDG_CACHE_HOME");

            if (xdgCacheHome != null && !xdgCacheHome.isBlank()) {
                path = xdgCacheHome;
            } else {
                path = Path.of(home, ".cache").toString();
            }
        }

        return new Variable.Variant(new yString.yStringInstance(path));
    }

    @Override
    public String getFnName() {
        return "getCache";
    }
}