package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

public class Fireman extends WalkingEnemy {
    private static final double TURN_ROLL_INTERVAL = 0.7;
    private final Random random = new Random();
    private double timeSinceTurnRoll = 0;

    public Fireman(Game game, Point2D position, double width, double height, Point2D velocity) {
        super(game, position, width, height, velocity);

        this.imageR = new Image(getClass().getResourceAsStream("fireman_right.gif"), width, height,
                true, true);
        this.imageL = new Image(getClass().getResourceAsStream("fireman_left.gif"), width, height,
                true, true);
    }

    @Override
    public void simulate(double timeDelta) {
        if (onLadder) {
            // Логика движения по лестнице
            // Например, изменение position в зависимости от направления движения по лестнице
        } else {
            super.simulate(timeDelta);
            timeSinceTurnRoll += timeDelta;
            if (timeSinceTurnRoll >= TURN_ROLL_INTERVAL) {
                timeSinceTurnRoll = 0;
                if (random.nextBoolean()) {
                    velocity = new Point2D(-velocity.getX(), velocity.getY());
                }
            }
            if (position.getX() < 50 || position.getX() > game.getWidth() - width - 50) {
                velocity = new Point2D(-velocity.getX(), velocity.getY());
            }
        }

        // Дополнительная логика
        // ...
    }
}
