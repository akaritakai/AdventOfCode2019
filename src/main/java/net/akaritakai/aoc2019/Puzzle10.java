package net.akaritakai.aoc2019;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

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
        .mapToInt(asteroid -> getColinearPoints(asteroid, asteroids).size())
        .max()
        .orElse(0);
    return String.valueOf(maxCount);
  }

  @Override
  public String solvePart2() {
    var asteroids = getAsteroids();
    var station = asteroids.stream()
        .max(Comparator.comparingInt(asteroid -> getColinearPoints(asteroid, asteroids).size()))
        .orElseThrow(() -> new IllegalStateException("No stations found"));
    asteroids.remove(station);
    var colinearPoints = getColinearPoints(station, asteroids);
    Point erased = null;
    for (int i = 0; i < 200; i++) {
      erased = colinearPoints.get(i % colinearPoints.size()).remove(0);
    }
    return String.valueOf(erased.x * 100 + erased.y);
  }

  private static List<List<Point>> getColinearPoints(Point origin, Set<Point> points) {
    return points.stream()
        .collect(Collectors.groupingBy(asteroid -> angle(origin, asteroid), Collectors.toSet()))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByKey())
        .map(e -> e.getValue()
            .stream()
            .sorted(Comparator.comparingLong(asteroid -> Math.abs(origin.x - asteroid.x) + Math.abs(origin.y - asteroid.y)))
            .collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  private static double angle(Point origin, Point point) {
    double angle = Math.toDegrees(Math.atan2(point.y - origin.y, point.x - origin.x));
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
    for (int y = 0; lines.hasNext(); y++) {
      var line = lines.next().chars().iterator();
      for (int x = 0; line.hasNext(); x++) {
        if (line.next() == '#') {
          points.add(new Point(x, y));
        }
      }
    }
    return points;
  }
}
