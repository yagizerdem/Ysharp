package ysharp;

import com.formdev.flatlaf.FlatDarkLaf;
import ysharp.evaluator.Core;

public class Main {


    public static void main(String[] args) throws  Exception {
        FlatDarkLaf.setup();

        Core core = new Core();
        core.start();


    }

} 