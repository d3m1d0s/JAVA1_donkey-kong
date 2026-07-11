package lab;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.util.Random;

public class Fireman extends WalkingEnemy {
    private static final double TURN_ROLL_INTERVAL = 0.7;
    private static final double CLIMB_UP_SPEED = 30;
    private static final double CLIMB_DOWN_SPEED = 80;
    private final Random random = new Random();
    private double timeSinceTurnRoll = 0;
    private double patrolVx;
    private boolean wasAtLadder = false;

    public Fireman(Game game, Point2D position, double width, double height, Point2D velocity) {
        super(game, position, width, height, velocity);
        this.patrolVx = velocity.getX();

        this.imageR = new Image(getClass().getResourceAsStream("fireman_right.gif"), width, height,
                true, true);
        this.imageL = new Image(getClass().getResourceAsStream("fireman_left.gif"), width, height,
                true, true);
    }

    @Override
    public void simulate(double timeDelta) {
        if (!isClimbing() && onPlatform) {
            considerClimbing();
        }
        super.simulate(timeDelta);
        if (isClimbing()) {
            return;
        }

        if (velocity.getX() == 0) {
            velocity = new Point2D(patrolVx, velocity.getY()); // resume patrol after a climb
        } else {
            patrolVx = velocity.getX();
        }

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

    // one 50% roll per ladder encounter, no reversal once on the ladder
    private void considerClimbing() {
        boolean canUp = canGrabLadderAbove();
        boolean canDown = canGrabLadderBelow() && marioIsBelow();
        boolean atLadder = canUp || canDown;
        if (atLadder && !wasAtLadder && random.nextBoolean()) {
            if (canUp && canDown && random.nextBoolean()) {
                canUp = false;
            }
            startClimbing();
            velocity = new Point2D(0, canUp ? -CLIMB_UP_SPEED : CLIMB_DOWN_SPEED);
        }
        wasAtLadder = atLadder;
    }

    // only lets a fireball descend a ladder when Mario is below it
    private boolean marioIsBelow() {
        return game.getMario().getPosition().getY() > position.getY() + height;
    }
}
