
package com.interpeter;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private Map<String, String> variables = new HashMap<String, String>();
    private Map<String, Function> functions = new HashMap<String, Function>();

    public Context() {
    }

    public void setVariable(String name, String value) {
        variables.put(name, value);
    }

    public String getVariable(String name) {
        return variables.get(name);
    }

    public void setFunction(String name, Function value) {
        functions.put(name, value);
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    public Map<String, Function> getFunctions() {
        return functions;
    }
    public Function getFunction(String name) {
        return functions.get(name);
    }
    public void setFunctions(Map<String, Function> functions) {
        this.functions = functions;
    }

}