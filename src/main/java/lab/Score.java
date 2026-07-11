package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Score extends GameEntity {
    private static final double HEART_SIZE = 24;
    private static final double HEART_GAP = 4;

    private int tileSize = 20;
    private final Image heartImage = new Image(getClass().getResourceAsStream("health.png"),
            HEART_SIZE, HEART_SIZE, true, true);

    public Score(Game game, Point2D position) {
        super(game, position);
    }

    public void printGameOver(GraphicsContext gc) {
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 60.0));
        gc.setFill(Color.RED);
        gc.fillText("Game over", this.game.getWidth() / 2.0 - 150.0, this.game.getHeight() / 2.0);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 30.0));
        if (game.getMario().getLives() == 0) {
            gc.fillText("You lost!", this.game.getWidth() / 2.0 - 75.0, this.game.getHeight() / 2.0 + 30.0);
        } else {
            gc.fillText("You won!", this.game.getWidth() / 2.0 - 75.0, this.game.getHeight() / 2.0 + 30.0);
        }
        gc.fillText("Score: " + String.valueOf(game.getMario().getScore()), this.game.getWidth() / 2.0 - 75.0, this.game.getHeight() / 2.0 + 60.0);
    }

    @Override
    public void simulate(double timeDelta) {

    }

    @Override
    protected void drawInternal(GraphicsContext gc) {
        gc.setFill(Color.LIGHTGRAY);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, this.tileSize * 2.0));
        gc.fillText("Score " + String.valueOf(game.getMario().getScore()), 0.0, this.tileSize * 2.5);

        for (int i = 0; i < game.getMario().getLives(); i++) {
            gc.drawImage(heartImage, i * (HEART_SIZE + HEART_GAP), this.tileSize * 3, HEART_SIZE, HEART_SIZE);
        }

        if (game.getIsGameOver()) {
            printGameOver(gc);
        }
    }
}
