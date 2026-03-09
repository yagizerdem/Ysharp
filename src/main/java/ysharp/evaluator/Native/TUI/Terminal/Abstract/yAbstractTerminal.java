package ysharp.evaluator.Native.TUI.Terminal.Abstract;

import com.googlecode.lanterna.terminal.Terminal;
import ysharp.evaluator.yClass;

import javax.swing.*;

public class yAbstractTerminal {

    public static abstract class AbstractTerminal extends yClass.ClassObjectInstance  {
        public Terminal instance;
    };

    public static abstract class AbstractSwingTerminal extends yAbstractTerminal.AbstractTerminal {
        public JFrame frame;
    };

}
