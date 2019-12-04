package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;


public class TestPuzzle04 extends BasePuzzleTest {
  @Test
  public void testPart1Example1() {
    Assert.assertTrue(Puzzle04.meetsPart1Criteria(11111));
  }

  @Test
  public void testPart1Example2() {
    Assert.assertFalse(Puzzle04.meetsPart1Criteria(223450));
  }

  @Test
  public void testPart1Example3() {
    Assert.assertFalse(Puzzle04.meetsPart1Criteria(123789));
  }

  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle04(getStoredInput(4));
    Assert.assertEquals(puzzle.solvePart1(), "2081");
  }

  @Test
  public void testPart2Example1() {
    Assert.assertTrue(Puzzle04.meetsPart2Criteria(112233));
  }

  @Test
  public void testPart2Example2() {
    Assert.assertFalse(Puzzle04.meetsPart2Criteria(123444));
  }

  @Test
  public void testPart2Example3() {
    Assert.assertTrue(Puzzle04.meetsPart2Criteria(111122));
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle04(getStoredInput(4));
    Assert.assertEquals(puzzle.solvePart2(), "1411");
  }
}
