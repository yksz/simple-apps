package weather.widget.dialog;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Dialog extends Stage {

    private static final int TRANSLATE_X = 20;
    private static final int TRANSLATE_Y = 20;

    protected final Scene scene;

    protected final Stage owner;
    protected final int width, height;

    public Dialog(Stage owner, int width, int height) {
        if (owner == null)
            throw new NullPointerException("owner must not be null");

        this.owner = owner;
        this.width = width;
        this.height = height;

        this.initModality(Modality.WINDOW_MODAL);
        this.initOwner(owner);

        Group root = new Group();
        scene = new Scene(root, width, height);
        this.setScene(scene);
    }

    public void setLocationRelativeToScreen() {
        this.requestFocus();

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        double dx = owner.getX() + TRANSLATE_X;
        double dy = owner.getY() + TRANSLATE_Y;
        // bottom
        if (dy + height > bounds.getMaxY())
            dy = bounds.getMaxY() - height;
        // top
        if (dy < bounds.getMinY())
            dy = bounds.getMinY();
        // right
        if (dx + width > bounds.getMaxX())
            dx = bounds.getMaxX() - width;
        // left
        if (dx < bounds.getMinX())
            dx = bounds.getMinX();
        this.setX(dx);
        this.setY(dy);
    }

}
