package ysharp.evaluator;

public abstract class Signal extends RuntimeException {

    public Signal() {
        super(null, null, false, false);
    }

    public static class BreakSignal extends Signal {}
    public static class ContinueSignal extends Signal {}
    public static class ReturnSignal extends Signal {
        public Variable.Variant value; // nullable
        ReturnSignal(Variable.Variant value) {
            this.value = value;
        }
    }
    public static class ThrowSignal extends RuntimeException {

        public final Variable.Variant value;

        public ThrowSignal(Variable.Variant value) {
            this.value = value;
        }
    }


}