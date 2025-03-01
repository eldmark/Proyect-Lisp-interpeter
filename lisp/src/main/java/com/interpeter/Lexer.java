package com.interpeter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Lexer {
    public Lexer() {
    }

    private static boolean reservedWord(String token) {
        final Set<String> lispReservedWords = new HashSet<>();
        // Special Forms from the Common Lisp standard
        String[] specialForms = { "defun", "lambda", "defmacro", "defparameter", "defvar", "defconstant", "setq",
                "setf",
                "quote", "progn", "if", "cond", "case", "when", "unless", "let", "let*", "multiple-value-bind",
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

    public ArrayList<String> tokenize(String input) {
        if (isCorrect(input)) {
            ArrayList<String> tokens = new ArrayList<String>();
            for (String i : input.split(" ")) {
                tokens.add(i);
            }
            return tokens;

        }
        return null;
    }
}
