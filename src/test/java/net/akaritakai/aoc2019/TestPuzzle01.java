package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class TestPuzzle01 extends BasePuzzleTest {
  @Test
  public void testPart1Example1() {
    var puzzle = new Puzzle01("12");
    Assert.assertEquals(puzzle.solvePart1(), "2");
  }

  @Test
  public void testPart1Example2() {
    var puzzle = new Puzzle01("14");
    Assert.assertEquals(puzzle.solvePart1(), "2");
  }

  @Test
  public void testPart1Example3() {
    var puzzle = new Puzzle01("1969");
    Assert.assertEquals(puzzle.solvePart1(), "654");
  }

  @Test
  public void testPart1Example4() {
    var puzzle = new Puzzle01("100756");
    Assert.assertEquals(puzzle.solvePart1(), "33583");
  }

  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle01(getStoredInput(1));
    Assert.assertEquals(puzzle.solvePart1(), "3390596");
  }

  @Test
  public void testPart2Example1() {
    var puzzle = new Puzzle01("14");
    Assert.assertEquals(puzzle.solvePart2(), "2");
  }

  @Test
  public void testPart2Example2() {
    var puzzle = new Puzzle01("1969");
    Assert.assertEquals(puzzle.solvePart2(), "966");
  }

  @Test
  public void testPart2Example3() {
    var puzzle = new Puzzle01("100756");
    Assert.assertEquals(puzzle.solvePart2(), "50346");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle01(getStoredInput(1));
    Assert.assertEquals(puzzle.solvePart2(), "5083024");
  }
}
