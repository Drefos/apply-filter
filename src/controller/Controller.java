package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Filter;
import model.FilterDB;
import model.MedianFilter;
import model.convolutionFilters.ConvolutionFilter;
import model.convolutionFilters.Kernel;
import model.functionFilters.*;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @FXML
    private ChoiceBox<String> otherFilterChoiceBox;

    @FXML
    private Button otherFilterButton;

    @FXML
    private VBox filterEditingVBox;

    @FXML
    private TextField fNameTextField;

    @FXML
    private ChoiceBox<Integer> kerColChoiceBox;

    @FXML
    private ChoiceBox<Integer> kerRowChoiceBox;

    @FXML
    private ChoiceBox<Integer> anchorRowChoiceBox;

    @FXML
    private ChoiceBox<Integer> anchorColChoiceBox;

    @FXML
    private Spinner<Double> offsetSpinner;

    @FXML
    private Spinner<Integer> divisorSpinner;

    @FXML
    private CheckBox divisorCheckBox;

    @FXML
    private VBox kernelVBox;

    @FXML
    private Button saveFilterButton;

    private boolean isSaved = true;
    private static Stage stage;
    private Image savedImg;
    private FilterDB filtersDB;
    private List<HBox> kernelGrid;
    private TextField[][] kernelFields;
    private boolean anchorChanging = false;

    public static void setStage(Stage s) {
        stage = s;
    }

    @FXML
    public void initialize() {
        preConfig();
        configMenu();
        configFilters();
        configFilterEditing();
        configFilterSaving();
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
                    if (!filterEditingVBox.isVisible()) filterEditingVBox.setVisible(true);
                    loadFilterToEdit();
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
        convolutionFilterChoiceBox.getItems().addAll(ConvolutionFilter.getFiltersNames());
        convolutionFilterChoiceBox.setValue(ConvolutionFilter.BLUR);
        convolutionFilterChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadFilterToEdit();
            }
        });
        otherFilterChoiceBox.getItems().add(MedianFilter.NAME);
        otherFilterChoiceBox.setValue(MedianFilter.NAME);
        createFiltersDB();
        clickHandling(functionFilterButton, functionFilterChoiceBox);
        clickHandling(convolutionFilterButton, convolutionFilterChoiceBox);
        clickHandling(otherFilterButton, otherFilterChoiceBox);
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

        filtersDB.addFilter(MedianFilter.NAME, new MedianFilter());
    }

    private void clickHandling(Button filterButton, ChoiceBox<String> filterChoiceBox) {
        filterButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                filterEditingVBox.setDisable(true);
                kernelVBox.setDisable(true);
                Filter functionFilter = filtersDB.getByName(filterChoiceBox.getValue());
                imageView.setImage(functionFilter.filterImage(imageView.getImage()));
                isSaved = false;
                reverseMenuItem.setDisable(false);
                filterEditingVBox.setDisable(false);
                kernelVBox.setDisable(false);
            }
        });
    }

    private void configFilterEditing() {
        configSpinners();
        configChoiceBoxes();
    }

    private void configSpinners() {
        offsetSpinner.valueFactoryProperty().setValue(new DoubleSpinnerValueFactory(-1.0, 1.0, 0.0, 0.05));
        offsetSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            makeCustom();
            if (!newValue) {
                offsetSpinner.increment(0);
            }
        });
        divisorSpinner.valueFactoryProperty().setValue(new IntegerSpinnerValueFactory(1, 100, 0, 1));
        divisorSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            makeCustom();
            if (!newValue) {
                divisorSpinner.increment(0);
            }
        });
        divisorCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                divisorSpinner.setDisable(divisorCheckBox.isSelected());
                if(divisorCheckBox.isSelected()) {
                    int sum=0;
                    for (int i = 0; i < kerRowChoiceBox.getValue(); i++)
                        for (int j = 0; j < kerColChoiceBox.getValue(); j++) {
                            sum += Integer.parseInt((kernelFields[i][j].getText().equals("-") || kernelFields[i][j].getText().isEmpty() ?"0":kernelFields[i][j].getText()));
                        }
                    divisorSpinner.valueFactoryProperty().get().setValue(sum);
                }
            }
        });
    }

    private void configChoiceBoxes() {
        kerColChoiceBox.getItems().addAll(1, 3, 5, 7, 9);
        kerColChoiceBox.setValue(1);
        kerColChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                makeCustom();
                generateKernelGrid();
                anchorChanging = true;
                anchorColChoiceBox.getItems().clear();
                anchorColChoiceBox.getItems().addAll(IntStream.rangeClosed(0, kerColChoiceBox.getValue() - 1).boxed().collect(Collectors.toList()));
                anchorColChoiceBox.setValue(0);
                anchorChanging = false;
                showAnchor();
            }
        });
        kerRowChoiceBox.getItems().addAll(1, 3, 5, 7, 9);
        kerRowChoiceBox.setValue(1);
        kerRowChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                makeCustom();
                generateKernelGrid();
                anchorChanging = true;
                anchorRowChoiceBox.getItems().clear();
                anchorRowChoiceBox.getItems().addAll(IntStream.rangeClosed(0, kerRowChoiceBox.getValue() - 1).boxed().collect(Collectors.toList()));
                anchorRowChoiceBox.setValue(0);
                anchorChanging = false;
                showAnchor();
            }
        });
        anchorRowChoiceBox.getItems().addAll(IntStream.rangeClosed(0, 8).boxed().collect(Collectors.toList()));
        anchorRowChoiceBox.setValue(0);
        anchorRowChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!anchorChanging) {
                    makeCustom();
                    showAnchor();
                }
            }
        });
        anchorColChoiceBox.getItems().addAll(IntStream.rangeClosed(0, 8).boxed().collect(Collectors.toList()));
        anchorColChoiceBox.setValue(0);
        anchorColChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!anchorChanging) {
                    makeCustom();
                    showAnchor();
                }

            }
        });
    }

    private void showAnchor() {
        for (int i = 0; i < kerRowChoiceBox.getValue(); i++)
            for (int j = 0; j < kerColChoiceBox.getValue(); j++) {
                kernelFields[i][j].setStyle("-fx-control-inner-background: #fff");
            }

        kernelFields[anchorRowChoiceBox.getValue()][anchorColChoiceBox.getValue()].setStyle("-fx-control-inner-background: #bbffbb");
    }

    private void loadFilterToEdit() {
        ConvolutionFilter filter = (ConvolutionFilter) filtersDB.getByName(convolutionFilterChoiceBox.getValue());
        kerRowChoiceBox.setValue(filter.kernel.coefficients.length);
        kerColChoiceBox.setValue(filter.kernel.coefficients[0].length);
        anchorRowChoiceBox.setValue(filter.kernel.anchorY);
        anchorColChoiceBox.setValue(filter.kernel.anchorX);
        showAnchor();
        offsetSpinner.valueFactoryProperty().get().setValue(filter.off);
        divisorSpinner.valueFactoryProperty().get().setValue(Kernel.calculateDivisor(filter.kernel));
        fNameTextField.setText(convolutionFilterChoiceBox.getValue());
        for (int i = 0; i < kerRowChoiceBox.getValue(); i++) {
            for (int j = 0; j < kerColChoiceBox.getValue(); j++) {
                kernelFields[i][j].setText(String.valueOf(filter.kernel.coefficients[i][j]));
            }
        }
    }

    private void generateKernelGrid() {
        if (kernelGrid == null) {
            kernelGrid = new ArrayList<>();
        } else {
            kernelVBox.getChildren().removeAll(kernelGrid);
            kernelGrid.clear();
        }
        kernelFields = new TextField[kerRowChoiceBox.getValue()][kerColChoiceBox.getValue()];
        for (int i = 0; i < kerRowChoiceBox.getValue(); i++) {
            kernelGrid.add(new HBox());
            for (int j = 0; j < kerColChoiceBox.getValue(); j++) {
                TextField textField = new TextField();
                textField.setText("0");
                textField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                        if (!newValue.matches("[-]?\\d{0,7}")) {
                            textField.setText(oldValue);
                        }
                        if(divisorCheckBox.isSelected()) {
                            int sum=0;
                            for (int i = 0; i < kerRowChoiceBox.getValue(); i++)
                                for (int j = 0; j < kerColChoiceBox.getValue(); j++) {
                                    sum += Integer.parseInt((kernelFields[i][j].getText().equals("-") || kernelFields[i][j].getText().isEmpty() ?"0":kernelFields[i][j].getText()));
                                }
                            divisorSpinner.valueFactoryProperty().get().setValue(sum);
                        }
                    }
                });
                kernelFields[i][j] = textField;
                kernelGrid.get(i).getChildren().add(textField);
            }
        }
        kernelVBox.getChildren().addAll(kernelGrid);
    }

    private void makeCustom() {
        if (filtersDB.contains(fNameTextField.getText())) {
            int i = 0;
            String name;
            do {
                i++;
                name = "Custom" + i;
            } while (filtersDB.contains(name));
            fNameTextField.setText(name);
        }
    }

    private void configFilterSaving() {
        saveFilterButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (filtersDB.contains(fNameTextField.getText())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Filter with name '" + fNameTextField.getText() + "' already exists!", ButtonType.OK);
                    alert.setTitle("Filter name already exists");
                    alert.showAndWait();
                    return;
                }
                Kernel kernel = new Kernel();
                kernel.coefficients = new int[kerRowChoiceBox.getValue()][kerColChoiceBox.getValue()];
                for (int i = 0; i < kerRowChoiceBox.getValue(); i++)
                    for (int j = 0; j < kerColChoiceBox.getValue(); j++) {
                        kernel.coefficients[i][j] = Integer.parseInt((kernelFields[i][j].getText().equals("-") || kernelFields[i][j].getText().isEmpty() ?"0":kernelFields[i][j].getText()));
                    }
                kernel.anchorX = anchorColChoiceBox.getValue();
                kernel.anchorY = anchorRowChoiceBox.getValue();
                filtersDB.addFilter(fNameTextField.getText(), new ConvolutionFilter(kernel, offsetSpinner.getValue(), divisorSpinner.getValue()));
                convolutionFilterChoiceBox.getItems().add(fNameTextField.getText());
            }
        });
    }
}
