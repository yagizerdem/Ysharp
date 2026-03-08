package ysharp.evaluator;

public class yThrow extends RuntimeException {

    public final Variable.Variant value;

    public yThrow(Variable.Variant value) {
        this.value = value;
    }
}
