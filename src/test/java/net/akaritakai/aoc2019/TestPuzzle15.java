package net.akaritakai.aoc2019;

import net.akaritakai.aoc2019.Puzzle15.Direction;
import net.akaritakai.aoc2019.Puzzle15.DroidState;
import net.akaritakai.aoc2019.Puzzle15.ShipState;
import net.akaritakai.aoc2019.Puzzle15.State;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.Set;

import static net.akaritakai.aoc2019.Puzzle15.Direction.*;


public class TestPuzzle15 extends BasePuzzleTest {
  @Test
  public void testPart1Example1() {
    // Isomorphic to the example given in part 1
    var droid = new MockDroidState(from(SOUTH, WEST),
        Set.of(from(), from(EAST), from(EAST, EAST), from(SOUTH), from(SOUTH, WEST)));
    var ship = new ShipState(droid);
    Assert.assertEquals(ship.distanceToOxygen(), 2);
  }

  @Test
  public void testSolvePart1() throws Exception {
    var puzzle = new Puzzle15(getStoredInput(15));
    Assert.assertEquals(puzzle.solvePart1(), "280");
  }

  @Test
  public void testPart2Example1() {
    // Isomorphic to the example given in part 2
    var droid = new MockDroidState(
        from(WEST, SOUTH, SOUTH, EAST),
        Set.of(
            from(),
            from(WEST),
            from(WEST, SOUTH),
            from(WEST, SOUTH, SOUTH),
            from(WEST, SOUTH, SOUTH, EAST),
            from(WEST, SOUTH, SOUTH, EAST, EAST),
            from(WEST, SOUTH, SOUTH, EAST, EAST, NORTH),
            from(WEST, SOUTH, SOUTH, EAST, EAST, NORTH, EAST)));
    var ship = new ShipState(droid);
    Assert.assertEquals(ship.longestPathFromOxygen(), 4);
  }

  @Test
  public void testSolvePart2() throws Exception {
    var puzzle = new Puzzle15(getStoredInput(15));
    Assert.assertEquals(puzzle.solvePart2(), "400");
  }

  private static Point from(Direction... directions) {
    var point = DroidState.STARTING_POSITION;
    for (Direction direction : directions) {
      point = direction.move(point);
    }
    return point;
  }

  private static class MockDroidState implements DroidState {
    private Set<Point> _reachable;
    private Point _oxygen;
    private Point _location = DroidState.STARTING_POSITION;

    public MockDroidState(Point oxygen, Set<Point> reachable) {
      _oxygen = oxygen;
      _reachable = reachable;
    }

    @Override
    public State move(Direction direction) {
      Point p = direction.move(_location);
      if (p.equals(_oxygen)) {
        _location = p;
        return State.OXYGEN_SYSTEM;
      }
      if (_reachable.contains(p)) {
        _location = p;
        return State.EMPTY;
      }
      return State.WALL;
    }

    @Override
    public Point getLocation() {
      return _location;
    }

    @Override
    public void close() {
    }
  }
}
