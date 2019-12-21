package net.akaritakai.aoc2019;

import com.google.common.collect.Sets;
import net.akaritakai.aoc2019.geom2d.Point;
import net.akaritakai.aoc2019.geom3d.Point3D;
import net.akaritakai.aoc2019.graph.GraphBuilder;
import net.akaritakai.aoc2019.graph.InfiniteUndirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.BidirectionalDijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Puzzle20 extends AbstractPuzzle {

  public Puzzle20(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 20;
  }

  @Override
  public String solvePart1() {
    var maze = new Maze2D(getPuzzleInput());
    return String.valueOf(maze.getShortestPathLength());
  }

  @Override
  public String solvePart2() {
    var maze = new Maze3D(getPuzzleInput());
    return String.valueOf(maze.getShortestPathLength());
  }

  private static class Maze2D {
    private final Point _start;
    private final Point _end;
    private final Graph<Point, DefaultWeightedEdge> _graph;

    private Maze2D(String input) {
      var grid = getRawGrid(input);
      var tunnels = getTunnels(grid);

      var outer = getOuterLabels(grid);
      var inner = getInnerLabels(grid);

      _start = outer.get("AA");
      _end = outer.get("ZZ");

      var portals = new HashMap<Point, Point>();
      inner.forEach((label, point1) -> {
        var point2 = outer.get(label);
        portals.put(point1, point2);
        portals.put(point2, point1);
      });

      _graph = GraphBuilder.buildUndirectedGraph(tunnels, point -> adjacentPoints(tunnels, portals, point));
      GraphBuilder.reduceGraph(_graph, Sets.union(portals.keySet(), Set.of(_start, _end)));
    }

    private long getShortestPathLength() {
      return (long) new BidirectionalDijkstraShortestPath<>(_graph).getPath(_start, _end).getWeight();
    }

    private static Set<Point> adjacentPoints(Set<Point> tunnels, Map<Point, Point> portals, Point point) {
      // Add valid points in all cardinal directions
      var adjacent = point.adjacentPoints();
      adjacent.removeIf(p -> !tunnels.contains(p));
      // Add portal destination if we are at a portal
      var portal = portals.get(point);
      if (portal != null) adjacent.add(portal);
      return adjacent;
    }
  }

  private static class Maze3D {
    private final Point3D _start;
    private final Point3D _end;
    private final Maze3DGraph _graph;

    private Maze3D(String input) {
      var grid = getRawGrid(input);
      var tunnels = getTunnels(grid);

      var outer = getOuterLabels(grid);
      var inner = getInnerLabels(grid);

      var planarStart = outer.get("AA");
      _start = new Point3D(planarStart.x, planarStart.y, 0);
      var planerEnd = outer.get("ZZ");
      _end = new Point3D(planerEnd.x, planerEnd.y, 0);

      var innerPortals = new HashMap<Point, Point>();
      var outerPortals = new HashMap<Point, Point>();
      inner.forEach((label, innerPoint) -> {
        var outerPoint = outer.get(label);
        innerPortals.put(innerPoint, outerPoint);
        outerPortals.put(outerPoint, innerPoint);
      });

      _graph = new Maze3DGraph(_start, _end, tunnels, innerPortals, outerPortals);
    }

    private long getShortestPathLength() {
      return new BidirectionalDijkstraShortestPath<>(_graph).getPath(_start, _end).getLength();
    }
  }

  private static class Maze3DGraph extends InfiniteUndirectedGraph<Point3D> {
    private final Point3D _start;
    private final Point3D _end;
    private final Set<Point> _tunnels;
    private final Map<Point, Point> _innerPortals;
    private final Map<Point, Point> _outerPortals;

    private Maze3DGraph(Point3D start, Point3D end, Set<Point> tunnels, Map<Point, Point> innerPortals,
                        Map<Point, Point> outerPortals) {
      _start = start;
      _end = end;
      _tunnels = tunnels;
      _innerPortals = innerPortals;
      _outerPortals = outerPortals;
    }

    @Override
    public Set<Point3D> adjacentVertices(Point3D vertex) {
      var adjacent = new HashSet<Point3D>();

      // Add points in all cardinal directions
      adjacent.add(new Point3D(vertex.x, vertex.y - 1, vertex.z));
      adjacent.add(new Point3D(vertex.x, vertex.y + 1, vertex.z));
      adjacent.add(new Point3D(vertex.x - 1, vertex.y, vertex.z));
      adjacent.add(new Point3D(vertex.x + 1, vertex.y, vertex.z));

      // Add inner portal path
      var in = new Point(vertex.x, vertex.y);
      var out = _innerPortals.get(in);
      if (out != null) adjacent.add(new Point3D(out.x, out.y, vertex.z + 1));

      // Add outer portal paths
      out = _outerPortals.get(in);
      if (out != null) adjacent.add(new Point3D(out.x, out.y, vertex.z - 1));

      // Remove invalid points
      adjacent.removeIf(p -> !containsVertex(p));

      return adjacent;
    }

    @Override
    public boolean containsVertex(Point3D point3D) {
      // Check that the point belongs to a valid layer
      if (point3D.z < 0) {
        return false;
      }

      // Start position only exists as a single 3D point (on the top level)
      if (point3D.x == _start.x && point3D.y == _start.y && point3D.z != 0) {
        return false;
      }

      // End position only exists as a single 3D point (on the top level)
      if (point3D.x == _end.x && point3D.y == _end.y && point3D.z != 0) {
        return false;
      }

      var planarPoint = new Point(point3D.x, point3D.y);

      // Check that the point is in one of our tunnels
      if (!_tunnels.contains(planarPoint)) {
        return false;
      }

      // Outer portals unusable on the top level
      if (_outerPortals.containsKey(planarPoint) && point3D.z == 0) {
        return false;
      }

      return true;
    }
  }

  private static Set<Point> getTunnels(String[][] grid) {
    var tunnels = new HashSet<Point>();
    var width = grid.length;
    var height = grid[0].length;
    for (var x = 0; x < width; x++) {
      for (var y = 0; y < height; y++) {
        if (grid[x][y].equals(".")) {
          tunnels.add(new Point(x, y));
        }
      }
    }
    return tunnels;
  }

  private static Map<String, Point> getInnerLabels(String[][] grid) {
    var labels = new HashMap<String, Point>();

    var width = grid.length;
    var height = grid[0].length;

    // Find top edge
    var top = height / 2;
    while (!grid[width / 2][top].equals("#") && !grid[width / 2][top].equals(".")) {
      top--;
    }

    // Find bottom edge
    var bottom = height / 2;
    while (!grid[width / 2][bottom].equals("#") && !grid[width / 2][bottom].equals(".")) {
      bottom++;
    }

    // Find left edge
    var left = width / 2;
    while (!grid[left][height / 2].equals("#") && !grid[left][height / 2].equals(".")) {
      left--;
    }

    // Find right edge
    var right = width / 2;
    while (!grid[right][height / 2].equals("#") && !grid[right][height / 2].equals(".")) {
      right++;
    }

    // Top edge
    for (var x = left + 1; x < right; x++) {
      String label = grid[x][top + 1] + grid[x][top + 2];
      if (isValidLabel(label)) {
        labels.put(label, new Point(x, top));
      }
    }

    // Bottom edge
    for (var x = left + 1; x < right; x++) {
      String label = grid[x][bottom - 2] + grid[x][bottom - 1];
      if (isValidLabel(label)) {
        labels.put(label, new Point(x, bottom));
      }
    }

    // Left edge
    for (var y = top + 1; y < bottom; y++) {
      String label = grid[left + 1][y] + grid[left + 2][y];
      if (isValidLabel(label)) {
        labels.put(label, new Point(left, y));
      }
    }

    // Right edge
    for (var y = top + 1; y < bottom; y++) {
      String label = grid[right - 2][y] + grid[right - 1][y];
      if (isValidLabel(label)) {
        labels.put(label, new Point(right, y));
      }
    }

    return labels;
  }

  private static Map<String, Point> getOuterLabels(String[][] grid) {
    var labels = new HashMap<String, Point>();

    var width = grid.length;
    var height = grid[0].length;

    // Top edge
    for (var x = 0; x < width; x++) {
      String label = grid[x][0] + grid[x][1];
      if (isValidLabel(label)) {
        labels.put(label, new Point(x, 2));
      }
    }

    // Bottom edge
    for (var x = 0; x < width; x++) {
      String label = grid[x][height - 2] + grid[x][height - 1];
      if (isValidLabel(label)) {
        labels.put(label, new Point(x, height - 3));
      }
    }

    // Left edge
    for (var y = 0; y < height; y++) {
      String label = grid[0][y] + grid[1][y];
      if (isValidLabel(label)) {
        labels.put(label, new Point(2, y));
      }
    }

    // Right edge
    for (var y = 0; y < height; y++) {
      String label = grid[width - 2][y] + grid[width - 1][y];
      if (isValidLabel(label)) {
        labels.put(label, new Point(width - 3, y));
      }
    }

    return labels;
  }

  private static boolean isValidLabel(String label) {
    return label != null
        && label.length() == 2
        && Character.isAlphabetic(label.charAt(0))
        && Character.isAlphabetic(label.charAt(1));
  }

  /**
   * Returns the puzzle output as a 2D array of length-1 strings in the (x,y) plane
   */
  private static String[][] getRawGrid(String input) {
    var lines = input.lines().collect(Collectors.toList());
    var height = lines.size();
    var width = lines.get(0).length();

    var grid = new String[width][height];
    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        var pos = (y * (width + 1)) + x; // We add 1 to the width to account for the newline we don't capture
        grid[x][y] = String.valueOf(input.charAt(pos));
      }
    }

    return grid;
  }
}
