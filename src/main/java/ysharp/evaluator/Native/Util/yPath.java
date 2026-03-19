package ysharp.evaluator.Native.Util;

import ysharp.YsharpError;
import ysharp.evaluator.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class yPath {

    public static class yPathClass extends yClass.SealedClassObject {

        yPathClass() {

            this.prototype = yClass.ClassPrototype;

            // Path.join(base, ...parts) — joins path segments
            class JoinFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return -1; // variadic
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    if (arguments.isEmpty()) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                0,
                                getClassName() + ".join requires at least 1 argument"
                        );
                    }

                    String base = requireString(arguments.get(0), getClassName(), 1);
                    Path result = Path.of(base);

                    for (int i = 1; i < arguments.size(); i++) {
                        String part = requireString(arguments.get(i), getClassName(), i + 1);
                        result = result.resolve(part);
                    }

                    return new Variable.Variant(new yString.yStringInstance(result.toString()));
                }

                @Override
                public String getFnName() {
                    return "join";
                }
            }

            JoinFn join = new JoinFn();
            this.set(join.getFnName(), new Variable(new Variable.Variant(join), true, "function"));

            // Path.basename(path) — returns the last component of the path
            class BasenameFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, 1, getClassName());
                    String path = requireString(arguments.getFirst(), getClassName(), 1);

                    Path p = Path.of(path);
                    Path fileName = p.getFileName();

                    String result = (fileName != null) ? fileName.toString() : "";
                    return new Variable.Variant(new yString.yStringInstance(result));
                }

                @Override
                public String getFnName() {
                    return "basename";
                }
            }

            BasenameFn basename = new BasenameFn();
            this.set(basename.getFnName(), new Variable(new Variable.Variant(basename), true, "function"));

            // Path.dirname(path) — returns the parent directory
            class DirnameFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, 1, getClassName());
                    String path = requireString(arguments.getFirst(), getClassName(), 1);

                    Path p = Path.of(path);
                    Path parent = p.getParent();

                    String result = (parent != null) ? parent.toString() : "";
                    return new Variable.Variant(new yString.yStringInstance(result));
                }

                @Override
                public String getFnName() {
                    return "dirname";
                }
            }

            DirnameFn dirname = new DirnameFn();
            this.set(dirname.getFnName(), new Variable(new Variable.Variant(dirname), true, "function"));

            // Path.extension(path) — returns the file extension including the dot (e.g. ".txt")
            class ExtensionFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, 1, getClassName());
                    String path = requireString(arguments.getFirst(), getClassName(), 1);

                    Path p = Path.of(path);
                    Path fileName = p.getFileName();

                    if (fileName == null) {
                        return new Variable.Variant(new yString.yStringInstance(""));
                    }

                    String name = fileName.toString();
                    int dotIndex = name.lastIndexOf('.');

                    String ext = (dotIndex >= 0) ? name.substring(dotIndex) : "";
                    return new Variable.Variant(new yString.yStringInstance(ext));
                }

                @Override
                public String getFnName() {
                    return "extension";
                }
            }

            ExtensionFn extension = new ExtensionFn();
            this.set(extension.getFnName(), new Variable(new Variable.Variant(extension), true, "function"));

            // Path.absolute(path) — resolves to an absolute path
            class AbsoluteFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, 1, getClassName());
                    String path = requireString(arguments.getFirst(), getClassName(), 1);

                    try {
                        String abs = Path.of(path).toAbsolutePath().toString();
                        return new Variable.Variant(new yString.yStringInstance(abs));
                    } catch (Exception e) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                0,
                                "Path.absolute failed: " + path
                        );
                    }
                }

                @Override
                public String getFnName() {
                    return "absolute";
                }
            }

            AbsoluteFn absolute = new AbsoluteFn();
            this.set(absolute.getFnName(), new Variable(new Variable.Variant(absolute), true, "function"));

            // Path.normalize(path) — removes redundant . and .. segments
            class NormalizeFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, 1, getClassName());
                    String path = requireString(arguments.getFirst(), getClassName(), 1);

                    String normalized = Path.of(path).normalize().toString();
                    return new Variable.Variant(new yString.yStringInstance(normalized));
                }

                @Override
                public String getFnName() {
                    return "normalize";
                }
            }

            NormalizeFn normalize = new NormalizeFn();
            this.set(normalize.getFnName(), new Variable(new Variable.Variant(normalize), true, "function"));

            // Path.isAbsolute(path) — returns true if the path is absolute
            class IsAbsoluteFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, 1, getClassName());
                    String path = requireString(arguments.getFirst(), getClassName(), 1);

                    boolean isAbs = Path.of(path).isAbsolute();
                    return new Variable.Variant(isAbs);
                }

                @Override
                public String getFnName() {
                    return "isAbsolute";
                }
            }

            IsAbsoluteFn isAbsolute = new IsAbsoluteFn();
            this.set(isAbsolute.getFnName(), new Variable(new Variable.Variant(isAbsolute), true, "function"));

            // Path.isDirectory(path) — returns true if path points to a directory
            class IsDirectoryFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, 1, getClassName());
                    String path = requireString(arguments.getFirst(), getClassName(), 1);

                    boolean isDir = Files.isDirectory(Path.of(path));
                    return new Variable.Variant(isDir);
                }

                @Override
                public String getFnName() {
                    return "isDirectory";
                }
            }

            IsDirectoryFn isDirectory = new IsDirectoryFn();
            this.set(isDirectory.getFnName(), new Variable(new Variable.Variant(isDirectory), true, "function"));

            // Path.mkdir(path) — creates the directory and any missing parents
            class MkdirFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments, 1, getClassName());
                    String path = requireString(arguments.getFirst(), getClassName(), 1);

                    try {
                        Files.createDirectories(Path.of(path));
                        return new Variable.Variant(true);
                    } catch (IOException e) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                0,
                                "Path.mkdir failed: " + path
                        );
                    }
                }

                @Override
                public String getFnName() {
                    return "mkdir";
                }
            }

            MkdirFn mkdir = new MkdirFn();
            this.set(mkdir.getFnName(), new Variable(new Variable.Variant(mkdir), true, "function"));
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, -1, "cannot take instance of static class");
        }

        @Override
        public String getClassName() {
            return "Path";
        }

        @Override
        public String getType() {
            return "Path";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yPathClass ctor = new yPathClass();

        Variable.Variant variant = new Variable.Variant(ctor);

        Variable var = new Variable(
                variant,
                true,
                "function"
        );

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}