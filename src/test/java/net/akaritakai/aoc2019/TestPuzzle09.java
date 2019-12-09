package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;


public class TestPuzzle09 extends BasePuzzleTest {
  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle09(getStoredInput(9));
    Assert.assertEquals(puzzle.solvePart1(), "2941952859");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle09(getStoredInput(9));
    Assert.assertEquals(puzzle.solvePart2(), "66113");
  }
}
