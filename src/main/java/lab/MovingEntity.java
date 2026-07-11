package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class MovingEntity extends GameEntity implements Collisionable {
    protected static final double GRAVITY = 200;
    private static final double LADDER_GRAB_MARGIN = 6;
    // max step height
    private static final double STEP_TOLERANCE = 10;
    protected Point2D velocity;
    protected double width;
    protected double height;
    protected boolean onPlatform = false;
    protected boolean onLadder = false;
    private double climbStartBottom;
    protected Image imageR;
    protected Image imageL;
    int imgR = 1;
    private double lastDrawnX;

    public MovingEntity(Game game, Point2D position, double width, double height, Point2D velocity) {
        super(game, position);
        this.velocity = velocity;
        this.width = width;
        this.height = height;
    }

    @Override
    public void simulate(double timeDelta) {
        if (onLadder) {
            double previousBottom = position.getY() + height;
            position = position.add(0, velocity.getY() * timeDelta);

            if (velocity.getY() > 0 && landOnPlatform(previousBottom)) {
                return;
            }
            if (!touchesAnyLadder()) {
                boolean wasDescending = velocity.getY() > 0;
                stopClimbing();
                velocity = new Point2D(velocity.getX(), 0);
                if (wasDescending) {
                    settleAfterDescent();
                }
            }
        } else {
            standardMovementLogic(timeDelta);
        }
    }

    // some ladders end a few px above the platform
    private void settleAfterDescent() {
        double bottom = position.getY() + height;
        for (Platform platform : game.getPlatforms()) {
            Rectangle2D box = platform.getBoundingBox();
            boolean overlapsX = position.getX() + width > box.getMinX() && position.getX() < box.getMaxX();
            if (overlapsX && Math.abs(box.getMinY() - bottom) <= 16) {
                position = new Point2D(position.getX(), box.getMinY() - height);
                onPlatform = true;
                return;
            }
        }
    }

    private boolean landOnPlatform(double previousBottom) {
        double bottom = position.getY() + height;
        for (Platform platform : game.getPlatforms()) {
            Rectangle2D box = platform.getBoundingBox();
            // don't land back on the row we started from
            boolean belowStart = box.getMinY() > climbStartBottom + STEP_TOLERANCE;
            boolean crossedTop = previousBottom < box.getMinY() && bottom > box.getMinY();
            boolean overlapsX = position.getX() + width > box.getMinX() && position.getX() < box.getMaxX();
            if (belowStart && crossedTop && overlapsX) {
                position = new Point2D(position.getX(), box.getMinY() - height);
                stopClimbing();
                velocity = new Point2D(velocity.getX(), 0);
                onPlatform = true;
                return true;
            }
        }
        return false;
    }

    protected void standardMovementLogic(double timeDelta) {
        if (onPlatform) {
            land();
        }
        if (!onPlatform) {
            fall(timeDelta);
        } else {
            Platform platformUnderEntity = null;
            for (Platform platform : game.getPlatforms()) {
                if (this.getBoundingBox().intersects(platform.getBoundingBox())) {
                    platformUnderEntity = platform;
                    break;
                }
            }

            if (platformUnderEntity != null) {
                double platformTopY = platformUnderEntity.getPosition().getY();
                double entityBottomY = position.getY() + height;

                if (Math.abs(entityBottomY - platformTopY) <= 5) {
                    position = new Point2D(position.getX(), platformTopY - height);
                } else {
                    // walked off the edge
                    onPlatform = false;
                }
            } else {
                onPlatform = false;
            }
        }

        position = position.add(velocity.multiply(timeDelta));
    }

    public void fall(double timeDelta) {
        if (onPlatform) {
            onPlatform = false;
        } else {
            velocity = velocity.add(0, GRAVITY * timeDelta);
        }
        position = position.add(0, velocity.getY() * timeDelta);
    }

    public void land() {
        if (velocity.getY() > 0) {
            velocity = new Point2D(velocity.getX(), 0);
            onPlatform = true;
        }
    }

    public void startClimbing() {
        onLadder = true;
        velocity = new Point2D(0, 0);
        climbStartBottom = position.getY() + height;
    }

    public void stopClimbing() {
        onLadder = false;
    }

    public boolean isClimbing() {
        return onLadder;
    }

    public boolean isOnPlatform() {
        return onPlatform;
    }

    public boolean canGrabLadderAbove() {
        Rectangle2D box = getBoundingBox();
        for (Ladder ladder : game.getLadders()) {
            Rectangle2D grab = grabBox(ladder);
            if (box.intersects(grab) && grab.getMinY() < box.getMinY()) {
                return true;
            }
        }
        return false;
    }

    public boolean canGrabLadderBelow() {
        Rectangle2D box = getBoundingBox();
        for (Ladder ladder : game.getLadders()) {
            Rectangle2D grab = grabBox(ladder);
            if (box.intersects(grab) && grab.getMaxY() > box.getMaxY()) {
                return true;
            }
        }
        return false;
    }

    private boolean touchesAnyLadder() {
        Rectangle2D box = getBoundingBox();
        for (Ladder ladder : game.getLadders()) {
            if (box.intersects(grabBox(ladder))) {
                return true;
            }
        }
        return false;
    }

    // ladders don't quite touch the platforms, so the grab zone sticks out on both ends
    private Rectangle2D grabBox(Ladder ladder) {
        Rectangle2D strip = ladder.getBoundingBox();
        return new Rectangle2D(strip.getMinX(), strip.getMinY() - LADDER_GRAB_MARGIN,
                strip.getWidth(), strip.getHeight() + 2 * LADDER_GRAB_MARGIN);
    }

    @Override
    protected void drawInternal(GraphicsContext gc) {
        double dx = position.getX() - lastDrawnX;
        lastDrawnX = position.getX();
        if (dx > 0) {
            imgR = 1;
        } else if (dx < 0) {
            imgR = 0;
        }
        gc.drawImage(imgR == 1 ? imageR : imageL, position.getX(), position.getY(), width, height);
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), width, height);
    }

    @Override
    public boolean intersects(Rectangle2D another) {
        return getBoundingBox().intersects(another);
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Platform platform && !onLadder) {
            resolvePlatformCollision(platform);
        }
    }

    private void resolvePlatformCollision(Platform platform) {
        Rectangle2D box = platform.getBoundingBox();
        double overlapTop = position.getY() + height - box.getMinY();
        double overlapBottom = box.getMaxY() - position.getY();
        double overlapLeft = position.getX() + width - box.getMinX();
        double overlapRight = box.getMaxX() - position.getX();

        if (overlapTop <= STEP_TOLERANCE) {
            position = new Point2D(position.getX(), box.getMinY() - height);
            if (velocity.getY() > 0) {
                velocity = new Point2D(velocity.getX(), 0);
            }
            onPlatform = true;
        } else if (overlapBottom <= STEP_TOLERANCE) {
            position = new Point2D(position.getX(), box.getMaxY());
            if (velocity.getY() < 0) {
                velocity = new Point2D(velocity.getX(), 0);
            }
        } else if (overlapLeft < overlapRight) {
            position = new Point2D(box.getMinX() - width, position.getY());
        } else {
            position = new Point2D(box.getMaxX(), position.getY());
        }
    }
}


