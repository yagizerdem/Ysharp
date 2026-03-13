package ysharp.evaluator;

import ysharp.YsharpError;

import java.util.List;

public class StdIO {

    public static void printStdErr(List<YsharpError> errors) {
        for(YsharpError err : errors) {
            System.err.println(err.toString());
        }
    }

    public static void printStdErr(String error) {
        System.err.println(error);
    }

}
