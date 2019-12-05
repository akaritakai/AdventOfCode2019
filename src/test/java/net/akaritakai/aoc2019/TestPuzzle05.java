package net.akaritakai.aoc2019;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;

import org.testng.Assert;
import org.testng.annotations.Test;


public class TestPuzzle05 extends BasePuzzleTest {
  @Test
  public void testPart1Example1() {
    IntStream.range(-10, 20).forEach(i -> {
      var program = Arrays.asList(3, 1, 99);
      var input = new Stack<Integer>();
      input.push(i);
      Puzzle05.runProgram(program, input, new Stack<>());
      Assert.assertEquals((int) program.get(1), i);
    });
  }

  @Test
  public void testPart1Example2() {
    IntStream.range(-10, 20).forEach(i -> {
      var program = Arrays.asList(4, 3, 99, i);
      var output = new Stack<Integer>();
      Puzzle05.runProgram(program, new Stack<>(), output);
      Assert.assertEquals((int) output.pop(), i);
    });
  }

  @Test
  public void testPart1Example3() {
    IntStream.range(-10, 20).forEach(i -> {
      var program = Arrays.asList(3, 0, 4, 0, 99);
      var input = new Stack<Integer>();
      input.push(i);
      var output = new Stack<Integer>();
      Puzzle05.runProgram(program, input, output);
      Assert.assertEquals((int) output.pop(), i);
    });
  }

  @Test
  public void testPart1Example4() {
    var program = Arrays.asList(1002, 4, 3, 4, 33);
    Puzzle05.runProgram(program, new Stack<>(), new Stack<>());
    Assert.assertEquals(program, List.of(1002, 4, 3, 4, 99));
  }

  @Test
  public void testPart1Example5() {
    var program = Arrays.asList(1101, 100, -1, 4, 0);
    Puzzle05.runProgram(program, new Stack<>(), new Stack<>());
    Assert.assertEquals(program, List.of(1101, 100, -1, 4, 99));
  }

  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle05(getStoredInput(5));
    Assert.assertEquals(puzzle.solvePart1(), "16348437");
  }

  @Test
  public void testPart2Example1() {
    IntStream.range(-10, 20).forEach(i -> {
      var program = Arrays.asList(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8);
      var input = new Stack<Integer>();
      input.push(i);
      var output = new Stack<Integer>();
      Puzzle05.runProgram(program, input, output);
      Assert.assertEquals((int) output.pop(), i == 8 ? 1 : 0);
    });
  }

  @Test
  public void testPart2Example2() {
    IntStream.range(-10, 20).forEach(i -> {
      var program = Arrays.asList(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8);
      var input = new Stack<Integer>();
      input.push(i);
      var output = new Stack<Integer>();
      Puzzle05.runProgram(program, input, output);
      Assert.assertEquals((int) output.pop(), i < 8 ? 1 : 0);
    });
  }

  @Test
  public void testPart2Example3() {
    IntStream.range(-10, 20).forEach(i -> {
      var program = Arrays.asList(3, 3, 1108, -1, 8, 3, 4, 3, 99);
      var input = new Stack<Integer>();
      input.push(i);
      var output = new Stack<Integer>();
      Puzzle05.runProgram(program, input, output);
      Assert.assertEquals((int) output.pop(), i == 8 ? 1 : 0);
    });
  }

  @Test
  public void testPart2Example4() {
    IntStream.range(-10, 20).forEach(i -> {
      var program = Arrays.asList(3, 3, 1107, -1, 8, 3, 4, 3, 99);
      var input = new Stack<Integer>();
      input.push(i);
      var output = new Stack<Integer>();
      Puzzle05.runProgram(program, input, output);
      Assert.assertEquals((int) output.pop(), i < 8 ? 1 : 0);
    });
  }

  @Test
  public void testPart2Example5() {
    IntStream.range(-10, 20).forEach(i -> {
      var program = Arrays.asList(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9);
      var input = new Stack<Integer>();
      input.push(i);
      var output = new Stack<Integer>();
      Puzzle05.runProgram(program, input, output);
      Assert.assertEquals((int) output.pop(), i == 0 ? 0 : 1);
    });
  }

  @Test
  public void testPart2Example6() {
    IntStream.range(-10, 20).forEach(i -> {
      var program = Arrays.asList(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1);
      var input = new Stack<Integer>();
      input.push(i);
      var output = new Stack<Integer>();
      Puzzle05.runProgram(program, input, output);
      Assert.assertEquals((int) output.pop(), i == 0 ? 0 : 1);
    });
  }

  @Test
  public void testPart2Example7() {
    IntStream.range(-10, 20).forEach(i -> {
      var program = Arrays.asList(3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31, 1106, 0, 36, 98,
          0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104, 999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98,
          99);
      var input = new Stack<Integer>();
      input.push(i);
      var output = new Stack<Integer>();
      Puzzle05.runProgram(program, input, output);
      Assert.assertEquals((int) output.pop(), (i < 8) ? 999 : ((i == 8) ? 1000 : 1001));
    });
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle05(getStoredInput(5));
    Assert.assertEquals(puzzle.solvePart2(), "6959377");
  }
}
