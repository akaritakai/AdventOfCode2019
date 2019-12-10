package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;


public class TestPuzzle10 extends BasePuzzleTest {
  @Test
  public void testPart1Example1() {
    var puzzle = new Puzzle10(
        "......#.#.\n" +
        "#..#.#....\n" +
        "..#######.\n" +
        ".#.#.###..\n" +
        ".#..#.....\n" +
        "..#....#.#\n" +
        "#..#....#.\n" +
        ".##.#..###\n" +
        "##...#..#.\n" +
        ".#....####");
    Assert.assertEquals(puzzle.solvePart1(), "33");
  }

  @Test
  public void testPart1Example2() {
    var puzzle = new Puzzle10(
        "#.#...#.#.\n" +
        ".###....#.\n" +
        ".#....#...\n" +
        "##.#.#.#.#\n" +
        "....#.#.#.\n" +
        ".##..###.#\n" +
        "..#...##..\n" +
        "..##....##\n" +
        "......#...\n" +
        ".####.###.");
    Assert.assertEquals(puzzle.solvePart1(), "35");
  }

  @Test
  public void testPart1Example3() {
    var puzzle = new Puzzle10(
        ".#..#..###\n" +
        "####.###.#\n" +
        "....###.#.\n" +
        "..###.##.#\n" +
        "##.##.#.#.\n" +
        "....###..#\n" +
        "..#.#..#.#\n" +
        "#..#.#.###\n" +
        ".##...##.#\n" +
        ".....#.#..");
    Assert.assertEquals(puzzle.solvePart1(), "41");
  }

  @Test
  public void testPart1Example4() {
    var puzzle = new Puzzle10(
        ".#..##.###...#######\n" +
        "##.############..##.\n" +
        ".#.######.########.#\n" +
        ".###.#######.####.#.\n" +
        "#####.##.#.##.###.##\n" +
        "..#####..#.#########\n" +
        "####################\n" +
        "#.####....###.#.#.##\n" +
        "##.#################\n" +
        "#####.##.###..####..\n" +
        "..######..##.#######\n" +
        "####.##.####...##..#\n" +
        ".#####..#.######.###\n" +
        "##...#.##########...\n" +
        "#.##########.#######\n" +
        ".####.#.###.###.#.##\n" +
        "....##.##.###..#####\n" +
        ".#.#.###########.###\n" +
        "#.#.#.#####.####.###\n" +
        "###.##.####.##.#..##");
    Assert.assertEquals(puzzle.solvePart1(), "210");
  }

  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle10(getStoredInput(10));
    Assert.assertEquals(puzzle.solvePart1(), "347");
  }

  @Test
  public void testPart2Example1() {
    var puzzle = new Puzzle10(
        ".#..##.###...#######\n" +
        "##.############..##.\n" +
        ".#.######.########.#\n" +
        ".###.#######.####.#.\n" +
        "#####.##.#.##.###.##\n" +
        "..#####..#.#########\n" +
        "####################\n" +
        "#.####....###.#.#.##\n" +
        "##.#################\n" +
        "#####.##.###..####..\n" +
        "..######..##.#######\n" +
        "####.##.####...##..#\n" +
        ".#####..#.######.###\n" +
        "##...#.##########...\n" +
        "#.##########.#######\n" +
        ".####.#.###.###.#.##\n" +
        "....##.##.###..#####\n" +
        ".#.#.###########.###\n" +
        "#.#.#.#####.####.###\n" +
        "###.##.####.##.#..##");
    Assert.assertEquals(puzzle.solvePart2(), "802");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle10(getStoredInput(10));
    Assert.assertEquals(puzzle.solvePart2(), "829");
  }
}
