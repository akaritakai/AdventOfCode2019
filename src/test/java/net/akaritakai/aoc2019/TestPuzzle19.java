package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;


public class TestPuzzle19 extends BasePuzzleTest {
  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle19(getStoredInput(19));
    Assert.assertEquals(puzzle.solvePart1(), "173");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle19(getStoredInput(19));
    Assert.assertEquals(puzzle.solvePart2(), "6671097");
  }
}
