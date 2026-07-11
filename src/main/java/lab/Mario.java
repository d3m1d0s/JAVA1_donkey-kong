package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Mario extends WalkingEnemy {
    private boolean lostTheLife = false;
    private boolean savedThePrincess = false;
    private int score = 0;
    private int lifes = 3;
    private static final double IMMUNITY_TIME = 3.0; // 3 секунды бессмертия после потери жизни
    private double lastHitTime = -IMMUNITY_TIME; // Время последней потери жизни
    public Mario(Game game, Point2D position, double width, double height, Point2D velocity) {
        super(game, position, width, height, new Point2D(0,0));
        this.imageR = new Image(getClass().getResourceAsStream("mario_go_right.gif"), width, height,
                true, true);
        this.imageL = new Image(getClass().getResourceAsStream("mario_go_left.gif"), width, height,
                true, true);
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public void jump() {
        if (onPlatform) {
            velocity = new Point2D(velocity.getX(), -90); // Примерное значение для прыжка
            onPlatform = false; // Персонаж покидает платформу
        }
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public boolean getLostTheLife() {
        return lostTheLife;
    }

    public int getLifes() {
        return lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public boolean getSavedThePrincess() {
        return savedThePrincess;
    }

    public void setLostTheLife() {
        this.lostTheLife = false;
    }

    public void moveRight() {
        setVelocity(new Point2D(100, getVelocity().getY())); // Например, скорость 100 вправо
    }

    public void moveLeft() {
        setVelocity(new Point2D(-100, getVelocity().getY())); // Например, скорость 100 влево
    }

    public void stopMoving() {
        setVelocity(new Point2D(0, getVelocity().getY())); // Остановка горизонтального движения
    }

    @Override
    public void simulate(double timeDelta) {
        double currentTime = System.nanoTime() / 1e9;

        if (this.lostTheLife && currentTime - lastHitTime >= IMMUNITY_TIME) {
            lastHitTime = currentTime; // Обновляем время последней потери жизни
            this.lostTheLife = false; // Сбрасываем флаг потери жизни
            this.lifes--; // Отнимаем одну жизнь

            if ((this.lifes <= 0) || (this.savedThePrincess)) {
                this.game.gameOver(); // Игра заканчивается, если жизни кончились
            } else {
                setPosition(new Point2D(100, 670));
            }
        }

        if (onLadder) {
            // Логика движения по лестнице
            // Например, изменение position в зависимости от направления движения по лестнице
        } else {
            super.simulate(timeDelta);
            if (
                    (position.getX() < 0) || (position.getX() > game.getWidth()) ||
                    (position.getY() < -50) || (position.getY() > game.getHeight())
            ) {
                lostTheLife = true;
            }
        }
        // Дополнительная логика
        // ...
    }

    @Override
    public Rectangle2D getBoundingBox2() {
        return new Rectangle2D(position.getX(), position.getY(), width, height);
    }

    @Override
    public void hitBy2(Collisionable another) {
        super.hitBy2(another);
        if (another instanceof WalkingEnemy) {
        }
    }

    @Override
    public void hitBy(Collisionable another) {
        super.hitBy(another);
        if (another instanceof MovingEntity || another instanceof DonkeyKong || another instanceof FireBarrel) {
            lostTheLife = true;
        }
        if (another instanceof Princess && !savedThePrincess) {
            score+=1000;
            savedThePrincess = true;
        }
    }
}
