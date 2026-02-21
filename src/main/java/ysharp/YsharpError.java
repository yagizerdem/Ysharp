package ysharp;

public class YsharpError extends Exception {


    public enum YsharpErrorType {
        SYNTAX,
        SEMANTIC,
        PROCESS
    }


    private final YsharpErrorType type;
    private final int line;
    private final String message;

    public YsharpError(YsharpErrorType type, int line, String message) {
        super(message);
        this.type    = type;
        this.line    = line;
        this.message = message;
    }


    public YsharpErrorType getType()    { return type; }
    public int             getLine()    { return line; }
    public String          getMessage() { return message; }


    @Override
    public String toString() {
        if (type == YsharpErrorType.PROCESS) {
            return message;
        }
        return errorTypeToString(type)
                + " error at Line : "
                + line
                + ": -> "
                + message;
    }


    private static String errorTypeToString(YsharpErrorType type) {
        return switch (type) {
            case SYNTAX   -> "Syntax";
            case SEMANTIC -> "Semantic";
            case PROCESS  -> "Process";
        };
    }
}