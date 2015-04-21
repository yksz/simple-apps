package weather.widget;

import java.util.Objects;

import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

class PreferencesDialog extends Dialog {

    private final WeatherWidget widget;
    private final Preferences prefs;

    public PreferencesDialog(Stage owner, WeatherWidget widget, Preferences prefs) {
        super(owner, 270, 180);
        this.widget = Objects.requireNonNull(widget, "widget must not be null");
        this.prefs = Objects.requireNonNull(prefs, "prefs must not be null");
        initStyle(StageStyle.TRANSPARENT);

        Accordion accordion = new Accordion();
        TitledPane providerPane = createProviderPane();
        TitledPane locationPane = createLocationPane();
        accordion.getPanes().addAll(providerPane, locationPane);
        accordion.setExpandedPane(locationPane);
        getScene().setRoot(accordion);
    }

    private TitledPane createProviderPane() {
        Label providedByLabel = new Label(" Provided by: ");
        providedByLabel.setAlignment(Pos.CENTER_LEFT);

        ComboBox<Provider> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(Provider.values());
        comboBox.setValue(prefs.getProvider());
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            prefs.setProvider(newValue);
        });

        Button okButton = new Button("OK");
        okButton.setOnAction(event -> {
            hide();
            widget.updateForecast();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> hide());

        HBox hbox = new HBox(10, okButton, cancelButton);
        hbox.setAlignment(Pos.CENTER_LEFT);

        VBox vbox = new VBox(5, providedByLabel, comboBox, hbox);
        vbox.setAlignment(Pos.CENTER_LEFT);

        return new TitledPane("Provider", vbox);
    }

    private TitledPane createLocationPane() {
        Label locationLabel = new Label(" Location: " + prefs.getLocation());
        locationLabel.setAlignment(Pos.CENTER_LEFT);

        TextField textField = new TextField();
        textField.setPromptText("Enter the location where you live");

        Button okButton = new Button("OK");
        okButton.setOnAction(event -> {
            String text = textField.getText();
            if (!text.isEmpty()) {
                textField.setText("");
                locationLabel.setText(" Location: " + text);
                prefs.setLocation(text);
            }
            hide();
            widget.updateForecast();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            textField.setText("");
            hide();
        });

        HBox hbox = new HBox(10, okButton, cancelButton);
        hbox.setAlignment(Pos.CENTER_LEFT);

        VBox vbox = new VBox(5, locationLabel, textField, hbox);
        vbox.setAlignment(Pos.CENTER_LEFT);

        return new TitledPane("Location", vbox);
    }

}
