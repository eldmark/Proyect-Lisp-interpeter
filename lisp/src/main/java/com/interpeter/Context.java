package com.interpeter;

import java.util.HashMap;
import java.util.Map;
import com.interpeter.Function;

/**
 * Clase que gestiona las variables y funciones en el intérprete.
 * Esta clase permite establecer y obtener variables y funciones,
 * así como gestionar colecciones de estas entidades.
 */
public class Context {
    
    /**
     * Crea una nueva instancia de Context.
     */
    private Map<String, String> variables = new HashMap<String, String>();
    private Map<String, Function> functions = new HashMap<String, Function>();

    public Context() {
    }

    /**
     * Establece una variable en el contexto.
     *
     * @param name El nombre de la variable.
     * @param value El valor de la variable.
     */
    public void setVariable(String name, String value) {
        if (variables.containsKey(name)) {
            variables.remove(name);
        }
        variables.put(name, value);
    }

    /**
     * Obtiene el valor de una variable del contexto.
     *
     * @param name El nombre de la variable.
     * @return El valor de la variable, o null si no existe.
     */
    public String getVariable(String name) {
        return variables.get(name);
    }

    /**
     * Establece una función en el contexto.
     *
     * @param name El nombre de la función.
     * @param value La función a establecer.
     */
    public void setFunction(String name, Function value) {
        functions.put(name, value);
    }

    /**
     * Obtiene todas las variables del contexto.
     *
     * @return Un mapa de todas las variables.
     */
    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    /**
     * Obtiene todas las funciones del contexto.
     *
     * @return Un mapa de todas las funciones.
     */
    public Map<String, Function> getFunctions() {
        return functions;
    }

    /**
     * Obtiene una función del contexto por su nombre.
     *
     * @param name El nombre de la función.
     * @return La función correspondiente, o null si no existe.
     */
    public Function getFunction(String name) {
        return functions.get(name);
    }

    /**
     * Establece un conjunto de funciones en el contexto.
     *
     * @param functions Un mapa de funciones a establecer.
     */
    public void setFunctions(Map<String, Function> functions) {
        this.functions = functions;
    }
}

