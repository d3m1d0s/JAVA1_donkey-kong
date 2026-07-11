package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

public class WalkingCharacter extends MovingEntity {

    public WalkingCharacter(Game game, Point2D position, double width, double height, Point2D velocity) {
        super(game, position, width, height,velocity);
    }

    public Rectangle2D getNearMissBox() {
        return new Rectangle2D(position.getX(), position.getY() - 50, width, height + 100);
    }

    public boolean intersectsNearMiss(Rectangle2D another) {
        return getNearMissBox().intersects(another);
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }
}
