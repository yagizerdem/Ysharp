package ysharp.parser;

public enum TypeTag {
    // primitive
    INT,
    DOUBLE,
    BOOL,
    CHAR,
    NULL,
    ANY, // matches all types

    OBJECT; // runtime object such as user defined classes

    public static TypeTag fromString(String tag) {
        return switch (tag) {
            case "int" -> INT;
            case "double" -> DOUBLE;
            case "bool" -> BOOL;
            case "char" -> CHAR;
            case "null" -> NULL;
            case "any" -> ANY;
            default -> OBJECT;
        };
    }
}
