package ysharp.treewalk.evaluator.Native.YPF;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.YPF.Button.yButton;
import ysharp.treewalk.evaluator.Native.YPF.Button.yButtonGroup;
import ysharp.treewalk.evaluator.Native.YPF.Button.yRadioButton;
import ysharp.treewalk.evaluator.Native.YPF.CheckBox.yCheckBox;
import ysharp.treewalk.evaluator.Native.YPF.ComboBox.yComboBox;
import ysharp.treewalk.evaluator.Native.YPF.Container.Frame.yFrame;
import ysharp.treewalk.evaluator.Native.YPF.Container.yContainer;
import ysharp.treewalk.evaluator.Native.YPF.Label.yLabel;
import ysharp.treewalk.evaluator.Native.YPF.Layout.*;
import ysharp.treewalk.evaluator.Native.YPF.List.yList;
import ysharp.treewalk.evaluator.Native.YPF.Container.Panel.yPanel;
import ysharp.treewalk.evaluator.Native.YPF.Container.Panel.yScrollPane;
import ysharp.treewalk.evaluator.Native.YPF.Container.Panel.ySplitPane;
import ysharp.treewalk.evaluator.Native.YPF.Container.Panel.yTabbedPane;
import ysharp.treewalk.evaluator.Native.YPF.ProgressBar.yProgressBar;
import ysharp.treewalk.evaluator.Native.YPF.Table.yTable;
import ysharp.treewalk.evaluator.Native.YPF.TextArea.yTextArea;
import ysharp.treewalk.evaluator.Native.YPF.TextBox.yTextBox;
import ysharp.treewalk.evaluator.Native.YPF.Util.yColor;

import javax.swing.*;
import java.util.List;

public class YPF {


    public static class YPFClass extends yClass.SealedClassObject {

        static int initializationCount = 0;

        public YPFClass() {
            if(YPFClass.initializationCount == 0) {
                FlatLightLaf.setup(); // default mode is light
            }
            YPFClass.initializationCount++;

            this.prototype = yClass.ClassPrototype;

            // natives

            // YPF.invokeLater(()=> do // put ui code here end)
             class InvokeLaterFn extends Function.NativeFunction implements Callable {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());

                    Variable.Variant fnVar = arguments.getFirst();

                    if (!fnVar.isCallable()) {
                        throw new YsharpException(
                                YsharpException.YsharpErrorType.PROCESS,
                                0,
                                "invokeLater expects a function."
                        );
                    }

                    Callable callable = fnVar.asCallable();

                    SwingUtilities.invokeLater(() -> {
                        try {
                            callable.call(interpreter.copy(), List.of());
                        } catch (YsharpException e) {
                            e.printStackTrace();
                        }
                    });

                    return new Variable.Variant(null);
                }

                @Override
                public String getFnName() {
                    return "invokeLater";
                }
            }

            InvokeLaterFn invokeLater = new InvokeLaterFn();
            Variable invokeLaterVar = new Variable(
                    new Variable.Variant(invokeLater),
                    true,
                    "function"
            );
            this.set(invokeLater.getFnName(), invokeLaterVar);

            // YPF.lightMode()
            class LightModeFn extends Function.NativeFunction implements Callable {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());
                    FlatLightLaf.setup();
                    return new Variable.Variant(null);
                }

                @Override
                public String getFnName() {
                    return "lightMode";
                }
            }

            LightModeFn lightMode = new LightModeFn();
            Variable lightModeVar = new Variable(
                    new Variable.Variant(lightMode),
                    true,
                    "function"
            );
            this.set(lightMode.getFnName(), lightModeVar);


            // YPF.darkMode()
            class DarkModeFn extends Function.NativeFunction implements Callable {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());
                    FlatDarkLaf.setup();
                    return new Variable.Variant(null);
                }

                @Override
                public String getFnName() {
                    return "darkMode";
                }
            }

            DarkModeFn darkMode = new DarkModeFn();
            Variable darkModeVar = new Variable(
                    new Variable.Variant(darkMode),
                    true,
                    "function"
            );
            this.set(darkMode.getFnName(), darkModeVar);

            // YPF.draculaMode()
            class DraculaModeFn extends Function.NativeFunction implements Callable {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter,
                                             List<Variable.Variant> arguments)
                        throws YsharpException {

                    requireArity(arguments, arity(), getFnName());
                    FlatDarkLaf.setup();
                    return new Variable.Variant(null);
                }

                @Override
                public String getFnName() {
                    return "draculaMode";
                }
            }

            DraculaModeFn draculaMode = new DraculaModeFn();
            Variable draculaModeVar = new Variable(
                    new Variable.Variant(draculaMode),
                    true,
                    "function"
            );
            this.set(draculaMode.getFnName(), draculaModeVar);


            // YPF.Frame
            this.RegisterClass(new yFrame.yFrameClass());
            // YPF.Button
            this.RegisterClass(new yButton.yButtonClass());
            // YPF.Label
            this.RegisterClass(new yLabel.yLabelClass());
            // YPF.Panel
            this.RegisterClass(new yPanel.yPanelClass());
            // YPF.TextBox
            this.RegisterClass(new yTextBox.yTextBoxClass());
            // YPF.TextArea
            this.RegisterClass(new yTextArea.yTextAreaClass());
            // YPF.CheckBox();
            this.RegisterClass(new yCheckBox.yCheckBoxClass());
            // YPF.ComboBox();
            this.RegisterClass(new yComboBox.yComboBoxClass());
            // YPF.ButtonGroup();
            this.RegisterClass(new yButtonGroup.yButtonGroupClass());
            // YPF.RadioButton();
            this.RegisterClass(new yRadioButton.yRadioButtonClass());
            // YPF.TabbedPane();
            this.RegisterClass(new yTabbedPane.yTabbedPaneClass());
            // YPF.ScrollPane();
            this.RegisterClass(new yScrollPane.yScrollPaneClass());
            // YPF.SplitPane();
            this.RegisterClass(new ySplitPane.ySplitPaneClass());
            // YPF.List();
            this.RegisterClass(new yList.yListClass());
            // YPF.ProgressBar();
            this.RegisterClass(new yProgressBar.yProgressBarClass());
            // YPF.Container();
            this.RegisterClass(new yContainer.yContainerClass());
            // YPF.Table()
            this.RegisterClass(new yTable.yTableClass());

            // layouts
            this.RegisterClass(new yBorderLayout.yBorderLayoutClass());
            this.RegisterClass(new yCardLayout.yCardLayoutClass());
            this.RegisterClass(new yBoxLayout.yBoxLayoutClass());
            this.RegisterClass(new yFlowLayout.yFlowLayoutClass());
            this.RegisterClass(new yGridLayout.yGridLayoutClass());

            // color
            this.RegisterClass(new yColor.yColorClass());
        }

        @Override
        public String getClassName() {
            return "YPF";
        }

        @Override
        public String getType() {
            return "YPF";
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
            throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1, "cannot take instance of YPF static class");
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        YPFClass ctor = new YPFClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}
