package com.interpeter;

import java.util.ArrayList;
import java.util.List;

public class Evaluator {

    private Context context;

    public Evaluator(Context context) {
        this.context = context;
    }

    /**
     * Evalúa una expresión Lisp que puede ser un número, variable o lista anidada.
     */
    public Object evaluate(Object expr) {
        if (expr instanceof Integer || expr instanceof Boolean) {
            return expr;
        } else if (expr instanceof String) {
            String varName = (String) expr;
            String value = context.getVariable(varName);
            // Si existe una variable con ese nombre, se intenta parsearla a entero;
            // si falla se retorna la cadena
            if (value != null) {
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return value;
                }
            }
            return varName;
        } else if (expr instanceof List) {
            List<?> list = (List<?>) expr;
            if (list.isEmpty()) {
                throw new IllegalArgumentException("La lista de expresión está vacía");
            }
            
            // El primer elemento determina la operación
            Object opObj = list.get(0);
            String operation = opObj.toString();

            // Validaciones previas: cada operador requiere un número mínimo de argumentos
            switch (operation) {
                case "set":
                    if (list.size() != 3) {
                        throw new IllegalArgumentException("set requiere 2 argumentos (nombre y valor)");
                    }
                    return evaluateSet(list);
                case "defun":
                    if (list.size() < 4) {
                        throw new IllegalArgumentException("defun requiere al menos 3 argumentos (nombre, parámetros y cuerpo)");
                    }
                    return evaluateDefun(list);
                case "dotimes":
                    if (list.size() < 3) {
                        throw new IllegalArgumentException("dotimes requiere al menos 2 argumentos (variable, límite y cuerpo)");
                    }
                    return evaluateDotimes(list.subList(1, list.size()));
                case "if":
                    if (list.size() != 4) {
                        throw new IllegalArgumentException("if requiere exactamente 3 argumentos (condición, verdadero y falso)");
                    }
                    return evaluateIf(list.subList(1, list.size()));
                case "quote":
                    return evaluateQuote(list.subList(1, list.size()));
                case "atom":
                    return evaluateAtom(list.subList(1, list.size()));
                case "list":
                    return evaluateList(list.subList(1, list.size()));
                case "cond":
                    if (list.size() < 2) {
                        throw new IllegalArgumentException("cond requiere al menos una cláusula");
                    }
                    return evaluateCond(list.subList(1, list.size()));
                // Operaciones aritméticas básicas
                case "+":
                case "-":
                case "*":
                case "/":
                    if (list.size() < 2) {
                        throw new IllegalArgumentException("Operación aritmética requiere al menos un operando");
                    }
                    return evaluateOperation(operation, list.subList(1, list.size()));
                // Comparaciones numéricas
                case "=":
                case "/=":
                case "<":
                case "<=":
                case ">":
                case ">=":
                    if (list.size() < 3) {
                        throw new IllegalArgumentException("Operación de comparación requiere al menos dos operandos");
                    }
                    return evaluateLogicalExpression(operation, list.subList(1, list.size()));
                default:
                    // Buscamos funciones definidas por el usuario
                    Function func = context.getFunction(operation);
                    if (func != null) {
                        if (list.size() - 1 < func.getParams().size()) {
                            throw new IllegalArgumentException("Número insuficiente de argumentos para la función " + operation);
                        }
                        return executeFunction(func, list.subList(1, list.size()));
                    }
                    throw new IllegalArgumentException("Operación desconocida: " + operation);
            }
        }
        throw new IllegalArgumentException("Tipo de expresión no soportada: " + expr.getClass().getSimpleName());
    }

    private Object evaluateSet(List<?> list) {
        String varName = list.get(1).toString();
        Object value = evaluate(list.get(2));
        context.setVariable(varName, value.toString());
        return value;
    }

    private Object evaluateDefun(List<?> list) {
        String funcName = list.get(1).toString();
        @SuppressWarnings("unchecked")
        ArrayList<String> params = new ArrayList<>((List<String>) list.get(2));
        // Se asume que el cuerpo de la función es una lista de expresiones (listas anidadas)
        ArrayList<ArrayList<?>> body = new ArrayList<>();
        for (int i = 3; i < list.size(); i++) {
            Object line = list.get(i);
            if (line instanceof List) {
                body.add(new ArrayList<>((List<?>) line));
            } else {
                throw new IllegalArgumentException("El cuerpo de la función debe estar formado por listas de expresiones");
            }
        }
        Function function = new Function(funcName, params, body);
        context.setFunction(funcName, function);
        return "Función " + funcName + " definida.";
    }

    private Object evaluateDotimes(List<?> args) {
        String varName = args.get(0).toString();
        int limit = (int) evaluate(args.get(1));
        List<?> body = args.subList(2, args.size());
        Object result = null;
        for (int i = 0; i < limit; i++) {
            context.setVariable(varName, Integer.toString(i));
            for (Object expr : body) {
                result = evaluate(expr);
            }
        }
        return result;
    }

    private Object evaluateOperation(String operation, List<?> args) {
        int result = (int) evaluate(args.get(0));
        for (int i = 1; i < args.size(); i++) {
            int value = (int) evaluate(args.get(i));
            switch (operation) {
                case "+":
                    result += value;
                    break;
                case "-":
                    result -= value;
                    break;
                case "*":
                    result *= value;
                    break;
                case "/":
                    if (value == 0) {
                        throw new ArithmeticException("División por cero");
                    }
                    result /= value;
                    break;
            }
        }
        return result;
    }

    private Object executeFunction(Function func, List<?> args) {
        // Crear un nuevo contexto temporal que hereda el actual
        Context tempContext = new Context();
        tempContext.getVariables().putAll(context.getVariables());
        tempContext.getFunctions().putAll(context.getFunctions());

        // Asignar valores a los parámetros de la función
        for (int i = 0; i < func.getParams().size(); i++) {
            String param = func.getParams().get(i);
            Object value = evaluate(args.get(i));
            tempContext.setVariable(param, value.toString());
        }

        Object result = null;
        // Cada línea del cuerpo es evaluada en el nuevo contexto
        for (List<?> line : func.getBody()) {
            result = new Evaluator(tempContext).evaluate(line);
        }
        return result;
    }

    private Object evaluateQuote(List<?> args) {
        if (args.size() != 1) {
            throw new IllegalArgumentException("quote espera exactamente un argumento");
        }
        return args.get(0);
    }

    /**
     * Evaluación de operaciones de comparación: =, /=, <, <=, >, >=
     * Se espera que todos los operandos sean evaluados a enteros.
     */
    private Object evaluateLogicalExpression(String operation, List<?> args) {
        int firstValue = (int) evaluate(args.get(0));
        for (int i = 1; i < args.size(); i++) {
            int nextValue = (int) evaluate(args.get(i));
            switch (operation) {
                case "=":
                    if (firstValue != nextValue)
                        return false;
                    break;
                case "/=":
                    if (firstValue == nextValue)
                        return false;
                    break;
                case "<":
                    if (firstValue >= nextValue)
                        return false;
                    break;
                case "<=":
                    if (firstValue > nextValue)
                        return false;
                    break;
                case ">":
                    if (firstValue <= nextValue)
                        return false;
                    break;
                case ">=":
                    if (firstValue < nextValue)
                        return false;
                    break;
                default:
                    throw new IllegalArgumentException("Operación de comparación desconocida: " + operation);
            }
            firstValue = nextValue; // Actualiza para la siguiente comparación
        }
        return true;
    }

    private Object evaluateAtom(List<?> args) {
        if (args.size() != 1) {
            throw new IllegalArgumentException("ATOM requiere exactamente 1 argumento");
        }
        Object value = evaluate(args.get(0));
        return !(value instanceof List);
    }

    private Object evaluateList(List<?> args) {
        if (args.size() != 1) {
            throw new IllegalArgumentException("LIST requiere exactamente 1 argumento");
        }
        Object value = evaluate(args.get(0));
        return value instanceof List;
    }

    /**
     * Evalúa COND: una secuencia de cláusulas donde cada cláusula es una lista (condición, expresión).
     * Devuelve el resultado de la primera cláusula cuya condición evalúa a verdadero.
     */
    private Object evaluateCond(List<?> args) {
        for (Object clauseObj : args) {
            if (!(clauseObj instanceof List)) {
                throw new IllegalArgumentException("Cada cláusula de COND debe ser una lista");
            }
            List<?> clause = (List<?>) clauseObj;
            if (clause.size() < 2) {
                throw new IllegalArgumentException("Cada cláusula de COND debe tener al menos 2 elementos");
            }
            Object condition = evaluate(clause.get(0));
            if (condition instanceof Boolean && (Boolean) condition) {
                return evaluate(clause.get(1));
            }
        }
        // Si ninguna condición es verdadera, se retorna null (o se podría definir como NIL)
        return null;
    }

    /**
     * Evaluación de la estructura IF (condición, verdadero, falso).
     * Aunque en la nota se indica que no es necesario implementarlo, se deja por compatibilidad.
     */
    private Object evaluateIf(List<?> args) {
        if (args.size() != 3) {
            throw new IllegalArgumentException("if requiere exactamente 3 argumentos");
        }
        Object condition = evaluate(args.get(0));
        if (!(condition instanceof Boolean)) {
            throw new IllegalArgumentException("La condición del if debe ser booleana");
        }
        return ((Boolean) condition) ? evaluate(args.get(1)) : evaluate(args.get(2));
    }
}
