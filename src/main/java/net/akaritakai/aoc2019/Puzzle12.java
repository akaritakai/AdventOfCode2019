package net.akaritakai.aoc2019;

import com.google.common.annotations.VisibleForTesting;

import java.util.*;
import java.util.regex.Pattern;
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
    var moons = getMoons();
    for (var i = 0; i < 1000; i++) {
      moons = step(moons);
    }
    long energy = moons.stream().mapToLong(Moon::getTotalEnergy).sum();
    return String.valueOf(energy);
  }

  @Override
  public String solvePart2() {
    Long x = null;
    Long y = null;
    Long z = null;
    var moons = step(getMoons());
    for (long cycles = 1; x == null || y == null || z == null; cycles++) {
      if (x == null && moons.stream().allMatch(moon -> moon.dx == 0)) {
        x = cycles;
      }
      if (y == null && moons.stream().allMatch(moon -> moon.dy == 0)) {
        y = cycles;
      }
      if (z == null && moons.stream().allMatch(moon -> moon.dz == 0)) {
        z = cycles;
      }
      moons = step(moons);
    }
    return String.valueOf(lcm(lcm(2 * x, 2 * y), 2 * z));
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
    private final long x;
    private final long y;
    private final long z;
    private final long dx;
    private final long dy;
    private final long dz;

    private Moon(long x, long y, long z) {
      this(x, y, z, 0, 0, 0);
    }

    @VisibleForTesting
    Moon(long x, long y, long z, long dx, long dy, long dz) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.dx = dx;
      this.dy = dy;
      this.dz = dz;
    }

    private Moon applyGravity(Collection<Moon> moons) {
      var ddx = moons.stream().filter(moon -> moon != this).mapToLong(moon -> Long.compare(moon.x, this.x)).sum();
      var ddy = moons.stream().filter(moon -> moon != this).mapToLong(moon -> Long.compare(moon.y, this.y)).sum();
      var ddz = moons.stream().filter(moon -> moon != this).mapToLong(moon -> Long.compare(moon.z, this.z)).sum();
      return new Moon(x, y, z, dx + ddx, dy + ddy, dz + ddz);
    }

    private Moon applyVelocity() {
      return new Moon(x + dx, y + dy, z + dz, dx, dy, dz);
    }

    @VisibleForTesting
    long getPotentialEnergy() {
      return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    @VisibleForTesting
    long getKineticEnergy() {
      return Math.abs(dx) + Math.abs(dy) + Math.abs(dz);
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
      return x == moon.x && y == moon.y && z == moon.z && dx == moon.dx && dy == moon.dy && dz == moon.dz;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y, z, dx, dy, dz);
    }
  }
}
