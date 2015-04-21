package calculator;

import java.math.BigDecimal;
import java.util.Queue;
import java.util.Stack;

import calculator.lexer.Operator;
import calculator.parser.SyntaxException;
import calculator.parser.SyntaxParser;

public final class Calculator {

    private static final int SCALE = 16;

    private Calculator() {
    }

    public static BigDecimal calculate(String expression) throws SyntaxException {
        if (expression == null)
            throw new NullPointerException("statement must not be null");

        Stack<BigDecimal> stack = new Stack<BigDecimal>();
        Queue<Object> queue = new SyntaxParser().parse(expression);
        BigDecimal calc = null;
        for (Object o : queue) {
            if (o instanceof BigDecimal) {
                stack.push((BigDecimal) o);
                calc = (BigDecimal) o;
            } else if (o instanceof Operator) {
                if (stack.isEmpty())
                    throw new SyntaxException("There is no right-hand value");
                BigDecimal right = stack.pop();
                if (stack.isEmpty())
                    throw new SyntaxException("There is no left-hand value");
                BigDecimal left = stack.pop();
                calc = execute(left, right, (Operator) o);
                stack.push(calc);
            }
        }
        return calc;
    }

    private static BigDecimal execute(BigDecimal left, BigDecimal right,
            Operator op) throws SyntaxException {
        switch (op) {
        case ADD:
            return left.add(right);
        case SUB:
            return left.subtract(right);
        case MUL:
            return left.multiply(right);
        case DIV:
            return left.divide(right, SCALE, BigDecimal.ROUND_HALF_UP);
        case MOD:
            double mod = left.doubleValue() % right.doubleValue();
            return new BigDecimal(mod).setScale(left.scale(), BigDecimal.ROUND_HALF_UP);
        case POW:
            return new BigDecimal(Math.pow(left.doubleValue(), right.doubleValue()));
        default:
            throw new SyntaxException("Invalid operator: " + op);
        }
    }

}
