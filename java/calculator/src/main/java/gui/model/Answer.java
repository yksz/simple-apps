package gui.model;

public class Answer {

    private String str = "";

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void clear() {
        str = "";
    }

    public void deleteDotZero() {
        if (str.indexOf('.') == -1)
            return;

        StringBuilder sb = new StringBuilder(str);
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

        str = sb.toString();
    }

}
