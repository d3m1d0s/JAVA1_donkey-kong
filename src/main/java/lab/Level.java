package lab;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Level {
    static final Point2D DONKEY_KONG_POSITION = new Point2D(100, 131);
    static final Point2D PRINCESS_POSITION = new Point2D(280, 97);
    static final Point2D FIRE_BARREL_POSITION = new Point2D(60, 671);
    static final Point2D FIRST_FIREMAN_POSITION = new Point2D(180, 687);
    static final Point2D BARREL_SPAWN = new Point2D(50, 180);
    static final Point2D FIREMAN_SPAWN = new Point2D(60, 671);

    private static final int NUMBER_OF_LEVELS = 7;
    private static final double SEGMENT_STEP = Game.TILE_SIZE * 2;
    // rows lean by this much per segment
    private static final double SLOPE_STEP = 2;

    private final List<Platform> platforms = new ArrayList<>();
    private final List<Ladder> ladders = new ArrayList<>();

    public Level(Game game, double width, double height) {
        double platformHeight = Game.TILE_SIZE;
        double platformWidth = SEGMENT_STEP;
        double verticalSpacing = (height - platformHeight * NUMBER_OF_LEVELS) / (NUMBER_OF_LEVELS + 1);

        double leftX = (width - platformWidth) / 2 - 11 * Game.TILE_SIZE;
        double rightX = (width - platformWidth) / 2 + 11 * Game.TILE_SIZE;
        double[] rowY = new double[NUMBER_OF_LEVELS];
        for (int i = 0; i < NUMBER_OF_LEVELS; i++) {
            rowY[i] = verticalSpacing * (i + 1) + platformHeight * i + Game.TILE_SIZE * 2;
        }

        // top row with the princess ladder
        addPlatformRun(game, leftX + 9 * Game.TILE_SIZE, rowY[0], 3, 1, false);
        addLadderColumn(game, leftX + 9 * Game.TILE_SIZE + 2 * SEGMENT_STEP, rowY[0] + 2, 4);

        // second row: a short slope, then a flat stretch
        addPlatformRun(game, rightX, rowY[1], 4, -1, true);
        addLadderAt(game, rightX, rowY[1], -1, true, 1, 3);
        double row1X = rightX - 4 * SEGMENT_STEP;
        double row1Y = rowY[1] - 4 * SLOPE_STEP;
        addPlatformRun(game, row1X, row1Y, 9, -1, false);
        addLadderAt(game, row1X, row1Y, -1, false, 3, 4);

        addPlatformRun(game, leftX, rowY[2], 13, 1, true);
        addLadderAt(game, leftX, rowY[2], 1, true, 1, 3);
        addLadderAt(game, leftX, rowY[2], 1, true, 3, 4);
        addLadderAt(game, leftX, rowY[2], 1, true, 9, 5);

        addPlatformRun(game, rightX, rowY[3], 13, -1, true);
        addLadderAt(game, rightX, rowY[3], -1, true, 1, 3);
        addLadderAt(game, rightX, rowY[3], -1, true, 5, 4);
        addLadderAt(game, rightX, rowY[3], -1, true, 9, 5);

        addPlatformRun(game, leftX, rowY[4], 13, 1, true);
        addLadderAt(game, leftX, rowY[4], 1, true, 1, 3);
        addLadderAt(game, leftX, rowY[4], 1, true, 5, 4);

        addPlatformRun(game, rightX, rowY[5], 13, -1, true);
        addLadderAt(game, rightX, rowY[5], -1, true, 1, 4);
        addLadderAt(game, rightX, rowY[5], -1, true, 7, 5);

        // bottom row: flat start, then a long ramp
        double row6X = leftX - SEGMENT_STEP;
        addPlatformRun(game, row6X, rowY[6], 6, 1, false);
        addPlatformRun(game, row6X + 6 * SEGMENT_STEP, rowY[6], 8, 1, true);
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public List<Ladder> getLadders() {
        return ladders;
    }

    private void addPlatformRun(Game game, double x, double y, int count, int direction, boolean sloped) {
        for (int j = 0; j < count; j++) {
            platforms.add(new Platform(game, new Point2D(x, y),
                    new Point2D(SEGMENT_STEP, Game.TILE_SIZE)));
            x += direction * SEGMENT_STEP;
            if (sloped) {
                y -= SLOPE_STEP;
            }
        }
    }

    // a ladder hanging from segment j of the run that starts at (x, y)
    private void addLadderAt(Game game, double x, double y, int direction, boolean sloped, int segment, int tiles) {
        double ladderX = x + direction * SEGMENT_STEP * segment;
        double ladderY = sloped ? y - SLOPE_STEP * segment : y;
        addLadderColumn(game, ladderX, ladderY, tiles);
    }

    private void addLadderColumn(Game game, double x, double y, int tiles) {
        for (int t = 0; t < tiles; t++) {
            ladders.add(new Ladder(game, new Point2D(x, y + t * Game.TILE_SIZE),
                    Game.TILE_SIZE, Game.TILE_SIZE));
        }
    }
}
