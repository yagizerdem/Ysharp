package ysharp.evaluator;

import ysharp.YsharpError;
import ysharp.evaluator.Native.Collections.yArray;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class yString  {

    private static yString.yStringInstance requireStringThis (Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    "Method " + "'" + fnName+ "'" + "called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yString.yStringInstance)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    "Method '" + fnName + "' expected 'string' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return  (yString.yStringInstance) obj;
    }

    public static RuntimeObject yString_Instance_Prototype;

    static {
        yString_Instance_Prototype = new RuntimeObject() {

            @Override
            public boolean isTruthy() { return true; }

            @Override
            public String getType() { return "__string__"; }

            @Override
            public String toString() {
                return "<prototype:string>";
            }
        };
        yString_Instance_Prototype.prototype = yClass.ClassPrototype;


        // str.length()
        class LengthFn extends Function.NativeFunction implements Callable {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                return new Variable.Variant((int)instance.data.length());
            }

            @Override
            public String getFnName() {
                return "length";
            }
        }

        LengthFn length = new LengthFn();
        Variable lengthVar = new Variable(
                new Variable.Variant(length),
                true,
                "function");

        yString_Instance_Prototype.set(length.getFnName(), lengthVar);


        // str.toUpper()
        class ToUpperFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                String upper = instance.data.toUpperCase();

                yStringInstance newStr = new yStringInstance(upper);

                return new Variable.Variant(newStr);
            }

            @Override
            public String getFnName() {
                return "toUpper";
            }
        }

        ToUpperFn toUpper = new ToUpperFn();
        Variable toUpperVar = new Variable(
                new Variable.Variant(toUpper),
                true,
                "function");
        yString_Instance_Prototype.set(toUpper.getFnName(), toUpperVar);

        // str.toLower()
        class ToLowerFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                String lower = instance.data.toLowerCase();

                yStringInstance newStr = new yStringInstance(lower);

                return new Variable.Variant(newStr);
            }

            @Override
            public String getFnName() {
                return "toLower";
            }
        }

        ToLowerFn toLower = new ToLowerFn();
        Variable toLowerVar = new Variable(
                new Variable.Variant(toLower),
                true,
                "function");
        yString_Instance_Prototype.set(toLower.getFnName(), toLowerVar);

        // str.charAt(index)
        class CharAtFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant indexVar = arguments.get(0);

                if (!indexVar.canImplicitlyConvertNumber()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'charAt' index must be a number."
                    );
                }

                int index = (int) indexVar.implicitlyConvertNumber();

                if (index < 0 || index >= instance.data.length()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "String index out of bounds."
                    );
                }

                char ch = instance.data.charAt(index);

                return new Variable.Variant(ch);
            }

            @Override
            public String getFnName() {
                return "charAt";
            }
        }

        CharAtFn charAt = new CharAtFn();
        Variable charAtVar = new Variable(
                new Variable.Variant(charAt),
                true,
                "function");
        yString_Instance_Prototype.set(charAt.getFnName(), charAtVar);

        // str.substring(start, end)
        class SubstringFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant startVar = arguments.get(0);
                Variable.Variant endVar   = arguments.get(1);

                if (!startVar.canImplicitlyConvertNumber() ||
                        !endVar.canImplicitlyConvertNumber()) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'substring' arguments must be numbers."
                    );
                }

                int start = (int) startVar.implicitlyConvertNumber();
                int end   = (int) endVar.implicitlyConvertNumber();

                int len = instance.data.length();

                if (start < 0 || end < 0 || start > end || end > len) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "Invalid substring range."
                    );
                }

                String sub = instance.data.substring(start, end);

                yStringInstance newStr = new yStringInstance(sub);

                return new Variable.Variant(newStr);
            }

            @Override
            public String getFnName() {
                return "substring";
            }
        }

        SubstringFn substring = new SubstringFn();
        Variable substringVar = new Variable(
                new Variable.Variant(substring),
                true,
                "function");
        yString_Instance_Prototype.set(substring.getFnName(), substringVar);

        // str.equals(other)
        class EqualsFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant otherVar = arguments.get(0);

                if (!otherVar.isRuntimeObject()) {
                    return new Variable.Variant(false);
                }

                RuntimeObject otherObj = otherVar.asRuntimeObject();

                if (!(otherObj instanceof yStringInstance)) {
                    return new Variable.Variant(false);
                }

                yStringInstance otherString = (yStringInstance) otherObj;

                boolean result = instance.data.equals(otherString.data);

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "equals";
            }
        }

        EqualsFn equals = new EqualsFn();
        Variable equalsVar = new Variable(
                new Variable.Variant(equals),
                true,
                "function");
        yString_Instance_Prototype.set(equals.getFnName(), equalsVar);

        // str.indexOf(other)
        class IndexOfFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant otherVar = arguments.get(0);

                if (!otherVar.isRuntimeObject()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'indexOf' argument must be a string."
                    );
                }

                RuntimeObject otherObj = otherVar.asRuntimeObject();

                if (!(otherObj instanceof yStringInstance)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'indexOf' argument must be a string."
                    );
                }

                yStringInstance otherString = (yStringInstance) otherObj;

                int index = instance.data.indexOf(otherString.data);

                return new Variable.Variant(index);
            }

            @Override
            public String getFnName() {
                return "indexOf";
            }
        }

        IndexOfFn indexOf = new IndexOfFn();
        Variable indexOfVar = new Variable(
                new Variable.Variant(indexOf),
                true,
                "function");
        yString_Instance_Prototype.set(indexOf.getFnName(), indexOfVar);

        // str.contains(other)
        class ContainsFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant otherVar = arguments.get(0);

                if (!otherVar.isRuntimeObject()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'contains' argument must be a string."
                    );
                }

                RuntimeObject otherObj = otherVar.asRuntimeObject();

                if (!(otherObj instanceof yStringInstance)) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'contains' argument must be a string."
                    );
                }

                yStringInstance otherString = (yStringInstance) otherObj;

                boolean result = instance.data.contains(otherString.data);

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "contains";
            }
        }

        ContainsFn contains = new ContainsFn();
        Variable containsVar = new Variable(
                new Variable.Variant(contains),
                true,
                "function");
        yString_Instance_Prototype.set(contains.getFnName(), containsVar);

        // str.trim()
        class TrimFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                String trimmed = instance.data.trim();

                return new Variable.Variant(new yStringInstance(trimmed));
            }

            @Override
            public String getFnName() {
                return "trim";
            }
        }

        TrimFn trim = new TrimFn();
        Variable trimVar = new Variable(
                new Variable.Variant(trim),
                true,
                "function");
        yString_Instance_Prototype.set(trim.getFnName(), trimVar);

        // str.trimLeft()
        class TrimLeftFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                String trimmed = instance.data.replaceAll("^\\s+", "");

                return new Variable.Variant(new yStringInstance(trimmed));
            }

            @Override
            public String getFnName() {
                return "trimLeft";
            }
        }

        TrimLeftFn trimLeft = new TrimLeftFn();
        Variable trimLeftVar = new Variable(
                new Variable.Variant(trimLeft),
                true,
                "function");
        yString_Instance_Prototype.set(trimLeft.getFnName(), trimLeftVar);
        yString_Instance_Prototype.set("trimStart", trimLeftVar); // added trimStart alias

        // str.trimRight()
        class TrimRightFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                String trimmed = instance.data.replaceAll("\\s+$", "");

                return new Variable.Variant(new yStringInstance(trimmed));
            }

            @Override
            public String getFnName() {
                return "trimRight";
            }
        }

        TrimRightFn trimRight = new TrimRightFn();
        Variable trimRightVar = new Variable(
                new Variable.Variant(trimRight),
                true,
                "function");
        yString_Instance_Prototype.set(trimRight.getFnName(), trimRightVar);
        yString_Instance_Prototype.set("trimEnd", trimRightVar);

        // str.repeat(n)
        class RepeatFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant countVar = arguments.get(0);

                if (!countVar.canImplicitlyConvertNumber()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'repeat' argument must be a number."
                    );
                }

                int count = (int) countVar.implicitlyConvertNumber();

                if (count < 0) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'repeat' count must be >= 0."
                    );
                }

                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < count; i++) {
                    sb.append(instance.data);
                }

                return new Variable.Variant(
                        new yStringInstance(sb.toString())
                );
            }

            @Override
            public String getFnName() {
                return "repeat";
            }
        }

        RepeatFn repeat = new RepeatFn();
        Variable repeatVar = new Variable(
                new Variable.Variant(repeat),
                true,
                "function");
        yString_Instance_Prototype.set(repeat.getFnName(), repeatVar);

        // str.startsWith(other)
        class StartsWithFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant otherVar = arguments.get(0);

                if (!otherVar.isRuntimeObject() ||
                        !(otherVar.asRuntimeObject() instanceof yStringInstance)) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'startsWith' argument must be a string."
                    );
                }

                yStringInstance other =
                        (yStringInstance) otherVar.asRuntimeObject();

                boolean result = instance.data.startsWith(other.data);

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "startsWith";
            }
        }

        StartsWithFn startsWith = new StartsWithFn();
        Variable startsWithVar = new Variable(
                new Variable.Variant(startsWith),
                true,
                "function");
        yString_Instance_Prototype.set(startsWith.getFnName(), startsWithVar);

        // str.endsWith(other)
        class EndsWithFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant otherVar = arguments.get(0);

                if (!otherVar.isRuntimeObject() ||
                        !(otherVar.asRuntimeObject() instanceof yStringInstance)) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'endsWith' argument must be a string."
                    );
                }

                yStringInstance other =
                        (yStringInstance) otherVar.asRuntimeObject();

                boolean result = instance.data.endsWith(other.data);

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "endsWith";
            }
        }

        EndsWithFn endsWith = new EndsWithFn();
        Variable endsWithVar = new Variable(
                new Variable.Variant(endsWith),
                true,
                "function");
        yString_Instance_Prototype.set(endsWith.getFnName(), endsWithVar);

        // str.replace(old, new)
        class ReplaceFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant oldVar = arguments.get(0);
                Variable.Variant newVar = arguments.get(1);

                if (!oldVar.isRuntimeObject() ||
                        !(oldVar.asRuntimeObject() instanceof yStringInstance) ||
                        !newVar.isRuntimeObject() ||
                        !(newVar.asRuntimeObject() instanceof yStringInstance)) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'replace' arguments must be strings."
                    );
                }

                yStringInstance oldStr =
                        (yStringInstance) oldVar.asRuntimeObject();

                yStringInstance newStr =
                        (yStringInstance) newVar.asRuntimeObject();

                String replaced =
                        instance.data.replace(oldStr.data, newStr.data);

                return new Variable.Variant(
                        new yStringInstance(replaced)
                );
            }

            @Override
            public String getFnName() {
                return "replace";
            }
        }

        ReplaceFn replace = new ReplaceFn();
        Variable replaceVar = new Variable(
                new Variable.Variant(replace),
                true,
                "function");
        yString_Instance_Prototype.set(replace.getFnName(), replaceVar);

        // str.isEmpty()
        class IsEmptyFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                return new Variable.Variant(instance.data.isEmpty());
            }

            @Override
            public String getFnName() {
                return "isEmpty";
            }
        }

        IsEmptyFn isEmpty = new IsEmptyFn();
        Variable isEmptyVar = new Variable(
                new Variable.Variant(isEmpty),
                true,
                "function");
        yString_Instance_Prototype.set(isEmpty.getFnName(), isEmptyVar);

        // str.reverse()
        class ReverseFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                String reversed =
                        new StringBuilder(instance.data)
                                .reverse()
                                .toString();

                return new Variable.Variant(
                        new yStringInstance(reversed)
                );
            }

            @Override
            public String getFnName() {
                return "reverse";
            }
        }

        ReverseFn reverse = new ReverseFn();
        Variable reverseVar = new Variable(
                new Variable.Variant(reverse),
                true,
                "function");
        yString_Instance_Prototype.set(reverse.getFnName(), reverseVar);

        // str.padLeft(len, padStr)
        class PadLeftFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant lenVar = arguments.get(0);
                Variable.Variant padVar = arguments.get(1);

                if (!lenVar.canImplicitlyConvertNumber()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'padLeft' first argument must be a number."
                    );
                }

                if (!padVar.isRuntimeObject() ||
                        !(padVar.asRuntimeObject() instanceof yStringInstance)) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'padLeft' second argument must be a string."
                    );
                }

                int targetLength = (int) lenVar.implicitlyConvertNumber();
                yStringInstance padString =
                        (yStringInstance) padVar.asRuntimeObject();

                if (padString.data.isEmpty()) {
                    return new Variable.Variant(instance);
                }

                String result = instance.data;

                while (result.length() < targetLength) {
                    result = padString.data + result;
                }

                if (result.length() > targetLength) {
                    result = result.substring(result.length() - targetLength);
                }

                return new Variable.Variant(
                        new yStringInstance(result)
                );
            }

            @Override
            public String getFnName() {
                return "padLeft";
            }
        }

        PadLeftFn padLeft = new PadLeftFn();
        Variable padLeftVar = new Variable(
                new Variable.Variant(padLeft),
                true,
                "function");
        yString_Instance_Prototype.set(padLeft.getFnName(), padLeftVar);

        // str.padRight(len, padStr)
        class PadRightFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant lenVar = arguments.get(0);
                Variable.Variant padVar = arguments.get(1);

                if (!lenVar.canImplicitlyConvertNumber()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'padRight' first argument must be a number."
                    );
                }

                if (!padVar.isRuntimeObject() ||
                        !(padVar.asRuntimeObject() instanceof yStringInstance)) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'padRight' second argument must be a string."
                    );
                }

                int targetLength = (int) lenVar.implicitlyConvertNumber();
                yStringInstance padString =
                        (yStringInstance) padVar.asRuntimeObject();

                if (padString.data.isEmpty()) {
                    return new Variable.Variant(instance);
                }

                String result = instance.data;

                while (result.length() < targetLength) {
                    result = result + padString.data;
                }

                if (result.length() > targetLength) {
                    result = result.substring(0, targetLength);
                }

                return new Variable.Variant(
                        new yStringInstance(result)
                );
            }

            @Override
            public String getFnName() {
                return "padRight";
            }
        }

        PadRightFn padRight = new PadRightFn();
        Variable padRightVar = new Variable(
                new Variable.Variant(padRight),
                true,
                "function");
        yString_Instance_Prototype.set(padRight.getFnName(), padRightVar);

        // str.compareTo(other)
        class CompareToFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant otherVar = arguments.get(0);

                if (!otherVar.isRuntimeObject() ||
                        !(otherVar.asRuntimeObject() instanceof yStringInstance)) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'compareTo' argument must be a string."
                    );
                }

                yStringInstance other =
                        (yStringInstance) otherVar.asRuntimeObject();

                int result = instance.data.compareTo(other.data);

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "compareTo";
            }
        }

        CompareToFn compareTo = new CompareToFn();
        Variable compareToVar = new Variable(
                new Variable.Variant(compareTo),
                true,
                "function");
        yString_Instance_Prototype.set(compareTo.getFnName(), compareToVar);

        // str.capitalize()
        class CapitalizeFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                String first = instance.data.substring(0, 1).toUpperCase();
                String rest  = instance.data.substring(1);

                return new Variable.Variant(
                        new yStringInstance(first + rest)
                );
            }

            @Override
            public String getFnName() {
                return "capitalize";
            }
        }

        CapitalizeFn capitalize = new CapitalizeFn();
        Variable capitalizeVar = new Variable(
                new Variable.Variant(capitalize),
                true,
                "function");
        yString_Instance_Prototype.set(capitalize.getFnName(), capitalizeVar);

        // str.split(string)
        class SplitFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                    requireArity(arguments, arity(), getFnName());
                    yStringInstance instance = requireStringThis(interpreter, getFnName());

                    String regex = requireString(arguments.getFirst(), getFnName(), 1);

                    String[] arr = instance.data.split(regex);
                    ArrayList<Variable.Variant> list = new ArrayList<>();
                    for (var it : arr) list.add(new Variable.Variant(it));

                    yArray.yArrayInstance yArray = new yArray.yArrayInstance(list);
                    return new Variable.Variant(yArray);
            }

            @Override
            public String getFnName() {
                return "split";
            }
        }

        SplitFn split = new SplitFn();
        Variable splitVar = new Variable(
                new Variable.Variant(split),
                true,
                "function");
        yString_Instance_Prototype.set(split.getFnName(), splitVar);

        // str.replaceAll(regex, replacement)
        class ReplaceAllFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant regexVar = arguments.get(0);
                Variable.Variant replVar  = arguments.get(1);

                if (!regexVar.isRuntimeObject() ||
                        !(regexVar.asRuntimeObject() instanceof yStringInstance) ||
                        !replVar.isRuntimeObject() ||
                        !(replVar.asRuntimeObject() instanceof yStringInstance)) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'replaceAll' arguments must be strings."
                    );
                }

                yStringInstance regex =
                        (yStringInstance) regexVar.asRuntimeObject();

                yStringInstance repl =
                        (yStringInstance) replVar.asRuntimeObject();

                String result = instance.data.replaceAll(regex.data, repl.data);

                return new Variable.Variant(
                        new yStringInstance(result)
                );
            }

            @Override
            public String getFnName() {
                return "replaceAll";
            }
        }

        ReplaceAllFn replaceAll = new ReplaceAllFn();
        Variable replaceAllVar = new Variable(
                new Variable.Variant(replaceAll),
                true,
                "function");
        yString_Instance_Prototype.set(replaceAll.getFnName(), replaceAllVar);


        // str.join(array)
        class JoinFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant arrVar = arguments.get(0);

                if (!arrVar.isRuntimeObject() ||
                        !(arrVar.asRuntimeObject() instanceof yArray.yArrayInstance)) {

                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'join' argument must be an array."
                    );
                }

                yArray.yArrayInstance arr =
                        (yArray.yArrayInstance) arrVar.asRuntimeObject();

                List<String> parts = new ArrayList<>();

                for (Variable.Variant v : arr.data) {
                    parts.add(v.toString());
                }

                String result = String.join(instance.data, parts);

                return new Variable.Variant(
                        new yStringInstance(result)
                );
            }

            @Override
            public String getFnName() {
                return "join";
            }
        }

        JoinFn join = new JoinFn();
        Variable joinVar = new Variable(
                new Variable.Variant(join),
                true,
                "function");
        yString_Instance_Prototype.set(join.getFnName(), joinVar);


        // str.format(...)
        class FormatFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return -1; // variadic
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Object[] args = new Object[arguments.size()];

                for (int i = 0; i < arguments.size(); i++) {
                    Variable.Variant v = arguments.get(i);

                    if (v.isInt())
                        args[i] = v.asInt();
                    else if (v.isDouble())
                        args[i] = v.asDouble();
                    else if (v.isBoolean())
                        args[i] = v.asBoolean();
                    else if (v.isChar())
                        args[i] = v.asCharacter();
                    else if (v.isNull())
                        args[i] = null;
                    else if (v.isString())
                        args[i] = v.asString();
                    else
                        args[i] = v.toString();
                }

                String result;

                try {
                    result = String.format(instance.data, args);
                }
                catch (Exception e) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "Invalid format string."
                    );
                }

                return new Variable.Variant(
                        new yStringInstance(result)
                );
            }

            @Override
            public String getFnName() {
                return "format";
            }
        }

        FormatFn format = new FormatFn();
        Variable formatVar = new Variable(
                new Variable.Variant(format),
                true,
                "function");

        yString_Instance_Prototype.set(format.getFnName(), formatVar);


        // str.toCharArray()
        class ToCharArrayFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                ArrayList<Variable.Variant> list =
                        new ArrayList<>(instance.data.length());

                for (int i = 0; i < instance.data.length(); i++) {
                    list.add(new Variable.Variant(instance.data.charAt(i)));
                }

                yArray.yArrayInstance arr =
                        new yArray.yArrayInstance(list);

                return new Variable.Variant(arr);
            }

            @Override
            public String getFnName() {
                return "toCharArray";
            }
        }

        ToCharArrayFn toCharArray = new ToCharArrayFn();
        Variable toCharArrayVar = new Variable(
                new Variable.Variant(toCharArray),
                true,
                "function");

        yString_Instance_Prototype.set(
                toCharArray.getFnName(),
                toCharArrayVar
        );


        // str.matches(regex)
        class MatchesFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                String regex = requireString(
                        arguments.getFirst(),
                        getFnName(),
                        1
                );

                boolean result =
                        instance.data.matches(regex);

                return new Variable.Variant(result);
            }

            @Override
            public String getFnName() {
                return "matches";
            }
        }

        MatchesFn matches = new MatchesFn();
        Variable matchesVar = new Variable(
                new Variable.Variant(matches),
                true,
                "function");

        yString_Instance_Prototype.set(
                matches.getFnName(),
                matchesVar
        );


        // str.replaceFirst(regex, repl)
        class ReplaceFirstFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                String regex = requireString(
                        arguments.get(0),
                        getFnName(),
                        1
                );

                String repl = requireString(
                        arguments.get(1),
                        getFnName(),
                        2
                );

                String result =
                        instance.data.replaceFirst(regex, repl);

                return new Variable.Variant(
                        new yStringInstance(result)
                );
            }

            @Override
            public String getFnName() {
                return "replaceFirst";
            }
        }

        ReplaceFirstFn replaceFirst = new ReplaceFirstFn();
        Variable replaceFirstVar = new Variable(
                new Variable.Variant(replaceFirst),
                true,
                "function");

        yString_Instance_Prototype.set(
                replaceFirst.getFnName(),
                replaceFirstVar
        );

        // str.lastIndexOf(str)
        class LastIndexOfFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                String search = requireString(
                        arguments.getFirst(),
                        getFnName(),
                        1
                );

                int index =
                        instance.data.lastIndexOf(search);

                return new Variable.Variant(index);
            }

            @Override
            public String getFnName() {
                return "lastIndexOf";
            }
        }

        LastIndexOfFn lastIndexOf = new LastIndexOfFn();
        Variable lastIndexOfVar = new Variable(
                new Variable.Variant(lastIndexOf),
                true,
                "function");

        yString_Instance_Prototype.set(
                lastIndexOf.getFnName(),
                lastIndexOfVar
        );


        // str.count(substr)
        class CountFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                String needle = requireString(
                        arguments.getFirst(),
                        getFnName(),
                        1
                );

                if (needle.isEmpty()) {
                    return new Variable.Variant(0);
                }

                String text = instance.data;

                int count = 0;
                int index = 0;

                while (true) {
                    index = text.indexOf(needle, index);
                    if (index == -1) break;

                    count++;
                    index += needle.length();
                }

                return new Variable.Variant(count);
            }

            @Override
            public String getFnName() {
                return "count";
            }
        }

        CountFn count = new CountFn();
        Variable countVar = new Variable(
                new Variable.Variant(count),
                true,
                "function"
        );

        yString_Instance_Prototype.set(count.getFnName(), countVar);


        // str.charCodeAt(index)
        class CharCodeAtFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                requireArity(arguments, arity(), getFnName());
                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant indexVar = arguments.get(0);

                if (!indexVar.canImplicitlyConvertNumber()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'charCodeAt' index must be a number."
                    );
                }

                int index = (int) indexVar.implicitlyConvertNumber();

                if (index < 0 || index >= instance.data.length()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "String index out of bounds."
                    );
                }

                int code = instance.data.charAt(index);

                return new Variable.Variant(code);
            }

            @Override
            public String getFnName() {
                return "charCodeAt";
            }
        }

        CharCodeAtFn charCodeAt = new CharCodeAtFn();
        Variable charCodeAtVar = new Variable(
                new Variable.Variant(charCodeAt),
                true,
                "function");

        yString_Instance_Prototype.set(charCodeAt.getFnName(), charCodeAtVar);


        // str.slice(start, end?)
        class SliceFn extends Function.NativeFunction implements Callable {

            @Override
            public int arity() {
                return -1; // 1 or 2 arg
            }

            @Override
            public Variable.Variant call(Interpreter interpreter,
                                         List<Variable.Variant> arguments)
                    throws YsharpError {

                if (arguments.size() < 1 || arguments.size() > 2) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'slice' expects 1 or 2 arguments."
                    );
                }

                yStringInstance instance = requireStringThis(interpreter, getFnName());

                Variable.Variant startVar = arguments.get(0);

                if (!startVar.canImplicitlyConvertNumber()) {
                    throw new YsharpError(
                            YsharpError.YsharpErrorType.PROCESS,
                            0,
                            "'slice' start must be a number."
                    );
                }

                int start = (int) startVar.implicitlyConvertNumber();

                int end;

                if (arguments.size() == 2) {
                    Variable.Variant endVar = arguments.get(1);

                    if (!endVar.canImplicitlyConvertNumber()) {
                        throw new YsharpError(
                                YsharpError.YsharpErrorType.PROCESS,
                                0,
                                "'slice' end must be a number."
                        );
                    }

                    end = (int) endVar.implicitlyConvertNumber();
                } else {
                    end = instance.data.length();
                }

                int len = instance.data.length();

                if (start < 0) start = len + start;
                if (end < 0) end = len + end;

                start = Math.max(0, Math.min(start, len));
                end   = Math.max(0, Math.min(end, len));

                if (end < start) end = start;

                String result = instance.data.substring(start, end);

                return new Variable.Variant(
                        new yStringInstance(result)
                );
            }

            @Override
            public String getFnName() {
                return "slice";
            }
        }

        SliceFn slice = new SliceFn();
        Variable sliceVar = new Variable(
                new Variable.Variant(slice),
                true,
                "function");

        yString_Instance_Prototype.set(slice.getFnName(), sliceVar);
    }

    public static class yStringInstance extends yClass.ClassObjectInstance {

        public final String data;

        public yStringInstance(String data) {
            this.data = data;
            this.prototype = yString_Instance_Prototype;
        }


        @Override
        public boolean isTruthy() {
            return !data.isEmpty();
        }

        @Override
        public String getType() {
            return "string";
        }

        @Override
        public String toString() {
            return data;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof yStringInstance)) return false;
            yStringInstance other = (yStringInstance) obj;
            return other.data.equals(this.data);
        }

        @Override
        public int hashCode() {
            return data.hashCode();
        }

    }

    public static class yStringClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 1;
        }

        public yStringClass() {
            this.prototype = yClass.ClassPrototype;
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpError {
            String value;
            if(arguments.getFirst().value instanceof yStringInstance) {
                value = ((yStringInstance) arguments.getFirst().value).data;
            }
            else if(arguments.getFirst().value instanceof String) {
                value = arguments.getFirst().asString();
            }
            else {
                throw new YsharpError(
                        YsharpError.YsharpErrorType.PROCESS,
                        0,
                        "String constructor expects a string argument."
                );
            }

            yStringInstance newString = new yStringInstance(value);

            return new Variable.Variant(newString);
        }

        @Override
        public String getClassName() {
            return "String";
        }

        @Override
        public String getType() {
            return "string";
        }

        @Override
        public String toString() {
            return "<class:string>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yStringClass stringCtor = new yStringClass();
        Variable.Variant variant = new Variable.Variant(stringCtor);
        Variable var = new Variable(variant, false, stringCtor.getType());
        interpreter.defineGlobal(stringCtor.getClassName(), var);
    }

}

