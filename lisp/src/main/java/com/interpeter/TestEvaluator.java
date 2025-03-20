package com.interpeter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase de prueba para el evaluador de expresiones.
 * Esta clase contiene el método principal que inicializa el contexto y el evaluador,
 * y ejecuta una serie de pruebas sobre expresiones similares a Lisp.
 */
public class TestEvaluator {
    
    /**
     * Método principal que ejecuta las pruebas.
     * Este método inicializa el contexto y el evaluador, y ejecuta varias expresiones
     * para verificar su correcto funcionamiento.
     *
     * @param args Argumentos de línea de comandos (no se utilizan en esta implementación).
     */
    public static void main(String[] args) {
        // Inicializa el contexto y el evaluador.
        Context context = new Context();
        Evaluator evaluator = new Evaluator(context);
        List<Object> tests = new ArrayList<>();

        // a) Operaciones aritméticas: (+ 1 2 3) => 6
        tests.add(Arrays.asList("+", 1, 2, 3));

        // b) QUOTE: (quote (1 2 3)) => retorna la lista sin evaluar.
        tests.add(Arrays.asList("quote", Arrays.asList(1, 2, 3)));

        // c) Definición de funciones (DEFUN):
        // Define la función sumar que suma dos números: (defun sumar (a b) ((+ a b)))
        tests.add(Arrays.asList("defun", "sumar", Arrays.asList("a", "b"),
                Arrays.asList(Arrays.asList("+", "a", "b"))));
        // Llamada a la función sumar: (sumar 10 15) => 25
        tests.add(Arrays.asList("sumar", 10, 15));

        // d) SETQ (en nuestro caso 'set'): (set x 100) y luego la variable 'x'
        tests.add(Arrays.asList("set", "x", 100));
        tests.add("x");

        // e) Predicados:
        // ATOM: (atom 5) => true
        tests.add(Arrays.asList("atom", 5));
        // ATOM: (atom (quote (1 2 3))) => false
        tests.add(Arrays.asList("atom", Arrays.asList("quote", Arrays.asList(1, 2, 3))));
        // LIST: (list (quote (1 2 3))) => true
        tests.add(Arrays.asList("list", Arrays.asList("quote", Arrays.asList(1, 2, 3))));
        // Comparación: (< 3 5) => true
        tests.add(Arrays.asList("<", 3, 5));
        // Comparación: (> 5 3) => true
        tests.add(Arrays.asList(">", 5, 3));
        // EQUAL: (equal 5 5) => true
        tests.add(Arrays.asList("equal", 5, 5));

        // f) Condicionales (COND):
        // (cond ((< 5 3) 'menor) ((> 5 3) 'mayor) ((equal 5 5) 'igual))
        tests.add(Arrays.asList("cond",
                Arrays.asList(Arrays.asList("<", 5, 3), "menor"),
                Arrays.asList(Arrays.asList(">", 5, 3), "mayor"),
                Arrays.asList(Arrays.asList("equal", 5, 5), "igual")
        ));

        // g) Paso de parámetros: función como parámetro.
        // Ejemplo: Definir una función 'aplicar' que recibe otra función y dos argumentos.
        // (defun aplicar (f a b) ((f a b)))
        tests.add(Arrays.asList("defun", "aplicar", Arrays.asList("f", "a", "b"),
                Arrays.asList(Arrays.asList("f", "a", "b"))));
        // Llamar a 'aplicar' pasando la función 'sumar' ya definida y dos números:
        // (aplicar sumar 20 30) => 50
        tests.add(Arrays.asList("aplicar", "sumar", 20, 30));

        // Iteramos sobre cada expresión de prueba y mostramos el input y su salida.
        for (Object test : tests) {
            Object result = evaluator.evaluate(test);
            System.out.println("Input: " + test);
            System.out.println("Output: " + result);
            System.out.println("----------------------------");
        }
    }
}
