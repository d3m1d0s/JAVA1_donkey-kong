package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class FireBarrel extends GameEntity implements Collisionable {
    private double width;
    private double height;
    private Image image;

    public FireBarrel(Game game, Point2D position, double width, double height) {
        super(game, position);
        this.width = width;
        this.height = height;
        this.image = new Image(getClass().getResourceAsStream("fire_barrel.gif"), width, height,
                true, true);
    }

    @Override
    protected void drawInternal(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY(), width, height);
    }

    @Override
    public void simulate(double timeDelta) {
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

    }
}
