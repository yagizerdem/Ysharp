package ysharp.treewalk.evaluator.Native.Util.Path;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringJoiner;

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
                        throws YsharpException {

                    if(arguments.isEmpty()) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                -1,
                                getFnName() + "() expects at least 1 argument."
                        );
                    }

                    for(int i = 0; i < arguments.size(); i++) {
                        Variable.Variant var = arguments.get(i);
                        if(!var.isString()) {
                            throw new YsharpException(
                                    YsharpException.YsharpErrorType.PROCESS,
                                    -1,
                                    getFnName() + "() argument " + (i + 1) + " must be a string."
                            );
                        }
                    }


                    Path dest = Paths.get(arguments.getFirst().asString());

                    for(int i = 1; i < arguments.size(); i++) {
                        Variable.Variant var = arguments.get(i);
                        dest = Paths.get(dest.toString(), arguments.get(i).asString());
                    }

                    return new Variable.Variant(new yString.yStringInstance(dest.toString()));
                }

                @Override
                public String getFnName() {
                    return "join";
                }
            }

            JoinFn join = new JoinFn();
            this.set(join.getFnName(), new Variable(new Variable.Variant(join), true, "function"));

            class IsDirExistFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                        requireArity(arguments, arity(), getFnName());
                        String path = requireString(arguments.getFirst(), getFnName(), 1);

                        File f = new File(path);
                        if (f.exists() && f.isDirectory()) {
                            return new Variable.Variant(true);
                        }

                        return  new Variable.Variant(false);
                }

                @Override
                public String getFnName() {
                    return "isDirExist";
                }
            }

            IsDirExistFn isDirExist = new IsDirExistFn();
            this.set(isDirExist.getFnName(), new Variable(new Variable.Variant(isDirExist), true, "function"));

            class IsFileExistFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());
                    String path = requireString(arguments.getFirst(), getFnName(), 1);

                    File f = new File(path);
                    if (f.exists() && f.isFile()) {
                        return new Variable.Variant(true);
                    }

                    return  new Variable.Variant(false);
                }

                @Override
                public String getFnName() {
                    return "isFileExist";
                }
            }

            IsFileExistFn isFileExist = new IsFileExistFn();
            this.set(isFileExist.getFnName(), new Variable(new Variable.Variant(isFileExist), true, "function"));

            class IsAbsoluteFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());
                    String uri = requireString(arguments.getFirst(), getFnName(), 1);

                    Path path = Path.of(uri);

                    return  new Variable.Variant(path.isAbsolute());
                }

                @Override
                public String getFnName() {
                    return "isAbsolute";
                }
            }

            IsAbsoluteFn isAbsolute = new IsAbsoluteFn();
            this.set(isAbsolute.getFnName(), new Variable(new Variable.Variant(isAbsolute), true, "function"));

            class GetFileNameFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());
                    String uri = requireString(arguments.getFirst(), getFnName(), 1);

                    Path path = Path.of(uri);

                    Path fileName = path.getFileName();

                    if(fileName == null) {
                        return new Variable.Variant(null);
                    }

                    return  new Variable.Variant(new yString.yStringInstance(fileName.toString()));
                }

                @Override
                public String getFnName() {
                    return "getFileName";
                }
            }

            GetFileNameFn getFileName = new GetFileNameFn();
            this.set(getFileName.getFnName(), new Variable(new Variable.Variant(getFileName), true, "function"));


            class GetParentFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());
                    String uri = requireString(arguments.getFirst(), getFnName(), 1);

                    Path path = Path.of(uri);

                    Path parent = path.getParent();

                    if (parent == null) {
                        return new Variable.Variant(null);
                    }

                    return  new Variable.Variant(new yString.yStringInstance(parent.toString()));
                }

                @Override
                public String getFnName() {
                    return "getParent";
                }
            }

            GetParentFn getParent = new GetParentFn();
            this.set(getParent.getFnName(), new Variable(new Variable.Variant(getParent), true, "function"));


            class GetRootFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());
                    String uri = requireString(arguments.getFirst(), getFnName(), 1);

                    Path path = Path.of(uri);

                    Path root = path.getRoot();

                    if(root == null) {
                        return new Variable.Variant(null);
                    }

                    return  new Variable.Variant(new yString.yStringInstance(root.toString()));
                }

                @Override
                public String getFnName() {
                    return "getRoot";
                }
            }

            GetRootFn getRoot = new GetRootFn();
            this.set(getRoot.getFnName(), new Variable(new Variable.Variant(getRoot), true, "function"));

            class GetNameCountFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());
                    String uri = requireString(arguments.getFirst(), getFnName(), 1);

                    Path path = Path.of(uri);

                    return  new Variable.Variant(path.getNameCount());
                }

                @Override
                public String getFnName() {
                    return "getNameCount";
                }
            }

            GetNameCountFn getNameCount = new GetNameCountFn();
            this.set(getNameCount.getFnName(), new Variable(new Variable.Variant(getNameCount), true, "function"));

            class SubPathFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 3;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());

                    String uri = requireString(arguments.getFirst(), getFnName(), 1);
                    int start = requireInt(arguments.get(1), getFnName(), 2);
                    int end = requireInt(arguments.get(2), getFnName(), 3);

                    Path path = Path.of(uri);
                    int nameCount = path.getNameCount();

                    if (start < 0) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                -1,
                                getFnName() + "() start index must be >= 0. Got: " + start
                        );
                    }

                    if (end < 0) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                -1,
                                getFnName() + "() end index must be >= 0. Got: " + end
                        );
                    }

                    if (start >= end) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                -1,
                                getFnName() + "() start index must be less than end index. Got start: "
                                        + start + ", end: " + end
                        );
                    }

                    if (end > nameCount) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                -1,
                                getFnName() + "() end index is out of range. Path has "
                                        + nameCount + " name elements, but end was: " + end
                        );
                    }

                    Path subPath = path.subpath(start, end);

                    return new Variable.Variant(
                            new yString.yStringInstance(subPath.toString())
                    );
                }

                @Override
                public String getFnName() {
                    return "subPath";
                }
            }
            SubPathFn subPath = new SubPathFn();
            this.set(subPath.getFnName(), new Variable(new Variable.Variant(subPath), true, "function"));

            class GetExtensionFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());
                    String uri = requireString(arguments.getFirst(), getFnName(), 1);

                    Path path = Path.of(uri);
                    Path fileNamePath = path.getFileName();

                    if (fileNamePath == null) {
                        return new Variable.Variant(null);
                    }

                    String fileName = fileNamePath.toString();
                    int dotIndex = fileName.lastIndexOf('.');

                    if (dotIndex <= 0) {
                        return new Variable.Variant(null);
                    }

                    String ext = fileName.substring(dotIndex);
                    return new Variable.Variant(new yString.yStringInstance(ext));
                }

                @Override
                public String getFnName() {
                    return "getExtension";
                }
            }

            GetExtensionFn getExtension = new GetExtensionFn();
            this.set(getExtension.getFnName(), new Variable(new Variable.Variant(getExtension), true, "function"));

            class NormalizeFn extends Function.NativeFunction {

                @Override
                public int arity() { return 1; }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());
                    String uri = requireString(arguments.getFirst(), getFnName(), 1);

                    String normalized = Path.of(uri).normalize().toString();
                    return new Variable.Variant(new yString.yStringInstance(normalized));
                }

                @Override
                public String getFnName() { return "normalize"; }
            }

            NormalizeFn normalize = new NormalizeFn();
            this.set(normalize.getFnName(), new Variable(new Variable.Variant(normalize), true, "function"));

            class ResolveFn extends Function.NativeFunction {

                @Override
                public int arity() { return 2; }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());
                    String base  = requireString(arguments.get(0), getFnName(), 1);
                    String other = requireString(arguments.get(1), getFnName(), 2);

                    String resolved = Path.of(base).resolve(other).toString();
                    return new Variable.Variant(new yString.yStringInstance(resolved));
                }

                @Override
                public String getFnName() { return "resolve"; }
            }

            ResolveFn resolve = new ResolveFn();
            this.set(resolve.getFnName(), new Variable(new Variable.Variant(resolve), true, "function"));

            class RelativizeFn extends Function.NativeFunction {

                @Override
                public int arity() { return 2; }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());
                    String base   = requireString(arguments.get(0), getFnName(), 1);
                    String target = requireString(arguments.get(1), getFnName(), 2);

                    try {
                        String rel = Path.of(base).relativize(Path.of(target)).toString();
                        return new Variable.Variant(new yString.yStringInstance(rel));
                    } catch (IllegalArgumentException e) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                -1,
                                getFnName() + "() both paths must be either absolute or relative. " + e.getMessage()
                        );
                    }
                }

                @Override
                public String getFnName() { return "relativize"; }
            }

            RelativizeFn relativize = new RelativizeFn();
            this.set(relativize.getFnName(), new Variable(new Variable.Variant(relativize), true, "function"));

        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpException {

            throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1, "cannot take instance of static class");
        }

        @Override
        public String getClassName() {
            return "Path";
        }

        @Override
        public String getType() {
            return "_Path_";
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