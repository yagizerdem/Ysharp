package ysharp.treewalk.evaluator.Native.P5ys.function.Implementation;

import ysharp.treewalk.evaluator.Native.P5ys.function.Implementation.Components.Ellipse;

import javax.swing.*;
import java.awt.*;

public class DrawingContext extends JFrame {

    private final AppCanvas c;

    public DrawingContext() {
        // creates drawing context
        super("YPF Drawer API");

        this.c = new AppCanvas();
        this.c.setBackground(Color.BLACK);

        this.add(c);
        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        c.addLine(x1, y1, x2, y2, color);
    }

    public void drawEllipse(int x, int y, int width, int height, Color color) {
        c.addEllipse(x, y, width, height, color);
    }

    public void drawEllipse(int x, int y, int width, int height,
                           Color fillColor, Color strokeColor, boolean filled) {
        c.addEllipse(x, y, width, height, fillColor, strokeColor, filled);
    }

    public void drawRectangle(int x, int y, int width, int height, Color color) {
        c.addRectangle(x, y, width, height, color);
    }

    public void drawRectangle(int x, int y, int width, int height,
                              Color fillColor, Color strokeColor, boolean filled) {
        c.addRectangle(x, y, width, height, fillColor, strokeColor, filled);
    }

    public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
        c.addTriangle(x1, y1, x2, y2, x3, y3, color);
    }

    public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3,
                             Color fillColor, Color strokeColor, boolean filled) {
        c.addTriangle(x1, y1, x2, y2, x3, y3, fillColor, strokeColor, filled);
    }

}