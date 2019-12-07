package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;


@Test
public class TestPuzzle02 extends BasePuzzleTest {
  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle02(getStoredInput(2));
    Assert.assertEquals(puzzle.solvePart1(), "2890696");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle02(getStoredInput(2));
    Assert.assertEquals(puzzle.solvePart2(), "8226");
  }
}
