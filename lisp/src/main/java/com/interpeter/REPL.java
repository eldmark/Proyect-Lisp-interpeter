package com.interpeter;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase que ejecuta el bucle de lectura-evaluación-impresión (REPL) para el intérprete LISP.
 * Permite a los usuarios ingresar expresiones LISP, que son tokenizadas, analizadas y evaluadas.
 */
public class REPL {
    
    /**
     * Inicia el bucle de entrada del usuario para el intérprete LISP.
     */
    private Lexer lexer = new Lexer();
    private Context context = new Context();
    private Evaluator evaluator = new Evaluator(context);

    public void iniciar() { 
        // Crea un escáner para leer la entrada del usuario
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("LISP> ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                // Tokeniza la expresión LISP ingresada 
                ArrayList<String> tokens = lexer.tokenize(input);
                
                // Parsea los tokens 
                Object ast = LispParser.parseTokens(new java.util.LinkedList<>(tokens));
                
                // Evalúa la expresión 
                Object result = evaluator.evaluate(ast);
                
                // Muestra el resultado 
                System.out.println("=> " + result);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
