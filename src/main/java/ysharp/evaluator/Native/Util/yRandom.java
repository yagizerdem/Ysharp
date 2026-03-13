package ysharp.evaluator.Native.Util;

import ysharp.YsharpError;
import ysharp.evaluator.*;

import java.util.List;
import java.util.Random;

public class yRandom {


    static {
        // all the methods in random class should be static because i want so !
    }

    public static class yRandomInstance extends yClass.ClassObjectInstance {

        public yRandomInstance() {}

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Random";
        }

        @Override
        public String toString() {
            return "<instance:Random>";
        }
    }


    public static class yRandomClass extends yClass.SealedClassObject {

        private static final Random rng = new Random();

        yRandomClass() {
            this.prototype = yClass.ClassPrototype;

            // add static methods here

            // Random.next() -> number in [0.0, 1.0)
            class NextFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    double response = rng.nextDouble();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "next";
                }
            }

            NextFn next = new NextFn();
            Variable nextVar = new Variable(
                    new Variable.Variant(next),
                    true,
                    "function");
            this.set(next.getFnName(), nextVar);


            // Random.nextInt(min: int, max: int) -> int in [min, max]
            class NextIntFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    double min = requireInt(arguments.get(0), getClassName(), 1);
                    double max = requireInt(arguments.get(1), getClassName(), 2);

                    double response = (double) ((int) min + rng.nextInt((int) max - (int) min + 1));

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "nextInt";
                }
            }

            NextIntFn nextInt = new NextIntFn();
            Variable nextIntVar = new Variable(
                    new Variable.Variant(nextInt),
                    true,
                    "function");
            this.set(nextInt.getFnName(), nextIntVar);


            // Random.nextFloat(min: number, max: number) -> number in [min, max)
            class NextFloatFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    double min = requireNumber(arguments.get(0), getClassName(), 1);
                    double max = requireNumber(arguments.get(1), getClassName(), 2);

                    double response = min + (max - min) * rng.nextDouble();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "nextFloat";
                }
            }

            NextFloatFn nextFloat = new NextFloatFn();
            Variable nextFloatVar = new Variable(
                    new Variable.Variant(nextFloat),
                    true,
                    "function");
            this.set(nextFloat.getFnName(), nextFloatVar);


            // Random.nextBool() -> boolean (0.0 or 1.0)
            class NextBoolFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    boolean response = rng.nextBoolean();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "nextBool";
                }
            }

            NextBoolFn nextBool = new NextBoolFn();
            Variable nextBoolVar = new Variable(
                    new Variable.Variant(nextBool),
                    true,
                    "function");
            this.set(nextBool.getFnName(), nextBoolVar);


            // Random.nextGaussian() -> number (mean=0, stddev=1)
            class NextGaussianFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 0;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    double response = rng.nextGaussian();

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "nextGaussian";
                }
            }

            NextGaussianFn nextGaussian = new NextGaussianFn();
            Variable nextGaussianVar = new Variable(
                    new Variable.Variant(nextGaussian),
                    true,
                    "function");
            this.set(nextGaussian.getFnName(), nextGaussianVar);


            // Random.setSeed(seed: int)
            class SetSeedFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double seed = requireInt(arguments.getFirst(), getClassName(), 1);
                    rng.setSeed((long) seed);

                    return new Variable.Variant((Object) null);
                }

                @Override
                public String getFnName() {
                    return "setSeed";
                }
            }

            SetSeedFn setSeed = new SetSeedFn();
            Variable setSeedVar = new Variable(
                    new Variable.Variant(setSeed),
                    true,
                    "function");
            this.set(setSeed.getFnName(), setSeedVar);


            // Random.chance(probability: number) -> bool  e.g. chance(0.3) = 30% true
            class ChanceFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 1;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 1, getClassName());

                    double probability = requireNumber(arguments.getFirst(), getClassName(), 1);
                    boolean response = rng.nextDouble() < probability;

                    return new Variable.Variant(response);
                }

                @Override
                public String getFnName() {
                    return "chance";
                }
            }

            ChanceFn chance = new ChanceFn();
            Variable chanceVar = new Variable(
                    new Variable.Variant(chance),
                    true,
                    "function");
            this.set(chance.getFnName(), chanceVar);


            // Random.pick(a: number, b: number) -> one of the two values at random
            class PickFn extends Function.NativeFunction {

                @Override
                public int arity() {
                    return 2;
                }

                @Override
                public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {

                    requireArity(arguments, 2, getClassName());

                    Variable.Variant a = arguments.get(0);
                    Variable.Variant b = arguments.get(1);

                    return rng.nextBoolean() ? a : b;
                }

                @Override
                public String getFnName() {
                    return "pick";
                }
            }

            PickFn pick = new PickFn();
            Variable pickVar = new Variable(
                    new Variable.Variant(pick),
                    true,
                    "function");
            this.set(pick.getFnName(), pickVar);
        }

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter,
                                     List<Variable.Variant> arguments)
                throws YsharpError {

            requireArity(arguments, 0, getClassName());

            yRandomInstance instance = new yRandomInstance();
            return new Variable.Variant(instance);
        }

        @Override
        public String getClassName() {
            return "Random";
        }

        @Override
        public String getType() {
            return "Random";
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