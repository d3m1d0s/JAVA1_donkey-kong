package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Mario extends WalkingEnemy {
    private boolean lostTheLife = false;
    private boolean savedThePrincess = false;
    private boolean jumpBonusPaid = true;
    private int score = 0;
    private int lifes = 3;
    private static final double IMMUNITY_TIME = 3.0; // 3 секунды бессмертия после потери жизни
    private double timeSinceHit = IMMUNITY_TIME; // starts expired so the first hit counts immediately
    // jump keeps working this long after the last ground contact
    private static final double JUMP_GRACE_TIME = 0.1;
    private double timeSinceGrounded = 1;
    private final Image imageUp;

    public Mario(Game game, Point2D position, double width, double height) {
        super(game, position, width, height, new Point2D(0, 0));
        this.imageR = new Image(getClass().getResourceAsStream("mario_go_right.gif"), width, height,
                true, true);
        this.imageL = new Image(getClass().getResourceAsStream("mario_go_left.gif"), width, height,
                true, true);
        this.imageUp = new Image(getClass().getResourceAsStream("mario_up.png"), width, height,
                true, true);
    }

    @Override
    protected void drawInternal(GraphicsContext gc) {
        if (isClimbing()) {
            if (imgR == 0) {
                // the climb sprite faces right, negative width mirrors it
                gc.drawImage(imageUp, position.getX() + width, position.getY(), -width, height);
            } else {
                gc.drawImage(imageUp, position.getX(), position.getY(), width, height);
            }
        } else {
            super.drawInternal(gc);
        }
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public void jump() {
        if (timeSinceGrounded <= JUMP_GRACE_TIME && !onLadder) {
            velocity = new Point2D(velocity.getX(), -90);
            onPlatform = false;
            jumpBonusPaid = false;
            timeSinceGrounded = 1;
        }
    }

    public boolean isJumpBonusPaid() {
        return jumpBonusPaid;
    }

    public void markJumpBonusPaid() {
        jumpBonusPaid = true;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public int getLifes() {
        return lifes;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public boolean getSavedThePrincess() {
        return savedThePrincess;
    }

    @Override
    public void simulate(double timeDelta) {
        timeSinceHit += timeDelta;
        timeSinceGrounded += timeDelta;
        if (onPlatform) {
            timeSinceGrounded = 0;
        }

        if (this.lostTheLife && timeSinceHit >= IMMUNITY_TIME) {
            timeSinceHit = 0;
            this.lostTheLife = false;
            this.lifes--;

            if (this.lifes > 0) {
                setPosition(new Point2D(100, 670));
            }
        } else if (this.lostTheLife) {
            this.lostTheLife = false; // ignore hits taken during the immunity window
        }

        super.simulate(timeDelta);
        if (
                (position.getX() + width < 0) || (position.getX() > game.getWidth()) ||
                (position.getY() < -50) || (position.getY() > game.getHeight())
        ) {
            lostTheLife = true;
        }
    }

    @Override
    public Rectangle2D getBoundingBox2() {
        return new Rectangle2D(position.getX(), position.getY(), width, height);
    }

    @Override
    public void hitBy(Collisionable another) {
        super.hitBy(another);
        if (another instanceof MovingEntity || another instanceof DonkeyKong || another instanceof FireBarrel) {
            lostTheLife = true;
        }
        if (another instanceof Princess && !savedThePrincess) {
            score+=1000;
            savedThePrincess = true;
        }
    }
}
