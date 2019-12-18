package net.akaritakai.aoc2019;

import net.akaritakai.aoc2019.geom2d.Direction;
import net.akaritakai.aoc2019.geom2d.Point;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Puzzle18 extends AbstractPuzzle {

  public Puzzle18(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 18;
  }

  @Override
  public String solvePart1() {
    var vault = new Vault(getPuzzleInput(), false);
    return String.valueOf(vault.minCollectionPath());
  }

  @Override
  public String solvePart2() {
    var vault = new Vault(getPuzzleInput(), true);
    return String.valueOf(vault.minCollectionPath());
  }

  private static class Vault {
    private final Map<Set<Point>, Graph<Point, DefaultWeightedEdge>> _memoizedGraphs = new ConcurrentHashMap<>();
    private final Map<Memo, Double> _memoizedCosts = new ConcurrentHashMap<>();
    private final List<Point> _entrances;
    private final DoorsAndKeys _doorsAndKeys;
    private final Graph<Point, DefaultWeightedEdge> _graph;

    private Vault(String input, boolean part2) {
      var x = 0;
      var y = 0;
      Point entrance = null;
      var tunnels = new HashSet<Point>();
      var doorsAndKeys = new HashMap<Character, Point>();
      for (char c : input.trim().toCharArray()) {
        var p = new Point(x, y);
        if (c == '@') {
          entrance = p;
        }
        if (Character.isAlphabetic(c)) {
          doorsAndKeys.put(c, p);
        }
        // All elements that aren't a wall or the next line are walkable
        if (c != '#' && c != '\n') {
          tunnels.add(p);
        }
        if (c == '\n') {
          x = 0;
          y++;
        } else {
          x++;
        }
      }

      _doorsAndKeys = new DoorsAndKeys(doorsAndKeys);
      if (!part2) {
        // Part 1: There's only one entrance and no tunnel modifications
        _entrances = List.of(entrance);
      } else {
        // Part 2:
        // Four entrances are added (one at each diagonal corner of the original entrance)
        // The original entrance is removed along with its adjacent points
        _entrances = List.of(
            new Point(entrance.x - 1, entrance.y - 1),
            new Point(entrance.x - 1, entrance.y + 1),
            new Point(entrance.x + 1, entrance.y - 1),
            new Point(entrance.x + 1, entrance.y + 1));
        tunnels.remove(entrance);
        tunnels.remove(Direction.NORTH.move(entrance));
        tunnels.remove(Direction.SOUTH.move(entrance));
        tunnels.remove(Direction.EAST.move(entrance));
        tunnels.remove(Direction.WEST.move(entrance));
      }

      var irreducible = new HashSet<Point>();
      irreducible.addAll(_entrances);
      irreducible.addAll(_doorsAndKeys.doors);
      irreducible.addAll(_doorsAndKeys.keys);
      _graph = buildGraph(tunnels, irreducible);
    }

    private long minCollectionPath() {
      var memo = new Memo(_entrances, _doorsAndKeys.keys);
      return (long) minCollectionPath(memo);
    }

    private double minCollectionPath(Memo input) {
      if (input.remainingKeys.isEmpty()) {
        return 0d;
      }

      if (_memoizedCosts.containsKey(input)) {
        return _memoizedCosts.get(input);
      }

      var minCost = Double.MAX_VALUE;
      for (int i = 0; i < input.robots.size(); i++) {
        var cost = minCollectionPath(input, i);
        minCost = Math.min(cost, minCost);
      }

      _memoizedCosts.put(input, minCost);

      return minCost;
    }

    private double minCollectionPath(Memo input, int robotIndex) {
      // Get the reachable keys and path costs
      var costs = keyCosts(input.remainingKeys, input.robots.get(robotIndex));

      var minCost = Double.MAX_VALUE;
      for (Map.Entry<Point, Double> keyCost : costs.entrySet()) {
        var key = keyCost.getKey();
        var cost = keyCost.getValue();

        // Move the robot to the key to collect it
        var robots = new ArrayList<>(input.robots);
        robots.set(robotIndex, key);
        var remainingKeys = new HashSet<>(input.remainingKeys);
        remainingKeys.remove(key);

        // Compute the remaining path
        var memo = new Memo(robots, remainingKeys);
        var pathCost = cost + minCollectionPath(memo);

        minCost = Math.min(minCost, pathCost);
      }

      return minCost;
    }

    private Map<Point, Double> keyCosts(Set<Point> remainingKeys, Point location) {
      var graph = graph(remainingKeys);

      var pathCosts = new HashMap<Point, Double>();
      var dijkstra = new DijkstraShortestPath<>(graph).getPaths(location);
      for (var key : remainingKeys) {
        var path = dijkstra.getPath(key);
        if (path != null && !pathObscured(path, remainingKeys)) {
          pathCosts.put(key, path.getWeight());
        }
      }
      return pathCosts;
    }

    private static boolean pathObscured(GraphPath<Point, DefaultWeightedEdge> path, Set<Point> remainingKeys) {
      // We stay that a path is "obscured" if an earlier key is encountered before our destination key
      var vertices = path.getVertexList();
      // The first vertex won't contain a key (it's either our starting location or a previously collected key)
      // The last vertex is our destination key which can't be obscured by itself
      // So, we only need to search the path from 1 to n-1
      for (int i = 1; i < vertices.size() - 1; i++) {
        var vertex = vertices.get(i);
        if (remainingKeys.contains(vertex)) {
          return true;
        }
      }
      return false;
    }

    private Graph<Point, DefaultWeightedEdge> graph(Set<Point> remainingKeys) {
      if (_memoizedGraphs.containsKey(remainingKeys)) {
        return _memoizedGraphs.get(remainingKeys);
      }

      var graph = copyGraph(_graph);
      var closedDoors = _doorsAndKeys.closedDoors(remainingKeys);
      for (Point door : closedDoors) {
        graph.removeVertex(door);
      }

      _memoizedGraphs.put(remainingKeys, graph);

      return graph;
    }

    private static Graph<Point, DefaultWeightedEdge> copyGraph(Graph<Point, DefaultWeightedEdge> graph) {
      var newGraph = new DefaultUndirectedWeightedGraph<Point, DefaultWeightedEdge>(DefaultWeightedEdge.class);
      for (var vertex : graph.vertexSet()) {
        for (var edge : graph.edgesOf(vertex)) {
          var source = graph.getEdgeSource(edge);
          var target = graph.getEdgeTarget(edge);
          var weight = graph.getEdgeWeight(edge);

          newGraph.addVertex(source);
          newGraph.addVertex(target);
          var newEdge = newGraph.addEdge(source, target);
          if (newEdge != null) {
            newGraph.setEdgeWeight(newEdge, weight);
          }
        }
      }
      return newGraph;
    }

    private static Graph<Point, DefaultWeightedEdge> buildGraph(Set<Point> tunnels, Set<Point> irreducibleVertices) {
      var graph = new DefaultUndirectedWeightedGraph<Point, DefaultWeightedEdge>(DefaultWeightedEdge.class);
      // Add all vertices
      tunnels.forEach(graph::addVertex);
      // Add all edges
      tunnels.forEach(point -> {
        adjacentPoints(tunnels, point).forEach(adjacent -> {
          var edge = graph.addEdge(point, adjacent);
          if (edge != null) {
            graph.setEdgeWeight(edge, 1);
          }
        });
      });
      reduceGraph(graph, irreducibleVertices);
      return graph;
    }

    private static void reduceGraph(Graph<Point, DefaultWeightedEdge> graph, Set<Point> irreducibleVertices) {
      // The idea of reducing the graph (removing vertices of degree 2 and combining their edges into a single weighted
      // edge) comes from /u/kroppeb
      // https://www.reddit.com/r/adventofcode/comments/ec8090/2019_day_18_solutions/fb9wkit/

      var modified = true;
      while (modified) {
        modified = false;
        for (var vertex : new HashSet<>(graph.vertexSet())) {
          if (!graph.containsVertex(vertex)) {
            continue; // This point was reduced already
          }
          if (irreducibleVertices.contains(vertex)) {
            continue; // This vertex cannot be reduced
          }
          modified |= reduceVertex(graph, vertex);
        }
      }
    }

    private static boolean reduceVertex(Graph<Point, DefaultWeightedEdge> graph, Point vertex) {
      if (graph.degreeOf(vertex) == 2) {
        // Vertex has 2 edges and can be reduced

        // Perform the replacement
        var weight = 0; // Weight of our new edge
        Point vertex1 = null; // Source of our new edge
        Point vertex2 = null; // Target of our new edge
        for (var edge : graph.edgesOf(vertex)) {
          // Add the edge we're removing to the weight of our new edge
          weight += graph.getEdgeWeight(edge);

          // Find the vertex in the edge that isn't the one we're removing and bubble it up
          var edgeVertex = graph.getEdgeSource(edge);
          if (edgeVertex.equals(vertex)) {
            edgeVertex = graph.getEdgeTarget(edge);
          }
          if (vertex1 == null) {
            vertex1 = edgeVertex;
          } else {
            vertex2 = edgeVertex;
          }
        }

        // Remove the reduced vertex and edges
        graph.removeVertex(vertex);

        // Create the new edge
        var edge = graph.addEdge(vertex1, vertex2);
        graph.setEdgeWeight(edge, weight);

        return true;
      }
      return false;
    }

    private static Set<Point> adjacentPoints(Set<Point> tunnels, Point point) {
      var points = new HashSet<Point>();
      var p = Direction.NORTH.move(point);
      if (tunnels.contains(p)) points.add(p);
      p = Direction.SOUTH.move(point);
      if (tunnels.contains(p)) points.add(p);
      p = Direction.EAST.move(point);
      if (tunnels.contains(p)) points.add(p);
      p = Direction.WEST.move(point);
      if (tunnels.contains(p)) points.add(p);
      return points;
    }
  }

  private static class DoorsAndKeys {
    private final Set<Point> doors = new HashSet<>();
    private final Set<Point> keys = new HashSet<>();
    private final Map<Point, Point> keyToDoor = new HashMap<>();

    private DoorsAndKeys(Map<Character, Point> doorsAndKeys) {
      var doors = new Point[26];
      var keys = new Point[26];
      doorsAndKeys.forEach((c, point) -> {
        if (c >= 'A' && c <= 'Z') {
          doors[c - 'A'] = point; // Uppercase letters are doors
        } else {
          keys[c - 'a'] = point; // Lowercase letters are keys
        }
      });
      for (int i = 0; i < 26; i++) {
        var door = doors[i];
        var key = keys[i];
        if (door != null) {
          this.doors.add(door);
        }
        if (key != null) {
          this.keys.add(key);
          if (door != null) {
            keyToDoor.put(key, door);
          }
        }
      }
    }

    private Set<Point> closedDoors(Set<Point> remainingKeys) {
      Set<Point> closedDoors = new HashSet<>();
      for (Point key : remainingKeys) {
        var door = keyToDoor.get(key);
        closedDoors.add(door);
      }
      return closedDoors;
    }
  }

  private static class Memo {
    private final List<Point> robots;
    private final Set<Point> remainingKeys;

    private Memo(List<Point> robots, Set<Point> remainingKeys) {
      this.robots = Collections.unmodifiableList(robots);
      this.remainingKeys = Collections.unmodifiableSet(remainingKeys);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Memo memo = (Memo) o;
      return Objects.equals(robots, memo.robots) && Objects.equals(remainingKeys, memo.remainingKeys);
    }

    @Override
    public int hashCode() {
      return Objects.hash(robots, remainingKeys);
    }
  }
}
