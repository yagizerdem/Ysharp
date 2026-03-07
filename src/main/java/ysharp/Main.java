package ysharp;



import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import ysharp.evaluator.Core;

public class Main {
    public static void main(String[] args) throws  Exception {
        Core core = new Core();
         core.start();


//        try (Terminal terminal = new DefaultTerminalFactory().createTerminal()) {
//            // Terminal functionality here
//
//            terminal.putCharacter('H');
//            terminal.putCharacter('e');
//            terminal.putCharacter('l');
//            terminal.putCharacter('l');
//            terminal.putCharacter('o');
//            terminal.flush();
//
//            while (true) {}
//        }

    }
}