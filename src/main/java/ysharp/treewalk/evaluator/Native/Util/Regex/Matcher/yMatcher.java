package ysharp.treewalk.evaluator.Native.Util.Regex.Matcher;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.function.instance.*;
import ysharp.treewalk.evaluator.Native.Util.Regex.Matcher.function.statix.QuoteReplacementFn;
import ysharp.treewalk.evaluator.Native.Util.Regex.Pattern.yPattern;

import java.math.MathContext;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class yMatcher {

    public static yMatcherInstance requireMatcherThis(Interpreter interpreter, String fnName) {
        Variable thisVar = interpreter.curEnv.getValue("this");

        if (thisVar == null) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' called without a valid 'this' context."
            );
        }

        RuntimeObject obj = thisVar.value.asRuntimeObject();

        if (!(obj instanceof yMatcherInstance)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    0,
                    "Method '" + fnName + "' expected 'Matcher' as 'this' but got '" + obj.getType() + "'."
            );
        }

        return (yMatcherInstance) obj;
    }

    public static RuntimeObject yMatcher_Instance_Prototype;

    static {
        yMatcher_Instance_Prototype = new RuntimeObject() {
            @Override public boolean isTruthy() { return true; }
            @Override public String getType() { return "__Matcher__"; }
            @Override public String toString() { return "<prototype:Matcher>"; }
        };

        yMatcher_Instance_Prototype.prototype = yClass.ClassPrototype;

        yMatcher_Instance_Prototype.RegisterNativeFn(new FindFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new EndFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new GroupFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new GroupCountFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new HasAnchoringBoundsFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new HasTransparentBoundsFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new HitEndFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new LookingAtFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new MatchesFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new PatternFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new RegionFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new RegionStartFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new RegionEndFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new ReplaceAllFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new ReplaceFirstFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new RequireEndFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new ResetFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new StartFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new ToStringFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new UseAnchoringBoundsFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new UseTransparentBoundsFn());
        yMatcher_Instance_Prototype.RegisterNativeFn(new UsePatternFn());
    }

    public static class yMatcherInstance extends yClass.ClassObjectInstance {

        public Matcher matcher;

        public yMatcherInstance(java.util.regex.Matcher mathcer) {
            this.prototype = yMatcher_Instance_Prototype;
            this.matcher = mathcer;
        }

        @Override public boolean isTruthy() { return true; }
        @Override public String getType() { return "Matcher"; }
        @Override public String toString() { return "<instance:Matcher>"; }
    }


    public static class yMatcherClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return -1;
        }

        public yMatcherClass(){
            this.prototype = yClass.ClassPrototype;

            this.RegisterNativeFn(new QuoteReplacementFn());
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments) throws YsharpException {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Cannot take instance of static Matcher class"
            );
        }

        @Override
        public String getClassName() {
            return "Matcher";
        }

        @Override
        public String getType() {
            return "_Matcher_";
        }

        @Override
        public String toString() {
            return "<class:Matcher>";
        }
    }
}