package ysharp;

import ysharp.evaluator.Core;

public class Main {
    public static void main(String[] args) throws  Exception {

        System.setProperty("java.awt.headless", "false");

        Core core = new Core();
        core.start();



    }
} 