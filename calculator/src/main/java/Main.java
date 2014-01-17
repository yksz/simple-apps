import gui.model.Model;
import gui.view.View;

public class Main {

    public static void main(String args[]) {
        Model model = new Model();
        View view = new View(model);
        view.setVisible(true);
    }

}
