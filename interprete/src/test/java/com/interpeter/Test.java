package com.interpeter;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

public class Test {

    @Test
    public void testReservedWord() {
        // Prueba de palabras reservadas
        assertTrue(Lexer.reservedWord("defun"));
        assertTrue(Lexer.reservedWord("setq"));
        assertTrue(Lexer.reservedWord("car"));
        assertFalse(Lexer.reservedWord("variable"));
    }

    @Test
    public void testIsCorrect() {
        Lexer lexer = new Lexer();
        assertTrue(lexer.isCorrect("(setq x 10)"));
        assertFalse(lexer.isCorrect("(setq x 10")); // Falta paréntesis de cierre
    }

    @Test
    public void testTokenize() {
        Lexer lexer = new Lexer();
        ArrayList<String> tokens = lexer.tokenize("(setq x 10)");
        assertNotNull(tokens);
        assertEquals(List.of("(", "SETQ", "x", "10", ")"), tokens);

        tokens = lexer.tokenize("(+ 1 2)");
        assertNotNull(tokens);
        assertEquals(List.of("(", "+", "1", "2", ")"), tokens);
    }

    @Test
    public void testParseValidExpression() {
        Object parsed = LispParser.parse("(+ 1 2)");
        assertTrue(parsed instanceof List);
        List<?> parsedList = (List<?>) parsed;
        assertEquals(List.of("+", 1, 2), parsedList);
    }

    @Test
    public void testParseInvalidExpression() {
        Exception exception = assertThrows(RuntimeException.class, () -> LispParser.parse("(+ 1 2"));
        assertEquals("Error en la expresión LISP: Paréntesis desbalanceados", exception.getMessage());
    }

    @Test
    public void testParseTokens() {
        LinkedList<String> tokens = new LinkedList<>(List.of("(", "+", "1", "2", ")"));
        Object result = LispParser.parseTokens(tokens);
        assertTrue(result instanceof List);
        assertEquals(List.of("+", 1, 2), result);
    }

    @Test
    public void testParserWithContext() {
        Lexer lexer = new Lexer();
        Context context = new Context();
        Parser parser = new Parser(lexer, context);

        parser.parse("(SETQ x 5)");
        assertEquals("5", context.getVariable("x"));
    }
}
