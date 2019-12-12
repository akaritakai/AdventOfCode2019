package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestPuzzle12 extends BasePuzzleTest {
  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle12(getStoredInput(12));
    Assert.assertEquals(puzzle.solvePart1(), "7202");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle12(getStoredInput(12));
    Assert.assertEquals(puzzle.solvePart2(), "537881600740876");
  }
}
