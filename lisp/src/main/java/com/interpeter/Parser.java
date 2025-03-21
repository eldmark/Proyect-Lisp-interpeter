package com.interpeter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase que se encarga de analizar expresiones LISP, gestionando la tokenización
 * y el contexto para el almacenamiento de variables.
 */
public class Parser {
    
    /**
     * Crea una nueva instancia de Parser.
     *
     * @param lexer El objeto Lexer utilizado para la tokenización.
     * @param context El contexto que contiene variables y funciones.
     */
    private Lexer lexer;
    private Context context;

    public Parser(Lexer lexer, Context context) {
        this.lexer = lexer;
        this.context = context;
    }

    /**
     * Analiza una expresión LISP y devuelve su representación estructurada.
     *
     * @param expression La expresión LISP en formato de cadena.
     * @return La representación estructurada de la expresión.
     * @throws RuntimeException Si hay un error en la expresión LISP.
     */
    public Object parse(String expression) {
        ArrayList<String> tokens = lexer.tokenize(expression);
        if (tokens == null) {
            throw new RuntimeException("Error en la expresión LISP: Paréntesis desbalanceados");
        }
        System.out.println("Tokens: " + tokens);
        return parseTokens(new LinkedList<>(tokens));
    }

    /**
     * Procesa una lista de tokens y los convierte en una estructura de datos.
     *
     * @param tokens La lista de tokens a procesar.
     * @return La estructura de datos resultante.
     * @throws RuntimeException Si hay un error en la lista de tokens.
     */
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

    /**
     * Procesa una asignación de variable (SETQ) en la lista de tokens.
     *
     * @param list La lista que representa la asignación de variable.
     * @return El valor asignado a la variable.
     * @throws RuntimeException Si el uso de SETQ es incorrecto.
     */
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

    /**
     * Convierte un token en su valor correspondiente.
     *
     * @param token El token a convertir.
     * @return El valor correspondiente al token.
     */
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

    /**
     * Método principal que inicia el bucle de entrada del usuario para el intérprete LISP.
     *
     * @param args Argumentos de línea de comandos (no utilizados).
     */
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
