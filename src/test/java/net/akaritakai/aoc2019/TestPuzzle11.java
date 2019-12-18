package net.akaritakai.aoc2019;

import net.akaritakai.aoc2019.Puzzle11.Color;
import net.akaritakai.aoc2019.Puzzle11.RobotState;
import net.akaritakai.aoc2019.geom2d.Point;
import net.akaritakai.aoc2019.geom2d.Turn;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static net.akaritakai.aoc2019.Puzzle11.Color.BLACK;
import static net.akaritakai.aoc2019.Puzzle11.Color.WHITE;
import static net.akaritakai.aoc2019.geom2d.Direction.WEST;
import static net.akaritakai.aoc2019.geom2d.Turn.LEFT;
import static net.akaritakai.aoc2019.geom2d.Turn.RIGHT;

public class TestPuzzle11 extends BasePuzzleTest {
  @Test
  public void testRobotInitOnEmptyHull() throws Exception {
    var hull = new HashMap<Point, Color>();
    var input = new LinkedBlockingQueue<Long>();
    var state = new RobotState(hull, input);

    Assert.assertEquals(state.getRobotPosition(), RobotState.ORIGIN); // Robot starts at the origin
    Assert.assertEquals(state.getRobotDirection(), RobotState.STARTING_DIRECTION); // Robot starts facing up
    Assert.assertTrue(hull.isEmpty()); // The hull has not been painted
    Assert.assertTrue(state.getPaintedSquares().isEmpty()); // The robot has not painted
    Assert.assertEquals((long) input.take(), Color.DEFAULT.getValue()); // The robot is told the hull is the default color
  }

  @Test
  public void testRobotInitOnBlackHull() throws Exception {
    var hull = new HashMap<Point, Color>();
    hull.put(RobotState.ORIGIN, BLACK);
    var input = new LinkedBlockingQueue<Long>();
    var state = new RobotState(hull, input);

    Assert.assertEquals(state.getRobotPosition(), RobotState.ORIGIN); // Robot starts at the origin
    Assert.assertEquals(state.getRobotDirection(), RobotState.STARTING_DIRECTION); // Robot starts facing up
    Assert.assertTrue(state.getPaintedSquares().isEmpty()); // The robot has not painted
    Assert.assertEquals((long) input.take(), BLACK.getValue()); // The robot is told the hull is black
  }

  @Test
  public void testRobotInitOnWhiteHull() throws Exception {
    var hull = new HashMap<Point, Color>();
    hull.put(RobotState.ORIGIN, WHITE);
    var input = new LinkedBlockingQueue<Long>();
    var state = new RobotState(hull, input);

    Assert.assertEquals(state.getRobotPosition(), RobotState.ORIGIN); // Robot starts at the origin
    Assert.assertEquals(state.getRobotDirection(), RobotState.STARTING_DIRECTION); // Robot starts facing up
    Assert.assertTrue(state.getPaintedSquares().isEmpty()); // The robot has not painted
    Assert.assertEquals((long) input.take(), WHITE.getValue()); // The robot is told the hull is black
  }

  @Test
  public void testPart1Example1() {
    var hull = new HashMap<Point, Color>();
    var input = new LinkedBlockingQueue<Long>();
    var state = new RobotState(hull, input);

    state.onOutput(WHITE.getValue()); // paint (0,0) white
    state.onOutput(turnValue(LEFT)); // turn left and move left to (-1,0)
    state.onOutput(BLACK.getValue()); // paint (-1,0) black
    state.onOutput(turnValue(LEFT)); // turn left and move down to (-1,-1)
    state.onOutput(WHITE.getValue()); // paint (-1,-1) white
    state.onOutput(turnValue(LEFT)); // turn left and move right to (0,-1)
    state.onOutput(WHITE.getValue()); // paint (0,-1) white
    state.onOutput(turnValue(LEFT)); // turn left and move up to (0,0)
    state.onOutput(BLACK.getValue()); // paint (0,0) black
    state.onOutput(turnValue(RIGHT)); // turn right and move right to (1,0)
    state.onOutput(WHITE.getValue()); // paint (1,0) white
    state.onOutput(turnValue(LEFT)); // turn left and move up to (1,1)
    state.onOutput(WHITE.getValue()); // paint (1,1) white
    state.onOutput(turnValue(LEFT)); // turn left and move left to (0,1)

    Assert.assertEquals(state.getRobotPosition(), new Point(0, 1));
    Assert.assertEquals(state.getRobotDirection(), WEST);
    Assert.assertEquals(state.getPaintedSquares().size(), 6);

    Map<Point, Color> expectedHull = Map.of(
        new Point(0, 0), BLACK,
        new Point(-1, 0), BLACK,
        new Point(-1, -1), WHITE,
        new Point(0, -1), WHITE,
        new Point(1, 0), WHITE,
        new Point(1, 1), WHITE);
    Assert.assertEquals(hull, expectedHull);
  }

  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle11(getStoredInput(11));
    Assert.assertEquals(puzzle.solvePart1(), "2276");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle11(getStoredInput(11));
    Assert.assertEquals(puzzle.solvePart2(), "\n"
        + " ##  ###  #    ###    ## ####  ##  #  #\n"
        + "#  # #  # #    #  #    #    # #  # #  #\n"
        + "#    ###  #    #  #    #   #  #    #  #\n"
        + "#    #  # #    ###     #  #   #    #  #\n"
        + "#  # #  # #    #    #  # #    #  # #  #\n"
        + " ##  ###  #### #     ##  ####  ##   ## ");
  }

  private static long turnValue(Turn turn) {
    if (turn == LEFT) return 0;
    if (turn == RIGHT) return 1;
    throw new UnsupportedOperationException("Unknown turn " + turn);
  }
}
