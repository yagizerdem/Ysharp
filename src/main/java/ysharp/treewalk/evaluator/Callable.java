package ysharp.treewalk.evaluator;

import ysharp.treewalk.YsharpError;

import java.util.List;

public interface Callable {

    int arity();

    Variable.Variant call(Interpreter interpreter,
                          List<Variable.Variant> arguments) throws YsharpError;
}