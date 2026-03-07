package ysharp.evaluator.Native.Util;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Y_File {

    public static RuntimeObject Y_File_Instance_Prototype;

    static {}

    public static class Y_File_Instance extends Y_Class.ClassObjectInstance {

        public Y_File_Instance(){}

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "File";
        }

        @Override
        public String toString() {
            return "<instance:File>";
        }
    }

    public static class Y_FileClass extends Y_Class.SealedClassObject {

        Y_FileClass(){

            this.prototype = Y_Class.ClassPrototype;

            // File.read(path)
            class ReadFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments,1,getClassName());

                    String path = requireString(arguments.getFirst(),getClassName(),1);

                    try {
                        String content = Files.readString(Path.of(path));
                        return new Variable.Variant(new Y_String.Y_StringInstance(content));
                    }
                    catch (IOException e){
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                0,
                                "File read failed: " + path
                        );
                    }
                }

                @Override
                public String getFnName() {
                    return "read";
                }
            }

            ReadFn read = new ReadFn();
            Variable readVar = new Variable(
                    new Variable.Variant(read),
                    true,
                    TypeTag.OBJECT
            );
            this.prototype.set(read.getFnName(),readVar);

            // File.write(path,content)
            class WriteFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments,2,getClassName());

                    String path = requireString(arguments.get(0),getClassName(),1);
                    String content = requireString(arguments.get(1),getClassName(),2);

                    try{
                        Files.writeString(Path.of(path),content);
                        return new Variable.Variant(true);
                    }
                    catch (IOException e){
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                0,
                                "File write failed: " + path
                        );
                    }
                }

                @Override
                public String getFnName() {
                    return "write";
                }
            }

            WriteFn write = new WriteFn();
            Variable writeVar = new Variable(
                    new Variable.Variant(write),
                    true,
                    TypeTag.OBJECT
            );
            this.prototype.set(write.getFnName(),writeVar);

            // File.append(path,content)
            class AppendFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments,2,getClassName());

                    String path = requireString(arguments.get(0),getClassName(),1);
                    String content = requireString(arguments.get(1),getClassName(),2);

                    try{
                        Files.writeString(Path.of(path),content,
                                java.nio.file.StandardOpenOption.CREATE,
                                java.nio.file.StandardOpenOption.APPEND);

                        return new Variable.Variant(true);
                    }
                    catch (IOException e){
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                0,
                                "File append failed: " + path
                        );
                    }
                }

                @Override
                public String getFnName() {
                    return "append";
                }
            }

            AppendFn append = new AppendFn();
            Variable appendVar = new Variable(
                    new Variable.Variant(append),
                    true,
                    TypeTag.OBJECT
            );
            this.prototype.set(append.getFnName(),appendVar);

            // File.exists(path)
            class ExistsFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments,1,getClassName());

                    String path = requireString(arguments.getFirst(),getClassName(),1);

                    boolean exists = Files.exists(Path.of(path));

                    return new Variable.Variant(exists);
                }

                @Override
                public String getFnName() {
                    return "exists";
                }
            }

            ExistsFn exists = new ExistsFn();
            Variable existsVar = new Variable(
                    new Variable.Variant(exists),
                    true,
                    TypeTag.OBJECT
            );
            this.prototype.set(exists.getFnName(),existsVar);

            // File.delete(path)
            class DeleteFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpError {

                    requireArity(arguments,1,getClassName());

                    String path = requireString(arguments.getFirst(),getClassName(),1);

                    try{
                        Files.deleteIfExists(Path.of(path));
                        return new Variable.Variant(true);
                    }
                    catch(IOException e){
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                0,
                                "File delete failed: " + path
                        );
                    }
                }

                @Override
                public String getFnName() {
                    return "delete";
                }
            }

            DeleteFn delete = new DeleteFn();
            Variable deleteVar = new Variable(
                    new Variable.Variant(delete),
                    true,
                    TypeTag.OBJECT
            );
            this.prototype.set(delete.getFnName(),deleteVar);

        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            Y_File_Instance instance = new Y_File_Instance();
            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "File";
        }

        @Override
        public String getType() {
            return "File";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        Y_FileClass ctor = new Y_FileClass();

        Variable.Variant variant = new Variable.Variant(ctor);

        Variable var = new Variable(
                variant,
                true,
                TypeTag.OBJECT
        );

        interpreter.defineGlobal(ctor.getClassName(),var);
    }

}