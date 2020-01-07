package net.akaritakai.aoc2019;

import net.akaritakai.aoc2019.Puzzle24.ComplexEris;
import net.akaritakai.aoc2019.Puzzle24.SimpleEris;
import net.akaritakai.aoc2019.geom2d.Point;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;


public class TestPuzzle24 extends BasePuzzleTest {
  @Test
  public void testPart1Example1() {
    var eris = new SimpleEris("" +
        "....#\n" +
        "#..#.\n" +
        "#..##\n" +
        "..#..\n" +
        "#....");

    // After 1 minute
    eris.evolve();
    Assert.assertEquals(eris, new SimpleEris("" +
        "#..#.\n" +
        "####.\n" +
        "###.#\n" +
        "##.##\n" +
        ".##.."));

    // After 2 minutes
    eris.evolve();
    Assert.assertEquals(eris, new SimpleEris("" +
        "#####\n" +
        "....#\n" +
        "....#\n" +
        "...#.\n" +
        "#.###"));

    // After 3 minutes
    eris.evolve();
    Assert.assertEquals(eris, new SimpleEris("" +
        "#....\n" +
        "####.\n" +
        "...##\n" +
        "#.##.\n" +
        ".##.#"));

    // After 4 minutes
    eris.evolve();
    Assert.assertEquals(eris, new SimpleEris("" +
        "####.\n" +
        "....#\n" +
        "##..#\n" +
        ".....\n" +
        "##..."));
  }

  @Test
  public void testPart1Example2() {
    var eris = new SimpleEris("" +
        "....#\n" +
        "#..#.\n" +
        "#..##\n" +
        "..#..\n" +
        "#....");

    var states = new HashSet<Set<Point>>();
    while (states.add(eris.bugs)) {
      eris.evolve();
    }

    Assert.assertEquals(eris, new SimpleEris("" +
        ".....\n" +
        ".....\n" +
        ".....\n" +
        "#....\n" +
        ".#..."));
  }

  @Test
  public void testPart1Example3() {
    var eris = new SimpleEris("" +
        ".....\n" +
        ".....\n" +
        ".....\n" +
        "#....\n" +
        ".#...");
    Assert.assertEquals(eris.biodiversityRating(), 2129920);
  }

  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle24(getStoredInput(24));
    Assert.assertEquals(puzzle.solvePart1(), "12129040");
  }

  @Test
  public void testPart2Example1() {
    var eris = new ComplexEris("" +
        "....#\n" +
        "#..#.\n" +
        "#..##\n" +
        "..#..\n" +
        "#....");
    for (var i = 0; i < 10; i++) {
      eris.evolve();
    }

    Assert.assertTrue(eris.bugs.stream().noneMatch(p -> p.z < -5));
    Assert.assertEquals(eris.bugs.stream().filter(p -> p.z == -5).count(), 7);
    Assert.assertEquals(eris.bugs.stream().filter(p -> p.z == -4).count(), 6);
    Assert.assertEquals(eris.bugs.stream().filter(p -> p.z == -3).count(), 6);
    Assert.assertEquals(eris.bugs.stream().filter(p -> p.z == -2).count(), 10);
    Assert.assertEquals(eris.bugs.stream().filter(p -> p.z == -1).count(), 10);
    Assert.assertEquals(eris.bugs.stream().filter(p -> p.z == 0).count(), 5);
    Assert.assertEquals(eris.bugs.stream().filter(p -> p.z == 1).count(), 15);
    Assert.assertEquals(eris.bugs.stream().filter(p -> p.z == 2).count(), 12);
    Assert.assertEquals(eris.bugs.stream().filter(p -> p.z == 3).count(), 7);
    Assert.assertEquals(eris.bugs.stream().filter(p -> p.z == 4).count(), 9);
    Assert.assertEquals(eris.bugs.stream().filter(p -> p.z == 5).count(), 12);
    Assert.assertTrue(eris.bugs.stream().noneMatch(p -> p.z > 5));
  }

  @Test
  public void testPart2Example2() {
    var eris = new ComplexEris("" +
        "....#\n" +
        "#..#.\n" +
        "#..##\n" +
        "..#..\n" +
        "#....");
    for (var i = 0; i < 10; i++) {
      eris.evolve();
    }
    Assert.assertEquals(eris.bugs.size(), 99);
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle24(getStoredInput(24));
    Assert.assertEquals(puzzle.solvePart2(), "2109");
  }
}
