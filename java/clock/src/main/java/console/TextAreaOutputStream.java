package console;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javafx.scene.control.TextArea;

class TextAreaOutputStream extends OutputStream {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final TextArea textArea;

    public TextAreaOutputStream(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void flush() {
        textArea.appendText(out.toString());
        out.reset();
    }

}
