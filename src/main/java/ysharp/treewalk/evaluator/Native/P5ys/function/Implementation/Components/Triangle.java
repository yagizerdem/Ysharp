package ysharp.treewalk.evaluator.Native.P5ys.function.Implementation.Components;

import java.awt.*;

public class Triangle implements IComponent {
    int x1, y1, x2, y2, x3, y3;
    Color fillColor;
    Color strokeColor;
    boolean filled;

    public Triangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
        this(x1, y1, x2, y2, x3, y3, color, null, true);
    }

    public Triangle(int x1, int y1, int x2, int y2, int x3, int y3,
                    Color fillColor, Color strokeColor, boolean filled) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.filled = filled;
    }

    @Override
    public void paint(Graphics g) {
        Color prev = g.getColor();

        int[] xs = { x1, x2, x3 };
        int[] ys = { y1, y2, y3 };

        if (filled && fillColor != null) {
            g.setColor(fillColor);
            g.fillPolygon(xs, ys, 3);
        }

        if (strokeColor != null) {
            g.setColor(strokeColor);
            g.drawPolygon(xs, ys, 3);
        }

        g.setColor(prev);
    }
}