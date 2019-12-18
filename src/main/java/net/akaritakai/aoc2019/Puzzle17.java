package net.akaritakai.aoc2019;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.primitives.Ints;
import net.akaritakai.aoc2019.geom2d.Direction;
import net.akaritakai.aoc2019.geom2d.Point;
import net.akaritakai.aoc2019.geom2d.Turn;
import net.akaritakai.aoc2019.intcode.IntcodeVm;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static net.akaritakai.aoc2019.geom2d.Turn.LEFT;
import static net.akaritakai.aoc2019.geom2d.Turn.RIGHT;

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
            vacuumDirection = Direction.NORTH;
            vacuumPosition = new Point(x, y);
            break;
          case '>':
            vacuumDirection = Direction.EAST;
            vacuumPosition = new Point(x, y);
            break;
          case 'v':
            vacuumDirection = Direction.SOUTH;
            vacuumPosition = new Point(x, y);
            break;
          case '<':
            vacuumDirection = Direction.WEST;
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

      // Turn the vacuum toward the scaffolding
      var scaffoldingStart = scaffolding.stream()
          .filter(p -> Math.abs(p.x - vacuumPosition.x) + Math.abs(p.y - vacuumPosition.y) == 1)
          .findAny()
          .orElseThrow(() -> new IllegalStateException("Couldn't find a point next to the vacuum's position"));
      var direction = Direction.fromSegment(vacuumPosition, scaffoldingStart);
      var turn = vacuumDirection.turn(direction);
      vacuumDirection = direction;
      directions.add(serialize(turn));

      while (explored.size() < scaffolding.size()) {
        // Try to go down the path as far as we can go
        var pathLength = 0;
        var forward = move(vacuumPosition, vacuumDirection);
        while (scaffolding.contains(forward)) { // if the scaffolding contains the path forward
          vacuumPosition = forward; // move our vacuum
          explored.add(forward); // mark the path as explored
          pathLength++; // increment the path length
          forward = move(vacuumPosition, vacuumDirection); // look for the next path forward
        }

        // We've gone down the path as far as we can go
        directions.add(String.valueOf(pathLength));

        // Turn towards the next length of scaffolding
        if (explored.size() < scaffolding.size()) {
          direction = vacuumDirection.turn(LEFT);
          if (scaffolding.contains(move(vacuumPosition, direction))) {
            vacuumDirection = direction;
            directions.add(serialize(LEFT));
            continue;
          }
          direction = vacuumDirection.turn(RIGHT);
          if (scaffolding.contains(move(vacuumPosition, direction))) {
            vacuumDirection = direction;
            directions.add(serialize(RIGHT));
            continue;
          }
          throw new IllegalStateException("Scaffolding does not accommodate a turn right or left");
        }
      }

      return Collections.unmodifiableList(directions);
    }

    private static Point move(Point point, Direction direction) {
      switch (direction) {
        case NORTH: return new Point(point.x, point.y - 1); // North on our map means the y value goes down
        case SOUTH: return new Point(point.x, point.y + 1); // South on our map means the y value goes up
        default: return direction.move(point);
      }
    }

    private static String serialize(Turn turn) {
      switch (turn) {
        case LEFT: return "L";
        case RIGHT: return "R";
      }
      throw new UnsupportedOperationException("Unknown turn type " + turn);
    }

    @VisibleForTesting
    synchronized String findInputSequence() {
      if (inputSequence != null) {
        return inputSequence;
      }
      inputSequence = findInputSequence(new ArrayList<>(getDirections()));
      return inputSequence;
    }

    private static String findInputSequence(List<String> input) {
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
              return String.join(",", mainRoutine) + "\n"
                  + String.join(",", a) + "\n"
                  + String.join(",", b) + "\n"
                  + String.join(",", c) + "\n"
                  + "n\n";
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
}
