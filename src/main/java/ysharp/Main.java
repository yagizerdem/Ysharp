package ysharp;

import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import ysharp.treewalk.TreeWalk;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import ysharp.treewalk.evaluator.Native.P5ys.function.Implementation.DrawingContext;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws  Exception {
         TreeWalk.start(args);
    }
}