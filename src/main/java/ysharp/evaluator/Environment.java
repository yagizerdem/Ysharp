package ysharp.evaluator;
import ysharp.YsharpError;
import ysharp.lexer.Token;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;


public class Environment {

    private final Map<String, Variable> values = new HashMap<>();
    private final Map<Variable.Variant, String > variantTypes = new IdentityHashMap<>();
    private final Environment enclosing;

    public Environment() {
        this.enclosing = null;
    }

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public Variable getValue(Token name) throws YsharpError {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        if (enclosing != null) return enclosing.getValue(name);

        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, -1,
                "Undefined variable name" + name.lexeme + ".");
    }

    public Variable getValue(String name) throws YsharpError {
        if (values.containsKey(name)) {
            return values.get(name);
        }

        if (enclosing != null) return enclosing.getValue(name);

        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, -1,
                "Undefined variable name" + name + ".");
    }

    public String getType(Token identifier) throws YsharpError {

        if (values.containsKey(identifier.lexeme)) {
            Variable var = values.get(identifier.lexeme);
            return var.getType();
        }

        if (enclosing != null) {
            return enclosing.getType(identifier);
        }

        throw new YsharpError(
                YsharpError.YsharpErrorType.PROCESS,
                identifier.line,
                "Undefined variable '" + identifier.lexeme + "'."
        );
    }

    public String getType(Variable.Variant variant) throws YsharpError {

        if (variantTypes.containsKey(variant)) {
            return variantTypes.get(variant);
        }

        if (enclosing != null) {
            return enclosing.getType(variant);
        }

        throw new YsharpError(
                YsharpError.YsharpErrorType.PROCESS,
                -1,
                "Undefined variable ."
        );
    }

    private Environment ancestor(int distance) throws YsharpError {
        Environment env = this;
        for (int i = 0; i < distance; i++) {
            if (env.enclosing == null)
                throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, -1,
                        "Invalid scope distance.");
            env = env.enclosing;
        }
        return env;
    }

    public void assign(Token name, Variable.Variant value) throws YsharpError {
        if (values.containsKey(name.lexeme)) {

            Variable variable = values.get(name.lexeme);

            variantTypes.remove(variable.value);

            variable.value = value;

            variantTypes.put(value, variable.getType());

            return;
        }

        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new YsharpError(
                YsharpError.YsharpErrorType.PROCESS,
                -1,
                "Undefined variable name " + name.lexeme + "."
        );
    }

    public void define(String name, Variable var) throws YsharpError {
        if (values.containsKey(name)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    "Variable '" +
                            name +
                            "' is already defined in this scope."
            );
        }

        values.put(name, var);
        variantTypes.put(var.value, var.getType());
    }

    public Variable getAt(int distance, String name) throws YsharpError {
        Environment env = ancestor(distance);

        if (!env.values.containsKey(name)) {
            throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, -1,
                    "Undefined variable reference.");
        }

        return env.values.get(name);
    }

    public void assignAt(int distance, Token name, Variable.Variant value) throws YsharpError {
        Environment env = ancestor(distance);

        if (!env.values.containsKey(name.lexeme)) {
            throw new YsharpError(
                    YsharpError.YsharpErrorType.PROCESS,
                    -1,
                    "Undefined variable reference"
            );
        }

        Variable variable = env.values.get(name.lexeme);
        env.variantTypes.remove(variable.value);
        variable.value = value;
        env.variantTypes.put(value, variable.getType());
    }

    public boolean existsAt(int distance, String name) throws YsharpError {
        Environment env = ancestor(distance);
        return env.values.containsKey(name);
    }

}