package ysharp.treewalk.evaluator.Native.HTTP;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;
import ysharp.treewalk.evaluator.Native.HTTP.function.statix.*;

import java.util.List;

public class yHTTP {

    public static class yHTTPClass extends yClass.SealedClassObject {

        @Override
        public int arity() {
            return 0;
        }

        public yHTTPClass() {
            this.prototype = yClass.ClassPrototype;


             // HTTP.get(url)
             this.RegisterNativeFn(new GetFn());

             // HTTP.post(url, body)
             this.RegisterNativeFn(new PostFn());

             //HTTP.put(url, body)
             this.RegisterNativeFn(new PutFn());

             //HTTP.delete(url)
             this.RegisterNativeFn(new DeleteFn());
        }

        @Override
        public Variable.Variant call(Interpreter interpreter, List<Variable.Variant> arguments)
                throws YsharpException {

            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "HTTP is a static class and cannot be instantiated."
            );
        }

        @Override
        public String getClassName() {
            return "HTTP";
        }

        @Override
        public String getType() {
            return "_HTTP_";
        }

        @Override
        public String toString() {
            return "<static class:HTTP>";
        }
    }

    public static void Register(Interpreter interpreter) throws Exception {
        yHTTPClass httpClass = new yHTTPClass();
        Variable.Variant variant = new Variable.Variant(httpClass);
        Variable var = new Variable(variant, true, httpClass.getType());

        interpreter.defineGlobal(httpClass.getClassName(), var);
    }
}