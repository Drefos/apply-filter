package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;

public class Controller {

    @FXML
    private VBox filters;

    @FXML
    private MenuItem openMenuItem;

    @FXML
    private MenuItem saveAsMenuItem;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private ImageView imageView;

    @FXML
    private RadioButton inversionRadioButton;

    @FXML
    private ToggleGroup function;

    @FXML
    private RadioButton brightnessRadioButton;

    @FXML
    private RadioButton contrastRadioButton;

    @FXML
    private RadioButton gammaRadioButton;

    @FXML
    private Button functionFilterButton;

    @FXML
    private RadioButton blurRadioButton;

    @FXML
    private ToggleGroup convolution;

    @FXML
    private RadioButton gaussianRadioButton;

    @FXML
    private RadioButton sharpenRadioButton;

    @FXML
    private RadioButton edgeRadioButton;

    @FXML
    private RadioButton embossRadioButton;

    @FXML
    private Button convolutionFilterButton;

    private static Stage stage;

    public static void setStage(Stage s) {
        stage = s;
    }

    @FXML
    public void initialize() {
        configMenu();
    }

    private void configMenu() {
        openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Load image");
                fileChooser.getExtensionFilters().addAll(
                        new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
                        new ExtensionFilter("All Files", "*.*"));
                File selectedFile = fileChooser.showOpenDialog(null);
                if (selectedFile != null) {
                    Image image = new Image(selectedFile.toURI().toString());
                    imageView.setFitHeight(image.getHeight());
                    imageView.setFitWidth(image.getWidth());
                    imageView.setImage(image);
                    saveAsMenuItem.setDisable(false);
                    filters.setDisable(false);
                }
            }
        });

        saveAsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            }
        });

        closeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            }
        });
    }

}
