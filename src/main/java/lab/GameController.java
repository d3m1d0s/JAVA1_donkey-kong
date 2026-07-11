package lab;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;

import java.util.EnumSet;
import java.util.Set;

public class GameController {
    private static final double WALK_SPEED = 100;
    private static final double CLIMB_SPEED = 80;

    private Game game;
    private AnimationTimer animationTimer;
    private Canvas canvas;
    private final Set<KeyCode> pressedKeys = EnumSet.noneOf(KeyCode.class);
    private KeyCode lastWalkKey;

    public GameController(Canvas canvas) {
        this.canvas = canvas;
        this.game = new Game(canvas.getWidth(), canvas.getHeight());
        // Дополнительная инициализация, если требуется
    }

    public void startGame() {
        /*
        // Инициализация игры
        game.initializeGameObjects(); // Предполагается, что это метод для инициализации объектов игры
         */
        game.startGame();

        // Настройка анимационного таймера
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                applyInput();
                game.simulate(1.0 / 60); // Предположим, что метод simulate принимает deltaTime
                game.draw(canvas.getGraphicsContext2D());
            }
        };

        animationTimer.start();
    }

    public void stopGame() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    public void initializeControlHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> {
            boolean firstPress = pressedKeys.add(event.getCode());
            if (!firstPress) {
                return;
            }
            if (event.getCode() == KeyCode.SPACE) {
                game.getMario().jump();
            } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                lastWalkKey = event.getCode();
            }
        });

        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));
    }

    public void clearInput() {
        pressedKeys.clear();
    }

    private void applyInput() {
        Mario mario = game.getMario();

        boolean right = pressedKeys.contains(KeyCode.RIGHT);
        boolean left = pressedKeys.contains(KeyCode.LEFT);
        int walk = 0;
        if (right && left) {
            walk = lastWalkKey == KeyCode.LEFT ? -1 : 1;
        } else if (right) {
            walk = 1;
        } else if (left) {
            walk = -1;
        }

        int climb = 0;
        if (pressedKeys.contains(KeyCode.UP)) { climb--; }
        if (pressedKeys.contains(KeyCode.DOWN)) { climb++; }

        if (!mario.isClimbing() && climb != 0) {
            boolean canGrab = climb < 0 ? mario.canGrabLadderAbove() : mario.canGrabLadderBelow();
            if (canGrab) {
                mario.startClimbing();
            }
        }

        if (mario.isClimbing()) {
            if (climb == 0 && walk != 0) {
                mario.stopClimbing(); // walking sideways steps off the ladder
                mario.setVelocity(new Point2D(walk * WALK_SPEED, 0));
            } else {
                mario.setVelocity(new Point2D(0, climb * CLIMB_SPEED));
            }
        } else {
            mario.setVelocity(new Point2D(walk * WALK_SPEED, mario.getVelocity().getY()));
        }
    }
}
