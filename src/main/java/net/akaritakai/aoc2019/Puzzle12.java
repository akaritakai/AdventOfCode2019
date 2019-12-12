package net.akaritakai.aoc2019;

import java.util.*;
import java.util.stream.Collectors;

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
    var moons = getBodies();
    for (int i = 0; i < 1000; i++) {
      moons = step(moons);
    }
    long energy = moons.stream().mapToLong(Moon::getTotalEnergy).sum();
    return String.valueOf(energy);
  }

  @Override
  public String solvePart2() {
    var moons = getBodies();
    var xSet = new HashSet<>(Collections.singleton(moons.stream().map(Moon::xOnly).collect(Collectors.toList())));
    var ySet = new HashSet<>(Collections.singleton(moons.stream().map(Moon::yOnly).collect(Collectors.toList())));
    var zSet = new HashSet<>(Collections.singleton(moons.stream().map(Moon::zOnly).collect(Collectors.toList())));
    var xCycle = Long.MAX_VALUE;
    var yCycle = Long.MAX_VALUE;
    var zCycle = Long.MAX_VALUE;
    for (long i = 1; xCycle == Long.MAX_VALUE || yCycle == Long.MAX_VALUE || zCycle == Long.MAX_VALUE; i++) {
      moons = step(moons);
      if (!xSet.add(moons.stream().map(Moon::xOnly).collect(Collectors.toList()))) {
        xCycle = Math.min(xCycle, i);
      }
      if (!ySet.add(moons.stream().map(Moon::yOnly).collect(Collectors.toList()))) {
        yCycle = Math.min(yCycle, i);
      }
      if (!zSet.add(moons.stream().map(Moon::zOnly).collect(Collectors.toList()))) {
        zCycle = Math.min(zCycle, i);
      }
    }
    return String.valueOf(lcm(lcm(xCycle, yCycle), zCycle));
  }

  private List<Moon> step(List<Moon> moons) {
    return moons.stream()
        .map(moon -> moon.applyGravity(moons))
        .map(Moon::applyVelocity)
        .collect(Collectors.toList());
  }

  private List<Moon> getBodies() {
    return getPuzzleInput()
        .lines()
        .map(line -> {
          long x = Long.parseLong(line.replaceAll("^<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>$", "$1"));
          long y = Long.parseLong(line.replaceAll("^<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>$", "$2"));
          long z = Long.parseLong(line.replaceAll("^<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>$", "$3"));
          return new Moon(new Position(x, y, z));
        })
        .collect(Collectors.toList());
  }

  private static class Moon {
    private final Position position;
    private final Velocity velocity;

    private Moon(Position position) {
      this.position = position;
      this.velocity = new Velocity(0, 0, 0);
    }

    private Moon(Position position, Velocity velocity) {
      this.position = position;
      this.velocity = velocity;
    }

    private Moon applyGravity(Collection<Moon> moons) {
      Velocity delta = moons.stream()
          .filter(moon -> moon != this)
          .map(moon -> {
            long dx = Long.compare(moon.position.x, this.position.x);
            long dy = Long.compare(moon.position.y, this.position.y);
            long dz = Long.compare(moon.position.z, this.position.z);
            return new Velocity(dx, dy, dz);
          })
          .reduce((v1, v2) -> new Velocity(v1.dx + v2.dx, v1.dy + v2.dy, v1.dz + v2.dz))
          .orElse(new Velocity(0, 0, 0));
      return new Moon(position, new Velocity(velocity.dx + delta.dx, velocity.dy + delta.dy,
          velocity.dz + delta.dz));
    }

    private Moon applyVelocity() {
      return new Moon(new Position(this.position.x + velocity.dx, this.position.y + velocity.dy,
          this.position.z + velocity.dz), velocity);
    }

    public long getPotentialEnergy() {
      return Math.abs(position.x) + Math.abs(position.y) + Math.abs(position.z);
    }

    public long getKineticEnergy() {
      return Math.abs(velocity.dx) + Math.abs(velocity.dy) + Math.abs(velocity.dz);
    }

    public long getTotalEnergy() {
      return getPotentialEnergy() * getKineticEnergy();
    }

    private Moon xOnly() {
      return new Moon(new Position(position.x, 0, 0), new Velocity(velocity.dx, 0, 0));
    }

    private Moon yOnly() {
      return new Moon(new Position(0, position.y, 0), new Velocity(0, velocity.dy, 0));
    }

    private Moon zOnly() {
      return new Moon(new Position(0, 0, position.z), new Velocity(0, 0, velocity.dz));
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Moon moon = (Moon) o;
      return Objects.equals(position, moon.position) && Objects.equals(velocity, moon.velocity);
    }

    @Override
    public int hashCode() {
      return Objects.hash(position, velocity);
    }
  }

  private static class Position {
    private final long x;
    private final long y;
    private final long z;

    public Position(long x, long y, long z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Position position = (Position) o;
      return x == position.x && y == position.y && z == position.z;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y, z);
    }
  }

  private static class Velocity {
    private final long dx;
    private final long dy;
    private final long dz;

    public Velocity(long dx, long dy, long dz) {
      this.dx = dx;
      this.dy = dy;
      this.dz = dz;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Velocity velocity = (Velocity) o;
      return dx == velocity.dx && dy == velocity.dy && dz == velocity.dz;
    }

    @Override
    public int hashCode() {
      return Objects.hash(dx, dy, dz);
    }
  }
}
