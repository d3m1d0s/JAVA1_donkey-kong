package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Platform extends GameEntity implements Collisionable {
    private static final Image TEXTURE = new Image(Platform.class.getResourceAsStream("platform.png"));

    private Point2D size;

    public Platform(Game game, Point2D position, Point2D size) {
        super(game, position);
        this.size = size;
    }

    @Override
    protected void drawInternal(GraphicsContext gc) {
        double sourceHeight = TEXTURE.getHeight();
        double sourceWidth = sourceHeight * size.getX() / size.getY();
        // slice offset depends on the position so neighboring segments don't repeat
        double sourceX = (position.getX() * 7 + position.getY() * 3) % (TEXTURE.getWidth() - sourceWidth);
        gc.drawImage(TEXTURE, sourceX, 0, sourceWidth, sourceHeight,
                position.getX(), position.getY(), size.getX(), size.getY());
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
