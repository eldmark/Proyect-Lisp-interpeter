package com.interpeter;

import java.util.ArrayList;
import java.util.Scanner;

public class REPL {
    private Lexer lexer = new Lexer();
    private Context context = new Context();
    private Evaluator evaluator = new Evaluator(context);

    public void iniciar() {
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
