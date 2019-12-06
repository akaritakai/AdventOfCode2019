package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;


public class TestPuzzle06 extends BasePuzzleTest {
  @Test
  public void testPart1Example1() {
    var puzzle = new Puzzle06("COM)B\nB)C\nC)D\nD)E\nE)F\nB)G\nG)H\nD)I\nE)J\nJ)K\nK)L");
    Assert.assertEquals(puzzle.pathToCenterOfMass("D"), List.of("C", "B", "COM"));
    Assert.assertEquals(puzzle.pathToCenterOfMass("L"), List.of("K", "J", "E", "D", "C", "B", "COM"));
    Assert.assertTrue(puzzle.pathToCenterOfMass("COM").isEmpty());
    Assert.assertEquals(puzzle.solvePart1(), "42");
  }

  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle06(getStoredInput(6));
    Assert.assertEquals(puzzle.solvePart1(), "417916");
  }

  @Test
  public void testPart2Example1() {
    var puzzle = new Puzzle06("COM)B\nB)C\nC)D\nD)E\nE)F\nB)G\nG)H\nD)I\nE)J\nJ)K\nK)L\nK)YOU\nI)SAN");
    Assert.assertEquals(puzzle.pathToCenterOfMass("YOU"), List.of("K", "J", "E", "D", "C", "B", "COM"));
    Assert.assertEquals(puzzle.pathToCenterOfMass("SAN"), List.of("I", "D", "C", "B", "COM"));
    Assert.assertEquals(puzzle.solvePart2(), "4");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle06(getStoredInput(6));
    Assert.assertEquals(puzzle.solvePart2(), "523");
  }
}
