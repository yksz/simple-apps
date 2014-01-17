import java.awt.AWTException;
import java.io.IOException;

import gui.Launcher;

public class Main {

    public static void main(String[] args) {
        Launcher launcher;
        try {
            launcher = new Launcher();
            launcher.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

}
