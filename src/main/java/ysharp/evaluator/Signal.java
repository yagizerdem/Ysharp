package ysharp.evaluator;

public abstract class Signal extends RuntimeException {

    public Signal() {
        super(null, null, false, false);
    }

    public static class BreakSignal extends Signal {}
    public static class ContinueSignal extends Signal {}
    public static class ReturnSignal extends Signal {}

}