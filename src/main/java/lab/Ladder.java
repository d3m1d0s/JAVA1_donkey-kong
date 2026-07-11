package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.security.interfaces.DSAPublicKey;

public class Ladder extends GameEntity implements Collisionable{
    private double width;
    private double height;
    private Image image;

    public Ladder(Game game, Point2D position, double width, double height) {
        super(game, position);
        this.width = width;
        this.height = height;
        this.image = new Image(getClass().getResourceAsStream("ladder.png"), width, height,
                true, true);
    }

    @Override
    protected void drawInternal(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY(), width, height);
    }

    @Override
    public void simulate(double timeDelta) {
        // Donkey Kong обычно не двигается, так что здесь ничего не происходит.
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX() + 10, position.getY(), 1, height);
    }

    @Override
    public boolean intersects(Rectangle2D another) {
        return getBoundingBox().intersects(another);
    }

    @Override
    public void hitBy(Collisionable another) {

    }

    public double getHeight() {
        return height;
    }
}
