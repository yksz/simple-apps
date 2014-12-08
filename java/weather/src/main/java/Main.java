import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import weather.widget.WeatherWidget;

public class Main extends Application {

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

    public static void main(String[] args) {
        launch(Main.class, args);
    }

}
