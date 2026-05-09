package ysharp.treewalk.evaluator.Native.YPF.FilePicker;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Function;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class yFilePicker {

    public static yFilePickerInstance requireFilePickerThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without 'this'."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yFilePickerInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Expected FilePicker but got '" + obj.getType() + "'"
            );
        }

        return (yFilePickerInstance) obj;
    }

    public static RuntimeObject yFilePicker_Instance_Prototype;

    static {
        yFilePicker_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__FilePicker__"; }
            @Override public String toString() { return "<prototype:FilePicker>"; }
        };

        yFilePicker_Instance_Prototype.prototype = yClass.ClassPrototype;

        Map<String, List<Method>> methodMap = new HashMap<>();

        for (Method m : JFileChooser.class.getMethods()) {
            if (m.getDeclaringClass() == Object.class) continue;

            methodMap
                    .computeIfAbsent(m.getName(), k -> new ArrayList<>())
                    .add(m);
        }

        for (String name : methodMap.keySet()) {
            yFilePicker_Instance_Prototype.set(name, new Variable(
                    new Variable.Variant(new Function.NativeFunction() {

                        @Override
                        public int arity() {
                            return -1;
                        }

                        @Override
                        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                                throws YsharpException {

                            yFilePickerInstance picker =
                                    yFilePicker.requireFilePickerThis(interpreter, name);

                            JFileChooser chooser = picker.fileChooser;

                            try {
                                Object[] javaArgs = new Object[args.size()];

                                for (int i = 0; i < args.size(); i++) {
                                    Variable.Variant v = args.get(i);
                                    javaArgs[i] = v.asJavaNative();
                                }

                                List<Method> availableMethods = methodMap.get(name);

                                Method selectedMethod = null;

                                for (Method method : availableMethods) {
                                    if (method.getParameterCount() != args.size()) continue;

                                    Parameter[] javaParameters = method.getParameters();

                                    boolean skip = false;

                                    for (int j = 0; j < javaParameters.length; j++) {
                                        if (!isCompatible(javaParameters[j].getType(), javaArgs[j])) {
                                            skip = true;
                                            break;
                                        }
                                    }

                                    if (skip) continue;

                                    selectedMethod = method;
                                    break;
                                }

                                if (selectedMethod == null) {
                                    throw new YsharpException(
                                            YsharpException.YsharpErrorType.PROCESS,
                                            -1,
                                            "method overload not found"
                                    );
                                }

                                Object result = selectedMethod.invoke(chooser, javaArgs);

                                return new Variable.Variant(JavaObjectWrapper.wrap(result));

                            } catch (YsharpException e) {
                                throw e;
                            } catch (Exception e) {
                                throw new YsharpException(
                                        YsharpException.YsharpErrorType.PROCESS,
                                        0,
                                        "Native call failed: " + name
                                );
                            }
                        }

                        @Override
                        public String getFnName() {
                            return name;
                        }
                    }),
                    true,
                    "function"
            ));
        }

        class ShowOpenDialogFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpException {

                requireArity(args, 0, getFnName());

                yFilePickerInstance picker =
                        yFilePicker.requireFilePickerThis(interpreter, getFnName());

                int result = picker.fileChooser.showOpenDialog(null);

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "showOpenDialog";
            }
        }

        class ShowSaveDialogFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpException {

                requireArity(args, 0, getFnName());

                yFilePickerInstance picker =
                        yFilePicker.requireFilePickerThis(interpreter, getFnName());

                int result = picker.fileChooser.showSaveDialog(null);

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "showSaveDialog";
            }
        }

        class GetSelectedPathFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpException {

                requireArity(args, 0, getFnName());

                yFilePickerInstance picker =
                        yFilePicker.requireFilePickerThis(interpreter, getFnName());

                File file = picker.fileChooser.getSelectedFile();

                if (file == null) {
                    return new Variable.Variant(null);
                }

                return new Variable.Variant(file.getAbsolutePath());
            }

            @Override
            public String getFnName() {
                return "getSelectedPath";
            }
        }

        class GetSelectedNameFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpException {

                requireArity(args, 0, getFnName());

                yFilePickerInstance picker =
                        yFilePicker.requireFilePickerThis(interpreter, getFnName());

                File file = picker.fileChooser.getSelectedFile();

                if (file == null) {
                    return new Variable.Variant(null);
                }

                return new Variable.Variant(file.getName());
            }

            @Override
            public String getFnName() {
                return "getSelectedName";
            }
        }

        class SetCurrentDirectoryFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpException {

                requireArity(args, 1, getFnName());

                yFilePickerInstance picker =
                        yFilePicker.requireFilePickerThis(interpreter, getFnName());

                String path = requireString(args.getFirst(), getFnName(), 1);

                picker.fileChooser.setCurrentDirectory(new File(path));

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "setCurrentDirectory";
            }
        }

        class SetSelectedFileFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpException {

                requireArity(args, 1, getFnName());

                yFilePickerInstance picker =
                        yFilePicker.requireFilePickerThis(interpreter, getFnName());

                String path = requireString(args.getFirst(), getFnName(), 1);

                picker.fileChooser.setSelectedFile(new File(path));

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "setSelectedFile";
            }
        }

        class SetFileSelectionModeFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpException {

                requireArity(args, 1, getFnName());

                yFilePickerInstance picker =
                        yFilePicker.requireFilePickerThis(interpreter, getFnName());

                int mode = requireInt(args.getFirst(), getFnName(), 1);

                picker.fileChooser.setFileSelectionMode(mode);

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "setFileSelectionMode";
            }
        }

        class SetMultiSelectionEnabledFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpException {

                requireArity(args, 1, getFnName());

                yFilePickerInstance picker =
                        yFilePicker.requireFilePickerThis(interpreter, getFnName());

                boolean enabled = requireBoolean(args.getFirst(), getFnName(), 1);

                picker.fileChooser.setMultiSelectionEnabled(enabled);

                return new Variable.Variant(null);
            }

            @Override
            public String getFnName() {
                return "setMultiSelectionEnabled";
            }
        }

        class GetSelectedPathsFn extends Function.NativeFunction {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                    throws YsharpException {

                requireArity(args, 0, getFnName());

                yFilePickerInstance picker =
                        yFilePicker.requireFilePickerThis(interpreter, getFnName());

                File[] files = picker.fileChooser.getSelectedFiles();

                RuntimeObject arr = new RuntimeObject() {
                    @Override public boolean isTruthy() { return true; }
                    @Override public String getType() { return "Array"; }
                };

                // Eğer senin Array wrapper'ın varsa burada onu kullan.
                // Bu geçici RuntimeObject array gibi davranmaz.
                // O yüzden istersen bu methodu şimdilik kaldır.
                // Doğru olan: yArray.yArrayInstance içine path'leri basmak.

                return new Variable.Variant(JavaObjectWrapper.wrap(files));
            }

            @Override
            public String getFnName() {
                return "getSelectedPaths";
            }
        }

        yFilePicker_Instance_Prototype.RegisterNativeFn(new ShowOpenDialogFn());
        yFilePicker_Instance_Prototype.RegisterNativeFn(new ShowSaveDialogFn());
        yFilePicker_Instance_Prototype.RegisterNativeFn(new GetSelectedPathFn());
        yFilePicker_Instance_Prototype.RegisterNativeFn(new GetSelectedNameFn());
        yFilePicker_Instance_Prototype.RegisterNativeFn(new SetCurrentDirectoryFn());
        yFilePicker_Instance_Prototype.RegisterNativeFn(new SetSelectedFileFn());
        yFilePicker_Instance_Prototype.RegisterNativeFn(new SetFileSelectionModeFn());
        yFilePicker_Instance_Prototype.RegisterNativeFn(new SetMultiSelectionEnabledFn());
        yFilePicker_Instance_Prototype.RegisterNativeFn(new GetSelectedPathsFn());
    }

    public static class yFilePickerInstance extends yClass.ClassObjectInstance {

        public final JFileChooser fileChooser;

        public yFilePickerInstance() {
            this.fileChooser = new JFileChooser();
            this.prototype = yFilePicker_Instance_Prototype;
        }

        public yFilePickerInstance(String currentDirectory) {
            this.fileChooser = new JFileChooser(currentDirectory);
            this.prototype = yFilePicker_Instance_Prototype;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "FilePicker"; }
        @Override public String toString() { return "<instance:FilePicker>"; }
        @Override public Object getNativeJavaObject() { return this.fileChooser; }
    }

    public static class yFilePickerClass extends yClass.SealedClassObject {

        public yFilePickerClass() {
            this.prototype = yClass.ClassPrototype;

            this.set("APPROVE_OPTION", new Variable(new Variable.Variant(JFileChooser.APPROVE_OPTION), true, "int"));
            this.set("CANCEL_OPTION", new Variable(new Variable.Variant(JFileChooser.CANCEL_OPTION), true, "int"));
            this.set("ERROR_OPTION", new Variable(new Variable.Variant(JFileChooser.ERROR_OPTION), true, "int"));

            this.set("FILES_ONLY", new Variable(new Variable.Variant(JFileChooser.FILES_ONLY), true, "int"));
            this.set("DIRECTORIES_ONLY", new Variable(new Variable.Variant(JFileChooser.DIRECTORIES_ONLY), true, "int"));
            this.set("FILES_AND_DIRECTORIES", new Variable(new Variable.Variant(JFileChooser.FILES_AND_DIRECTORIES), true, "int"));
        }

        @Override
        public int arity() {
            return -1;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> args)
                throws YsharpException {

            if (args.size() == 0) {
                return new Variable.Variant(new yFilePickerInstance());
            }

            if (args.size() == 1) {
                String currentDirectory = requireString(args.getFirst(), "FilePicker", 1);
                return new Variable.Variant(new yFilePickerInstance(currentDirectory));
            }

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "FilePicker expects 0 or 1 arguments."
            );
        }

        @Override public String getClassName() { return "FilePicker"; }
        @Override public String getType() { return "_FilePicker_"; }
        @Override public String toString() { return "<class:FilePicker>"; }
    }
}