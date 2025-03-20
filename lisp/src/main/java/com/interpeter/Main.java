package com.interpeter;

import java.util.Scanner;

/**
 * Clase principal que inicia el intérprete LISP.
 * Esta clase configura los componentes necesarios y gestiona la entrada del usuario.
 */
public class Main {
    
    /**
     * Método principal que ejecuta el intérprete LISP.
     *
     * @param args Argumentos de línea de comandos (no utilizados).
     */

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Lexer lexer = new Lexer();
        Context context = new Context();
        Parser parser = new Parser(lexer, context);
        Evaluator evaluator = new Evaluator(context);

        System.out.println("Intérprete LISP listo. Escribe 'exit' para salir.\n"); 
        // Inicia el bucle de entrada del usuario

        while (true) {
            System.out.print("LISP> "); 
            // Lee la entrada del usuario
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                Object parsed = parser.parse(input);
                System.out.println("Árbol Parseado: " + parsed);

                Object result = evaluator.evaluate(parsed);
                System.out.println("Resultado Evaluado: " + result); 
                // Muestra las variables almacenadas en el contexto
                System.out.println("Variables en Contexto: " + context.getVariables());

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
