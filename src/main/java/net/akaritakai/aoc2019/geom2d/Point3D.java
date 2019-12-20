package net.akaritakai.aoc2019.geom2d;

import java.util.Objects;

public class Point3D {
  public final long x;
  public final long y;
  public final long z;

  public Point3D(long x, long y, long z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Point3D(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Point3D point = (Point3D) o;
    return x == point.x && y == point.y && z == point.z;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public String toString() {
    return "(" + x + "," + y + "," + z + ")";
  }
}
