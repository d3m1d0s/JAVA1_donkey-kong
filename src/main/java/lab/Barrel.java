package lab;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Barrel extends WalkingEnemy {
    private boolean shouldBeRemoved = false;


    public Barrel(Game game, Point2D position, double width, double height, Point2D velocity) {
        super(game, position, width, height, velocity);
        this.imageR = new Image(getClass().getResourceAsStream("common_barrel_right.gif"), width, height, true, true);
        this.imageL = new Image(getClass().getResourceAsStream("common_barrel_left.gif"), width, height, true, true);
    }

    @Override
    public void simulate(double timeDelta) {
        if (onPlatform) {
            if (position.getX() < 50 || position.getX() > game.getWidth() - width - 50) {
                velocity = new Point2D(-velocity.getX(), velocity.getY());
            }
            // rows 3 and 5 are reversed lanes: there the barrel travels against its velocity
            if (position.getY() > 291.5 - height && position.getY() < 335.5) {
                position = position.add(-2 * velocity.getX() * timeDelta, 0);
            }
            if (position.getY() > 488.5 - height && position.getY() < 532.5) {
                position = position.add(-2 * velocity.getX() * timeDelta, 0);
            }
            standardMovementLogic(timeDelta);
        } else {
            fall(timeDelta);
        }
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Platform) {
            onPlatform = true;
        } else if (another instanceof Ladder) {
            Ladder ladder = (Ladder) another;
            if (!ladder.isAtBottom(position, height)) {
                nearLadder = true;
                onPlatform = false;
            }
        } else if (another instanceof FireBarrel) {
            shouldBeRemoved = true;  // Установка флага для удаления объекта
        }
    }

    public boolean shouldBeRemoved() {
        return shouldBeRemoved;
    }

    // Методы и поля класса MovingEntity и другие методы, если они есть
    // ...
}
