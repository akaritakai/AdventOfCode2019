package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static net.akaritakai.aoc2019.Puzzle16.fft;


public class TestPuzzle16 extends BasePuzzleTest {
  @Test
  public void testPart1Example1() {
    List<Integer> signal = List.of(1, 2, 3, 4, 5, 6, 7, 8);
    Assert.assertEquals(fft(signal), List.of(4, 8, 2, 2, 6, 1, 5, 8));
  }

  @Test
  public void testPart1Example2() {
    List<Integer> signal = List.of(4, 8, 2, 2, 6, 1, 5, 8);
    Assert.assertEquals(fft(signal), List.of(3, 4, 0, 4, 0, 4, 3, 8));
  }

  @Test
  public void testPart1Example3() {
    List<Integer> signal = List.of(3, 4, 0, 4, 0, 4, 3, 8);
    Assert.assertEquals(fft(signal), List.of(0, 3, 4, 1, 5, 5, 1, 8));
  }

  @Test
  public void testPart1Example4() {
    List<Integer> signal = List.of(0, 3, 4, 1, 5, 5, 1, 8);
    Assert.assertEquals(fft(signal), List.of(0, 1, 0, 2, 9, 4, 9, 8));
  }

  @Test
  public void testPart1Example5() {
    var puzzle = new Puzzle16("80871224585914546619083218645595");
    Assert.assertEquals(puzzle.solvePart1(), "24176176");
  }

  @Test
  public void testPart1Example6() {
    var puzzle = new Puzzle16("19617804207202209144916044189917");
    Assert.assertEquals(puzzle.solvePart1(), "73745418");
  }

  @Test
  public void testPart1Example7() {
    var puzzle = new Puzzle16("69317163492948606335995924319873");
    Assert.assertEquals(puzzle.solvePart1(), "52432133");
  }

  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle16(getStoredInput(16));
    Assert.assertEquals(puzzle.solvePart1(), "30550349");
  }

  @Test
  public void testPart2Example1() {
    var puzzle = new Puzzle16("03036732577212944063491565474664");
    Assert.assertEquals(puzzle.solvePart2(), "84462026");
  }

  @Test
  public void testPart2Example2() {
    var puzzle = new Puzzle16("02935109699940807407585447034323");
    Assert.assertEquals(puzzle.solvePart2(), "78725270");
  }

  @Test
  public void testPart2Example3() {
    var puzzle = new Puzzle16("03081770884921959731165446850517");
    Assert.assertEquals(puzzle.solvePart2(), "53553731");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle16(getStoredInput(16));
    Assert.assertEquals(puzzle.solvePart2(), "62938399");
  }
}
