package lab;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.util.Random;

public class Barrel extends WalkingEnemy {
    private static final double LADDER_DESCENT_SPEED = 150;
    // ladder levels below the spawn row on this map
    private static final int MAX_DESCENTS = 4;
    private static final int INITIAL_DESCENT_CHANCE_PERCENT = 90;
    // landing this much lower counts as dropping a row
    private static final double LEVEL_DROP_TOLERANCE = 30;
    // blocks the drop turn right after a wall bounce
    private static final double WALL_TURN_GRACE = 1.0;

    private final Random random = new Random();
    private boolean shouldBeRemoved = false;
    private boolean burned = false;
    private double rollVx;
    private boolean wasAtLadder = false;
    private int descentsDone = 0;
    private double lastGroundY = Double.NaN;
    private double reversalBlockTimer = 0;

    public Barrel(Game game, Point2D position, double width, double height, Point2D velocity) {
        super(game, position, width, height, velocity);
        this.rollVx = velocity.getX();
        this.imageR = new Image(getClass().getResourceAsStream("common_barrel_right.gif"), width, height, true, true);
        this.imageL = new Image(getClass().getResourceAsStream("common_barrel_left.gif"), width, height, true, true);
    }

    @Override
    public void simulate(double timeDelta) {
        if (!isClimbing() && onPlatform) {
            considerDescending();
        }
        super.simulate(timeDelta);

        if (reversalBlockTimer > 0) {
            reversalBlockTimer -= timeDelta;
        }
        if (position.getX() < 50 && rollVx < 0
                || position.getX() > game.getWidth() - width - 50 && rollVx > 0) {
            rollVx = -rollVx;
            velocity = new Point2D(rollVx, velocity.getY());
            reversalBlockTimer = WALL_TURN_GRACE;
        }

        if (position.getY() > game.getHeight()) {
            shouldBeRemoved = true;
        }
    }

    // roll once per ladder: skip it or go down
    private void considerDescending() {
        boolean atLadder = canGrabLadderBelow();
        if (atLadder && !wasAtLadder && random.nextInt(100) < descentChancePercent()) {
            descentsDone++;
            startClimbing();
            velocity = new Point2D(0, LADDER_DESCENT_SPEED);
        }
        wasAtLadder = atLadder;
    }

    private int descentChancePercent() {
        if (descentsDone >= MAX_DESCENTS) {
            return 0;
        }
        return INITIAL_DESCENT_CHANCE_PERCENT * (MAX_DESCENTS - descentsDone) / MAX_DESCENTS;
    }

    @Override
    public void hitBy(Collisionable another) {
        super.hitBy(another);
        // don't move this to simulate, onPlatform flickers there
        if (another instanceof Platform && onPlatform && !onLadder) {
            if (!Double.isNaN(lastGroundY) && position.getY() > lastGroundY + LEVEL_DROP_TOLERANCE
                    && reversalBlockTimer <= 0) {
                rollVx = -rollVx;
            }
            lastGroundY = position.getY();
            velocity = new Point2D(rollVx, velocity.getY());
        }
        if (another instanceof FireBarrel) {
            burned = true;
            shouldBeRemoved = true;
        }
    }

    public boolean shouldBeRemoved() {
        return shouldBeRemoved;
    }

    public boolean isBurned() {
        return burned;
    }
}
