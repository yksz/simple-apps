package weather.widget.dialog.preferences;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import weather.widget.WeatherWidget;
import weather.widget.attribute.Provider;
import weather.widget.dialog.Dialog;

public class PreferencesDialog extends Dialog {

    private final Preferences prefs;

    public PreferencesDialog(Stage owner, Preferences prefs) {
        super(owner, 240, 120);

        if (prefs == null)
            throw new NullPointerException("prefs");
        if ((owner instanceof WeatherWidget) == false)
            throw new IllegalArgumentException("owner must be instance of WeatherWidget");
        this.prefs  = prefs;

        this.initStyle(StageStyle.TRANSPARENT);

        TitledPane providerPane = createProviderPane();
        TitledPane createLocationPane = createLocationPane();

        Accordion accordion = new Accordion();
        accordion.getPanes().addAll(providerPane, createLocationPane);
        accordion.setExpandedPane(createLocationPane);

        scene.setFill(Color.rgb(255, 255, 255, 0));
        scene.setRoot(accordion);
    }

    private TitledPane createProviderPane() {
        ComboBox<Provider> comboBox = new ComboBox<Provider>();
        comboBox.getItems().addAll(Provider.values());
        comboBox.setValue(prefs.getProvider());
        comboBox.valueProperty().addListener(new ChangeListener<Provider>() {
            @Override
            public void changed(ObservableValue<? extends Provider> observable,
                    Provider oldValue, Provider newValue) {
                prefs.setProvider(newValue);
            }
        });

        Button button = new Button("OK");
        button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                hide();
                ((WeatherWidget) owner).updateForecast();
            }
        });

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(comboBox, button);

        Label providedByLabel = new Label(" Provided by: ");
        providedByLabel.setAlignment(Pos.CENTER_LEFT);

        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll(providedByLabel, hbox);

        return new TitledPane("Provider", vbox);
    }

    private TitledPane createLocationPane() {
        Label locationLabel = new Label(" Location: " + prefs.getLocation());
        locationLabel.setAlignment(Pos.CENTER_LEFT);

        TextField textField = new TextField();
        textField.setPromptText("Enter the location where you live");

        Button okButton = new Button("OK");
        okButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if (textField.getText().length() != 0) {
                    String text = textField.getText();
                    textField.setText("");
                    locationLabel.setText(" Location: " + text);
                    prefs.setLocation(text);
                }
                hide();
                ((WeatherWidget) owner).updateForecast();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                textField.setText("");
                hide();
            }
        });

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(okButton, cancelButton);

        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll(locationLabel, textField, hbox);

        return new TitledPane("Location", vbox);
    }

}
