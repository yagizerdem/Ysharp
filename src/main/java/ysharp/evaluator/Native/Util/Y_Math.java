package ysharp.evaluator.Native.Util;

import ysharp.YsharpError;
import ysharp.evaluator.*;
import ysharp.parser.TypeTag;

import java.util.List;

public class Y_Math {


    static {
        // all the methods in math class should be static because i want so !
    }

    public static class Y_MathInstance extends Y_Class.ClassObjectInstance {

        public Y_MathInstance() {}

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Math";
        }

        @Override
        public String toString() {
            return "<instance:Math>";
        }
    }


    public static class Y_MathClass extends Y_Class.SealedClassObject {

        Y_MathClass(){
            this.prototype =  Y_Class.ClassPrototype;

            // add static methods here

            // Math.pow(a: number, b: int)
            class PowFn extends Function.NativeFunction {
                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    double base = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double power = requireInt(arguments.get(1), getClassName(), 2);
                    double response = Math.pow(base, power);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "pow";
                }
            }

            PowFn pow = new PowFn();
            Variable powVar = new Variable(
                    new Variable.Variant(pow),
                    true,
                    TypeTag.OBJECT);
            this.prototype.set(pow.getFnName(), powVar);

            // Math.sqrt(a: number)
            class SqrtFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.sqrt(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "sqrt";
                }
            }

            SqrtFn sqrt = new SqrtFn();
            Variable sqrtVar = new Variable(
                    new Variable.Variant(sqrt),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(sqrt.getFnName(), sqrtVar);


            // Math.abs(a: number)
            class AbsFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.abs(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "abs";
                }
            }

            AbsFn abs = new AbsFn();
            Variable absVar = new Variable(
                    new Variable.Variant(abs),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(abs.getFnName(), absVar);


            // Math.floor(a: number)
            class FloorFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.floor(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "floor";
                }
            }

            FloorFn floor = new FloorFn();
            Variable floorVar = new Variable(
                    new Variable.Variant(floor),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(floor.getFnName(), floorVar);


            // Math.ceil(a: number)
            class CeilFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.ceil(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "ceil";
                }
            }

            CeilFn ceil = new CeilFn();
            Variable ceilVar = new Variable(
                    new Variable.Variant(ceil),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(ceil.getFnName(), ceilVar);


            // Math.max(a: number, b: number)
            class MaxFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    double a = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double b = requireNumber(arguments.get(1), getClassName(), 2);

                    double response = Math.max(a, b);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "max";
                }
            }

            MaxFn max = new MaxFn();
            Variable maxVar = new Variable(
                    new Variable.Variant(max),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(max.getFnName(), maxVar);


            // Math.min(a: number, b: number)
            class MinFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    double a = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double b = requireNumber(arguments.get(1), getClassName(), 2);

                    double response = Math.min(a, b);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "min";
                }
            }

            MinFn min = new MinFn();
            Variable minVar = new Variable(
                    new Variable.Variant(min),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(min.getFnName(), minVar);


            // Math.sin(a: number)
            class SinFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.sin(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "sin";
                }
            }

            SinFn sin = new SinFn();
            Variable sinVar = new Variable(
                    new Variable.Variant(sin),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(sin.getFnName(), sinVar);


            // Math.cos(a: number)
            class CosFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.cos(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "cos";
                }
            }

            CosFn cos = new CosFn();
            Variable cosVar = new Variable(
                    new Variable.Variant(cos),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(cos.getFnName(), cosVar);


            // Math.tan(a: number)
            class TanFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.tan(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "tan";
                }
            }

            TanFn tan = new TanFn();
            Variable tanVar = new Variable(
                    new Variable.Variant(tan),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(tan.getFnName(), tanVar);


            // Math.log(a: number)
            class LogFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.log(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "log";
                }
            }

            LogFn log = new LogFn();
            Variable logVar = new Variable(
                    new Variable.Variant(log),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(log.getFnName(), logVar);


            // Math.exp(a: number)
            class ExpFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.exp(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "exp";
                }
            }

            ExpFn exp = new ExpFn();
            Variable expVar = new Variable(
                    new Variable.Variant(exp),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(exp.getFnName(), expVar);


            // Math.round(a: number)
            class RoundFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.round(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "round";
                }
            }

            RoundFn round = new RoundFn();
            Variable roundVar = new Variable(
                    new Variable.Variant(round),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(round.getFnName(), roundVar);


            // Math.random()
            class RandomFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    double response = Math.random();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "random";
                }
            }

            RandomFn random = new RandomFn();
            Variable randomVar = new Variable(
                    new Variable.Variant(random),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(random.getFnName(), randomVar);


            // Math.clamp(value: number, min: number, max: number)
            class ClampFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 3;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 3, getClassName());

                    double value = requireNumber(arguments.get(0), getClassName(), 1);
                    double min = requireNumber(arguments.get(1), getClassName(), 2);
                    double max = requireNumber(arguments.get(2), getClassName(), 3);

                    double response = Math.max(min, Math.min(max, value));

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "clamp";
                }
            }

            ClampFn clamp = new ClampFn();
            Variable clampVar = new Variable(
                    new Variable.Variant(clamp),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(clamp.getFnName(), clampVar);

            // Math.atan(a: number)
            class AtanFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.atan(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "atan";
                }
            }

            AtanFn atan = new AtanFn();
            Variable atanVar = new Variable(
                    new Variable.Variant(atan),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(atan.getFnName(), atanVar);


            // Math.atan2(y: number, x: number)
            class Atan2Fn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    double y = requireNumber(arguments.get(0), getClassName(), 1);
                    double x = requireNumber(arguments.get(1), getClassName(), 2);

                    double response = Math.atan2(y, x);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "atan2";
                }
            }

            Atan2Fn atan2 = new Atan2Fn();
            Variable atan2Var = new Variable(
                    new Variable.Variant(atan2),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(atan2.getFnName(), atan2Var);


            // Math.hypot(a: number, b: number)
            class HypotFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    double a = requireNumber(arguments.get(0), getClassName(), 1);
                    double b = requireNumber(arguments.get(1), getClassName(), 2);

                    double response = Math.hypot(a, b);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "hypot";
                }
            }

            HypotFn hypot = new HypotFn();
            Variable hypotVar = new Variable(
                    new Variable.Variant(hypot),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(hypot.getFnName(), hypotVar);


            // Math.sign(a: number)
            class SignFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.signum(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "sign";
                }
            }

            SignFn sign = new SignFn();
            Variable signVar = new Variable(
                    new Variable.Variant(sign),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(sign.getFnName(), signVar);


            // Math.degToRad(a: number)
            class DegToRadFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double degrees = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = degrees * (Math.PI / 180.0);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "degToRad";
                }
            }

            DegToRadFn degToRad = new DegToRadFn();
            Variable degToRadVar = new Variable(
                    new Variable.Variant(degToRad),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(degToRad.getFnName(), degToRadVar);


            // Math.radToDeg(a: number)
            class RadToDegFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double radians = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = radians * (180.0 / Math.PI);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "radToDeg";
                }
            }

            RadToDegFn radToDeg = new RadToDegFn();
            Variable radToDegVar = new Variable(
                    new Variable.Variant(radToDeg),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(radToDeg.getFnName(), radToDegVar);


            // Math.lerp(a: number, b: number, t: number)
            class LerpFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 3;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 3, getClassName());

                    double a = requireNumber(arguments.get(0), getClassName(), 1);
                    double b = requireNumber(arguments.get(1), getClassName(), 2);
                    double t = requireNumber(arguments.get(2), getClassName(), 3);

                    double response = a + (b - a) * t;

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "lerp";
                }
            }

            LerpFn lerp = new LerpFn();
            Variable lerpVar = new Variable(
                    new Variable.Variant(lerp),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(lerp.getFnName(), lerpVar);


            // Math.asin(a: number)
            class AsinFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.asin(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "asin";
                }
            }

            AsinFn asin = new AsinFn();
            Variable asinVar = new Variable(
                    new Variable.Variant(asin),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(asin.getFnName(), asinVar);


            // Math.acos(a: number)
            class AcosFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.acos(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "acos";
                }
            }

            AcosFn acos = new AcosFn();
            Variable acosVar = new Variable(
                    new Variable.Variant(acos),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(acos.getFnName(), acosVar);


            // Math.log10(a: number)
            class Log10Fn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.log10(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "log10";
                }
            }

            Log10Fn log10 = new Log10Fn();
            Variable log10Var = new Variable(
                    new Variable.Variant(log10),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(log10.getFnName(), log10Var);


            // Math.log2(a: number)
            class Log2Fn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.log(value) / Math.log(2);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "log2";
                }
            }

            Log2Fn log2 = new Log2Fn();
            Variable log2Var = new Variable(
                    new Variable.Variant(log2),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(log2.getFnName(), log2Var);


            // Math.cbrt(a: number)
            class CbrtFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);
                    double response = Math.cbrt(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "cbrt";
                }
            }

            CbrtFn cbrt = new CbrtFn();
            Variable cbrtVar = new Variable(
                    new Variable.Variant(cbrt),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(cbrt.getFnName(), cbrtVar);


            // Math.trunc(a: number)
            class TruncFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);

                    double response = (value >= 0) ? Math.floor(value) : Math.ceil(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "trunc";
                }
            }

            TruncFn trunc = new TruncFn();
            Variable truncVar = new Variable(
                    new Variable.Variant(trunc),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(trunc.getFnName(), truncVar);


            // Math.fract(a: number)
            class FractFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double value = requireNumber(arguments.getFirst(), getClassName(), 1);

                    double response = value - Math.floor(value);

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "fract";
                }
            }

            FractFn fract = new FractFn();
            Variable fractVar = new Variable(
                    new Variable.Variant(fract),
                    true,
                    TypeTag.OBJECT);

            this.prototype.set(fract.getFnName(), fractVar);


            // add constants
            Variable PI_var  = new Variable(
                    new Variable.Variant(Math.PI),
                    true,
                    TypeTag.OBJECT
            );
            this.prototype.set("PI", PI_var);

            Variable E_var  = new Variable(
                    new Variable.Variant(Math.E),
                    true,
                    TypeTag.OBJECT
            );
            this.prototype.set("E", E_var);
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            requireArity(arguments,0, getClassName());

            Y_MathInstance instance = new Y_MathInstance();
            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "Math";
        }

        @Override
        public String getType() {
            return "Math";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {

        Y_MathClass ctor = new Y_MathClass();

        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant,
                true,
                TypeTag.OBJECT);

        interpreter.defineGlobal(ctor.getClassName(), var);
    }

}
