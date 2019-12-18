package net.akaritakai.aoc2019;

import com.google.common.collect.Sets;
import net.akaritakai.aoc2019.geom2d.Direction;
import net.akaritakai.aoc2019.geom2d.Point;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    Vault vault = new Vault(getPuzzleInput());
    return String.valueOf(vault.minKeyCollectionCost(vault._entrance, vault._keys.keySet()));
  }

  @Override
  public String solvePart2() {
    Vault2 vault2 = new Vault2(getPuzzleInput());
    KeyCollectionCostInput2 input = new KeyCollectionCostInput2(vault2._entrance1, vault2._entrance2,
        vault2._entrance3, vault2._entrance4, vault2._keys.keySet());
    return String.valueOf(vault2.minKeyCollectionCost(input));
  }

  static class Vault {
    private final Map<String, Point> _keys = new HashMap<>();
    private final Map<String, Point> _doors = new HashMap<>();
    private final Set<Point> _tunnels = new HashSet<>();
    private Point _entrance;

    private Vault(String maze) {
      int x = 0;
      int y = 0;
      for (char c : maze.trim().toCharArray()) {
        Point p = new Point(x, y);
        if (c == '@') {
          _entrance = p;
        }
        if (c >= 'a' && c <= 'z') {
          _keys.put(String.valueOf(c), p);
        }
        if (c >= 'A' && c <= 'Z') {
          _doors.put(String.valueOf(c), p);
        }
        if (c != '#' && c != '\n') {
          _tunnels.add(p);
        }
        if (c == '\n') {
          x = 0;
          y++;
        } else {
          x++;
        }
      }
    }

    private final Map<KeyCollectionCostInput, Integer> _memo = new ConcurrentHashMap<>();

    private int minKeyCollectionCost(Point location, Set<String> remainingKeys) {
      if (remainingKeys.isEmpty()) {
        return 0;
      }
      var memo = new KeyCollectionCostInput(location, remainingKeys);
      if (_memo.containsKey(memo)) {
        return _memo.get(memo);
      }
      var reachableKeys = reachableKeys(remainingKeys);
      var dijkstra = new DijkstraShortestPath<>(graph(remainingKeys)).getPaths(location);
      var result = reachableKeys.stream()
          .mapToInt(key -> dijkstra.getPath(_keys.get(key)).getLength()
              + minKeyCollectionCost(_keys.get(key), Sets.difference(remainingKeys, Set.of(key))))
          .min()
          .orElse(0);
      _memo.put(memo, result);
      return result;
    }

    private Set<String> reachableKeys(Set<String> remainingKeys) {
      var graph = graph(remainingKeys);
      var connectivity = new ConnectivityInspector<>(graph);
      var reachable = remainingKeys.stream()
          .filter(key -> connectivity.pathExists(_entrance, _keys.get(key)))
          .collect(Collectors.toSet());

      var dijkstra = new DijkstraShortestPath<>(graph).getPaths(_entrance);
      var it1 = reachable.iterator();
      while (it1.hasNext()) {
        var key1 = it1.next();
        var it2 = reachable.iterator();
        while (it2.hasNext()) {
          var key2 = it2.next();
          if (key1.equals(key2)) {
            continue;
          }
          if (dijkstra.getPath(_keys.get(key1)).getVertexList().contains(_keys.get(key2))) {
            it1.remove(); // Key 1 is reachable only through Key 2
            break;
          }
        }
      }

      return reachable;
    }

    private Graph<Point, DefaultEdge> graph(Set<String> remainingKeys) {
      // Determine which doors are still closed to us
      Set<String> unopenedDoors = remainingKeys.stream().map(String::toUpperCase).filter(_doors::containsKey).collect(Collectors.toSet());
      Set<Point> doors = unopenedDoors.stream().map(_doors::get).collect(Collectors.toSet());

      // Remove those edges on our graph
      Graph<Point, DefaultEdge> graph = buildGraph();
      for (Point door : doors) {
        for (Point adjacent : adjacentPoints(door)) {
          graph.removeEdge(door, adjacent);
        }
      }

      return graph;
    }

    private Graph<Point, DefaultEdge> buildGraph() {
      Graph<Point, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
      _tunnels.forEach(graph::addVertex); // Add all vertices
      _tunnels.forEach(point -> {
        adjacentPoints(point).forEach(adjacent -> {
          graph.addEdge(point, adjacent);
        });
      }); // Add all edges
      return graph;
    }

    private Set<Point> adjacentPoints(Point point) {
      return Arrays.stream(Direction.values())
          .map(direction -> direction.move(point))
          .filter(_tunnels::contains)
          .collect(Collectors.toSet());
    }
  }

  static class Vault2 {
    private final Map<String, Point> _keys = new HashMap<>();
    private final Map<String, Point> _doors = new HashMap<>();
    private final Set<Point> _tunnels = new HashSet<>();
    private Point _entrance1;
    private Point _entrance2;
    private Point _entrance3;
    private Point _entrance4;

    private Vault2(String maze) {
      int x = 0;
      int y = 0;
      Point entrance = null;
      for (char c : maze.trim().toCharArray()) {
        Point p = new Point(x, y);
        if (c == '@') {
          entrance = p;
        }
        if (c >= 'a' && c <= 'z') {
          _keys.put(String.valueOf(c), p);
        }
        if (c >= 'A' && c <= 'Z') {
          _doors.put(String.valueOf(c), p);
        }
        if (c != '#' && c != '\n') {
          _tunnels.add(p);
        }
        if (c == '\n') {
          x = 0;
          y++;
        } else {
          x++;
        }
      }
      _tunnels.remove(Direction.NORTH.move(entrance));
      _tunnels.remove(Direction.SOUTH.move(entrance));
      _tunnels.remove(Direction.EAST.move(entrance));
      _tunnels.remove(Direction.WEST.move(entrance));
      _entrance1 = new Point(entrance.x - 1, entrance.y - 1);
      _entrance2 = new Point(entrance.x - 1, entrance.y + 1);
      _entrance3 = new Point(entrance.x + 1, entrance.y - 1);
      _entrance4 = new Point(entrance.x + 1, entrance.y + 1);
    }

    private final Map<KeyCollectionCostInput2, Long> _memo = new ConcurrentHashMap<>();

    private long minKeyCollectionCost(KeyCollectionCostInput2 input) {
      if (input.remainingKeys.isEmpty()) {
        return 0;
      }
      if (_memo.containsKey(input)) {
        return _memo.get(input);
      }

      var graph = graph(input.remainingKeys);

      var reachable1 = reachableKeys(input.robot1, input.remainingKeys);
      var reachable2 = reachableKeys(input.robot2, input.remainingKeys);
      var reachable3 = reachableKeys(input.robot3, input.remainingKeys);
      var reachable4 = reachableKeys(input.robot4, input.remainingKeys);

      var dijkstra1 = new DijkstraShortestPath<>(graph).getPaths(input.robot1);
      var dijkstra2 = new DijkstraShortestPath<>(graph).getPaths(input.robot2);
      var dijkstra3 = new DijkstraShortestPath<>(graph).getPaths(input.robot3);
      var dijkstra4 = new DijkstraShortestPath<>(graph).getPaths(input.robot4);

      Function<String, KeyCollectionCostInput2> input1 = key ->
          new KeyCollectionCostInput2(_keys.get(key), input.robot2, input.robot3, input.robot4, Sets.difference(input.remainingKeys, Set.of(key)));
      Function<String, KeyCollectionCostInput2> input2 = key ->
          new KeyCollectionCostInput2(input.robot1, _keys.get(key), input.robot3, input.robot4, Sets.difference(input.remainingKeys, Set.of(key)));
      Function<String, KeyCollectionCostInput2> input3 = key ->
          new KeyCollectionCostInput2(input.robot1, input.robot2, _keys.get(key), input.robot4, Sets.difference(input.remainingKeys, Set.of(key)));
      Function<String, KeyCollectionCostInput2> input4 = key ->
          new KeyCollectionCostInput2(input.robot1, input.robot2, input.robot3, _keys.get(key), Sets.difference(input.remainingKeys, Set.of(key)));

      var result1 = reachable1.stream()
          .mapToLong(key -> dijkstra1.getPath(_keys.get(key)).getLength() + minKeyCollectionCost(input1.apply(key)))
          .min()
          .orElse(Integer.MAX_VALUE);
      var result2 = reachable2.stream()
          .mapToLong(key -> dijkstra2.getPath(_keys.get(key)).getLength() + minKeyCollectionCost(input2.apply(key)))
          .min()
          .orElse(Integer.MAX_VALUE);
      var result3 = reachable3.stream()
          .mapToLong(key -> dijkstra3.getPath(_keys.get(key)).getLength() + minKeyCollectionCost(input3.apply(key)))
          .min()
          .orElse(Integer.MAX_VALUE);
      var result4 = reachable4.stream()
          .mapToLong(key -> dijkstra4.getPath(_keys.get(key)).getLength() + minKeyCollectionCost(input4.apply(key)))
          .min()
          .orElse(Integer.MAX_VALUE);

      var result = Math.min(Math.min(Math.min(result1, result2), result3), result4);
      _memo.put(input, result);
      return result;
    }

    private Set<String> reachableKeys(Point location, Set<String> remainingKeys) {
      var graph = graph(remainingKeys);
      var connectivity = new ConnectivityInspector<>(graph);
      var reachable = remainingKeys.stream()
          .filter(key -> connectivity.pathExists(location, _keys.get(key)))
          .collect(Collectors.toSet());

      var dijkstra = new DijkstraShortestPath<>(graph).getPaths(location);
      var it1 = reachable.iterator();
      while (it1.hasNext()) {
        var key1 = it1.next();
        var it2 = reachable.iterator();
        while (it2.hasNext()) {
          var key2 = it2.next();
          if (key1.equals(key2)) {
            continue;
          }
          if (dijkstra.getPath(_keys.get(key1)).getVertexList().contains(_keys.get(key2))) {
            it1.remove(); // Key 1 is reachable only through Key 2
            break;
          }
        }
      }

      return reachable;
    }

    private Graph<Point, DefaultEdge> graph(Set<String> remainingKeys) {
      // Determine which doors are still closed to us
      Set<String> unopenedDoors = remainingKeys.stream().map(String::toUpperCase).filter(_doors::containsKey).collect(Collectors.toSet());
      Set<Point> doors = unopenedDoors.stream().map(_doors::get).collect(Collectors.toSet());

      // Remove those edges on our graph
      Graph<Point, DefaultEdge> graph = buildGraph();
      for (Point door : doors) {
        for (Point adjacent : adjacentPoints(door)) {
          graph.removeEdge(door, adjacent);
        }
      }

      return graph;
    }

    private Graph<Point, DefaultEdge> buildGraph() {
      Graph<Point, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
      _tunnels.forEach(graph::addVertex); // Add all vertices
      _tunnels.forEach(point -> {
        adjacentPoints(point).forEach(adjacent -> {
          graph.addEdge(point, adjacent);
        });
      }); // Add all edges
      return graph;
    }

    private Set<Point> adjacentPoints(Point point) {
      return Arrays.stream(Direction.values())
          .map(direction -> direction.move(point))
          .filter(_tunnels::contains)
          .collect(Collectors.toSet());
    }
  }

  private static class KeyCollectionCostInput2 {
    private final Point robot1;
    private final Point robot2;
    private final Point robot3;
    private final Point robot4;
    private final Set<String> remainingKeys;

    public KeyCollectionCostInput2(Point robot1, Point robot2, Point robot3, Point robot4, Set<String> remainingKeys) {
      this.robot1 = robot1;
      this.robot2 = robot2;
      this.robot3 = robot3;
      this.robot4 = robot4;
      this.remainingKeys = remainingKeys;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      KeyCollectionCostInput2 that = (KeyCollectionCostInput2) o;
      return Objects.equals(robot1, that.robot1) &&
          Objects.equals(robot2, that.robot2) &&
          Objects.equals(robot3, that.robot3) &&
          Objects.equals(robot4, that.robot4) &&
          Objects.equals(remainingKeys, that.remainingKeys);
    }

    @Override
    public int hashCode() {
      return Objects.hash(robot1, robot2, robot3, robot4, remainingKeys);
    }
  }

  private static class KeyCollectionCostInput {
    private final Point point;
    private final Set<String> remainingKeys;

    public KeyCollectionCostInput(Point point, Set<String> remainingKeys) {
      this.point = point;
      this.remainingKeys = remainingKeys;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      KeyCollectionCostInput that = (KeyCollectionCostInput) o;
      return Objects.equals(point, that.point) &&
          Objects.equals(remainingKeys, that.remainingKeys);
    }

    @Override
    public int hashCode() {
      return Objects.hash(point, remainingKeys);
    }
  }

}
