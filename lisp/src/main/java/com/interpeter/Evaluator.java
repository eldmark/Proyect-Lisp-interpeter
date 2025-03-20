package com.interpeter;

import java.util.ArrayList;
import java.util.List;

public class Evaluator {
    private Context context;

    public Evaluator(Context context) {
        this.context = context;
    }

    public Object evaluate(Object expr) {
        if (expr instanceof Integer || expr instanceof Boolean) return expr;

        if (expr instanceof String) {
            String varName = (String) expr;
            Object value = context.getVariable(varName);
            if (value != null) {
                return value;
            }
            return varName;
        }

        if (expr instanceof List) {
            List<?> list = (List<?>) expr;
            if (list.isEmpty()) throw new IllegalArgumentException("La lista está vacía");

            String operation = list.get(0).toString().toLowerCase();

            switch (operation) {
                case "defun": return evaluateDefun(list);
                case "if": return evaluateIf(list.subList(1, list.size()));
                case "+": case "-": case "*": case "/": 
                    return evalArithmetic(operation, list.subList(1, list.size()));
                case "<": case "<=": case ">": case ">=": case "=": case "/=": 
                    return evalLogical(operation, list.subList(1, list.size()));
                default:
                    Function func = context.getFunction(operation);
                    if (func != null) {
                        return executeFunction(func, list.subList(1, list.size()));
                    }
                    throw new IllegalArgumentException("Función no definida: " + operation);
            }
        }
        throw new IllegalArgumentException("Tipo de expresión no soportada");
    }

    private Object evaluateDefun(List<?> list) {
        String funcName = list.get(1).toString();
        @SuppressWarnings("unchecked")
        List<String> params = (List<String>) list.get(2);
        ArrayList<List<?>> body = new ArrayList<>();
        for (int i = 3; i < list.size(); i++) {
            body.add((List<?>) list.get(i));
        }
        context.setFunction(funcName, new Function(funcName, params, body));
        return "Función " + funcName + " definida.";
    }

    private Object executeFunction(Function func, List<?> args) {
        Context temp = new Context();
        temp.getVariables().putAll(context.getVariables());
        temp.getFunctions().putAll(context.getFunctions());

        List<String> params = func.getParams();
        for (int i = 0; i < params.size(); i++) {
            Object evaluatedArg = evaluate(args.get(i));
            temp.setVariable(params.get(i), evaluatedArg.toString());
        }

        Object result = null;
        for (List<?> expr : func.getBody()) {
            result = new Evaluator(temp).evaluate(expr);
        }
        return result;
    }

    private Object evaluateIf(List<?> args) {
        Object condition = evaluate(args.get(0));
        if (condition instanceof Boolean) {
            return (Boolean) condition ? evaluate(args.get(1)) : evaluate(args.get(2));
        } else if (condition instanceof Integer) {
            return ((Integer) condition != 0) ? evaluate(args.get(1)) : evaluate(args.get(2));
        }
        throw new RuntimeException("Condición inválida en IF");
    }

    private Object evalArithmetic(String op, List<?> args) {
        if (args.isEmpty()) throw new RuntimeException("Se requieren argumentos para operación aritmética");
        
        double result = resolveToInt(evaluate(args.get(0)));
        for (int i = 1; i < args.size(); i++) {
            double val = resolveToInt(evaluate(args.get(i)));
            switch (op) {
                case "+": result += val; break;
                case "-": result -= val; break;
                case "*": result *= val; break;
                case "/": 
                    if (val == 0) throw new ArithmeticException("División por cero");
                    result /= val; 
                    break;
            }
        }
        return result;
    }

    private Object evalLogical(String op, List<?> args) {
        if (args.size() != 2) throw new RuntimeException("Las operaciones lógicas requieren exactamente 2 argumentos");
        
        double a = resolveToInt(evaluate(args.get(0)));
        double b = resolveToInt(evaluate(args.get(1)));
        
        switch (op) {
            case "<": return a < b;
            case "<=": return a <= b;
            case ">": return a > b;
            case ">=": return a >= b;
            case "=": return Math.abs(a - b) < 0.0001; // Para comparación de doubles
            case "/=": return Math.abs(a - b) >= 0.0001;
            default: throw new RuntimeException("Operador lógico no soportado: " + op);
        }
    }

    private double resolveToInt(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj).doubleValue();
        } else if (obj instanceof Double) {
            return (Double) obj;
        } else if (obj instanceof String) {
            try {
                return Double.parseDouble((String) obj);
            } catch (NumberFormatException e) {
                throw new RuntimeException("No se pudo convertir a número: " + obj);
            }
        }
        throw new RuntimeException("No se pudo convertir a número: " + obj);
    }
}
