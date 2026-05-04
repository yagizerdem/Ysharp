package ysharp.treewalk.evaluator.Native.P5ys.function.Implementation.Components;

import java.awt.*;

public class Ellipse implements IComponent {
    int x, y, width, height;
    Color fillColor;
    Color strokeColor;
    boolean filled;

    public Ellipse(int x, int y, int width, int height, Color color) {
        this(x, y, width, height, color, null, true);
    }

    public Ellipse(int x, int y, int width, int height, Color fillColor, Color strokeColor, boolean filled) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.filled = filled;
    }

    @Override
    public void paint(Graphics g) {
        Color prev = g.getColor();

        if (filled && fillColor != null) {
            g.setColor(fillColor);
            g.fillOval(x, y, width, height);
        }

        if (strokeColor != null) {
            g.setColor(strokeColor);
            g.drawOval(x, y, width, height);
        }

        g.setColor(prev);
    }
}