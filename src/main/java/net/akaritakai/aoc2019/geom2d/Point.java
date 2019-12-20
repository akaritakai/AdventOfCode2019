package net.akaritakai.aoc2019.geom2d;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Point {
  public final long x;
  public final long y;

  public Point(long x, long y) {
    this.x = x;
    this.y = y;
  }

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Set<Point> adjacentPoints() {
    var adjacent = new HashSet<Point>();
    adjacent.add(new Point(x, y - 1));
    adjacent.add(new Point(x, y + 1));
    adjacent.add(new Point(x - 1, y));
    adjacent.add(new Point(x + 1, y));
    return adjacent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Point point = (Point) o;
    return x == point.x && y == point.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "(" + x + "," + y + ")";
  }
}
