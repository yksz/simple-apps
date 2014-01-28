package widget.dialog;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Dialog extends Stage {

    private static final int TRANSLATE_X = 20;
    private static final int TRANSLATE_Y = 20;

    protected final Scene _scene;

    protected final Stage _owner;
    protected final int _width, _height;

    public Dialog(Stage owner, int width, int height) {
        if (owner == null)
            throw new NullPointerException("owner must not be null");

        _owner = owner;
        _width = width;
        _height = height;

        this.initModality(Modality.WINDOW_MODAL);
        this.initOwner(owner);

        Group root = new Group();
        _scene = new Scene(root, _width, _height);
        this.setScene(_scene);
    }

    public void setLocationRelativeToScreen() {
        this.requestFocus();

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        double dx = _owner.getX() + TRANSLATE_X;
        double dy = _owner.getY() + TRANSLATE_Y;

        // bottom
        if (dy + _height > bounds.getMaxY())
            dy = bounds.getMaxY() - _height;

        // top
        if (dy < bounds.getMinY())
            dy = bounds.getMinY();

        // right
        if (dx + _width > bounds.getMaxX())
            dx = bounds.getMaxX() - _width;

        // left
        if (dx < bounds.getMinX())
            dx = bounds.getMinX();

        this.setX(dx);
        this.setY(dy);
    }

}
