import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller.setStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("view/view.fxml"));
        primaryStage.setTitle("Apply filter v1.0");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(370);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
