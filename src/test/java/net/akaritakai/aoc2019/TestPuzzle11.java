package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class TestPuzzle11 extends BasePuzzleTest {
  @Test
  public void testRobotInitOnEmptyHull() throws Exception {
    var hull = new HashMap<Point, Puzzle11.Color>();
    var input = new LinkedBlockingQueue<Long>();
    var state = new Puzzle11.RobotState(hull, input);

    Assert.assertEquals(state.getRobotPosition(), Puzzle11.RobotState.ORIGIN); // Robot starts at the origin
    Assert.assertEquals(state.getRobotDirection(), Puzzle11.RobotState.STARTING_DIRECTION); // Robot starts facing up
    Assert.assertTrue(hull.isEmpty()); // The hull has not been painted
    Assert.assertTrue(state.getPaintedSquares().isEmpty()); // The robot has not painted
    Assert.assertEquals((long) input.take(), Puzzle11.Color.DEFAULT.getValue()); // The robot is told the hull is the default color
  }

  @Test
  public void testRobotInitOnBlackHull() throws Exception {
    var hull = new HashMap<Point, Puzzle11.Color>();
    hull.put(Puzzle11.RobotState.ORIGIN, Puzzle11.Color.BLACK);
    var input = new LinkedBlockingQueue<Long>();
    var state = new Puzzle11.RobotState(hull, input);

    Assert.assertEquals(state.getRobotPosition(), Puzzle11.RobotState.ORIGIN); // Robot starts at the origin
    Assert.assertEquals(state.getRobotDirection(), Puzzle11.RobotState.STARTING_DIRECTION); // Robot starts facing up
    Assert.assertTrue(state.getPaintedSquares().isEmpty()); // The robot has not painted
    Assert.assertEquals((long) input.take(), Puzzle11.Color.BLACK.getValue()); // The robot is told the hull is black
  }

  @Test
  public void testRobotInitOnWhiteHull() throws Exception {
    var hull = new HashMap<Point, Puzzle11.Color>();
    hull.put(Puzzle11.RobotState.ORIGIN, Puzzle11.Color.WHITE);
    var input = new LinkedBlockingQueue<Long>();
    var state = new Puzzle11.RobotState(hull, input);

    Assert.assertEquals(state.getRobotPosition(), Puzzle11.RobotState.ORIGIN); // Robot starts at the origin
    Assert.assertEquals(state.getRobotDirection(), Puzzle11.RobotState.STARTING_DIRECTION); // Robot starts facing up
    Assert.assertTrue(state.getPaintedSquares().isEmpty()); // The robot has not painted
    Assert.assertEquals((long) input.take(), Puzzle11.Color.WHITE.getValue()); // The robot is told the hull is black
  }

  @Test
  public void testPart1Example1() {
    var hull = new HashMap<Point, Puzzle11.Color>();
    var input = new LinkedBlockingQueue<Long>();
    var state = new Puzzle11.RobotState(hull, input);

    state.onOutput(Puzzle11.Color.WHITE.getValue()); // paint (0,0) white
    state.onOutput(Puzzle11.Turn.LEFT.getValue()); // turn left and move left to (-1,0)
    state.onOutput(Puzzle11.Color.BLACK.getValue()); // paint (-1,0) black
    state.onOutput(Puzzle11.Turn.LEFT.getValue()); // turn left and move down to (-1,-1)
    state.onOutput(Puzzle11.Color.WHITE.getValue()); // paint (-1,-1) white
    state.onOutput(Puzzle11.Turn.LEFT.getValue()); // turn left and move right to (0,-1)
    state.onOutput(Puzzle11.Color.WHITE.getValue()); // paint (0,-1) white
    state.onOutput(Puzzle11.Turn.LEFT.getValue()); // turn left and move up to (0,0)
    state.onOutput(Puzzle11.Color.BLACK.getValue()); // paint (0,0) black
    state.onOutput(Puzzle11.Turn.RIGHT.getValue()); // turn right and move right to (1,0)
    state.onOutput(Puzzle11.Color.WHITE.getValue()); // paint (1,0) white
    state.onOutput(Puzzle11.Turn.LEFT.getValue()); // turn left and move up to (1,1)
    state.onOutput(Puzzle11.Color.WHITE.getValue()); // paint (1,1) white
    state.onOutput(Puzzle11.Turn.LEFT.getValue()); // turn left and move left to (0,1)

    Assert.assertEquals(state.getRobotPosition(), new Point(0, 1));
    Assert.assertEquals(state.getRobotDirection(), Puzzle11.Direction.LEFT);
    Assert.assertEquals(state.getPaintedSquares().size(), 6);

    Map<Point, Puzzle11.Color> expectedHull = Map.of(
        new Point(0, 0), Puzzle11.Color.BLACK,
        new Point(-1, 0), Puzzle11.Color.BLACK,
        new Point(-1, -1), Puzzle11.Color.WHITE,
        new Point(0, -1), Puzzle11.Color.WHITE,
        new Point(1, 0), Puzzle11.Color.WHITE,
        new Point(1, 1), Puzzle11.Color.WHITE);
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
}
