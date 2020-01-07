package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;


public class TestPuzzle25 extends BasePuzzleTest {
  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle25(getStoredInput(25));
    Assert.assertEquals(puzzle.solvePart1(), "1073815584");
  }
}
