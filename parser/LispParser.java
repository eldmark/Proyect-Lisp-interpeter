import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Clase que implementa un parser y evaluador para expresiones LISP.
 */
public class LispParser {
    
    /**
     * Parsea una expresión LISP y la convierte en una estructura de listas anidadas.
     * 
     * @param expression La expresión LISP en formato de cadena.
     * @return Una estructura de listas representando la expresión.
     */
    public static Object parse(String expression) {
        List<String> tokens = tokenize(expression);
        return parseTokens(new LinkedList<>(tokens));
    }
    
    /**
     * Tokeniza la expresión LISP dividiéndola en una lista de tokens.
     * 
     * @param expression La expresión LISP en formato de cadena.
     * @return Una lista de tokens extraídos de la expresión.
     */
    private static List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        
        for (char c : expression.toCharArray()) {
            if (c == '(' || c == ')') {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else if (Character.isWhitespace(c)) {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
            } else {
                token.append(c);
            }
        }
        
        if (token.length() > 0) {
            tokens.add(token.toString());
        }
        
        return tokens;
    }
    
    /**
     * Convierte una lista de tokens en una estructura anidada de listas.
     * 
     * @param tokens Lista de tokens a procesar.
     * @return Una estructura de listas anidadas representando la expresión LISP.
     */
    private static Object parseTokens(LinkedList<String> tokens) {
        if (tokens.isEmpty()) {
            throw new RuntimeException("Unexpected end of input");
        }
        
        String token = tokens.poll();
        if ("(".equals(token)) {
            List<Object> list = new ArrayList<>();
            while (!tokens.isEmpty() && !")".equals(tokens.peek())) {
                list.add(parseTokens(tokens));
            }
            if (tokens.isEmpty()) {
                throw new RuntimeException("Unmatched parentheses");
            }
            tokens.poll(); // Consume ')'
            return list;
        } else if (")".equals(token)) {
            throw new RuntimeException("Unexpected closing parenthesis");
        } else {
            return token.matches("\\d+") ? Integer.parseInt(token) : token;
        }
    }
    
    /**
     * Evalúa una estructura de listas representando una expresión LISP.
     * 
     * @param expression La estructura anidada de la expresión.
     * @param variables Mapa de variables y sus valores.
     * @return El resultado de evaluar la expresión.
     */
    private static Object evaluate(Object expression, Map<String, Integer> variables) {
        if (expression instanceof Integer) {
            return expression;
        }
        if (expression instanceof String) {
            String var = (String) expression;
            if (variables.containsKey(var)) {
                return variables.get(var);
            }
            return var; // Devuelve la variable como está si no tiene valor asignado
        }
        
        List<?> list = (List<?>) expression;
        if (list.isEmpty()) {
            throw new RuntimeException("Empty expression");
        }
        
        String operator = list.get(0).toString();
        if ("QUOTE".equals(operator)) {
            return list.subList(1, list.size());
        }
        
        if (list.size() < 3) {
            throw new RuntimeException("Invalid expression: " + list);
        }
        
        Object leftObj = evaluate(list.get(1), variables);
        Object rightObj = evaluate(list.get(2), variables);
        
        if (!(leftObj instanceof Integer) || !(rightObj instanceof Integer)) {
            return list; // Devuelve la expresión sin evaluar si hay variables sin asignar
        }
        
        int left = (int) leftObj;
        int right = (int) rightObj;
        
        switch (operator) {
            case "+":
                return left + right;
            case "-":
                return left - right;
            case "*":
                return left * right;
            case "/":
                if (right == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return left / right;
            default:
                throw new RuntimeException("Unknown operator: " + operator);
        }
    }
    
    /**
     * Formatea la salida para que se muestre sin comas, manteniendo la estructura de listas.
     * 
     * @param parsed La estructura anidada de la expresión LISP parseada.
     * @return Una cadena de texto con el formato adecuado.
     */
    private static String formatOutput(Object parsed) {
        if (parsed instanceof List) {
            List<?> list = (List<?>) parsed;
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(" ");
                sb.append(formatOutput(list.get(i)));
            }
            sb.append("]");
            return sb.toString();
        } else {
            return parsed.toString();
        }
    }
    
    /**
     * Método principal que solicita una expresión LISP al usuario, la parsea y la evalúa.
     * 
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese una expresión LISP: ");
        String expression = scanner.nextLine();
        scanner.close();
        
        Object parsedExpression = parse(expression);
        System.out.println("Parseado: " + formatOutput(parsedExpression));
        
        Map<String, Integer> variables = new HashMap<>(); // Aquí se pueden definir valores para variables
        Object result = evaluate(parsedExpression, variables);
        System.out.println("Evaluado: " + formatOutput(result));
    }
}
