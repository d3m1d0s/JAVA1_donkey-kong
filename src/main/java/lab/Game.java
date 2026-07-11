package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Iterator;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final double width;
    private final double height;

    private boolean isStarted = false;
    private boolean isGameOver = false;

    private List<Barrel> barrels;
    private double timeSinceLastSpawn = 0;
    private final double spawnInterval = 3.0;
    private static final int MAX_FIREMANS = 5;
    private List<Platform> platforms;
    private DonkeyKong donkeyKong;
    private Mario mario;
    private Princess princess;
    private List<Fireman> firemans;
    private FireBarrel fireBarrel;
    private Score score;
    private List<Ladder> ladders;
    private DrawableSimulable[] objects;
    private final Random random = new Random();

    public Game(double width, double height) {
        double brink = 20;
        this.width = width;
        this.height = height;

        this.barrels = new ArrayList<>();
        this.firemans = new ArrayList<>();

        this.platforms = new ArrayList<>();

        //лестницы
        // Инициализация лестниц
        double ladderWidth = brink; // Примерная ширина лестницы
        this.ladders = new ArrayList<>();

        //платформы
        double platformHeight = brink; // Высота платформы
        double platformWidth = brink * 2;
        int numberOfLevels = 7; // Количество уровней платформ

        // Определяем вертикальное расстояние между платформами
        double verticalSpacing = (height - platformHeight * numberOfLevels) / (numberOfLevels + 1);

        for (int i = 0; i < numberOfLevels; i++) {
            // Определяем X координату платформы
            double posX = i % 2 == 0 ? (width - platformWidth) / 2 - 11 * brink : (width - platformWidth) / 2 + 11 * brink;

            // Определяем Y координату платформы
            double posY = verticalSpacing * (i + 1) + platformHeight * i;
            posY += brink * 2;

            double ladderHeight = brink;
            if (i != 0) {
                if (i % 2 != 0) {
                    if (i != 1) {
                        for (int j = 0; j < 13; j++) {
                            if (j == 1 && i == 3) {
                                for (double k = 0; k < 3 * brink; k += brink) {
                                    this.ladders.add(new Ladder(this, new Point2D(posX, posY  + k), ladderWidth, ladderHeight));
                                }
                            } if (j == 1 && i == 5) {
                                for (double k = 0; k < 4 * brink; k += brink) {
                                    this.ladders.add(new Ladder(this, new Point2D(posX, posY  + k), ladderWidth, ladderHeight));
                                }
                            }
                            else if (j == 7 && i == 5) {
                                for (double k = 0; k < 5 * brink; k += brink) {
                                    this.ladders.add(new Ladder(this, new Point2D(posX, posY  + k), ladderWidth, ladderHeight));
                                }
                            } else if (j == 5 && i == 3) {
                                for (double k = 0; k < 4 * brink; k += brink) {
                                    this.ladders.add(new Ladder(this, new Point2D(posX, posY  + k), ladderWidth, ladderHeight));
                                }
                            } else if (j == 9 && i == 3) {
                                for (double k = 0; k < 5 * brink; k += brink) {
                                    this.ladders.add(new Ladder(this, new Point2D(posX, posY  + k), ladderWidth, ladderHeight));
                                }
                            }
                            this.platforms.add(new Platform(this, new Point2D(posX, posY), new Point2D(platformWidth, platformHeight)));
                            posX -= brink * 2; posY-=2;
                        }
                    } else {
                        for (int j = 0; j < 4; j++) {
                            if (j == 1) {
                                for (double k = 0; k < 3 * brink; k += brink) {
                                    this.ladders.add(new Ladder(this, new Point2D(posX, posY  + k), ladderWidth, ladderHeight));
                                }
                            }
                            this.platforms.add(new Platform(this, new Point2D(posX, posY), new Point2D(platformWidth, platformHeight)));
                            posX -= brink * 2; posY-=2;
                        }
                        for (int j = 0; j < 9; j++) {
                            if (j == 3) {
                                for (double k = 0; k < 4 * brink; k += brink) {
                                    this.ladders.add(new Ladder(this, new Point2D(posX, posY  +  k), ladderWidth, ladderHeight));
                                }
                            }
                            this.platforms.add(new Platform(this, new Point2D(posX, posY), new Point2D(platformWidth, platformHeight)));
                            posX -= brink * 2;
                        }
                    }
                }

                if (i % 2 == 0) {
                    if (i != 6) {
                        for (int j = 0; j < 13; j++) {
                            if (j == 1) {
                                for (double k = 0; k < 3 * brink; k += brink) {
                                    this.ladders.add(new Ladder(this, new Point2D(posX, posY + k), ladderWidth, ladderHeight));
                                }
                            } else if (j == 3 && i == 2) {
                                for (double k = 0; k < 4 * brink; k += brink) {
                                    this.ladders.add(new Ladder(this, new Point2D(posX, posY + k), ladderWidth, ladderHeight));
                                }
                            } else if (j == 9 && i == 2) {
                                for (double k = 0; k < 5 * brink; k += brink) {
                                    this.ladders.add(new Ladder(this, new Point2D(posX, posY + k), ladderWidth, ladderHeight));
                                }
                            } else if (j == 5 && i == 4) {
                                for (double k = 0; k < 4 * brink; k += brink) {
                                    this.ladders.add(new Ladder(this, new Point2D(posX, posY + k), ladderWidth, ladderHeight));
                                }
                            }
                            this.platforms.add(new Platform(this, new Point2D(posX, posY), new Point2D(platformWidth, platformHeight)));
                            posX += brink * 2; posY-=2;
                        }
                    } else {
                        posX -= brink * 2;
                        for (int j = 0; j < 6; j++) {
                            this.platforms.add(new Platform(this, new Point2D(posX, posY), new Point2D(platformWidth, platformHeight)));
                            posX += brink * 2;
                        }
                        for (int j = 0; j < 8; j++) {
                            this.platforms.add(new Platform(this, new Point2D(posX, posY), new Point2D(platformWidth, platformHeight)));
                            posX += brink * 2; posY-=2;
                        }
                    }

                }
            } else {
                posX += brink * 9;
                for (int j = 0; j < 3; j++) {
                    if (j == 2) {
                        for (double k = 0; k < 4 * brink; k += brink) {
                            this.ladders.add(new Ladder(this, new Point2D(posX, posY +2+ k), ladderWidth, ladderHeight));
                        }
                    }
                    this.platforms.add(new Platform(this, new Point2D(posX, posY), new Point2D(platformWidth, platformHeight)));
                    posX += brink * 2;
                }
            }
        }

        //Donkey Kong
        double donkeyKongWidth = brink * 4 * 1.5; // Примерная ширина Donkey Kong
        double donkeyKongHeight = brink * 4; // Примерная высота Donkey Kong
        this.donkeyKong = new DonkeyKong(this, new Point2D(100, 131), donkeyKongWidth, donkeyKongHeight);

        //Princess
        double princessWidth = brink * 0.5; // Примерная ширина Princess
        double princessHeight = brink; // Примерная высота Princess
        this.princess = new Princess(this, new Point2D(280, 97), princessWidth, princessHeight);

        //Mario
        double marioWidth = brink; // Примерная ширина Mario
        double marioHeight = brink; // Примерная высота Mario
        this.mario = new Mario(this, new Point2D(100, 670), marioWidth, marioHeight);

        //Fireman
        double firemanWidth = brink; // Примерная ширина Fireman
        double firemanHeight = brink; // Примерная высота Fireman
        this.firemans.add(new Fireman(this, new Point2D(180, 687), firemanWidth, firemanHeight, new Point2D(100, 0)));

        //FireBarrel
        double fireBarrelWidth = brink * 2; // Примерная ширина FireBarrel
        double fireBarrelHeight = brink * 2; // Примерная высота FireBarrel
        this.fireBarrel = new FireBarrel(this, new Point2D(60, 671), fireBarrelWidth, fireBarrelHeight);

        //Score
        this.score = new Score(this, new Point2D(0, 0));

        updateObjectsArray();
    }


    public void draw(GraphicsContext gc) {
        gc.clearRect(0.0, 0.0, this.width, this.height);
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,width,height);


        // ... рисуем объекты ...
        for (DrawableSimulable obj : objects) {
            obj.draw(gc);
        }
    }

    public void simulate(double timeDelta) {
        if (this.isStarted) {
            // ... симуляция объектов ...
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
                if (objects[i] instanceof Collisionable obj1Col) {
                    for (int j = i + 1; j < objects.length; j++) {
                        if (objects[j] instanceof Collisionable obj2Col) {
                            if (obj1Col.intersects(obj2Col.getBoundingBox())) {
                                obj1Col.hitBy(obj2Col);
                                obj2Col.hitBy(obj1Col);
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < objects.length; i++) {
                if (objects[i] instanceof WalkingEnemy obj1Col) {
                    for (int j = i + 1; j < objects.length; j++) {
                        if (objects[j] instanceof WalkingEnemy obj2Col) {
                            if (!(obj1Col instanceof Mario) && !(obj2Col instanceof Mario)) {
                                continue;
                            }

                            boolean nearMiss = obj1Col.intersects2(obj2Col.getBoundingBox2())
                                    && !obj1Col.intersects(obj2Col.getBoundingBox());
                            if (nearMiss && !mario.isJumpBonusPaid()
                                    && !mario.isOnPlatform() && !mario.isClimbing()) {
                                mario.markJumpBonusPaid();
                                mario.setScore(mario.getScore() + 100);
                            }
                        }
                    }
                }
            }

            if (mario.getSavedThePrincess() || mario.getLifes() <= 0) {
                gameOver();
            }
        }
    }

    private void spawnBarrel() {
        double barrelWidth = 20; // Примерная ширина бочки
        double barrelHeight = 20; // Примерная высота бочки
        Point2D spawnPosition = new Point2D(50, 180); // Примерная начальная позиция
        Point2D velocity = new Point2D(120, 0);

        int chance = random.nextInt(100);

        if (chance < 25) {
            // Создаем UniqueBarrel
            UniqueBarrel uniqueBarrel = new UniqueBarrel(this, spawnPosition, barrelWidth, barrelHeight, velocity);
            barrels.add(uniqueBarrel);
        } else {
            // Создаем обычную Barrel
            Barrel barrel = new Barrel(this, spawnPosition, barrelWidth, barrelHeight, velocity);
            barrels.add(barrel);
        }

        // Обновите массив objects, если он используется для отрисовки и симуляции всех объектов
        updateObjectsArray();
    }

    private void spawnFire() {
        if (firemans.size() >= MAX_FIREMANS) {
            return;
        }

        double firemanWidth = 20;
        double firemanHeight = 20;
        Point2D spawnPosition = new Point2D(60, 671);
        double speed = 70 + random.nextInt(61); // 70..130, different speeds keep firemen from bunching up
        double direction = random.nextBoolean() ? 1 : -1;
        Point2D velocity = new Point2D(direction * speed, 0);

        this.firemans.add(new Fireman(this, spawnPosition, firemanWidth, firemanHeight, velocity));

        updateObjectsArray();
    }

    private void updateObjectsArray() {
        objects = new DrawableSimulable[platforms.size() + barrels.size() + ladders.size() + 5 + firemans.size()]; // +5 для DonkeyKong, Mario, Princess, Score и FireBarrel
        int index = 0;

        // Добавляем платформы
        for (Platform platform : platforms) {
            objects[index++] = platform;
        }

        // Добавляем лестницы
        for (Ladder ladder : ladders) {
            objects[index++] = ladder;
        }

        // Добавляем бочки
        for (Barrel barrel : barrels) {
            objects[index++] = barrel;
        }

        // Добавляем персонажей
        objects[index++] = donkeyKong;
        objects[index++] = mario;
        objects[index++] = princess;
        objects[index++] = fireBarrel;
        objects[index++] = score;

        for (Fireman fireman : firemans) {
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
