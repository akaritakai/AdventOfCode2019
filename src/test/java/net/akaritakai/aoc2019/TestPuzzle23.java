package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;


public class TestPuzzle23 extends BasePuzzleTest {
  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle23(getStoredInput(23));
    Assert.assertEquals(puzzle.solvePart1(), "26779");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle23(getStoredInput(23));
    Assert.assertEquals(puzzle.solvePart2(), "19216");
  }
}
