package ysharp.treewalk.evaluator.Native.TUI.Terminal;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
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
import java.net.URL;
import java.util.List;

public class ySwingTerminal {

    public static RuntimeObject ySwingTerminal_Instance_Prototype;

    static {
        ySwingTerminal_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() {
                return true;
            }

            @Override
            public String getType() {
                return "__YPFTerminal__";
            }
        };
        ySwingTerminal_Instance_Prototype.prototype = yBaseTerminal.yBaseTerminal_Instance_Prototype;
    }

    public static class ySwingTerminalInstance extends  yAbstractTerminal.AbstractTerminal {

        public ySwingTerminalInstance() {
            try {
                this.prototype = ySwingTerminal_Instance_Prototype;

                DefaultTerminalFactory factory = new DefaultTerminalFactory();
                factory.setInitialTerminalSize(new TerminalSize(80, 24));
                factory.setForceAWTOverSwing(true);
                Terminal terminal = factory.createTerminal();

                this.instance = terminal;

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


                Variable autoFlushVar = new Variable(new Variable.Variant(false), false, "bool");
                this.set("autoFlush", autoFlushVar);
            }catch (Exception ex) {
                throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1 , ex.getMessage());
            }

        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "YPFTerminal";
        }

        @Override
        public String toString() {
            return "<instance:YPFTerminal>";
        }
    }

    public static class ySwingTerminalClass extends yClass.SealedClassObject {

        public ySwingTerminalClass(){
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

            ySwingTerminalInstance instance = new ySwingTerminalInstance();

            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "YPFTerminal";
        }

        @Override
        public String getType() {
            return "YPFTerminal";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        ySwingTerminalClass ctor = new ySwingTerminalClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }
}
