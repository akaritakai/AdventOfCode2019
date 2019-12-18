package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;


public class TestPuzzle18 extends BasePuzzleTest {
  @Test
  public void testPart1Example1() {
    var puzzle = new Puzzle18(
        "#########\n" +
            "#b.A.@.a#\n" +
            "#########");
    Assert.assertEquals(puzzle.solvePart1(), "8");
  }

  @Test
  public void testPart1Example2() {
    var puzzle = new Puzzle18(
        "########################\n" +
            "#f.D.E.e.C.b.A.@.a.B.c.#\n" +
            "######################.#\n" +
            "#d.....................#\n" +
            "########################");
    Assert.assertEquals(puzzle.solvePart1(), "86");
  }

  @Test
  public void testPart1Example3() {
    var puzzle = new Puzzle18(
        "########################\n" +
            "#...............b.C.D.f#\n" +
            "#.######################\n" +
            "#.....@.a.B.c.d.A.e.F.g#\n" +
            "########################");
    Assert.assertEquals(puzzle.solvePart1(), "132");
  }

  @Test
  public void testPart1Example4() {
    var puzzle = new Puzzle18(
        "#################\n" +
            "#i.G..c...e..H.p#\n" +
            "########.########\n" +
            "#j.A..b...f..D.o#\n" +
            "########@########\n" +
            "#k.E..a...g..B.n#\n" +
            "########.########\n" +
            "#l.F..d...h..C.m#\n" +
            "#################");
    Assert.assertEquals(puzzle.solvePart1(), "136");
  }

  @Test
  public void testPart1Example5() {
    var puzzle = new Puzzle18(
        "########################\n" +
            "#@..............ac.GI.b#\n" +
            "###d#e#f################\n" +
            "###A#B#C################\n" +
            "###g#h#i################\n" +
            "########################");
    Assert.assertEquals(puzzle.solvePart1(), "81");
  }

  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle18(getStoredInput(18));
    Assert.assertEquals(puzzle.solvePart1(), "3646");
  }

  @Test
  public void testPart2Example1() {
    var puzzle = new Puzzle18(
        "#######\n" +
            "#a.#Cd#\n" +
            "##...##\n" +
            "##.@.##\n" +
            "##...##\n" +
            "#cB#Ab#\n" +
            "#######");
    Assert.assertEquals(puzzle.solvePart2(), "8");
  }

  @Test
  public void testPart2Example2() {
    var puzzle = new Puzzle18(
        "###############\n" +
            "#d.ABC.#.....a#\n" +
            "######...######\n" +
            "######.@.######\n" +
            "######...######\n" +
            "#b.....#.....c#\n" +
            "###############");
    Assert.assertEquals(puzzle.solvePart2(), "24");
  }

  @Test
  public void testPart2Example3() {
    var puzzle = new Puzzle18(
        "#############\n" +
            "#DcBa.#.GhKl#\n" +
            "#.###...#I###\n" +
            "#e#d#.@.#j#k#\n" +
            "###C#...###J#\n" +
            "#fEbA.#.FgHi#\n" +
            "#############");
    Assert.assertEquals(puzzle.solvePart2(), "32");
  }

  @Test
  public void testPart2Example4() {
    var puzzle = new Puzzle18(
        "#############\n" +
            "#g#f.D#..h#l#\n" +
            "#F###e#E###.#\n" +
            "#dCba...BcIJ#\n" +
            "#####.@.#####\n" +
            "#nK.L...G...#\n" +
            "#M###N#H###.#\n" +
            "#o#m..#i#jk.#\n" +
            "#############");
    Assert.assertEquals(puzzle.solvePart2(), "72");
  }

  @Test
  public void testPart2Example5() {
    // Tricky pitfall provided by /u/AlphaDart1337
    // https://www.reddit.com/r/adventofcode/comments/ec8090/2019_day_18_solutions/fb9whyt/

    var longPath = 24; // One of the robots needs to walk around the door to get the key
    var shortPath = 14; // The remaining robot can take the direct path since the door is open

    // The idea is that it is not possible to apply solutions to all four vaults independently and sum up the results.
    // An incorrect implementation might calculate longPath + longPath
    // But the correct implementation will calculate longPath + shortPath
    var puzzle = new Puzzle18(
        "#################################\n" +
            "#...............#...............#\n" +
            "#.#############.#.#############.#\n" +
            "#.#############.#.#############.#\n" +
            "#.#############.#.#############.#\n" +
            "#.#############.#.#############.#\n" +
            "#bA...........................Ba#\n" +
            "###############.@.###############\n" +
            "#...............................#\n" +
            "#...............#...............#\n" +
            "#...............#...............#\n" +
            "#...............#...............#\n" +
            "#################################");
    Assert.assertEquals(puzzle.solvePart2(), String.valueOf(longPath + shortPath));
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle18(getStoredInput(18));
    Assert.assertEquals(puzzle.solvePart2(), "1730");
  }
}
