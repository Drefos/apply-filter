package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.convolutionFilters.*;
import model.functionFilters.*;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

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

    private boolean isSaved = true;
    private static Stage stage;

    public static void setStage(Stage s) {
        stage = s;
    }

    @FXML
    public void initialize() {
        preConfig();
        configMenu();
        configFunctionFilters();
        configConvolutionFilters();
    }

    private void preConfig() {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                exit();
            }
        });
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
                saveImg();
            }
        });

        closeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exit();
            }
        });
    }

    private boolean saveImg() {
        FileChooser imageSaver = new FileChooser();
        imageSaver.setTitle("Save image");
        imageSaver.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("BMP Files", "*.bmp"),
                new FileChooser.ExtensionFilter("GIF Files", "*.gif"));
        File f = imageSaver.showSaveDialog(null);
        if (f != null) {
            try {
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
                String extension = f.getName().substring(1 + f.getName().lastIndexOf(".")).toLowerCase();
                ImageIO.write(renderedImage, extension, f);
                isSaved = true;
                return true;
            } catch (IOException ioe) {
                // Error with saving
                return false;
            }
        } else {
            // saving canceled
            return false;
        }
    }

    private void exit() {
        if (!isSaved) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Do you want to save changes before exiting?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setTitle("Save your work");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    if (saveImg()) System.exit(1);
                } else if (response == ButtonType.NO) System.exit(1);
            });
        } else {
            System.exit(1);
        }
    }

    private void configFunctionFilters() {
        functionFilterButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AbstractFunctionFilter functionFilter = new IdentityFunctionFilter();
                if (inversionRadioButton.isSelected()) {
                    functionFilter = new InverseFunctionFilter();
                } else if (brightnessRadioButton.isSelected()) {
                    functionFilter = new BrightnessFunctionFilter();
                } else if (contrastRadioButton.isSelected()) {
                    functionFilter = new ContrastFunctionFilter();
                } else if (gammaRadioButton.isSelected()) {
                    functionFilter = new GammaFunctionFilter();
                }
                imageView.setImage(functionFilter.filterImage(imageView.getImage()));
                isSaved = false;
            }
        });
    }

    private void configConvolutionFilters() {
        convolutionFilterButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AbstractConvolutionFilter convolutionFilter = new IdentityConvolutionFilter();
                if (blurRadioButton.isSelected()) {
                    convolutionFilter = new BlurConvolutionFilter();
                } else if (gaussianRadioButton.isSelected()) {
                    convolutionFilter = new GBlurConvolutionFilter();
                } else if (sharpenRadioButton.isSelected()) {
                    convolutionFilter = new SharpenConvolutionFilter();
                } else if (edgeRadioButton.isSelected()) {
                    convolutionFilter = new HEdgeDetectionConvolutionFilter();
                }
                imageView.setImage(convolutionFilter.filterImage(imageView.getImage()));
                isSaved = false;
            }
        });
    }
}
