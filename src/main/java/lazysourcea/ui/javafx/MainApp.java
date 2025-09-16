package lazysourcea.ui.javafx;


import javafx.scene.image.Image;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lazysourcea.lazysourcea;

public class MainApp extends Application {
    private lazysourcea core;

    @Override
    public void start(Stage stage) {
        try {
            core = new lazysourcea("data", "lazysourcea.txt");
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = loader.load();
            loader.<MainWindow>getController().setCore(core);

            Scene scene = new Scene(root);
            stage.setScene(scene);

            // set window icon (resource path from src/main/resources)
            Image icon = new Image(MainApp.class.getResourceAsStream("/images/app_icon.png"));
            stage.getIcons().add(icon);

            stage.setMinWidth(1000);
            stage.setMinHeight(800);
            stage.setWidth(1000);
            stage.setHeight(800);
            stage.centerOnScreen();

            stage.setTitle("lazysourcea");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
