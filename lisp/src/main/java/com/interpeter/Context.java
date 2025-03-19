
package com.interpeter;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private Map<String, String> variables = new HashMap<String,String>();
    private Map<String,Function > functions = new HashMap<String,Function>();

    public Context() {
    }

    public void setVariable(String name,  String value) {
        variables.put(name, value);
    }

    public String getVariable(String name) {
        return variables.get(name);
    }
    public void setFunction(String name,  Function value) {
        functions.put(name, value);
    }
    
}