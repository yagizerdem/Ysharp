package ysharp.treewalk.evaluator;

import ysharp.treewalk.YsharpException;

import java.util.List;

public class StdIO {

    public static void printStdErr(List<YsharpException> errors) {
        for(YsharpException err : errors) {
            System.err.println(err.toString());
        }
    }

    public static void printStdErr(String error) {
        System.err.println(error);
    }

}
