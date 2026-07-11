package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class GameEntity implements DrawableSimulable {
    protected Point2D position;
    protected final Game game;

    public GameEntity(Game game, Point2D position) {
        this.game = game;
        this.position = position;
    }

    public final void draw(GraphicsContext gc){
        gc.save();
        drawInternal(gc);
        gc.restore();
    }

    protected abstract void drawInternal(GraphicsContext gc);

    protected Point2D getPosition() {
        return position;
    }
}
