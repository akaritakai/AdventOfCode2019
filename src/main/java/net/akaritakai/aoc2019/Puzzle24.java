package net.akaritakai.aoc2019;

import com.google.common.annotations.VisibleForTesting;
import net.akaritakai.aoc2019.geom2d.Point;
import net.akaritakai.aoc2019.geom3d.Point3D;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Puzzle24 extends AbstractPuzzle {

  public Puzzle24(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 24;
  }

  @Override
  public String solvePart1() {
    var eris = new SimpleEris(getPuzzleInput());
    var states = new HashSet<Set<Point>>();
    while (states.add(eris.bugs)) {
      eris.evolve();
    }
    return String.valueOf(eris.biodiversityRating());
  }

  @Override
  public String solvePart2() {
    var eris = new ComplexEris(getPuzzleInput());
    for (int i = 0; i < 200; i++) {
      eris.evolve();
    }
    return String.valueOf(eris.bugs.size());
  }

  @VisibleForTesting
  static class SimpleEris {
    Set<Point> bugs = new HashSet<>();

    SimpleEris(String input) {
      var lines = input.lines().collect(Collectors.toList());
      for (var y = 0; y < 5; y++) {
        var line = lines.get(y);
        for (var x = 0; x < 5; x++) {
          if (line.charAt(x) == '#') {
            bugs.add(new Point(x, y));
          }
        }
      }
    }

    void evolve() {
      var newBugs = new HashSet<Point>();
      for (var x = 0; x < 5; x++) {
        for (var y = 0; y < 5; y++) {
          var point = new Point(x, y);
          var bugCount = bugCount(point);
          var isBug = bugs.contains(point);
          if ((!isBug && (bugCount == 1 || bugCount == 2)) || (isBug && bugCount == 1)) {
            newBugs.add(point);
          }
        }
      }
      bugs = newBugs;
    }

    long bugCount(Point position) {
      return position.adjacentPoints()
          .stream()
          .filter(p -> bugs.contains(p))
          .count();
    }

    long biodiversityRating() {
      var i = 0;
      long rating = 0;
      for (var y = 0; y < 5; y++) {
        for (var x = 0; x < 5; x++) {
          if (bugs.contains(new Point(x, y))) {
            rating += 1 << i;
          }
          i++;
        }
      }
      return rating;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      SimpleEris that = (SimpleEris) o;
      return Objects.equals(bugs, that.bugs);
    }

    @Override
    public int hashCode() {
      return Objects.hash(bugs);
    }
  }

  @VisibleForTesting
  static class ComplexEris {
    Set<Point3D> bugs = new HashSet<>();

    ComplexEris(String input) {
      var lines = input.lines().collect(Collectors.toList());
      for (var y = 0; y < 5; y++) {
        var line = lines.get(y);
        for (var x = 0; x < 5; x++) {
          if (line.charAt(x) == '#') {
            bugs.add(new Point3D(x, y, 0));
          }
        }
      }
    }

    Set<Point3D> adjacent(Point3D point) {
      var points = new HashSet<Point3D>();

      // Add points on the same level
      points.add(new Point3D(point.x - 1, point.y, point.z));
      points.add(new Point3D(point.x + 1, point.y, point.z));
      points.add(new Point3D(point.x, point.y - 1, point.z));
      points.add(new Point3D(point.x, point.y + 1, point.z));

      // Top outer edge
      if (point.y == 0) {
        points.add(new Point3D(2, 1, point.z - 1));
      }
      // Bottom outer edge
      if (point.y == 4) {
        points.add(new Point3D(2, 3, point.z - 1));
      }
      // Left outer edge
      if (point.x == 0) {
        points.add(new Point3D(1, 2, point.z - 1));
      }
      // Right outer edge
      if (point.x == 4) {
        points.add(new Point3D(3, 2, point.z - 1));
      }

      // Top inner edge
      if (point.x == 2 && point.y == 1) {
        points.add(new Point3D(0, 0, point.z + 1));
        points.add(new Point3D(1, 0, point.z + 1));
        points.add(new Point3D(2, 0, point.z + 1));
        points.add(new Point3D(3, 0, point.z + 1));
        points.add(new Point3D(4, 0, point.z + 1));
      }
      // Bottom inner edge
      if (point.x == 2 && point.y == 3) {
        points.add(new Point3D(0, 4, point.z + 1));
        points.add(new Point3D(1, 4, point.z + 1));
        points.add(new Point3D(2, 4, point.z + 1));
        points.add(new Point3D(3, 4, point.z + 1));
        points.add(new Point3D(4, 4, point.z + 1));
      }
      // Left inner edge
      if (point.x == 1 && point.y == 2) {
        points.add(new Point3D(0, 0, point.z + 1));
        points.add(new Point3D(0, 1, point.z + 1));
        points.add(new Point3D(0, 2, point.z + 1));
        points.add(new Point3D(0, 3, point.z + 1));
        points.add(new Point3D(0, 4, point.z + 1));
      }
      // Right inner edge
      if (point.x == 3 && point.y == 2) {
        points.add(new Point3D(4, 0, point.z + 1));
        points.add(new Point3D(4, 1, point.z + 1));
        points.add(new Point3D(4, 2, point.z + 1));
        points.add(new Point3D(4, 3, point.z + 1));
        points.add(new Point3D(4, 4, point.z + 1));
      }

      // Remove any invalid points
      points.removeIf(p -> p.x < 0 || p.x > 4 || p.y < 0 || p.y > 4);
      points.removeIf(p -> p.x == 2 && p.y == 2); // Center point is always invalid

      return points;
    }

    void evolve() {
      var newBugs = new HashSet<Point3D>();
      var minZ = bugs.stream().mapToLong(p -> p.z).min().orElse(0) - 1;
      var maxZ = bugs.stream().mapToLong(p -> p.z).max().orElse(0) + 1;
      for (var x = 0; x < 5; x++) {
        for (var y = 0; y < 5; y++) {
          if (x == 2 && y == 2) {
            continue; // The center point is not a real point
          }
          for (var z = minZ; z <= maxZ; z++) {
            var point = new Point3D(x, y, z);
            var bugCount = bugCount(point);
            var isBug = bugs.contains(point);
            if ((!isBug && (bugCount == 1 || bugCount == 2)) || (isBug && bugCount == 1)) {
              newBugs.add(point);
            }
          }
        }
      }
      bugs = newBugs;
    }

    long bugCount(Point3D position) {
      return adjacent(position).stream()
          .filter(p -> bugs.contains(p))
          .count();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ComplexEris that = (ComplexEris) o;
      return Objects.equals(bugs, that.bugs);
    }

    @Override
    public int hashCode() {
      return Objects.hash(bugs);
    }
  }
}
