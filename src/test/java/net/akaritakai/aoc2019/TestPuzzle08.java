package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;


public class TestPuzzle08 extends BasePuzzleTest {
  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle08(getStoredInput(8));
    Assert.assertEquals(puzzle.solvePart1(), "1905");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle08(getStoredInput(8));
    Assert.assertEquals(puzzle.solvePart2(), "\n"
        + "█░░███░░██░██░█░░░██░░░░█\n"
        + "░██░█░██░█░█░██░██░████░█\n"
        + "░██░█░████░░███░██░███░██\n"
        + "░░░░█░████░█░██░░░███░███\n"
        + "░██░█░██░█░█░██░████░████\n"
        + "░██░██░░██░██░█░████░░░░█");
  }
}
