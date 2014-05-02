package calculator.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Lexer {

    public List<Token> analyze(String statement) {
        if (statement == null)
            throw new NullPointerException("statement must not be null");

        List<Token> list = new ArrayList<Token>();
        StringTokenizer stringTokenizer = new StringTokenizer(statement);
        while (stringTokenizer.hasMoreTokens()) {
            String text = stringTokenizer.nextToken();
            Tokenizer tokenizer = new Tokenizer(text);
            while (tokenizer.hasMoreTokens())
                list.add(tokenizer.nextToken());
        }
        return list;
    }

}
