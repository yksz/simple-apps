package calculator.ui.model;

import java.math.BigDecimal;

public class Answer {

    private String string;

    public Answer(BigDecimal num) {
        if (num == null)
            this.string = "";
        else
            this.string = deleteDotZero(num.toString());
    }

    public Answer(String string) {
        this.string = string;
    }

    public String toString() {
        return string;
    }

    private String deleteDotZero(String decimal) {
        if (decimal.indexOf('.') == -1)
            return decimal;

        StringBuilder sb = new StringBuilder(decimal);
        while (sb.length() > 1) {
            int lastIndex = sb.length() - 1;
            if (sb.charAt(lastIndex) == '0')
                sb.deleteCharAt(lastIndex);
            else
                break;
        }
        int lastIndex = sb.length() - 1;
        if (sb.charAt(lastIndex) == '.')
            sb.deleteCharAt(lastIndex);
        return sb.toString();
    }

}
