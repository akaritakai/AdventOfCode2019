package net.akaritakai.aoc2019;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.primitives.Ints;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Puzzle17 extends AbstractPuzzle {

  public Puzzle17(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 17;
  }

  @Override
  public String solvePart1() {
    var output = new StringBuilder();
    var vm = new IntcodeVm(getPuzzleInput(), () -> null, c -> output.append(Character.toString(Math.toIntExact(c))));
    vm.run();
    var scaffolding = new Scaffolding(output.toString());
    var alignmentSum = scaffolding.getIntersections().stream().mapToLong(p -> p.x * p.y).sum();
    return String.valueOf(alignmentSum);
  }

  @Override
  public String solvePart2() {
    var output = new StringBuilder();
    var vm = new IntcodeVm(getPuzzleInput(), () -> null, c -> output.append(Character.toString(Math.toIntExact(c))));
    vm.run();
    var scaffolding = new Scaffolding(output.toString());
    var input = scaffolding.findInputSequence().chars().asLongStream().iterator();
    var dust = new AtomicLong();
    vm = new IntcodeVm(getPuzzleInput(), input::next, dust::set);
    vm.memory().put(0L, 2L);
    vm.run();
    return String.valueOf(dust.get());
  }

  @VisibleForTesting
  static class Scaffolding {
    private Point vacuumPosition;
    private Direction vacuumDirection;
    private Set<Point> scaffolding = new HashSet<>();
    private List<String> directions;
    private String inputSequence;

    @VisibleForTesting
    Scaffolding(String input) {
      var x = 0;
      var y = 0;
      for (char c : input.toCharArray()) {
        switch (c) {
          case '#':
            scaffolding.add(new Point(x, y));
            break;
          case '^':
            vacuumDirection = Direction.UP;
            vacuumPosition = new Point(x, y);
            break;
          case '>':
            vacuumDirection = Direction.RIGHT;
            vacuumPosition = new Point(x, y);
            break;
          case 'v':
            vacuumDirection = Direction.DOWN;
            vacuumPosition = new Point(x, y);
            break;
          case '<':
            vacuumDirection = Direction.LEFT;
            vacuumPosition = new Point(x, y);
            break;
          case '\n':
            x = -1;
            y++;
            break;
        }
        x++;
      }
    }

    @VisibleForTesting
    Set<Point> getIntersections() {
      return scaffolding.stream()
          .filter(p -> scaffolding.contains(new Point(p.x, p.y - 1))
              && scaffolding.contains(new Point(p.x, p.y + 1))
              && scaffolding.contains(new Point(p.x - 1, p.y))
              && scaffolding.contains(new Point(p.x + 1, p.y)))
          .collect(Collectors.toSet());
    }

    @VisibleForTesting
    synchronized List<String> getDirections() {
      if (directions != null) {
        return Collections.unmodifiableList(directions);
      }
      directions = new ArrayList<>();
      var explored = new HashSet<Point>();

      var turn = getFirstTurn();
      vacuumDirection = vacuumDirection.turn(turn);
      directions.add(turn.toString());
      while (explored.size() < scaffolding.size()) {
        // Try to go down the path as far as we can go
        var pathLength = 0;
        var forward = vacuumDirection.move(vacuumPosition);
        while (scaffolding.contains(forward)) { // if the scaffolding contains this
          vacuumPosition = forward; // move our vacuum
          explored.add(forward); // mark the path as explored
          pathLength++; // increment the path length
          forward = vacuumDirection.move(vacuumPosition); // look for the next path forward
        }

        // We've gone down the path as far as we can go
        directions.add(String.valueOf(pathLength));

        if (explored.size() < scaffolding.size()) {
          var direction = vacuumDirection.turn(Turn.LEFT);
          if (scaffolding.contains(direction.move(vacuumPosition))) {
            vacuumDirection = direction;
            directions.add(Turn.LEFT.toString());
            continue;
          }
          direction = vacuumDirection.turn(Turn.RIGHT);
          if (scaffolding.contains(direction.move(vacuumPosition))) {
            vacuumDirection = direction;
            directions.add(Turn.RIGHT.toString());
            continue;
          }
          throw new IllegalStateException("Scaffolding does not accommodate a turn right or left");
        }
      }

      return Collections.unmodifiableList(directions);
    }

    private Turn getFirstTurn() {
      var point1 = scaffolding.stream()
          .filter(p -> Math.abs(p.x - vacuumPosition.x) + Math.abs(p.y - vacuumPosition.y) == 1)
          .findAny()
          .orElseThrow(() -> new IllegalStateException("Couldn't find a point next to the vacuum's position"));
      var point2 = scaffolding.stream()
          .filter(p -> Math.abs(p.x - point1.x) + Math.abs(p.y - point1.y) == 1)
          .findAny()
          .orElseThrow(() -> new IllegalStateException("Couldn't find a point adjacent to vacuum's first move"));
      var desiredDirection = Direction.getLineDirection(vacuumPosition, point1, point2);
      return Turn.of(vacuumDirection, desiredDirection);
    }

    @VisibleForTesting
    synchronized String findInputSequence() {
      if (inputSequence != null) {
        return inputSequence;
      }
      var input = new ArrayList<>(getDirections());
      for (var aLength = input.size(); aLength >= 0; aLength--) {
        var a = input.subList(input.size() - aLength, input.size());
        if (serializedSize(a) > 20) {
          continue;
        }
        var aInput = removePattern(input.subList(0, input.size() - aLength), a);
        for (var bLength = aInput.size(); bLength >= 0; bLength--) {
          var b = aInput.subList(aInput.size() - bLength, aInput.size());
          if (serializedSize(b) > 20) {
            continue;
          }
          var bInput = removePattern(aInput.subList(0, aInput.size() - bLength), b);
          for (var cLength = bInput.size(); cLength >= 0; cLength--) {
            var c = bInput.subList(bInput.size() - cLength, bInput.size());
            if (serializedSize(c) > 20) {
              continue;
            }
            var cInput = removePattern(bInput.subList(0, bInput.size() - cLength), c);
            if (cInput.isEmpty()) {
              var mainRoutine = mainRoutine(input, a, b, c);
              if (serializedSize(mainRoutine) > 20) {
                continue;
              }
              inputSequence = String.join(",", mainRoutine) + "\n"
                  + String.join(",", a) + "\n"
                  + String.join(",", b) + "\n"
                  + String.join(",", c) + "\n"
                  + "n\n";
              return inputSequence;
            }
          }
        }
      }
      throw new IllegalStateException("Unable to find valid routine for path");
    }

    private static List<String> removePattern(List<String> input, List<String> pattern) {
      var newInput = new ArrayList<>(input);
      if (!pattern.isEmpty()) {
        var i = 0;
        while (i <= newInput.size() - pattern.size()) {
          var match = newInput.subList(i, i + pattern.size());
          if (match.equals(pattern)) {
            match.clear();
          } else {
            i++;
          }
        }
      }
      return newInput;
    }

    private static List<String> mainRoutine(List<String> input, List<String> a, List<String> b, List<String> c) {
      input = new ArrayList<>(input);
      var routine = new ArrayList<String>();
      while (!input.isEmpty()) {
        if (input.size() >= a.size() && input.subList(0, a.size()).equals(a)) {
          input.subList(0, a.size()).clear();
          routine.add("A");
          continue;
        }
        if (input.size() >= b.size() && input.subList(0, b.size()).equals(b)) {
          input.subList(0, b.size()).clear();
          routine.add("B");
          continue;
        }
        if (input.size() >= c.size() && input.subList(0, c.size()).equals(c)) {
          input.subList(0, c.size()).clear();
          routine.add("C");
        }
      }
      return routine;
    }

    private static int serializedSize(List<String> input) {
      var tokensSize = input.stream().mapToInt(String::length).sum();
      var commasSize = Ints.constrainToRange(input.size() - 1, 0, Integer.MAX_VALUE);
      var newLineSize = 1;
      return tokensSize + commasSize + newLineSize;
    }
  }

  private enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
      this.dx = dx;
      this.dy = dy;
    }

    private Point move(Point point) {
      return new Point(point.x + dx, point.y + dy);
    }

    private static boolean isStraightLine(List<Point> points) {
      return points.stream().map(p -> p.x).distinct().count() == 1
          || points.stream().map(p -> p.y).distinct().count() == 1;
    }

    private static Direction getLineDirection(Point... points) {
      return getLineDirection(Arrays.stream(points).collect(Collectors.toList()));
    }

    private static Direction getLineDirection(List<Point> points) {
      if (!isStraightLine(points)) {
        return null;
      }

      int dx = points.get(points.size() - 1).x - points.get(0).x;
      int dy = points.get(points.size() - 1).y - points.get(0).y;

      if (dy > 0) return UP;
      if (dy < 0) return DOWN;
      if (dx > 0) return RIGHT;
      if (dx < 0) return LEFT;

      throw new IllegalStateException("Line has no 2 distinct points");
    }

    private Direction turn(Turn turn) {
      switch (this) {
        case UP:
          if (turn == Turn.LEFT) return LEFT;
          if (turn == Turn.RIGHT) return RIGHT;
          break;
        case DOWN:
          if (turn == Turn.LEFT) return RIGHT;
          if (turn == Turn.RIGHT) return LEFT;
          break;
        case LEFT:
          if (turn == Turn.LEFT) return DOWN;
          if (turn == Turn.RIGHT) return UP;
          break;
        case RIGHT:
          if (turn == Turn.LEFT) return UP;
          if (turn == Turn.RIGHT) return DOWN;
          break;
      }
      throw new UnsupportedOperationException("Unknown direction or turn");
    }
  }

  private enum Turn {
    LEFT,
    RIGHT;

    private static Turn of(Direction start, Direction end) {
      switch (start) {
        case UP:
          if (end == Direction.LEFT) return LEFT;
          if (end == Direction.RIGHT) return RIGHT;
          break;
        case DOWN:
          if (end == Direction.RIGHT) return LEFT;
          if (end == Direction.LEFT) return RIGHT;
          break;
        case LEFT:
          if (end == Direction.DOWN) return LEFT;
          if (end == Direction.UP) return RIGHT;
          break;
        case RIGHT:
          if (end == Direction.UP) return LEFT;
          if (end == Direction.DOWN) return RIGHT;
          break;
      }
      throw new IllegalStateException("Direction change is not a 90 degree turn (i.e. 0 degrees or 180 degrees)");
    }

    @Override
    public String toString() {
      switch (this) {
        case LEFT: return "L";
        case RIGHT: return "R";
      }
      throw new UnsupportedOperationException("Turn cannot be converted into String");
    }
  }
}
