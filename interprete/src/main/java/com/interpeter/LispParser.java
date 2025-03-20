package com.interpeter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase que implementa un parser y evaluador para expresiones LISP.
 */
public class LispParser {
    
    private static Lexer lexer = new Lexer();
    
    /**
     * Parsea una expresión LISP y la convierte en una estructura de listas anidadas.
     * 
     * @param expression La expresión LISP en formato de cadena.
     * @return Una estructura de listas representando la expresión.
     */
    public static Object parse(String expression) {
        ArrayList<String> tokens = lexer.tokenize(expression);
        if (tokens == null) {
            throw new RuntimeException("Error en la expresión LISP: Paréntesis desbalanceados");
        }
        return parseTokens(new LinkedList<>(tokens));
    }
    
    /**
     * Convierte una lista de tokens en una estructura anidada de listas.
     * 
     * @param tokens Lista de tokens a procesar.
     * @return Una estructura de listas anidadas representando la expresión LISP.
     */
    public static Object parseTokens(LinkedList<String> tokens) {
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
            return list;
        } else if (")".equals(token)) {
            throw new RuntimeException("Unexpected closing parenthesis");
        } else {
            return token.matches("\\d+") ? Integer.parseInt(token) : token;
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese una expresión LISP: ");
        String expression = scanner.nextLine();
        scanner.close();
        
        try {
            Object parsedExpression = parse(expression);
            System.out.println("Parseado: " + parsedExpression);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
