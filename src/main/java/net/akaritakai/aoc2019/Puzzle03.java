package net.akaritakai.aoc2019;

import com.google.common.collect.Sets;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle03 extends AbstractPuzzle {
  private static final Point ORIGIN = new Point(0, 0);

  public Puzzle03(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 3;
  }

  @Override
  public String solvePart1() {
    Set<Point> intersections = getIntersections(getWires());
    var minDistance = intersections.stream()
        .mapToInt(p -> Math.abs(p.x) + Math.abs(p.y))
        .min()
        .orElseThrow(() -> new IllegalArgumentException("No points available"));
    return String.valueOf(minDistance);
  }

  @Override
  public String solvePart2() {
    var wires = getWires();
    var minSteps = getIntersections(wires).stream()
        .mapToInt(intersection -> wires.stream().mapToInt(wire -> numStepsToReachPoint(wire, intersection)).sum())
        .min()
        .orElseThrow(() -> new IllegalArgumentException("No points available"));
    return String.valueOf(minSteps);
  }

  private int numStepsToReachPoint(List<String> wireInstructions, Point point) {
    var steps = 0;
    var x = ORIGIN.x;
    var y = ORIGIN.y;
    for (String instruction : wireInstructions) {
      var direction = instruction.substring(0, 1);
      var length = Integer.parseInt(instruction.substring(1));
      switch (direction) {
        case "U" -> {
          while (length-- > 0) {
            steps++;
            y++;
            if (x == point.x && y == point.y) {
              return steps;
            }
          }
        }
        case "D" -> {
          while (length-- > 0) {
            steps++;
            y--;
            if (x == point.x && y == point.y) {
              return steps;
            }
          }
        }
        case "L" -> {
          while (length-- > 0) {
            steps++;
            x--;
            if (x == point.x && y == point.y) {
              return steps;
            }
          }
        }
        case "R" -> {
          while (length-- > 0) {
            steps++;
            x++;
            if (x == point.x && y == point.y) {
              return steps;
            }
          }
        }
      }
    }
    throw new IllegalStateException("Point not reachable on the wire");
  }

  private Set<Point> getIntersections(List<List<String>> wires) {
    Set<Point> intersections = new HashSet<>();
    var wirePoints = wires.stream().map(this::getWirePoints).collect(Collectors.toList());
    for (int i = 0; i < wires.size(); i++) {
      for (int j = i + 1; j < wires.size(); j++) {
        var wire1 = wirePoints.get(i);
        var wire2 = wirePoints.get(j);
        Sets.intersection(wire1, wire2)
            .stream()
            .filter(p -> !p.equals(ORIGIN))
            .forEach(intersections::add);
      }
    }
    return intersections;
  }

  private Set<Point> getWirePoints(List<String> wire) {
    Set<Point> points = new HashSet<>();
    points.add(ORIGIN);
    var x = ORIGIN.x;
    var y = ORIGIN.y;
    for (String instruction : wire) {
      var direction = instruction.substring(0, 1);
      var length = Integer.parseInt(instruction.substring(1));
      switch (direction) {
        case "U" -> {
          while (length-- > 0) {
            points.add(new Point(x, ++y));
          }
        }
        case "D" -> {
          while (length-- > 0) {
            points.add(new Point(x, --y));
          }
        }
        case "L" -> {
          while (length-- > 0) {
            points.add(new Point(--x, y));
          }
        }
        case "R" -> {
          while (length-- > 0) {
            points.add(new Point(++x, y));
          }
        }
      }
    }
    return points;
  }

  public List<List<String>> getWires() {
    return getPuzzleInput().lines()
        .map(line -> Arrays.stream(line.split(",")).collect(Collectors.toList()))
        .collect(Collectors.toList());
  }
}
