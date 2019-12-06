package net.akaritakai.aoc2019;

import com.google.common.annotations.VisibleForTesting;

import java.util.*;

public class Puzzle06 extends AbstractPuzzle {

  private static final String CENTER_OF_MASS = "COM";
  private final Map<String, String> _orbits;

  public Puzzle06(String puzzleInput) {
    super(puzzleInput);
    _orbits = getOrbits(puzzleInput);
  }

  @Override
  public int getDay() {
    return 6;
  }

  @Override
  public String solvePart1() {
    var bodies = _orbits.keySet();
    var checksum = bodies.stream().mapToInt(this::getOrbitCounts).sum();
    return String.valueOf(checksum);
  }

  @Override
  public String solvePart2() {
    var youPath = pathToCenterOfMass("YOU");
    var santaPath = pathToCenterOfMass( "SAN");
    var commonAncestor = youPath.stream()
        .filter(santaPath::contains)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("YOU and SAN are not on the same connected graph"));
    return String.valueOf(youPath.indexOf(commonAncestor) + santaPath.indexOf(commonAncestor));
  }

  @VisibleForTesting
  List<String> pathToCenterOfMass(String body) {
    var path = new ArrayList<String>();
    while (!body.equals(CENTER_OF_MASS)) {
      body = _orbits.get(body);
      path.add(body);
    }
    return path;
  }

  @VisibleForTesting
  int getOrbitCounts(String body) {
    if (body.equals(CENTER_OF_MASS)) {
      return 0;
    }
    return 1 + getOrbitCounts( _orbits.get(body));
  }

  private static Map<String, String> getOrbits(String orbitalMapData) {
    var orbits = new HashMap<String, String>();
    orbitalMapData.trim().lines().forEach(line -> {
      String child = line.split("\\)")[1];
      String parent = line.split("\\)")[0];
      orbits.put(child, parent);
    });
    return orbits;
  }
}
