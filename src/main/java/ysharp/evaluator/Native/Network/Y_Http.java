package ysharp.evaluator.Native.Network;

import ysharp.YsharpError;
import ysharp.evaluator.Function;
import ysharp.evaluator.Interpreter;
import ysharp.evaluator.RuntimeObject;
import ysharp.evaluator.Variable;
import ysharp.parser.TypeTag;

import java.net.http.HttpClient;
import java.util.List;

public class Y_Http {

    public static RuntimeObject Y_Http_Prototype;

    static {
        Y_Http_Prototype = new RuntimeObject() {
            @Override
            public boolean isTruthy() { return true; }

            @Override
            public String getType() { return "http_prototype"; }
        };
    }


    public static class Y_HttpObject extends RuntimeObject {

        private final HttpClient httpClient;

        public Y_HttpObject() {
            this.httpClient = HttpClient.newHttpClient();
            this.prototype = Y_Http_Prototype;
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


    public static class Y_HttpInit extends Function.NativeFunction {

        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Variable.Variant call(
                Interpreter interpreter,
                List<Variable.Variant> arguments
        ) throws YsharpError {

            Y_HttpObject obj = new Y_HttpObject();
            return new Variable.Variant(obj);
        }

        @Override
        public String getFnName() {
            return "Http";
        }
    }

    public static void register(Interpreter interpreter) throws Exception {

        Y_HttpInit ctor = new Y_HttpInit();
        Variable.Variant variant = new Variable.Variant(ctor);
        Variable var = new Variable(variant, false, TypeTag.OBJECT);

        interpreter.defineGlobal(ctor.getFnName(), var);
    }
}