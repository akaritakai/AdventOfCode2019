package net.akaritakai.aoc2019;

import com.google.common.annotations.VisibleForTesting;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.math3.util.ArithmeticUtils.lcm;

public class Puzzle12 extends AbstractPuzzle {

  public Puzzle12(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 12;
  }

  @Override
  public String solvePart1() {
    var moons = getMoons();
    for (var i = 0; i < 1000; i++) {
      moons = step(moons);
    }
    long energy = moons.stream().mapToLong(Moon::getTotalEnergy).sum();
    return String.valueOf(energy);
  }

  @Override
  public String solvePart2() {
    var moons = getMoons();
    var xCycle = new VectorCycle();
    var yCycle = new VectorCycle();
    var zCycle = new VectorCycle();
    while (xCycle.notFound() || yCycle.notFound() || zCycle.notFound()) {
      xCycle.add(moons.stream().map(moon -> moon.x));
      yCycle.add(moons.stream().map(moon -> moon.y));
      zCycle.add(moons.stream().map(moon -> moon.z));
      moons = step(moons);
    }
    return String.valueOf(lcm(lcm(xCycle.length(), yCycle.length()), zCycle.length()));
  }

  @VisibleForTesting
  List<Moon> step(List<Moon> moons) {
    return moons.stream()
        .map(moon -> moon.applyGravity(moons))
        .map(Moon::applyVelocity)
        .collect(Collectors.toList());
  }

  @VisibleForTesting
  List<Moon> getMoons() {
    var inputFormat = Pattern.compile("^<x=(\\S+), y=(\\S+), z=(\\S+)>$");
    return getPuzzleInput()
        .lines()
        .map(line -> {
          var x = Long.parseLong(line.replaceAll(inputFormat.pattern(), "$1"));
          var y = Long.parseLong(line.replaceAll(inputFormat.pattern(), "$2"));
          var z = Long.parseLong(line.replaceAll(inputFormat.pattern(), "$3"));
          return new Moon(x, y, z);
        })
        .collect(Collectors.toList());
  }

  @VisibleForTesting
  static class Moon {
    private final Vector x;
    private final Vector y;
    private final Vector z;

    private Moon(long xPos, long yPos, long zPos) {
      x = new Vector(xPos, 0);
      y = new Vector(yPos, 0);
      z = new Vector(zPos, 0);
    }

    private Moon(Vector x, Vector y, Vector z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    @VisibleForTesting
    Moon(long xPos, long yPos, long zPos, long dx, long dy, long dz) {
      x = new Vector(xPos, dx);
      y = new Vector(yPos, dy);
      z = new Vector(zPos, dz);
    }

    private Moon applyGravity(Collection<Moon> moons) {
      Vector x = this.x.gravity(moons.stream().map(moon -> moon.x));
      Vector y = this.y.gravity(moons.stream().map(moon -> moon.y));
      Vector z = this.z.gravity(moons.stream().map(moon -> moon.z));
      return new Moon(x, y, z);
    }

    private Moon applyVelocity() {
      return new Moon(x.velocity(), y.velocity(), z.velocity());
    }

    @VisibleForTesting
    long getPotentialEnergy() {
      return Math.abs(x.position) + Math.abs(y.position) + Math.abs(z.position);
    }

    @VisibleForTesting
    long getKineticEnergy() {
      return Math.abs(x.velocity) + Math.abs(y.velocity) + Math.abs(z.velocity);
    }

    @VisibleForTesting
    long getTotalEnergy() {
      return getPotentialEnergy() * getKineticEnergy();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Moon moon = (Moon) o;
      return Objects.equals(x, moon.x) && Objects.equals(y, moon.y) && Objects.equals(z, moon.z);
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y, z);
    }
  }

  private static class Vector {
    private final long position;
    private final long velocity;

    private Vector(long position, long velocity) {
      this.position = position;
      this.velocity = velocity;
    }

    private Vector gravity(Stream<Vector> vectors) {
      var dv = vectors.filter(vector -> vector != this)
          .mapToLong(vector -> Long.compare(vector.position, position))
          .sum();
      return new Vector(position, velocity + dv);
    }

    private Vector velocity() {
      return new Vector(position + velocity, velocity);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Vector vector = (Vector) o;
      return position == vector.position && velocity == vector.velocity;
    }

    @Override
    public int hashCode() {
      return Objects.hash(position, velocity);
    }
  }

  private static class VectorCycle {
    private final Set<List<Vector>> _vectors = new HashSet<>();
    private boolean _found = false;
    private long _length = 0;

    private void add(Stream<Vector> vectors) {
      if (!_found) {
        _length = _vectors.isEmpty() ? 0 : _length + 1;
        _found = !_vectors.add(vectors.collect(Collectors.toList()));
      }
    }

    private boolean notFound() {
      return !_found;
    }

    private long length() {
      return _length;
    }
  }
}
