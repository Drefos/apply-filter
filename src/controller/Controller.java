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
import model.Filter;
import model.FilterDB;
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
    private MenuItem reverseMenuItem;

    @FXML
    private ImageView imageView;

    @FXML
    private ChoiceBox<String> functionFilterChoiceBox;

    @FXML
    private Button functionFilterButton;

    @FXML
    private ChoiceBox<String> convolutionFilterChoiceBox;

    @FXML
    private Button convolutionFilterButton;

    private boolean isSaved = true;
    private static Stage stage;
    private Image savedImg;
    private FilterDB filtersDB;

    public static void setStage(Stage s) {
        stage = s;
    }

    @FXML
    public void initialize() {
        preConfig();
        configMenu();
        configFilters();
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
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    Image image = new Image(selectedFile.toURI().toString());
                    imageView.setFitHeight(image.getHeight());
                    imageView.setFitWidth(image.getWidth());
                    imageView.setImage(image);
                    saveAsMenuItem.setDisable(false);
                    filters.setDisable(false);
                    savedImg = image;
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

        reverseMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                reverseChanges();
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
        File f = imageSaver.showSaveDialog(stage);
        if (f != null) {
            try {
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
                String extension = f.getName().substring(1 + f.getName().lastIndexOf(".")).toLowerCase();
                ImageIO.write(renderedImage, extension, f);
                isSaved = true;
                savedImg = imageView.getImage();
                reverseMenuItem.setDisable(true);
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

    private void reverseChanges() {
        imageView.setImage(savedImg);
        isSaved = true;
        reverseMenuItem.setDisable(true);
    }

    private void configFilters() {
        functionFilterChoiceBox.getItems().addAll(AbstractFunctionFilter.getFiltersNames());
        functionFilterChoiceBox.setValue(AbstractFunctionFilter.INVERSE);
        convolutionFilterChoiceBox.getItems().addAll( ConvolutionFilter.getFiltersNames());
        convolutionFilterChoiceBox.setValue(ConvolutionFilter.BLUR);
        createFiltersDB();
        clickHandling(functionFilterButton, functionFilterChoiceBox);
        clickHandling(convolutionFilterButton, convolutionFilterChoiceBox);
    }

    private void createFiltersDB() {
        filtersDB = new FilterDB();
        filtersDB.addFilter(AbstractFunctionFilter.INVERSE, new InverseFunctionFilter());
        filtersDB.addFilter(AbstractFunctionFilter.BRIGHTNESS, new BrightnessFunctionFilter());
        filtersDB.addFilter(AbstractFunctionFilter.CONTRAST, new ContrastFunctionFilter());
        filtersDB.addFilter(AbstractFunctionFilter.GAMMA, new GammaFunctionFilter());

        filtersDB.addFilter(ConvolutionFilter.BLUR, new ConvolutionFilter(Kernel.getBlurKernel(), 0, 9));
        filtersDB.addFilter(ConvolutionFilter.GAUSSIAN, new ConvolutionFilter(Kernel.getGaussianBlurKernel(), 0, 8));
        filtersDB.addFilter(ConvolutionFilter.SHARPEN, new ConvolutionFilter(Kernel.getSharpenKernel(), 0, 1));
        filtersDB.addFilter(ConvolutionFilter.EDGE, new ConvolutionFilter(Kernel.getEdgeKernel(), 0.5, 1));
        filtersDB.addFilter(ConvolutionFilter.EMBOSS, new ConvolutionFilter(Kernel.getEmbossKernel(), 0, 1));
    }

    private void clickHandling(Button filterButton, ChoiceBox<String> filterChoiceBox) {
        filterButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Filter functionFilter = filtersDB.getByName(filterChoiceBox.getValue());
                imageView.setImage(functionFilter.filterImage(imageView.getImage()));
                isSaved = false;
                reverseMenuItem.setDisable(false);
            }
        });
    }
}
