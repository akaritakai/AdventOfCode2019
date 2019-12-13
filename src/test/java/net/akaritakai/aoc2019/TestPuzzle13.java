package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.Map;


public class TestPuzzle13 extends BasePuzzleTest {
  @Test
  public void testPart1Example1() {
    var state = new Puzzle13.GameState();
    state.onOutput(1);
    state.onOutput(2);
    state.onOutput(3);
    state.onOutput(6);
    state.onOutput(5);
    state.onOutput(4);
    Assert.assertEquals(state.getScreen(), Map.of(
        new Point(1, 2), Puzzle13.TileType.HORIZONTAL_PADDLE,
        new Point(6, 5), Puzzle13.TileType.BALL
    ));
  }

  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle13(getStoredInput(13));
    Assert.assertEquals(puzzle.solvePart1(), "180");
  }

  @Test
  public void testPart2Example1() {
    var state = new Puzzle13.GameState();
    state.onOutput(-1);
    state.onOutput(0);
    state.onOutput(12345);
    Assert.assertEquals(state.getScore(), 12345);
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle13(getStoredInput(13));
    Assert.assertEquals(puzzle.solvePart2(), "8777");
  }
}
