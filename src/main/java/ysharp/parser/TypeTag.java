package ysharp.parser;

public enum TypeTag {
    // primitive
    INT,
    DOUBLE,
    NUMBER,
    BOOL,
    CHAR,
    NULL,
    ANY, // matches all types

    OBJECT; // runtime object such as user defined classes

    public static TypeTag fromString(String tag) {
        return switch (tag) {
            case "int" -> INT;
            case "double" -> DOUBLE;
            case "number" -> NUMBER;
            case "bool" -> BOOL;
            case "char" -> CHAR;
            case "null" -> NULL;
            case "any" -> ANY;
            default -> OBJECT;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case INT -> "int";
            case DOUBLE -> "double";
            case NUMBER -> "number";
            case BOOL -> "bool";
            case CHAR -> "char";
            case NULL -> "null";
            case ANY -> "any";
            case OBJECT -> "object";
        };
    }
}
