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
            String value = context.getVariable(varName);
            if (value != null) {
                try { return Integer.parseInt(value); } catch (NumberFormatException e) { return value; }
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
                case "+": case "-": case "*": case "/": return evalArithmetic(operation, list.subList(1, list.size()));
                case "<": case "<=": case ">": case ">=": case "=": case "/=": return evalLogical(operation, list.subList(1, list.size()));
                default:
                    Function func = context.getFunction(operation);
                    if (func != null) return executeFunction(func, list.subList(1, list.size()));
                    return evalArithmetic(operation, list.subList(1, list.size())); // fallback aritmético
            }
        }
        throw new IllegalArgumentException("Tipo de expresión no soportada");
    }

    private Object evaluateDefun(List<?> list) {
        String funcName = list.get(1).toString();
        ArrayList<String> params = new ArrayList<>((List<String>) list.get(2));
        ArrayList<List<?>> body = new ArrayList<>();
        for (int i = 3; i < list.size(); i++) {
            body.add((List<?>) list.get(i)); // Guardamos la AST parseada
        }
        context.setFunction(funcName, new Function(funcName, params, body));
        return "Función " + funcName + " definida.";
    }

    private Object executeFunction(Function func, List<?> args) {
        Context temp = new Context();
        temp.getVariables().putAll(context.getVariables());
        temp.getFunctions().putAll(context.getFunctions());

        for (int i = 0; i < func.getParams().size(); i++) {
            temp.setVariable(func.getParams().get(i), evaluate(args.get(i)).toString());
        }

        Object result = null;
        for (List<?> expr : func.getBody()) {
            result = new Evaluator(temp).evaluate(expr);  // Soporta recursividad
        }
        return result;
    }

    private Object evaluateIf(List<?> args) {
        Object condition = evaluate(args.get(0));
        if (Boolean.TRUE.equals(condition) || (condition instanceof Integer && (int) condition != 0)) {
            return evaluate(args.get(1));
        } else {
            return evaluate(args.get(2));
        }
    }

    private Object evalArithmetic(String op, List<?> args) {
        int result = resolveToInt(args.get(0));
        for (int i = 1; i < args.size(); i++) {
            int val = resolveToInt(args.get(i));
            switch (op) {
                case "+": result += val; break;
                case "-": result -= val; break;
                case "*": result *= val; break;
                case "/": result /= val; break;
            }
        }
        return result;
    }
    

    private Object evalLogical(String op, List<?> args) {
        int a = (int) evaluate(args.get(0));
        int b = (int) evaluate(args.get(1));
        switch (op) {
            case "<": return a < b;
            case "<=": return a <= b;
            case ">": return a > b;
            case ">=": return a >= b;
            case "=": return a == b;
            case "/=": return a != b;
        }
        return false;
    }

    private int resolveToInt(Object obj) {
        System.out.println("Resolviendo a entero: " + obj);
        Object evaluated = evaluate(obj);
        if (evaluated instanceof Integer) {
            return (Integer) evaluated;
        } else if (evaluated instanceof String) {
            try { return Integer.parseInt((String) evaluated); } 
            catch (NumberFormatException e) { 
                throw new RuntimeException("No se pudo convertir a entero: " + evaluated); 
            }
        }
        throw new RuntimeException("No se pudo convertir a entero: " + evaluated);
    }
    
}
