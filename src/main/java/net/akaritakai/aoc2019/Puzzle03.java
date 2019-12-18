package net.akaritakai.aoc2019;

import com.google.common.collect.Sets;
import net.akaritakai.aoc2019.geom2d.Direction;
import net.akaritakai.aoc2019.geom2d.Point;

import java.util.*;
import java.util.stream.Collectors;

public class Puzzle03 extends AbstractPuzzle {

  public Puzzle03(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 3;
  }

  @Override
  public String solvePart1() {
    Set<Point> intersections = getAllIntersections(getWires());
    var minDistance = intersections.stream()
        .mapToLong(p -> Math.abs(p.x) + Math.abs(p.y))
        .min()
        .orElseThrow(() -> new IllegalArgumentException("No points available"));
    return String.valueOf(minDistance);
  }

  @Override
  public String solvePart2() {
    var wires = getWires();
    var minSteps = getAllIntersections(wires).stream()
        .mapToInt(intersection -> wires.stream().mapToInt(wire -> wire.getWireLength(intersection)).sum())
        .min()
        .orElseThrow(() -> new IllegalArgumentException("No points available"));
    return String.valueOf(minSteps);
  }

  private List<Wire> getWires() {
    return getPuzzleInput().trim().lines().map(Wire::new).collect(Collectors.toList());
  }

  private Set<Point> getAllIntersections(List<Wire> wires) {
    Set<Point> intersections = new HashSet<>();
    for (var i = 0; i < wires.size(); i++) {
      for (var j = i + 1; j < wires.size(); j++) {
        var wire1 = wires.get(i);
        var wire2 = wires.get(j);
        intersections.addAll(wire1.getIntersections(wire2));
      }
    }
    return intersections;
  }

  private static class Wire {
    private Map<Point, Integer> _pointLengthMap = new HashMap<>();

    private Wire(String instructionString) {
      var position = new Point(0, 0);
      var length = 0;
      for (String instruction : instructionString.split(",")) {
        var direction = getDirection(instruction.substring(0, 1));
        var distance = Integer.parseInt(instruction.substring(1));
        while (distance-- > 0) {
          position = direction.move(position);
          _pointLengthMap.put(position, ++length);
        }
      }
    }

    private Set<Point> getIntersections(Wire other) {
      return Sets.intersection(_pointLengthMap.keySet(), other._pointLengthMap.keySet());
    }

    private int getWireLength(Point point) {
      return _pointLengthMap.getOrDefault(point, 0);
    }

    private static Direction getDirection(String instruction) {
      switch (instruction) {
        case "U": return Direction.NORTH;
        case "D": return Direction.SOUTH;
        case "L": return Direction.WEST;
        case "R": return Direction.EAST;
      }
      throw new IllegalArgumentException("Invalid direction " + instruction);
    }
  }
}
