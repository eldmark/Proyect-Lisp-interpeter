# LISP Interpreter in Java

A basic **LISP interpreter implemented in Java**, capable of parsing and evaluating expressions, managing variables and user-defined functions, supporting recursion, control structures, and list operations. The project simulates the internal mechanics of an interpreted programming language.

---

##  Project Overview

This project implements a **custom LISP interpreter** built from scratch in Java. It includes all core components required for interpreting a programming language, such as tokenization, parsing, abstract syntax tree (AST) evaluation, and execution context management.

The interpreter supports arithmetic and logical expressions, control flow structures, recursion, and list manipulation, providing a functional subset of the LISP language.

---

## Key Concepts Implemented

- Lexical analysis (tokenization)
- Parsing expressions into nested structures (AST)
- Expression evaluation
- Execution context (variables and functions)
- Recursive function calls
- List processing and symbolic computation

---

##  Supported Features

- Variable definition and assignment (`setq`)
- User-defined functions (`defun`)
- Arithmetic and logical operations
- Conditional expressions (`if`, `cond`)
- Looping constructs (`dotimes`)
- Recursive evaluation (e.g. Fibonacci)
- List operations (`car`, `cdr`, `cons`, `append`, etc.)
- Error handling (invalid syntax, missing arguments, mismatched parentheses)

---

## Project Structure


Each component is designed with a clear responsibility, following a modular and maintainable structure.
```plaintext
/src/com/interpreter/
│
├── Lexer.java # Tokenizes LISP expressions
├── Parser.java # Parses tokens into nested lists (AST)
├── Evaluator.java # Evaluates expressions and executes logic
├── Context.java # Stores variables and user-defined functions
├── Function.java # Represents user-defined LISP functions
└── Main.java # Entry point and REPL execution

```

## Technologies Used

- Java (JDK 11+)
- Core data structures (lists, trees)
- Recursive algorithms
- Interpreter design principles

---

## Getting Started

### Prerequisites

- Java JDK 11 or higher
- Java-compatible IDE or terminal environment

---

### Compilation and Execution

Compile and run the project using your preferred IDE or via terminal:
          ```bash

                              javac Main.java
                              java Main

Once running, the interpreter starts a REPL (Read–Eval–Print Loop).

### Example Usage

          ( defun fibonacci ( n ) 
                   ( if ( <= n 1 ) n 
                   ( + ( fibonacci ( - n 1 ) ) ( fibonacci ( - n 2 ) ) ) 
                   ) 
                   ) 
                   ( fibonacci 7 )

### My Contributions
This project was developed as a team collaboration.
My specific contributions include:
- eldmark (Marco Díaz)
- The context module
  - where functions and variables and are stored
- Recursive logic
- Lexer logic


Project Status
- Completed
- Supports recursion, control structures, and list operations
- Ready for testing and future extensions (e.g. macros, additional LISP primitives)

 Disclaimer
This project was developed for educational purposes, focusing on interpreter design and language implementation concepts.
