package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class TestPuzzle17 extends BasePuzzleTest {
  @Test
  public void testPart1Example1() {
    var scaffolding = new Puzzle17.Scaffolding(
        "..#..........\n" +
            "..#..........\n" +
            "#######...###\n" +
            "#.#...#...#.#\n" +
            "#############\n" +
            "..#...#...#..\n" +
            "..#####...^..");
    var alignmentSum = scaffolding.getIntersections().stream().mapToLong(p -> p.x * p.y).sum();
    Assert.assertEquals(alignmentSum, 76);
  }

  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle17(getStoredInput(17));
    Assert.assertEquals(puzzle.solvePart1(), "8520");
  }

  @Test
  public void testPart2Example1() {
    var scaffolding = new Puzzle17.Scaffolding(
        "#######...#####\n" +
            "#.....#...#...#\n" +
            "#.....#...#...#\n" +
            "......#...#...#\n" +
            "......#...###.#\n" +
            "......#.....#.#\n" +
            "^########...#.#\n" +
            "......#.#...#.#\n" +
            "......#########\n" +
            "........#...#..\n" +
            "....#########..\n" +
            "....#...#......\n" +
            "....#...#......\n" +
            "....#...#......\n" +
            "....#####......");

    var expectedPath = List.of("R", "8", "R", "8", "R", "4", "R", "4", "R", "8", "L", "6", "L", "2", "R", "4", "R", "4",
        "R", "8", "R", "8", "R", "8", "L", "6", "L", "2");
    Assert.assertEquals(scaffolding.getDirections(), expectedPath);

    var inputSequence = scaffolding.findInputSequence().lines().collect(Collectors.toList());
    Assert.assertEquals(inputSequence.size(), 5);
    Assert.assertEquals(scaffolding.findInputSequence(),
        inputSequence.get(0).trim() + "\n"
            + inputSequence.get(1).trim() + "\n"
            + inputSequence.get(2).trim() + "\n"
            + inputSequence.get(3).trim() + "\n"
            + "n\n");

    var routine = Arrays.stream(inputSequence.get(0).trim().split(",")).collect(Collectors.toList());
    var a = Arrays.stream(inputSequence.get(1).trim().split(",")).collect(Collectors.toList());
    var b = Arrays.stream(inputSequence.get(2).trim().split(",")).collect(Collectors.toList());
    var c = Arrays.stream(inputSequence.get(3).trim().split(",")).collect(Collectors.toList());

    var path = routine.stream()
        .flatMap(r -> {
          if (r.equals("A")) return a.stream();
          if (r.equals("B")) return b.stream();
          if (r.equals("C")) return c.stream();
          throw new IllegalStateException("Invalid routine value: " + r);
        })
        .collect(Collectors.toList());
    Assert.assertEquals(path, expectedPath);
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle17(getStoredInput(17));
    Assert.assertEquals(puzzle.solvePart2(), "926819");
  }
}
