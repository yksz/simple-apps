import widget.WeatherWidget;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(Main.class, args);
    }

    @Override
    public void start(Stage dummy) throws Exception {
        dummy.initStyle(StageStyle.UTILITY);
        dummy.setOpacity(0);
        dummy.setWidth(1);
        dummy.setHeight(1);

        WeatherWidget widget = new WeatherWidget(dummy);
        dummy.show();
        widget.show();
        widget.toBack();
    }

}
