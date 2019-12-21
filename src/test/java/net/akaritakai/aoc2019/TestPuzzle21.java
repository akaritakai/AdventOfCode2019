package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;


public class TestPuzzle21 extends BasePuzzleTest {
  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle21(getStoredInput(21));
    Assert.assertEquals(puzzle.solvePart1(), "19355364");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle21(getStoredInput(21));
    Assert.assertEquals(puzzle.solvePart2(), "1142530574");
  }
}
