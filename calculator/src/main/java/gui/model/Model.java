package gui.model;

public class Model {

    private final Expression expression;
    private final Answer answer;

    public Model() {
        expression = new Expression();
        answer = new Answer();
    }

    public Expression getExpression() {
        return expression;
    }

    public Answer getAnswer() {
        return answer;
    }

}
