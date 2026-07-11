package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Score extends GameEntity {
    private double width;
    private double height;
    private int brink = 20;

    public Score(Game game, Point2D position, double width, double height) {
        super(game, position);
        this.width = width;
        this.height = height;
    }

    public void printGameOver(GraphicsContext gc) {
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 60.0));
        gc.setFill(Color.RED);
        gc.fillText("Game over", this.game.getWidth() / 2.0 - 150.0, this.game.getHeight() / 2.0);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 30.0));
        if (game.getMario().getLifes() == 0) {
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

        for(int gap = 1; this.brink * (double)gap <= this.height - 2.0 * this.brink; gap += 2) {
            gc.fillRect((this.width - this.brink) / 2.0, this.brink * (double)gap, this.brink, this.brink);
        }

        gc.setFont(Font.font("Nevim jaky font", FontWeight.BOLD, this.brink * 2.0));
        gc.fillText("Life: " + String.valueOf(game.getMario().getLifes()), 0.0, this.brink * 5.0);
        gc.setFont(Font.font("Nevim jaky font", FontWeight.BOLD, this.brink * 2.0));
        gc.fillText("Score " + String.valueOf(game.getMario().getScore()), 0.0, this.brink * 2.5);
        if (game.getIsGameOver()) {
            printGameOver(gc);
        }
    }
}
