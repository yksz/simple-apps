package calculator.parser;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import calculator.lexer.Lexer;
import calculator.lexer.Operator;
import calculator.lexer.Operator.Associativity;
import calculator.lexer.Token;

public class SyntaxParser {

    private final Lexer lexer;
    private final Stack<Operator> stack;
    private final Queue<Object> queue;

    public SyntaxParser() {
        lexer = new Lexer();
        stack = new Stack<Operator>();
        queue = new LinkedList<Object>();
    }

    public Queue<Object> parse(String statement) throws SyntaxException {
        if (statement == null)
            throw new NullPointerException("statement must not be null");

        stack.clear();
        queue.clear();

        List<Token> tokens = lexer.analyze(statement);
        for (Token token : tokens) {
            switch (token.getType()) {
            case NUMBER:
                parseNumber(token);
                break;
            case SYMBOL:
                parseSymbol(token);
                break;
            case PARENTHESIS:
                parseParenthesis(token);
                break;
            default:
            }
        }

        while (!stack.empty()) {
            Operator o = stack.pop();
            if (o == Operator.OPEN_PARENTHESIS)
                throw new SyntaxException("')' is not found");
            queue.offer(o);
        }

        return queue;
    }

    private void parseNumber(Token token) {
        queue.offer(new BigDecimal(token.getText()));
    }

    private void parseSymbol(Token token) throws SyntaxException {
        Operator o1 = Operator.getBySymbol(token.getText());
        if (o1 == Operator.UNKNOWN)
            throw new SyntaxException("Operator is unknown");

        while (stack.empty() == false) {
            Operator o2 = stack.peek();
            if ((o1.getAssociativity() == Associativity.LEFT_TO_RIGHT
                    && o1.getPrecedence() == o2.getPrecedence())
                    || o1.getPrecedence() < o2.getPrecedence()) {
                o2 = stack.pop();
                queue.offer(o2);
            } else {
                break;
            }
        }
        stack.push(o1);
    }

    private void parseParenthesis(Token token) throws SyntaxException {
        Operator o1 = Operator.getBySymbol(token.getText());
        if (o1 == Operator.UNKNOWN)
            throw new SyntaxException("Operator is unknown");

        if (o1 == Operator.OPEN_PARENTHESIS) {
            stack.push(o1);
        } else if (o1 == Operator.CLOSE_PARENTHESIS) {
            Operator o2 = null;
            while (stack.empty() == false) {
                o2 = stack.pop();
                if (o2 == Operator.OPEN_PARENTHESIS)
                    break;
                else
                    queue.offer(o2);
            }
            if (o2 == null || o2 != Operator.OPEN_PARENTHESIS)
                throw new SyntaxException("'(' is not found");
        } else {
        }
    }

}
