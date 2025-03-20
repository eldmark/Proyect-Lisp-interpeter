package com.interpeter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    private Lexer lexer;
    private Context context;

    public Parser(Lexer lexer, Context context) {
        this.lexer = lexer;
        this.context = context;
    }

    public Object parse(String expression) {
        ArrayList<String> tokens = lexer.tokenize(expression);
        if (tokens == null) {
            throw new RuntimeException("Error en la expresión LISP: Paréntesis desbalanceados");
        }
        System.out.println("Tokens: " + tokens);
        return parseTokens(new LinkedList<>(tokens));
    }

    private Object parseTokens(LinkedList<String> tokens) {
        if (tokens.isEmpty()) {
            throw new RuntimeException("Fin de entrada inesperado");
        }

        String token = tokens.poll();

        if ("(".equals(token)) {
            List<Object> list = new ArrayList<>();
            while (!tokens.isEmpty() && !")".equals(tokens.peek())) {
                list.add(parseTokens(tokens));
            }
            if (tokens.isEmpty()) {
                throw new RuntimeException("Paréntesis no balanceados");
            }
            tokens.poll();
            
            return procesarSetq(list);
        } else if (")".equals(token)) {
            throw new RuntimeException("Paréntesis de cierre inesperado");
        } else {
            return parseTokenValue(token);
        }
    }

    private Object procesarSetq(List<Object> list) {
        if (!list.isEmpty() && "SETQ".equalsIgnoreCase(list.get(0).toString())) {
            if (list.size() == 3) {
                String varName = list.get(1).toString();
                Object varValueObj = list.get(2);
                String varValue = varValueObj.toString();
                
                context.setVariable(varName, varValue); // Guardar correctamente la variable
                System.out.println("Variable guardada: " + varName + " = " + varValue);
                return varValueObj;
            } else {
                throw new RuntimeException("Uso incorrecto de SETQ");
            }
        }
        return list;
    }

    private Object parseTokenValue(String token) {
        if (token.matches("\\d+")) {
            return Integer.parseInt(token);
        }
        String value = context.getVariable(token);
        if (value != null) {
            System.out.println("Variable encontrada: " + token + " = " + value);
            return Integer.parseInt(value);
        }
        return token;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Lexer lexer = new Lexer();
        Context context = new Context();
        Parser parser = new Parser(lexer, context);

        while (true) {
            System.out.print("\nIngrese una expresión LISP (o 'exit' para salir): ");
            String expression = scanner.nextLine();

            if (expression.equalsIgnoreCase("exit")) break;

            try {
                Object parsedExpression = parser.parse(expression);
                System.out.println("Parseado: " + parsedExpression);
                System.out.println("Variables en contexto: " + context.getVariables());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
