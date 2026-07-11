package lab;

import javafx.geometry.Rectangle2D;

public interface Collidable {
    Rectangle2D getBoundingBox();
    boolean intersects(Rectangle2D another);
    void hitBy(Collidable another);
}