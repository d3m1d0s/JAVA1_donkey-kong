package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Platform extends GameEntity implements Collisionable {
    private Point2D size;

    public Platform(Game game, Point2D position, Point2D size) {
        super(game, position);
        this.size = size;
    }

    @Override
    protected void drawInternal(GraphicsContext gc) {
        gc.setFill(Color.BROWN); // Цвет платформы
        gc.fillRect(position.getX(), position.getY(), size.getX(), size.getY());
    }

    @Override
    public void simulate(double timeDelta) {
        // Платформы статичны, так что здесь ничего не происходит.
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), size.getX(), size.getY());
    }

    @Override
    public boolean intersects(Rectangle2D another) {
        return getBoundingBox().intersects(another);
    }

    @Override
    public void hitBy(Collisionable another) {

    }


    // Методы для обновления позиции платформы, если это необходимо
}
