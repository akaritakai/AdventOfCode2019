package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;


public class TestPuzzle13 extends BasePuzzleTest {
  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle13(getStoredInput(13));
    Assert.assertEquals(puzzle.solvePart1(), "180");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle13(getStoredInput(13));
    Assert.assertEquals(puzzle.solvePart2(), "8777");
  }
}
