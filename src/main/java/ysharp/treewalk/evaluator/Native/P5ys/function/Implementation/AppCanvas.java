package ysharp.treewalk.evaluator.Native.P5ys.function.Implementation;

import ysharp.treewalk.evaluator.Native.P5ys.function.Implementation.Components.Ellipse;
import ysharp.treewalk.evaluator.Native.P5ys.function.Implementation.Components.IComponent;
import ysharp.treewalk.evaluator.Native.P5ys.function.Implementation.Components.Line;
import ysharp.treewalk.evaluator.Native.P5ys.function.Implementation.Components.Triangle;

import java.awt.*;
import java.util.ArrayList;

public class AppCanvas extends Canvas {
    private final java.util.List<IComponent> components = new ArrayList<>();


    public void addLine(int x1, int y1, int x2, int y2, Color color) {
        components.add(new Line(x1, y1, x2, y2, color));
        repaint();
    }

    public void addEllipse(int x, int y, int width, int height, Color color) {
        components.add(new Ellipse(x, y, width, height, color));
        repaint();
    }

    public void addEllipse(int x, int y, int width, int height,
                           Color fillColor, Color strokeColor, boolean filled) {
        components.add(new Ellipse(x, y, width, height, fillColor, strokeColor, filled));
        repaint();
    }

    public void addRectangle(int x, int y, int width, int height, Color color) {
        components.add(new ysharp.treewalk.evaluator.Native.P5ys.function.Implementation.Components.Rectangle(x, y, width, height, color));
        repaint();
    }

    public void addRectangle(int x, int y, int width, int height,
                             Color fillColor, Color strokeColor, boolean filled) {
        components.add(new ysharp.treewalk.evaluator.Native.P5ys.function.Implementation.Components.Rectangle(x, y, width, height, fillColor, strokeColor, filled));
        repaint();
    }

    public void addTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
        components.add(new Triangle(x1, y1, x2, y2, x3, y3, color));
        repaint();
    }

    public void addTriangle(int x1, int y1, int x2, int y2, int x3, int y3,
                            Color fillColor, Color strokeColor, boolean filled) {
        components.add(new Triangle(x1, y1, x2, y2, x3, y3, fillColor, strokeColor, filled));
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (IComponent component : components) {
            component.paint(g);
        }
    }

}
