package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;


public class TestPuzzle05 extends BasePuzzleTest {
  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle05(getStoredInput(5));
    Assert.assertEquals(puzzle.solvePart1(), "16348437");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle05(getStoredInput(5));
    Assert.assertEquals(puzzle.solvePart2(), "6959377");
  }
}
