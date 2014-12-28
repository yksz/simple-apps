package weather.widget.dialog;

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
import weather.widget.WeatherWidget;
import weather.widget.attribute.Preferences;
import weather.widget.attribute.Provider;

public class PreferencesDialog extends Dialog {

    private final WeatherWidget widget;
    private final Preferences prefs;

    public PreferencesDialog(Stage owner, WeatherWidget widget, Preferences prefs) {
        super(owner, 270, 180);

        if (widget == null)
            throw new NullPointerException("widget must not be null");
        if (prefs == null)
            throw new NullPointerException("prefs must not be null");
        this.widget = widget;
        this.prefs  = prefs;

        this.initStyle(StageStyle.TRANSPARENT);

        TitledPane providerPane = createProviderPane();
        TitledPane locationPane = createLocationPane();

        Accordion accordion = new Accordion();
        accordion.getPanes().addAll(providerPane, locationPane);
        accordion.setExpandedPane(locationPane);

        scene.setRoot(accordion);
    }

    private TitledPane createProviderPane() {
        Label providedByLabel = new Label(" Provided by: ");
        providedByLabel.setAlignment(Pos.CENTER_LEFT);

        ComboBox<Provider> comboBox = new ComboBox<Provider>();
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
        cancelButton.setOnAction(event -> {
            hide();
        });

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(okButton, cancelButton);

        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll(providedByLabel, comboBox, hbox);

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

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(okButton, cancelButton);

        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll(locationLabel, textField, hbox);

        return new TitledPane("Location", vbox);
    }

}
