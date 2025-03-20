package com.interpeter;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una función en el intérprete LISP.
 * Esta clase incluye el nombre de la función, sus parámetros y su cuerpo.
 */
public class Function {
    
    /**
     * Crea una nueva instancia de Function.
     *
     * @param name El nombre de la función.
     * @param params La lista de parámetros de la función.
     * @param body El cuerpo de la función, representado como una lista de líneas.
     */

        private String name;
        private List<String> params;
        private ArrayList<List<?>> body;
    
        public Function(String name, List<String> params, ArrayList<List<?>> body) {
            this.name = name;
            this.params = params;
            this.body = body;
        }
    
        // Other methods and constructors


    /**
     * Obtiene el nombre de la función.
     *
     * @return El nombre de la función.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la lista de parámetros de la función.
     *
     * @return La lista de parámetros.
     */
    public List<String> getParams() {
        return params;
    }

    /**
     * Obtiene el cuerpo de la función.
     *
     * @return El cuerpo de la función, representado como una lista de líneas.
     */
    public ArrayList<List<?>> getBody() {
        return body;
    }

    /**
     * Establece el nombre de la función.
     *
     * @param name El nuevo nombre de la función.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Establece la lista de parámetros de la función.
     *
     * @param params La nueva lista de parámetros.
     */
    public void setParams(List<String> params) {
        this.params = params;
    }

    /**
     * Establece el cuerpo de la función.
     *
     * @param body El nuevo cuerpo de la función, representado como una lista de líneas.
     */
    public void setBody(ArrayList<List<?>> body) {
        this.body = body;
    }

    /**
     * Reemplaza las ocurrencias de los nombres de los parámetros en el cuerpo de la función con sus valores correspondientes.
     * Este método itera a través de cada línea del cuerpo de la función y verifica si algún elemento
     * en la línea coincide con un nombre de parámetro. Si se encuentra una coincidencia, el nombre del parámetro
     * se reemplaza con su valor correspondiente de la lista de parámetros.
      * tokens en el cuerpo de la funcion.
     */
    public void replaceParamsInBody() {
        for (int i = 0; i < body.size(); i++) {
            List<?> line = body.get(i);
        for (int j = 0; j < line.size(); j++) {
            int paramIndex = params.indexOf(line.get(j));
            if (paramIndex != -1) {
                ((ArrayList<Object>) line).set(j, params.get(paramIndex));
            }
        }
    
    body.set(i, line);
            }
    }
}
