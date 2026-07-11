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
            if (firstPress && event.getCode() == KeyCode.SPACE) {
                game.getMario().jump();
            }
        });

        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));
    }

    public void clearInput() {
        pressedKeys.clear();
    }

    private void applyInput() {
        Mario mario = game.getMario();

        int walk = 0;
        if (pressedKeys.contains(KeyCode.RIGHT)) { walk++; }
        if (pressedKeys.contains(KeyCode.LEFT)) { walk--; }

        int climb = 0;
        if (pressedKeys.contains(KeyCode.UP)) { climb--; }
        if (pressedKeys.contains(KeyCode.DOWN)) { climb++; }

        if (!mario.isClimbing() && climb != 0 && mario.isOnLadder()) {
            mario.startClimbing();
        }

        if (mario.isClimbing()) {
            mario.setVelocity(new Point2D(0, climb * CLIMB_SPEED));
        } else {
            mario.setVelocity(new Point2D(walk * WALK_SPEED, mario.getVelocity().getY()));
        }
    }
}
