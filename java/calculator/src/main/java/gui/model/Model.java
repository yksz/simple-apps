package gui.model;

public class Model {

    private final Expression expression = new Expression();
    private Answer answer;

    public Expression getExpression() {
        return expression;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

}
