package ysharp.treewalk.evaluator.Native.Util;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Util.Random.function.statix.*;

import java.util.List;
import java.util.Random;

public class yRandom {

    public static class yRandomClass extends yClass.SealedClassObject {

        yRandomClass() {
            this.prototype = yClass.ClassPrototype;

            // add static methods here

            // Random.next() -> number in [0.0, 1.0)
            RegisterNativeFn(new NextFn());
            // Random.nextInt(min: int, max: int) -> int in [min, max)
            RegisterNativeFn(new NextIntFn());
            // Random.nextFloat(min: number, max: number) -> number in [min, max)
            RegisterNativeFn(new NextFloatFn());
            // Random.nextBool() -> boolean (0.0 or 1.0)
            RegisterNativeFn(new NextBoolFn());
            // Random.nextGaussian() -> number (mean=0, stddev=1)
            RegisterNativeFn(new NextGaussianFn());
            // Random.setSeed(seed: int)
            RegisterNativeFn(new SetSeedFn());
            // Random.chance(probability: number) -> bool  e.g. chance(0.3) = 30% true
            RegisterNativeFn(new ChanceFn());
            // Random.pick(a: number, b: number) -> one of the two values at random
            RegisterNativeFn(new PickFn());

        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpException {

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Cannot create instance of static class '" + getClassName() + "'."
            );
        }

        @Override
        public String getClassName() {
            return "Random";
        }

        @Override
        public String getType() {
            return "_Random_";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yRandomClass ctor = new yRandomClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                "function");

        interpreter.defineGlobal(ctor.getClassName(), var);
    }

}