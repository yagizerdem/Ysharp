package ysharp.evaluator;

public abstract class RuntimeObject {

    abstract boolean isTruthy();

    public static class StringObject extends RuntimeObject {
        final String data;

        public StringObject(String data){
            this.data = data;
        }

        @Override
        boolean isTruthy() {
            return  !this.data.isEmpty();
        }
    }

    public static class FunctionObject extends RuntimeObject {

        @Override
        boolean isTruthy() {
            return true;
        }
    }

    public static class ClassObject extends RuntimeObject {

        @Override
        boolean isTruthy() {
            return true;
        }
    }

}
