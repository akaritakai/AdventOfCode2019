package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;


@Test
public class TestPuzzle03 extends BasePuzzleTest {
  @Test
  public void testPart1Example1() {
    Puzzle03 puzzle = new Puzzle03("R8,U5,L5,D3\nU7,R6,D4,L4");
    Assert.assertEquals(puzzle.solvePart1(), "6");
  }

  @Test
  public void testPart1Example2() {
    Puzzle03 puzzle = new Puzzle03("R75,D30,R83,U83,L12,D49,R71,U7,L72\nU62,R66,U55,R34,D71,R55,D58,R83");
    Assert.assertEquals(puzzle.solvePart1(), "159");
  }

  @Test
  public void testPart1Example3() {
    Puzzle03 puzzle = new Puzzle03("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51\n"
        + "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7");
    Assert.assertEquals(puzzle.solvePart1(), "135");
  }

  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle03(getStoredInput(3));
    Assert.assertEquals(puzzle.solvePart1(), "1626");
  }

  @Test
  public void testPart2Example1() {
    Puzzle03 puzzle = new Puzzle03("R8,U5,L5,D3\nU7,R6,D4,L4");
    Assert.assertEquals(puzzle.solvePart2(), "30");
  }

  @Test
  public void testPart2Example2() {
    Puzzle03 puzzle = new Puzzle03("R75,D30,R83,U83,L12,D49,R71,U7,L72\nU62,R66,U55,R34,D71,R55,D58,R83");
    Assert.assertEquals(puzzle.solvePart2(), "610");
  }

  @Test
  public void testPart2Example3() {
    Puzzle03 puzzle = new Puzzle03("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51\n"
        + "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7");
    Assert.assertEquals(puzzle.solvePart2(), "410");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle03(getStoredInput(3));
    Assert.assertEquals(puzzle.solvePart2(), "27330");
  }
}
