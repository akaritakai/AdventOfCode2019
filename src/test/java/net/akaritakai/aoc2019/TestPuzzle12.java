package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestPuzzle12 extends BasePuzzleTest {
  @Test
  public void testPart1Example1() {
    var puzzle = new Puzzle12(
        "<x=-1, y=0, z=2>\n" +
        "<x=2, y=-10, z=-7>\n" +
        "<x=4, y=-8, z=8>\n" +
        "<x=3, y=5, z=-1>");

    var moons = puzzle.getMoons();
    Assert.assertEquals(moons.size(), 4);

    // Step 0
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(-1, 0, 2, 0, 0, 0));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(2, -10, -7, 0, 0, 0));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(4, -8, 8, 0, 0, 0));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(3, 5, -1, 0, 0, 0));
    moons = puzzle.step(moons);

    // Step 1
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(2, -1, 1, 3, -1, -1));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(3, -7, -4, 1, 3, 3));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(1, -7, 5, -3, 1, -3));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(2, 2, 0, -1, -3, 1));
    moons = puzzle.step(moons);

    // Step 2
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(5, -3, -1, 3, -2, -2));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(1, -2, 2, -2, 5, 6));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(1, -4, -1, 0, 3, -6));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(1, -4, 2, -1, -6, 2));
    moons = puzzle.step(moons);

    // Step 3
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(5, -6, -1, 0, -3, 0));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(0, 0, 6, -1, 2, 4));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(2, 1, -5, 1, 5, -4));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(1, -8, 2, 0, -4, 0));
    moons = puzzle.step(moons);

    // Step 4
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(2, -8, 0, -3, -2, 1));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(2, 1, 7, 2, 1, 1));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(2, 3, -6, 0, 2, -1));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(2, -9, 1, 1, -1, -1));
    moons = puzzle.step(moons);

    // Step 5
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(-1, -9, 2, -3, -1, 2));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(4, 1, 5, 2, 0, -2));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(2, 2, -4, 0, -1, 2));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(3, -7, -1, 1, 2, -2));
    moons = puzzle.step(moons);

    // Step 6
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(-1, -7, 3, 0, 2, 1));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(3, 0, 0, -1, -1, -5));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(3, -2, 1, 1, -4, 5));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(3, -4, -2, 0, 3, -1));
    moons = puzzle.step(moons);

    // Step 7
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(2, -2, 1, 3, 5, -2));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(1, -4, -4, -2, -4, -4));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(3, -7, 5, 0, -5, 4));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(2, 0, 0, -1, 4, 2));
    moons = puzzle.step(moons);

    // Step 8
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(5, 2, -2, 3, 4, -3));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(2, -7, -5, 1, -3, -1));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(0, -9, 6, -3, -2, 1));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(1, 1, 3, -1, 1, 3));
    moons = puzzle.step(moons);

    // Step 9
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(5, 3, -4, 0, 1, -2));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(2, -9, -3, 0, -2, 2));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(0, -8, 4, 0, 1, -2));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(1, 1, 5, 0, 0, 2));
    moons = puzzle.step(moons);

    // Step 10
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(2, 1, -3, -3, -2, 1));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(1, -8, 0, -1, 1, 3));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(3, -6, 1, 3, 2, -3));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(2, 0, 4, 1, -1, -1));

    // Energy Test
    Assert.assertEquals(moons.get(0).getPotentialEnergy(), 6);
    Assert.assertEquals(moons.get(0).getKineticEnergy(), 6);
    Assert.assertEquals(moons.get(0).getTotalEnergy(), 36);
    Assert.assertEquals(moons.get(1).getPotentialEnergy(), 9);
    Assert.assertEquals(moons.get(1).getKineticEnergy(), 5);
    Assert.assertEquals(moons.get(1).getTotalEnergy(), 45);
    Assert.assertEquals(moons.get(2).getPotentialEnergy(), 10);
    Assert.assertEquals(moons.get(2).getKineticEnergy(), 8);
    Assert.assertEquals(moons.get(2).getTotalEnergy(), 80);
    Assert.assertEquals(moons.get(3).getPotentialEnergy(), 6);
    Assert.assertEquals(moons.get(3).getKineticEnergy(), 3);
    Assert.assertEquals(moons.get(3).getTotalEnergy(), 18);
  }

  @Test
  public void testPart1Example2() {
    var puzzle = new Puzzle12(
        "<x=-8, y=-10, z=0>\n" +
        "<x=5, y=5, z=10>\n" +
        "<x=2, y=-7, z=3>\n" +
        "<x=9, y=-8, z=-3>");

    var moons = puzzle.getMoons();
    Assert.assertEquals(moons.size(), 4);

    // Step 0
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(-8, -10, 0, 0, 0, 0));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(5, 5, 10, 0, 0, 0));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(2, -7, 3, 0, 0, 0));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(9, -8, -3, 0, 0, 0));
    for (var i = 0; i < 10; i++) {
      moons = puzzle.step(moons);
    }

    // Step 10
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(-9, -10, 1, -2, -2, -1));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(4, 10, 9, -3, 7, -2));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(8, -10, -3, 5, -1, -2));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(5, -10, 3, 0, -4, 5));
    for (var i = 0; i < 10; i++) {
      moons = puzzle.step(moons);
    }

    // Step 20
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(-10, 3, -4, -5, 2, 0));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(5, -25, 6, 1, 1, -4));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(13, 1, 1, 5, -2, 2));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(0, 1, 7, -1, -1, 2));
    for (var i = 0; i < 10; i++) {
      moons = puzzle.step(moons);
    }

    // Step 30
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(15, -6, -9, -5, 4, 0));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(-4, -11, 3, -3, -10, 0));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(0, -1, 11, 7, 4, 3));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(-3, -2, 5, 1, 2, -3));
    for (var i = 0; i < 10; i++) {
      moons = puzzle.step(moons);
    }

    // Step 40
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(14, -12, -4, 11, 3, 0));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(-1, 18, 8, -5, 2, 3));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(-5, -14, 8, 1, -2, 0));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(0, -12, -2, -7, -3, -3));
    for (var i = 0; i < 10; i++) {
      moons = puzzle.step(moons);
    }

    // Step 50
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(-23, 4, 1, -7, -1, 2));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(20, -31, 13, 5, 3, 4));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(-4, 6, 1, -1, 1, -3));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(15, 1, -5, 3, -3, -3));
    for (var i = 0; i < 10; i++) {
      moons = puzzle.step(moons);
    }

    // Step 60
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(36, -10, 6, 5, 0, 3));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(-18, 10, 9, -3, -7, 5));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(8, -12, -3, -2, 1, -7));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(-18, -8, -2, 0, 6, -1));
    for (var i = 0; i < 10; i++) {
      moons = puzzle.step(moons);
    }

    // Step 70
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(-33, -6, 5, -5, -4, 7));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(13, -9, 2, -2, 11, 3));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(11, -8, 2, 8, -6, -7));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(17, 3, 1, -1, -1, -3));
    for (var i = 0; i < 10; i++) {
      moons = puzzle.step(moons);
    }

    // Step 80
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(30, -8, 3, 3, 3, 0));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(-2, -4, 0, 4, -13, 2));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(-18, -7, 15, -8, 2, -2));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(-2, -1, -8, 1, 8, 0));
    for (var i = 0; i < 10; i++) {
      moons = puzzle.step(moons);
    }

    // Step 90
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(-25, -1, 4, 1, -3, 4));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(2, -9, 0, -3, 13, -1));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(32, -8, 14, 5, -4, 6));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(-1, -2, -8, -3, -6, -9));
    for (var i = 0; i < 10; i++) {
      moons = puzzle.step(moons);
    }

    // Step 100
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(8, -12, -9, -7, 3, 0));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(13, 16, -3, 3, -11, -5));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(-29, -11, -1, -3, 7, 4));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(16, -13, 23, 7, 1, 1));

    // Energy Test
    Assert.assertEquals(moons.get(0).getPotentialEnergy(), 29);
    Assert.assertEquals(moons.get(0).getKineticEnergy(), 10);
    Assert.assertEquals(moons.get(0).getTotalEnergy(), 290);
    Assert.assertEquals(moons.get(1).getPotentialEnergy(), 32);
    Assert.assertEquals(moons.get(1).getKineticEnergy(), 19);
    Assert.assertEquals(moons.get(1).getTotalEnergy(), 608);
    Assert.assertEquals(moons.get(2).getPotentialEnergy(), 41);
    Assert.assertEquals(moons.get(2).getKineticEnergy(), 14);
    Assert.assertEquals(moons.get(2).getTotalEnergy(), 574);
    Assert.assertEquals(moons.get(3).getPotentialEnergy(), 52);
    Assert.assertEquals(moons.get(3).getKineticEnergy(), 9);
    Assert.assertEquals(moons.get(3).getTotalEnergy(), 468);
  }

  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle12(getStoredInput(12));
    Assert.assertEquals(puzzle.solvePart1(), "7202");
  }

  @Test
  public void testPart2Example1() {
    var puzzle = new Puzzle12(
        "<x=-1, y=0, z=2>\n" +
            "<x=2, y=-10, z=-7>\n" +
            "<x=4, y=-8, z=8>\n" +
            "<x=3, y=5, z=-1>");

    var originalState = puzzle.getMoons(); // capture moons as they are
    var moons = puzzle.getMoons();
    Assert.assertEquals(moons, originalState);
    Assert.assertNotSame(moons, originalState);
    Assert.assertEquals(moons.size(), 4);

    // Step 0
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(-1, 0, 2, 0, 0, 0));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(2, -10, -7, 0, 0, 0));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(4, -8, 8, 0, 0, 0));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(3, 5, -1, 0, 0, 0));
    for (var i = 0; i < 2770; i++) {
      moons = puzzle.step(moons);
      Assert.assertNotSame(moons, originalState);
    }

    // Step 2770
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(2, -1, 1, -3, 2, 2));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(3, -7, -4, 2, -5, -6));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(1, -7, 5, 0, -3, 6));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(2, 2, 0, 1, 6, -2));
    moons = puzzle.step(moons);
    Assert.assertNotSame(moons, originalState);

    // Step 2771
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(-1, 0, 2, -3, 1, 1));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(2, -10, -7, -1, -3, -3));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(4, -8, 8, 3, -1, 3));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(3, 5, -1, 1, 3, -1));
    moons = puzzle.step(moons);
    Assert.assertNotSame(moons, originalState);

    // Step 2772
    Assert.assertEquals(moons.get(0), new Puzzle12.Moon(-1, 0, 2, 0, 0, 0));
    Assert.assertEquals(moons.get(1), new Puzzle12.Moon(2, -10, -7, 0, 0, 0));
    Assert.assertEquals(moons.get(2), new Puzzle12.Moon(4, -8, 8, 0, 0, 0));
    Assert.assertEquals(moons.get(3), new Puzzle12.Moon(3, 5, -1, 0, 0, 0));
    Assert.assertNotSame(moons, originalState);

    Assert.assertEquals(moons, originalState);
  }

  @Test
  public void testPart2Example2() {
    var puzzle = new Puzzle12(
        "<x=-8, y=-10, z=0>\n" +
            "<x=5, y=5, z=10>\n" +
            "<x=2, y=-7, z=3>\n" +
            "<x=9, y=-8, z=-3>");
    Assert.assertEquals(puzzle.solvePart2(), "4686774924");
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle12(getStoredInput(12));
    Assert.assertEquals(puzzle.solvePart2(), "537881600740876");
  }
}
