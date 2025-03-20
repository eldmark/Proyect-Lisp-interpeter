package com.interpeter;

import java.util.ArrayList;

public class Function {
    private String name;
    private ArrayList<String> params;
    private ArrayList<ArrayList<String>> body;

    public Function(String name, ArrayList<String> params, ArrayList<ArrayList<String>> body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public ArrayList<ArrayList<String>> getBody() {
        return body;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParams(ArrayList<String> params) {
        this.params = params;
    }

    public void setBody(ArrayList<ArrayList<String>> body) {
        this.body = body;
    }

    /**
     * Replaces occurrences of parameter names in the function body with their corresponding values.
     * 
     * This method iterates through each line of the function body and checks if any element
     * in the line matches a parameter name. If a match is found, the parameter name is replaced
     * with its corresponding value from the parameter list.
     * 
     * The function assumes that `params` is a list of parameter names and their corresponding
     * values, and `body` is a list of lines where each line is an array of strings representing
     * tokens in the function body.
     */
    public void replaceParamsInBody() {
        for (int i = 0; i < body.size(); i++) {
            ArrayList<String> line = body.get(i);  // Ahora `line` es un ArrayList<String>
            for (int j = 0; j < line.size(); j++) {
                String token = line.get(j);
                int paramIndex = params.indexOf(token);
                if (paramIndex != -1) {
                    line.set(j, params.get(paramIndex));  // Corrige el valor en la lista
                }
            }
        }
    }
}