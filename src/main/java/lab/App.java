package lab;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private static final double CANVAS_WIDTH = 672;
	private static final double CANVAS_HEIGHT = 768;

	private GameController gameController;

	@Override
	public void start(Stage primaryStage) {
		try {
			Group root = new Group();
			Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
			root.getChildren().add(canvas);
			Scene scene = new Scene(root, CANVAS_WIDTH, CANVAS_HEIGHT);

			gameController = new GameController(canvas);
			gameController.initializeControlHandlers(scene);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Donkey Kong");
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(this::exitProgram);
			primaryStage.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
				if (!isFocused) {
					gameController.clearInput();
				}
			});
			primaryStage.show();

			gameController.startGame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void exitProgram(WindowEvent event) {
		gameController.stopGame();
		System.exit(0);
	}
}
