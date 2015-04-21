package weather.widget;

import java.util.Objects;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

class Dialog extends Stage {

    private static final int TRANSLATE_X = 20;
    private static final int TRANSLATE_Y = 20;

    protected final Stage owner;
    protected final int width, height;

    public Dialog(Stage owner, int width, int height) {
        this.owner = Objects.requireNonNull(owner, "owner must not be null");
        this.width = width;
        this.height = height;
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);
        setScene(new Scene(new Group(), width, height));
    }

    public void setLocationRelativeToScreen() {
        requestFocus();

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
        setX(dx);
        setY(dy);
    }

}
