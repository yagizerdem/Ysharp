package ysharp.treewalk.evaluator;
import ysharp.treewalk.YsharpException;
import ysharp.treewalk.lexer.Token;

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

    public Variable getValue(Token name) throws YsharpException {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        if (enclosing != null) return enclosing.getValue(name);

        throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1,
                "Undefined variable name" + name.lexeme + ".");
    }

    public Variable getValue(String name) throws YsharpException {
        if (values.containsKey(name)) {
            return values.get(name);
        }

        if (enclosing != null) return enclosing.getValue(name);

        throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1,
                "Undefined variable name" + name + ".");
    }

    public Variable getValueOrDefault(String name) {
        if (values.containsKey(name)) {
            return values.get(name);
        }

        if (enclosing != null) {
            return enclosing.getValueOrDefault(name);
        }

        return null;
    }

    public Variable getValueOrDefault(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        if (enclosing != null) {
            return enclosing.getValueOrDefault(name);
        }

        return null;
    }

    public String getType(Token identifier) throws YsharpException {

        if (values.containsKey(identifier.lexeme)) {
            Variable var = values.get(identifier.lexeme);
            return var.getType();
        }

        if (enclosing != null) {
            return enclosing.getType(identifier);
        }

        throw new YsharpException(
                YsharpException.YsharpErrorType.PROCESS,
                identifier.line,
                "Undefined variable '" + identifier.lexeme + "'."
        );
    }

    public String getType(Variable.Variant variant) throws YsharpException {

        if (variantTypes.containsKey(variant)) {
            return variantTypes.get(variant);
        }

        if (enclosing != null) {
            return enclosing.getType(variant);
        }

        throw new YsharpException(
                YsharpException.YsharpErrorType.PROCESS,
                -1,
                "Undefined variable ."
        );
    }

    private Environment ancestor(int distance) throws YsharpException {
        Environment env = this;
        for (int i = 0; i < distance; i++) {
            if (env.enclosing == null)
                throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1,
                        "Invalid scope distance.");
            env = env.enclosing;
        }
        return env;
    }

    public void assign(Token name, Variable.Variant value) throws YsharpException {
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

        throw new YsharpException(
                YsharpException.YsharpErrorType.PROCESS,
                -1,
                "Undefined variable name " + name.lexeme + "."
        );
    }

    public void define(String name, Variable var) throws YsharpException {
        if (values.containsKey(name)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Variable '" +
                            name +
                            "' is already defined in this scope."
            );
        }

        values.put(name, var);
        variantTypes.put(var.value, var.getType());
    }

    public Variable getAt(int distance, String name) throws YsharpException {
        Environment env = ancestor(distance);

        if (!env.values.containsKey(name)) {
            throw new YsharpException(YsharpException.YsharpErrorType.PROCESS, -1,
                    "Undefined variable reference.");
        }

        return env.values.get(name);
    }

    public Variable getAtOrDefault(int distance, String name) throws YsharpException {
        Environment env = ancestor(distance);
        if (!env.values.containsKey(name)) return null;
        return env.values.get(name);
    }

    public void assignAt(int distance, Token name, Variable.Variant value) throws YsharpException {
        Environment env = ancestor(distance);

        if (!env.values.containsKey(name.lexeme)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "Undefined variable reference"
            );
        }

        Variable variable = env.values.get(name.lexeme);
        env.variantTypes.remove(variable.value);
        variable.value = value;
        env.variantTypes.put(value, variable.getType());
    }

    public boolean existsAt(int distance, String name) throws YsharpException {
        Environment env = ancestor(distance);
        return env.values.containsKey(name);
    }

    public boolean exists(String name) throws YsharpException {
        return existsAt(0, name);
    }

    public void remove(String name) {
        Variable.Variant variant = null;
        if(this.values.containsKey(name)) {
            variant = this.values.get(name).value;
            this.values.remove(name);
        }
        this.variantTypes.remove(variant);
    }

    public void removeAt(int distance, String name) {
        Environment env = this;
        for(int i = 0; i < distance; i++) {
            if(env == null) throw new RuntimeException("[Programmatic error] : distance is out of env chain");
            env = env.enclosing;
        }
        this.values.remove(name);

    }


}