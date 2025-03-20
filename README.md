LISP Interpreter - Java Project
Descripción
Este proyecto es un intérprete de LISP básico desarrollado en Java. Permite:

✅ Definir y evaluar funciones (`defun`)  
✅ Manejar variables (`setq`)  
✅ Soportar expresiones aritméticas y lógicas  
✅ Ejecutar estructuras de control (`if`, `cond`, `dotimes`)  
✅ Evaluar recursividad (ejemplo: `fibonacci`)  
✅ Trabajar con listas (`car`, `cdr`, `cons`, `append`, etc.)

El programa parsea expresiones LISP, las evalúa y permite almacenar variables y funciones en un contexto propio.

Estructura del proyecto
```
/src/com/interpeter/
│
├── Lexer.java          # Tokenizador de expresiones LISP
├── Parser.java         # Convierte las expresiones en listas anidadas (AST)
├── Evaluator.java      # Ejecuta las expresiones y funciones LISP
├── Context.java        # Guarda variables y funciones
├── Function.java       # Representa funciones definidas por el usuario
└── Main.java           # Ejecuta el REPL y conecta todo el flujo
```

Requisitos para ejecutar:
- JDK 11 o superior
- IDE o terminal con compilador Java

Uso básico en consola:
```lisp
LISP> (setq x 5)
LISP> (+ x 10)
Resultado Evaluado: 15

LISP> (defun fibonacci (n) (if (<= n 1) n (+ (fibonacci (- n 1)) (fibonacci (- n 2)))))
LISP> (fibonacci 7)
Resultado Evaluado: 13
```
Características principales:
- Recursividad soportada
- Almacenamiento de variables
- Evaluación de condicionales y bucles
- Soporte de listas y operaciones sobre ellas
- Manejo de errores (paréntesis, argumentos faltantes, etc.)

Ejemplos de expresiones soportadas
```lisp
( defun malan ( m n ) ( if ( = n 0 ) 1 ( * m ( malan m ( - n 1 ) ) ) ) )
( malan 2 3 )

( defun fibonacci ( n ) ( if ( <= n 1 ) n ( + ( fibonacci ( - n 1 ) ) ( fibonacci ( - n 2 ) ) ) ) )
( fibonacci 7 )

( defun convertir ( c ) ( + ( * c ( / 9 5 ) ) 32 ) )
( convertir 30 )

( SETQ x 2 )
( + x 2 )
```

Créditos
- Autores: 
          - Marco Díaz (24229)
          - Norman Aguirre (24479)
          - Hugo Méndez (241265)
- Universidad del Valle de Guatemala
- Curso de Algoritmos y Estructuras de datos - Semestre 1 - 2025

Estado del proyecto
✔ Finalizado: Se soportan funciones recursivas, estructuras de control y operaciones de listas.  
✔ Listo para pruebas o futuras extensiones (manejo de macros, más funciones LISP).

Disclaimer: Este proyecto es académico y fue desarrollado con fines educativos. ***
