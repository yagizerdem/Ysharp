package ysharp.treewalk.evaluator.Native.Util.Math;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Collections.TreeSet.function.instance.CeilingFn;
import ysharp.treewalk.evaluator.Native.Util.Math.function.statix.*;

import java.util.List;

public class yMath {


    public static class yMathClass extends yClass.SealedClassObject {

        private yMathClass(){
            this.prototype =  yClass.ClassPrototype;

            // Math.pow(a: number, b: int)
            RegisterNativeFn(new PowFn());
            // Math.sqrt(a: number)
            RegisterNativeFn(new SqrtFn());
            // Math.abs(a: number)
            RegisterNativeFn(new AbsFn());
            // Math.floor(a: number)
            RegisterNativeFn(new FloorFn());
            // Math.ceil(a: number)
            RegisterNativeFn(new CeilingFn());
            // Math.max(a: number, b: number)
            RegisterNativeFn(new MaxFn());
            // Math.min(a: number, b: number)
            RegisterNativeFn(new MinFn());
            // Math.sin(a: number)
            RegisterNativeFn(new SinFn());
            // Math.cos(a: number)
            RegisterNativeFn(new CosFn());
            // Math.tan(a: number)
            RegisterNativeFn(new TanFn());
            // Math.log(a: number)
            RegisterNativeFn(new LogFn());
            // Math.exp(a: number)
            RegisterNativeFn(new ExpFn());
            // Math.round(a: number)
            RegisterNativeFn(new RoundFn());
            // Math.random()
            RegisterNativeFn(new RoundFn());
            // Math.clamp(value: number, min: number, max: number)
            RegisterNativeFn(new ClampFn());
            // Math.atan(a: number)
            RegisterNativeFn(new AtanFn());
            // Math.atan2(y: number, x: number)
            RegisterNativeFn(new Atan2Fn());
            // Math.hypot(a: number, b: number)
            RegisterNativeFn(new HypotFn());
            // Math.sign(a: number)
            RegisterNativeFn(new SignFn());
            // Math.degToRad(a: number)
            RegisterNativeFn(new DegToRadFn());
            // Math.radToDeg(a: number)
            RegisterNativeFn(new RadToDegFn());
            // Math.lerp(a: number, b: number, t: number)
            RegisterNativeFn(new LerpFn());
            // Math.asin(a: number)
            RegisterNativeFn(new AsinFn());
            // Math.acos(a: number)
            RegisterNativeFn(new AcosFn());
            // Math.log10(a: number)
            RegisterNativeFn(new Log10Fn());
            // Math.log2(a: number)
            RegisterNativeFn(new Log2Fn());
            // Math.cbrt(a: number)
            RegisterNativeFn(new CbrtFn());
            // Math.trunc(a: number)
            RegisterNativeFn(new TruncFn());
            // Math.fract(a: number)
            RegisterNativeFn(new FractFn());


            // add constants
            this.set("PI", new Variable(
                    new Variable.Variant(Math.PI),
                    true,
                    "function"
            ));

            this.set("E", new Variable(
                    new Variable.Variant(Math.E),
                    true,
                    "function"));
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
            return "Math";
        }

        @Override
        public String getType() {
            return "_Math_";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        yMathClass ctor = new yMathClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                ctor.getType());

        interpreter.defineGlobal(ctor.getClassName(), var);
    }

}
