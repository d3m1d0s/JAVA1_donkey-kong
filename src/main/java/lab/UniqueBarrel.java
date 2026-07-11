package lab;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class UniqueBarrel extends Barrel {
    public UniqueBarrel(Game game, Point2D position, double width, double height, Point2D velocity) {
        super(game, position, width, height, velocity);
        Image image = new Image(getClass().getResourceAsStream("unique_barrel.gif"), width, height,
                true, true);
        this.imageR = image;
        this.imageL = image;
    }
}
