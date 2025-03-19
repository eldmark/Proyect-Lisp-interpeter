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

    /**
     * Parsea una expresión LISP y la convierte en una estructura de listas anidadas.
     * @param expression La expresión LISP en formato de cadena.
     * @return Una estructura de listas representando la expresión.
     */
    public Object parse(String expression) {
        ArrayList<String> tokens = lexer.tokenize(expression);
        if (tokens == null) {
            throw new RuntimeException("Error en la expresión LISP: Paréntesis desbalanceados");
        }
        return parseTokens(new LinkedList<>(tokens));
    }

    /**
     * Convierte una lista de tokens en una estructura anidada de listas y gestiona el contexto.
     * @param tokens Lista de tokens a procesar.
     * @return Una estructura de listas anidadas representando la expresión LISP.
     */
    private Object parseTokens(LinkedList<String> tokens) {
        if (tokens.isEmpty()) {
            throw new RuntimeException("Unexpected end of input");
        }

        String token = tokens.poll();
        if ("(".equals(token)) {
            List<Object> list = new ArrayList<>();
            while (!tokens.isEmpty() && !")".equals(tokens.peek())) {
                list.add(parseTokens(tokens));
            }
            if (tokens.isEmpty()) {
                throw new RuntimeException("Unmatched parentheses");
            }
            tokens.poll(); // Consume ')'

            // Manejo básico del setq
            if (!list.isEmpty() && "SETQ".equals(list.get(0))) {
                // ejemplo: (setq x 5)
                if (list.size() == 3) {
                    String varName = list.get(1).toString();
                    String varValue = list.get(2).toString();
                    context.setVariable(varName, varValue);
                    System.out.println("Variable guardada en contexto: " + varName + " = " + varValue);
                } else {
                    throw new RuntimeException("Uso incorrecto de SETQ");
                }
            }

            return list;
        } else if (")".equals(token)) {
            throw new RuntimeException("Unexpected closing parenthesis");
        } else {
            // Chequea si es un número o deja como string
            return token.matches("\\d+") ? Integer.parseInt(token) : token;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Lexer lexer = new Lexer();
        Context context = new Context();
        Parser parser = new Parser(lexer, context);

        System.out.print("Ingrese una expresión LISP: ");
        String expression = scanner.nextLine();
        scanner.close();

        try {
            Object parsedExpression = parser.parse(expression);
            System.out.println("Parseado: " + parsedExpression);

            // Probando acceso al Context
            System.out.print("Ingrese una variable a consultar en el Contexto: ");
            Scanner varScanner = new Scanner(System.in);
            String var = varScanner.nextLine();
            varScanner.close();
            String value = context.getVariable(var);
            if (value != null) {
                System.out.println("Valor de " + var + ": " + value);
            } else {
                System.out.println("Variable no encontrada.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
