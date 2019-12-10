package net.akaritakai.aoc2019;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle10 extends AbstractPuzzle {

  public Puzzle10(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 10;
  }

  @Override
  public String solvePart1() {
    var asteroids = getAsteroids();
    var maxCount = asteroids.stream()
        .mapToInt(asteroid -> getColinearPointRays(asteroid, asteroids).size())
        .max()
        .orElse(0);
    return String.valueOf(maxCount);
  }

  @Override
  public String solvePart2() {
    var asteroids = getAsteroids();
    var station = asteroids.stream()
        .max(Comparator.comparingInt(asteroid -> getColinearPointRays(asteroid, asteroids).size()))
        .orElseThrow(() -> new IllegalStateException("No stations found"));
    asteroids.remove(station);
    var orderedRays = getColinearPointRays(station, asteroids);
    var bet = IntStream.iterate(0, i -> i + 1)
        .mapToObj(i -> {
          var ray = orderedRays.get(i % orderedRays.size());
          return ray.isEmpty() ? null : ray.remove(0);
        })
        .filter(Objects::nonNull)
        .skip(199)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Less than 200 asteroids present"));
    return String.valueOf(bet.x * 100 + bet.y);
  }

  private static List<List<Point>> getColinearPointRays(Point origin, Set<Point> points) {
    return points.stream()
        .collect(Collectors.groupingBy(asteroid -> angle(origin, asteroid), Collectors.toSet()))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByKey())
        .map(e -> e.getValue()
            .stream()
            .sorted(Comparator.comparingLong(body -> Math.abs(origin.x - body.x) + Math.abs(origin.y - body.y)))
            .collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  private static double angle(Point origin, Point point) {
    var angle = Math.toDegrees(Math.atan2(point.y - origin.y, point.x - origin.x));
    if (angle < 0) {
      angle += 360;
    }
    if (angle < 270) {
      angle += 360;
    }
    angle -= 270;
    return angle;
  }

  private Set<Point> getAsteroids() {
    var points = new HashSet<Point>();
    var lines = getPuzzleInput().lines().iterator();
    for (var y = 0; lines.hasNext(); y++) {
      var line = lines.next().chars().iterator();
      for (var x = 0; line.hasNext(); x++) {
        if (line.next() == '#') {
          points.add(new Point(x, y));
        }
      }
    }
    return points;
  }
}
