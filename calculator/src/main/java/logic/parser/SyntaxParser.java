package logic.parser;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import logic.lexer.Lexer;
import logic.lexer.Token;
import logic.lexer.attribute.Associativity;
import logic.lexer.attribute.Operator;
import logic.lexer.attribute.Type;

public class SyntaxParser {

    private SyntaxParser() {
    }

    public static Queue<Object> parse(String statement) throws SyntaxException {
        if (statement == null)
            throw new NullPointerException("statement must not be null");

        Stack<Operator> stack = new Stack<Operator>();
        Queue<Object> queue = new LinkedList<Object>();
        List<Token> tokens = Lexer.analyze(statement);

        for (Token token : tokens) {
            if (token.getType() == Type.NUMBER) {

                queue.offer(new BigDecimal(token.getText()));

            } else if (token.getType() == Type.SYMBOL) {
                Operator o1 = Operator.getOperator(token.getText());
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

            } else if (token.getType() == Type.PARENTHESIS) {
                Operator o1 = Operator.getOperator(token.getText());
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

        while (stack.empty() == false) {
            Operator o = stack.pop();
            if (o == Operator.OPEN_PARENTHESIS)
                throw new SyntaxException("')' is not found");

            queue.offer(o);
        }

        return queue;
    }

}
