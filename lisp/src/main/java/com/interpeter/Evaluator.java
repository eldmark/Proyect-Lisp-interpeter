package com.interpeter;

import java.util.ArrayList;
import java.util.List;

public class Evaluator {

    private Context context;

    public Evaluator(Context context) {
        this.context = context;
    }

    public Object evaluate(Object expr) {
        if (expr instanceof Integer) {
            return expr;
        } else if (expr instanceof String) {
            String varName = (String) expr;
            String value = context.getVariable(varName);
            return (value != null) ? Integer.parseInt(value) : varName;
        } else if (expr instanceof List) {
            List<?> list = (List<?>) expr;
            if (list.isEmpty()) return null;

            if (list.get(0) instanceof Integer) {
            return evaluateOperation("+", list);
            }

            String operation = list.get(0).toString();

            // Definición de variables
            if (operation.equals("set")) {
                String varName = list.get(1).toString();
                Object value = evaluate(list.get(2));
                context.setVariable(varName, value.toString());
                return value;
            }
            
            // Definición de funciones
            if (operation.equals("defun")) {
                String funcName = list.get(1).toString();
                @SuppressWarnings("unchecked")
                ArrayList<String> params = new ArrayList<>((List<String>) list.get(2));
                ArrayList<String[]> body = new ArrayList<>();
                for (int i = 3; i < list.size(); i++) {
                    Object line = list.get(i);
                    if (line instanceof List) {
                        List<?> lineList = (List<?>) line;
                        String[] tokens = lineList.stream().map(Object::toString).toArray(String[]::new);
                        body.add(tokens);
                    }
                }
                Function function = new Function(funcName, params, body);
                context.setFunction(funcName, function);
                return "Función " + funcName + " definida.";
            }
            
            // Llamada a funciones definidas
            Function func = context.getFunction(operation);
            if (func != null) {
                if (operation.equals("if")) {
                    return evaluateLogicalExpresion(list.get(1).toString(), list.subList(2, list.size()));
                }
    
                // Operaciones aritméticas básicas
                return evaluateOperation(operation, list.subList(1, list.size()));
            }
            if (context.getFunction(operation) != null) {
                return executeFunction(context.getFunction(operation), list.subList(1, list.size()));
            }
            if (operation.equals("dotimes")) {
                return evaluateDotimes(list.subList(1, list.size()));
            }
            return null;

        }
        return null;
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
                case "+": result += value; break;
                case "-": result -= value; break;
                case "*": result *= value; break;
                case "/": result /= value; break;
            }
        }
        return result;
    }

    private Object executeFunction(Function func, List<?> args) {
        // Crear un nuevo contexto temporal
        Context tempContext = new Context();
        tempContext.getVariables().putAll(context.getVariables());
        tempContext.getFunctions().putAll(context.getFunctions());

        // Asignar valores a los parámetros
        for (int i = 0; i < func.getParams().size(); i++) {
            String param = func.getParams().get(i);
            Object value = evaluate(args.get(i));
            tempContext.setVariable(param, value.toString());
        }

        Object result = null;
        for (String[] line : func.getBody()) {
            List<String> lineList = List.of(line);
            result = new Evaluator(tempContext).evaluate(lineList);
        }
        return result;

    }
    private Object evaluateLogicalExpresion(String operation, List<?> args) {
        int firstValue = (int) evaluate(args.get(0));
        if (operation.equals("=") || operation.equals("/=") || operation.equals("<") || 
            operation.equals("<=") || operation.equals(">") || operation.equals(">=")) {
            // Comparison operations
            for (int i = 1; i < args.size(); i++) {
                int nextValue = (int) evaluate(args.get(i));
                switch (operation) {
                    case "=":
                        if (firstValue != nextValue) return false;
                        break;
                    case "/=":
                        if (firstValue == nextValue) return false;
                        break;
                    case "<":
                        if (firstValue >= nextValue) return false;
                        break;
                    case "<=":
                        if (firstValue > nextValue) return false;
                        break;
                    case ">":
                        if (firstValue <= nextValue) return false;
                        break;
                    case ">=":
                        if (firstValue < nextValue) return false;
                        break;
                }
                firstValue = nextValue; // Update for the next comparison
        } 
    }        return true;


}
    }
