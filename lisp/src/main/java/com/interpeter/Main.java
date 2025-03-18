package com.interpeter;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        NewLexer lexer = new NewLexer();
        String lispExpression = "defun test ( x ) ( + x 1 )";
        ArrayList<String> tokens=lexer.tokenize(lispExpression);
        System.err.println(tokens);
    }
}