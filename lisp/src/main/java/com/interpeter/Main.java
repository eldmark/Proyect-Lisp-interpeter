package com.interpeter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Lexer lexer = new Lexer();
        Context context = new Context();
        Parser parser = new Parser(lexer, context);
        Evaluator evaluator = new Evaluator(context);

        System.out.println("Intérprete LISP listo. Escribe 'exit' para salir.\n");

        while (true) {
            System.out.print("LISP> ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                Object parsed = parser.parse(input);
                System.out.println("Árbol Parseado: " + parsed);

                Object result = evaluator.evaluate(parsed);
                System.out.println("Resultado Evaluado: " + result);
                System.out.println("Variables en Contexto: " + context.getVariables());

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
