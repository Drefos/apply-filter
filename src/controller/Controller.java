package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    }

}
