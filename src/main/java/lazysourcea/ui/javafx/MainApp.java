package lazysourcea.ui.javafx;

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
            stage.setScene(new Scene(root));
            stage.setTitle("lazysourcea");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
