package ysharp.treewalk.evaluator.Native.TUI.Terminal;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFrame;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Interpreter;
import ysharp.treewalk.evaluator.Native.TUI.Terminal.Abstract.yAbstractTerminal;
import ysharp.treewalk.evaluator.Native.TUI.Terminal.Abstract.yBaseTerminal;
import ysharp.treewalk.evaluator.RuntimeObject;
import ysharp.treewalk.evaluator.Variable;
import ysharp.treewalk.evaluator.yClass;

import javax.imageio.ImageIO;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class yDefaultTerminal {

    public static RuntimeObject yDefaultTerminal_Instance_Prototype;

    static {
        yDefaultTerminal_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__DefaultTerminal__";
            }

            @Override
            public String toString() {
                return "<prototype:DefaultTerminal>";
            }
        };
        yDefaultTerminal_Instance_Prototype.prototype = yBaseTerminal.yBaseTerminal_Instance_Prototype;


    }

    public static class YDefaultTerminalInstance extends yAbstractTerminal.AbstractTerminal {

        public YDefaultTerminalInstance() {
            this.prototype = yDefaultTerminal_Instance_Prototype;
            try {
                this.instance = new DefaultTerminalFactory().createTerminal();

                if (instance instanceof SwingTerminalFrame frame) {
                    frame.setTitle("Ysharp TUI");
                    // load ysharp logo
                    URL logoUrl = getClass().getClassLoader().getResource("ysharplogo.png");

                    BufferedImage icon = null;

                    if (logoUrl != null) {
                        icon = ImageIO.read(logoUrl);
                    }

                    if (icon != null) {
                        frame.setIconImage(icon);
                    }

                    frame.setIconImage(icon);

                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            assign("isClosed", new Variable.Variant(true));
                        }

                        @Override
                        public void windowClosed(WindowEvent e) {
                            assign("isClosed", new Variable.Variant(true));
                        }
                    });
                }

                if (instance instanceof AWTTerminalFrame frame) {
                    frame.setTitle("Ysharp TUI");
                    // load ysharp logo
                    URL logoUrl = getClass().getClassLoader().getResource("ysharplogo.png");

                    BufferedImage icon = null;

                    if (logoUrl != null) {
                        icon = ImageIO.read(logoUrl);
                    }

                    if (icon != null) {
                        frame.setIconImage(icon);
                    }

                    frame.setIconImage(icon);

                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            assign("isClosed", new Variable.Variant(true));

                            try {
                                instance.close();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            System.exit(0);
                        }

                        @Override
                        public void windowClosed(WindowEvent e) {
                            assign("isClosed", new Variable.Variant(true));
                        }
                    });
                }

            }
            catch (IOException ex) {
                throw new YsharpException(YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "Failed to initialize terminal.");
            }

        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "DefaultTerminal";
        }

        @Override
        public String toString() {
            return "<instance:DefaultTerminal>";
        }
    }

    public static class yDefaultTerminalClass extends yClass.SealedClassObject {

        public yDefaultTerminalClass(){
            this.prototype =  yClass.ClassPrototype;

        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpException {

            requireArity(arguments,0, getClassName());

            YDefaultTerminalInstance instance = new YDefaultTerminalInstance();

            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "DefaultTerminal";
        }

        @Override
        public String getType() {
            return "DefaultTerminal";
        }

        @Override
        public String toString() {
            return "<class:DefaultTerminal>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yDefaultTerminalClass ctor = new yDefaultTerminalClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}
