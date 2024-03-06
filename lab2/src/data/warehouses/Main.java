package data.warehouses;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GeneratorUI.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Generator danych");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        GeneratorController controller = fxmlLoader.getController();
        addShortcutHandler(primaryStage, controller);
    }

    private void addShortcutHandler(Stage primaryStage, GeneratorController controller) {
        primaryStage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<>() {
            KeyCombination shortcut = new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN);

            public void handle(KeyEvent keyEvent) {
                if (shortcut.match(keyEvent)) {
                    controller.showSampleUpdate();
                    keyEvent.consume();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}