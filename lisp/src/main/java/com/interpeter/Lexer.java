package com.interpeter;

/*
 * This class is responsible for tokenizing the input string.
 * It checks if the input string is correct and then tokenizes it.
 * It also checks if the token is a reserved word.
 * This class was made by Marco Alejandro Díaz Castañeda
 * Carné 24229
 * Universidad del Valle de Guatemala
 * 28/2/2025
 * Los comentarios fueron hechos con inteligencia artificial 
 * 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Lexer {
    
    public Lexer() {
    }

    /**
     * Checks if a given token is a reserved word in Lisp.
     *
     * @param token the token to check
     * @return true if the token is a reserved word, false otherwise
     * This class was based on a class made by Chatgpt
     * It was addapted to the needs of the project by Marco Díaz
     */
    private static boolean reservedWord(String token) {
        final Set<String> lispReservedWords = new HashSet<>();
        // Special Forms from the Common Lisp standard
        String[] specialForms = { "defun", "lambda", "defmacro", "defparameter", "defvar", "defconstant", "setq",
                "setf", "quote", "progn", "if", "cond", "case", "when", "unless", "let", "let*", "multiple-value-bind",
                "loop", "do", "do*", "dolist", "dotimes", "return", "return-from", "block", "go", "tagbody",
                "eval-when", "progv", "unwind-protect", "catch", "throw" };

        // Standard Functions from the Common Lisp standard
        String[] standardFunctions = { "+", "-", "*", "/", "mod", "rem", "abs", "=", "/=", "<", "<=", ">", ">=", "eq",
                "eql", "equal", "equalp", "and", "or", "not", "typep", "subtypep", "numberp", "integerp", "symbolp",
                "listp", "functionp", "arrayp", "stringp", "car", "cdr", "cons", "append", "reverse", "nth",
                "nthcdr", "mapcar", "reduce", "concatenate", "string=", "string>", "string<", "format", "read",
                "write", "print", "princ", "terpri", "map", "apply", "funcall", "compose", "make-hash-table",
                "gethash", "remhash", "defstruct", "make-instance", "slot-value", "in-package", "use-package",
                "export", "import" };

        // Reserved symbols from the Common Lisp standard
        String[] reservedSymbols = { "t", "nil" };

        // Lisp Keywords (starts with :)
        String[] lispKeywords = { ":test", ":test-not", ":key", ":allow-other-keys", ":initial-element",
                ":initial-contents", ":start", ":end", ":from-end" };

        // Add all reserved words to the HashSet
        for (String word : specialForms) {
            lispReservedWords.add(word);
        }
        for (String word : standardFunctions) {
            lispReservedWords.add(word);
        }
        for (String word : reservedSymbols) {
            lispReservedWords.add(word);
        }
        for (String word : lispKeywords) {
            lispReservedWords.add(word);
        }
        return lispReservedWords.contains(token.toLowerCase());
    }

    /**
     * Checks if the input string is correct by verifying the balance of parentheses.
     *
     * @param input the input string to check
     * @return true if the input string is correct, false otherwise
     */
    public boolean isCorrect(String input) {
        ArrayList<String> tokens = new ArrayList<String>();

        for (String i : input.split(" ")) {
            tokens.add(i);
        }
        if (tokens.size() == 0) {
            return false;
        }
    
        int count = Collections.frequency(tokens, "(");
        int count2 = Collections.frequency(tokens, ")");
        if (count != count2) {
            return false;
        }

        return true;
    }

    /**
     * Tokenizes the input string into a list of tokens.
     * Reserved words are converted to uppercase.
     *
     * @param input the input string to tokenize
     * @return a list of tokens, or null if the input string is incorrect
     */
    public ArrayList<String> tokenize(String input) {
        if (isCorrect(input)) {
            ArrayList<String> tokens = new ArrayList<String>();
            for (String i : input.split(" ")) {
                if(reservedWord(i)){
                    tokens.add(i.toUpperCase());
                    // se ponen en mayuscula las palabras reservadas para diferenciarlas de las variables
                }
                else{
                    tokens.add(i);
                }
            }
            return tokens;

        }
        return null;
    }
}
