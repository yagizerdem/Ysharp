package ysharp.treewalk.evaluator.Native.P5ys.function.Implementation.Components;

import javax.swing.*;
import java.awt.*;

public class Line implements IComponent {
    int x1, y1, x2, y2;
    Color color;

    public Line(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    @Override
    public void paint(Graphics g) {
        Color prev = g.getColor();

        g.setColor(this.color);
        g.drawLine(this.x1, this.y1, this.x2, this.y2);

        g.setColor(prev);
    }
}