package calculator.lexer;

public enum Operator {
    UNKNOWN ("unknown", 0, null),

    OPEN_PARENTHESIS  ("(", 0, null),
    CLOSE_PARENTHESIS (")", 0, null),

    ASSIGN ("=", 1, Associativity.RIGHT_TO_LEFT),
    ADD    ("+", 2, Associativity.LEFT_TO_RIGHT),
    SUB    ("-", 2, Associativity.LEFT_TO_RIGHT),
    MUL    ("*", 3, Associativity.LEFT_TO_RIGHT),
    DIV    ("/", 3, Associativity.LEFT_TO_RIGHT),
    MOD    ("%", 3, Associativity.LEFT_TO_RIGHT),
    POW    ("^", 4, Associativity.LEFT_TO_RIGHT),
    ;

    public enum Associativity {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT,
    }

    private static final Operator[] OPERATOR = Operator.values();
    private final String symbol;
    private final int precedence;
    private final Associativity associativity;

    private Operator(String symbol, int precedence, Associativity associativity) {
        this.symbol = symbol;
        this.precedence = precedence;
        this.associativity = associativity;
    }

    public String getName() {
        return symbol;
    }

    public int getPrecedence() {
        return precedence;
    }

    public Associativity getAssociativity() {
        return associativity;
    }

    public static Operator getBySymbol(String symbol) {
        for (Operator operator : OPERATOR)
            if (operator.getName().equals(symbol))
                return operator;
        return Operator.UNKNOWN;
    }
}
