import java.awt.AWTException;
import java.io.IOException;

import launcher.Launcher;

public class Main {

    public static void main(String[] args) {
        try {
            new Launcher().start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

}
