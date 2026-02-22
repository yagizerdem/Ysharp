package ysharp.evaluator;

public abstract class RuntimeObject {

    abstract boolean isTruthy();

    abstract String getType();


    public static class StringObject extends RuntimeObject {
        final String data;

        public StringObject(String data){
            this.data = data;
        }

        @Override
        boolean isTruthy() {
            return  !this.data.isEmpty();
        }

        @Override
        String getType() {
            return "string";
        }

        @Override
        public String toString() {
            return this.data;
        }
    }

    public static class FunctionObject extends RuntimeObject {

        @Override
        boolean isTruthy() {
            return true;
        }

        @Override
        String getType() {
            return "function";
        }

        @Override
        public String toString() {
            return "function";
        }
    }

    public static class ClassObject extends RuntimeObject {

        @Override
        boolean isTruthy() {
            return true;
        }

        @Override
        String getType() {
            return "class";
        }

        @Override
        public String toString() {
            return "class";
        }
    }

}
