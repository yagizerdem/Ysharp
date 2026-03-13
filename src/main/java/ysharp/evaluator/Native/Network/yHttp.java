package ysharp.evaluator.Native.Network;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.RuntimeObject;
import ysharp.evaluator.Variable;

import java.net.http.HttpClient;
import java.util.List;

public class yHttp {

    public static RuntimeObject yHttp_Prototype;

    static {
        yHttp_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() { return true; }

            @Override
            public String getType() { return "http_prototype"; }
        };
    }


    public static class yHttpObject extends RuntimeObject {

        private final HttpClient httpClient;

        public yHttpObject() {
            this.httpClient = HttpClient.newHttpClient();
            this.prototype = yHttp_Prototype;
        }

        public HttpClient getClient() {
            return httpClient;
        }

        @Override
        public boolean isTruthy() {
            return true;
        }

        @Override
        public String getType() {
            return "Http";
        }

        @Override
        public String toString() {
            return "<class:Http>";
        }
    }


    public static class yHttpInit extends Function.NativeFunction {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(
                Interpreter interpreter,
                List<Variable.Variant> arguments
        ) throws YsharpError {

            yHttpObject obj = new yHttpObject();
            return new Variable.Variant(obj);
        }

        @Override
        public String getFnName() {
            return "Http";
        }
    }

    public static void register(Interpreter interpreter) throws Exception {

        yHttpInit ctor = new yHttpInit();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, false, "function");

        interpreter.defineGlobal(ctor.getFnName(), var);
    }
}