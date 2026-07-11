package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Iterator;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static final double TILE_SIZE = 20;
    private static final int JUMP_BONUS = 100;

    private final double width;
    private final double height;

    private boolean isStarted = false;
    private boolean isGameOver = false;

    private List<Barrel> barrels;
    private double timeSinceLastSpawn = 0;
    private final double spawnInterval = 3.0;
    private static final int MAX_FIREMEN = 5;
    private List<Platform> platforms;
    private DonkeyKong donkeyKong;
    private Mario mario;
    private Princess princess;
    private List<Fireman> firemen;
    private FireBarrel fireBarrel;
    private Score score;
    private List<Ladder> ladders;
    private DrawableSimulable[] objects;
    private final Random random = new Random();

    public Game(double width, double height) {
        this.width = width;
        this.height = height;

        this.barrels = new ArrayList<>();
        this.firemen = new ArrayList<>();

        Level level = new Level(this, width, height);
        this.platforms = level.getPlatforms();
        this.ladders = level.getLadders();

        double donkeyKongWidth = TILE_SIZE * 4 * 1.5;
        double donkeyKongHeight = TILE_SIZE * 4;
        this.donkeyKong = new DonkeyKong(this, Level.DONKEY_KONG_POSITION, donkeyKongWidth, donkeyKongHeight);

        double princessWidth = TILE_SIZE * 0.5;
        double princessHeight = TILE_SIZE;
        this.princess = new Princess(this, Level.PRINCESS_POSITION, princessWidth, princessHeight);

        double marioWidth = TILE_SIZE;
        double marioHeight = TILE_SIZE;
        this.mario = new Mario(this, Mario.START_POSITION, marioWidth, marioHeight);

        double firemanWidth = TILE_SIZE;
        double firemanHeight = TILE_SIZE;
        this.firemen.add(new Fireman(this, Level.FIRST_FIREMAN_POSITION, firemanWidth, firemanHeight, new Point2D(100, 0)));

        double fireBarrelWidth = TILE_SIZE * 2;
        double fireBarrelHeight = TILE_SIZE * 2;
        this.fireBarrel = new FireBarrel(this, Level.FIRE_BARREL_POSITION, fireBarrelWidth, fireBarrelHeight);

        this.score = new Score(this, new Point2D(0, 0));

        updateObjectsArray();
    }


    public void draw(GraphicsContext gc) {
        gc.clearRect(0.0, 0.0, this.width, this.height);
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width,height);

        for (DrawableSimulable obj : objects) {
            obj.draw(gc);
        }
    }

    public void simulate(double timeDelta) {
        if (this.isStarted) {
            for (DrawableSimulable obj : objects) {
                obj.simulate(timeDelta);
            }

            timeSinceLastSpawn += timeDelta;
            boolean removedAny = false;
            for (Iterator<Barrel> iterator = barrels.iterator(); iterator.hasNext();) {
                Barrel barrel = iterator.next();
                if (barrel.shouldBeRemoved()) {
                    iterator.remove();
                    removedAny = true;
                    if (barrel instanceof UniqueBarrel && barrel.isBurned()) {
                        spawnFire();
                    }
                }
            }
            if (removedAny) {
                updateObjectsArray();
            }
            if (timeSinceLastSpawn >= spawnInterval) {
                spawnBarrel();
                timeSinceLastSpawn = 0;
            }

            for (int i = 0; i < objects.length; i++) {
                if (objects[i] instanceof Collidable obj1Col) {
                    for (int j = i + 1; j < objects.length; j++) {
                        if (objects[j] instanceof Collidable obj2Col) {
                            if (obj1Col.intersects(obj2Col.getBoundingBox())) {
                                obj1Col.hitBy(obj2Col);
                                obj2Col.hitBy(obj1Col);
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < objects.length; i++) {
                if (objects[i] instanceof WalkingCharacter obj1Col) {
                    for (int j = i + 1; j < objects.length; j++) {
                        if (objects[j] instanceof WalkingCharacter obj2Col) {
                            if (!(obj1Col instanceof Mario) && !(obj2Col instanceof Mario)) {
                                continue;
                            }

                            boolean nearMiss = obj1Col.intersectsNearMiss(obj2Col.getNearMissBox())
                                    && !obj1Col.intersects(obj2Col.getBoundingBox());
                            if (nearMiss && !mario.isJumpBonusPaid()
                                    && !mario.isOnPlatform() && !mario.isClimbing()) {
                                mario.markJumpBonusPaid();
                                mario.setScore(mario.getScore() + JUMP_BONUS);
                            }
                        }
                    }
                }
            }

            if (mario.getSavedThePrincess() || mario.getLives() <= 0) {
                gameOver();
            }
        }
    }

    private void spawnBarrel() {
        double barrelWidth = 20;
        double barrelHeight = 20;
        Point2D spawnPosition = Level.BARREL_SPAWN;
        Point2D velocity = new Point2D(120, 0);

        int chance = random.nextInt(100);

        if (chance < 25) {
            UniqueBarrel uniqueBarrel = new UniqueBarrel(this, spawnPosition, barrelWidth, barrelHeight, velocity);
            barrels.add(uniqueBarrel);
        } else {
            Barrel barrel = new Barrel(this, spawnPosition, barrelWidth, barrelHeight, velocity);
            barrels.add(barrel);
        }

        updateObjectsArray();
    }

    private void spawnFire() {
        if (firemen.size() >= MAX_FIREMEN) {
            return;
        }

        double firemanWidth = 20;
        double firemanHeight = 20;
        Point2D spawnPosition = Level.FIREMAN_SPAWN;
        double speed = 70 + random.nextInt(61); // unequal speeds keep firemen from bunching up
        double direction = random.nextBoolean() ? 1 : -1;
        Point2D velocity = new Point2D(direction * speed, 0);

        this.firemen.add(new Fireman(this, spawnPosition, firemanWidth, firemanHeight, velocity));

        updateObjectsArray();
    }

    private void updateObjectsArray() {
        // +5: DonkeyKong, Mario, Princess, FireBarrel, Score
        objects = new DrawableSimulable[platforms.size() + barrels.size() + ladders.size() + 5 + firemen.size()];
        int index = 0;

        for (Platform platform : platforms) {
            objects[index++] = platform;
        }

        for (Ladder ladder : ladders) {
            objects[index++] = ladder;
        }

        for (Barrel barrel : barrels) {
            objects[index++] = barrel;
        }

        objects[index++] = donkeyKong;
        objects[index++] = mario;
        objects[index++] = princess;
        objects[index++] = fireBarrel;
        objects[index++] = score;

        for (Fireman fireman : firemen) {
            objects[index++] = fireman;
        }
    }

    public void startGame() {
        this.isStarted = true;
        this.isGameOver = false;
    }

    public void gameOver() {
        this.isStarted = false;
        this.isGameOver = true;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public List<Ladder> getLadders() {
        return ladders;
    }

    public Mario getMario() {
        return mario;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }
}
