package ysharp;

import javax.swing.*;
import java.awt.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import ysharp.evaluator.Core;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Main {


    public static void main(String[] args) throws  Exception {
        FlatDarkLaf.setup();

        Core core = new Core();
        core.start();


    }

} 