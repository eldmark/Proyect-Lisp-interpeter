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
                return executeFunction(func, list.subList(1, list.size()));
            }

            // Operaciones aritméticas básicas
            return evaluateOperation(operation, list.subList(1, list.size()));
        }
        return null;
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
}
