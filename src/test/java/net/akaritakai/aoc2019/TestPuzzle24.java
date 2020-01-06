package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;


public class TestPuzzle24 extends BasePuzzleTest {
  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle24(getStoredInput(24));
    Assert.assertEquals(puzzle.solvePart1(), "12129040");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle24(getStoredInput(24));
    Assert.assertEquals(puzzle.solvePart2(), "2109");
  }
}
