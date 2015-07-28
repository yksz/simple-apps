package calculator.ui.model;

import java.math.BigDecimal;

class Answer {

    private String str;

    public Answer(BigDecimal num) {
        if (num == null)
            this.str = "";
        else
            this.str = deleteDotZero(num.toString());
    }

    public Answer(String string) {
        this.str = string;
    }

    public String toString() {
        return str;
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
