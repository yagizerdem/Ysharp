package ysharp;

import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import ysharp.treewalk.TreeWalk;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws  Exception {
        TreeWalk.start(args);
    }
}