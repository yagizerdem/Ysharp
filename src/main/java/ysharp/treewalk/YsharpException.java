package ysharp.treewalk;

public class YsharpException extends RuntimeException {


    public enum YsharpErrorType {
        SYNTAX,
        SEMANTIC,
        PROCESS,
    }


    private final YsharpErrorType type;
    private final int line;
    private final String message;
    private final boolean printMessage;

    public YsharpException(YsharpErrorType type, int line, String message) {
        super(message);
        this.type    = type;
        this.line    = line;
        this.message = message;
        this.printMessage = true;
    }


    public YsharpException(YsharpErrorType type, int line, String message, boolean printMessage) {
        super(message);
        this.type    = type;
        this.line    = line;
        this.message = message;
        this.printMessage = printMessage;
    }


    public YsharpErrorType getType()    { return type; }
    public int             getLine()    { return line; }
    public String          getMessage() { return message; }
    public boolean          getPrintMessage() { return printMessage; }


    @Override
    public String toString() {
        if(line < 1) {
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