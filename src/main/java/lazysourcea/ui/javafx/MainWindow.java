package lazysourcea.ui.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;

import lazysourcea.lazysourcea;

public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private lazysourcea core;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setCore(lazysourcea core) {
        this.core = core;
        dialogContainer.getChildren().add(
                DialogBox.getDukeDialog(core.getWelcomeMessage(), dukeImage)
        );
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String response = core.getResponse(input);

        var user = DialogBox.getUserDialog(input, userImage);
        var bot  = DialogBox.getDukeDialog(response, dukeImage);

        if (input.trim().toLowerCase().startsWith("help")) {
            bot.withMonospace();
        }

        dialogContainer.getChildren().addAll(user, bot);

        userInput.clear();
        if (core.isExit()) {
            PauseTransition delay = new PauseTransition(Duration.millis(250));
            delay.setOnFinished(e -> Platform.exit());  // or: stage.close() if you prefer
            delay.play();
        }
    }
}
