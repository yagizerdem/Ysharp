package ysharp.evaluator;
import ysharp.YsharpError;
import ysharp.lexer.Token;

import java.util.HashMap;
import java.util.Map;


public class Environment {

    private final Map<String, Variable> values = new HashMap<>();
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
            Variable variable = this.values.get(name.lexeme);
            variable.value = value;
            return;
        }

        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, -1,
                "Undefined variable name" + name.lexeme + ".");
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
            throw new YsharpError(YsharpError.YsharpErrorType.PROCESS, -1,
                    "Undefined variable reference");
        }

        Variable variable = env.values.get(name.lexeme);
        variable.value = value;
    }

    public boolean existsAt(int distance, String name) throws YsharpError {
        Environment env = ancestor(distance);
        return env.values.containsKey(name);
    }
}