package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

public class WalkingEnemy extends MovingEntity {

    public WalkingEnemy(Game game, Point2D position, double width, double height, Point2D velocity) {
        super(game, position, width, height,velocity);
    }

    public Rectangle2D getBoundingBox2() {
        return new Rectangle2D(position.getX(), position.getY() - 50, width, height + 100);
    }

    public boolean intersects2(Rectangle2D another) {
        return getBoundingBox2().intersects(another);
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }
}
