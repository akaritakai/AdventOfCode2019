package net.akaritakai.aoc2019;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;


@Test
public class TestPuzzle02 extends BasePuzzleTest {
  @Test
  public void testPart1Example1() {
    var program = Arrays.asList(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50);
    Puzzle02.runProgram(program);
    Assert.assertEquals(program, List.of(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50));
  }

  @Test
  public void testPart1Example2() {
    var program = Arrays.asList(1, 0, 0, 0, 99);
    Puzzle02.runProgram(program);
    Assert.assertEquals(program, List.of(2, 0, 0, 0, 99));
  }

  @Test
  public void testPart1Example3() {
    var program = Arrays.asList(2, 3, 0, 3, 99);
    Puzzle02.runProgram(program);
    Assert.assertEquals(program, List.of(2, 3, 0, 6, 99));
  }

  @Test
  public void testPart1Example4() {
    var program = Arrays.asList(2, 4, 4, 5, 99, 0);
    Puzzle02.runProgram(program);
    Assert.assertEquals(program, List.of(2, 4, 4, 5, 99, 9801));
  }

  @Test
  public void testPart1Example5() {
    var program = Arrays.asList(1, 1, 1, 4, 99, 5, 6, 0, 99);
    Puzzle02.runProgram(program);
    Assert.assertEquals(program, List.of(30, 1, 1, 4, 2, 5, 6, 0, 99));
  }

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
