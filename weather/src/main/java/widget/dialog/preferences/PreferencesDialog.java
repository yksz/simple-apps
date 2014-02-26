package widget.dialog.preferences;

import widget.WeatherWidget;
import widget.attribute.Provider;
import widget.dialog.Dialog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TitledPaneBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

        TitledPane providerPane       = this.createProviderPane();
        TitledPane createLocationPane = this.createLocationPane();

        final Accordion accordion = new Accordion();
        accordion.getPanes().addAll(
                providerPane,
                createLocationPane
        );
        accordion.setExpandedPane(createLocationPane);

        scene.setFill(Color.rgb(255, 255, 255, 0));
        scene.setRoot(accordion);
    }

    private TitledPane createProviderPane() {
        final Label providedByLabel = LabelBuilder.create()
                .text(" Provided by: ")
                .alignment(Pos.CENTER_LEFT)
                .build();

        final ComboBox<Provider> comboBox = new ComboBox<Provider>();
        comboBox.getItems().addAll(
                Provider.values()
        );
        comboBox.setValue(prefs.getProvider());
        comboBox.valueProperty().addListener(new ChangeListener<Provider>() {
            @Override
            public void changed(ObservableValue<? extends Provider> observable,
                    Provider oldValue, Provider newValue) {
                prefs.setProvider(newValue);
            }
        });

        final Button button = ButtonBuilder.create()
                .text("OK")
                .onAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        hide();
                        ((WeatherWidget) owner).updateForecast();
                    }
                })
                .build();

        final HBox hbox = HBoxBuilder.create()
                .alignment(Pos.CENTER)
                .spacing(10)
                .build();
        hbox.getChildren().addAll(
                comboBox,
                button
        );

        final VBox vbox = VBoxBuilder.create()
                .alignment(Pos.CENTER_LEFT)
                .spacing(5)
                .build();
        vbox.getChildren().addAll(
                providedByLabel,
                hbox
        );

        return TitledPaneBuilder.create()
                .text("Provider")
                .content(vbox)
                .build();
    }

    private TitledPane createLocationPane() {
        final Label locationLabel = LabelBuilder.create()
                .text(" Location: " + prefs.getLocation())
                .alignment(Pos.CENTER_LEFT)
                .build();

        final TextField textField = TextFieldBuilder.create()
                .promptText("Enter the location where you live")
                .build();

        final HBox hbox = HBoxBuilder.create()
                .alignment(Pos.CENTER)
                .spacing(10)
                .build();
        hbox.getChildren().addAll(
                ButtonBuilder.create()
                .text("OK")
                .onAction(new EventHandler<ActionEvent>(){
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
                })
                .build()
                ,
                ButtonBuilder.create()
                .text("Cancel")
                .onAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        textField.setText("");
                        hide();
                    }
                })
                .build()

        );

        final VBox vbox = VBoxBuilder.create()
                .alignment(Pos.CENTER_LEFT)
                .spacing(5)
                .build();
        vbox.getChildren().addAll(
                locationLabel,
                textField,
                hbox
        );

        return TitledPaneBuilder.create()
                .text("Location")
                .content(vbox)
                .build();
    }

}
