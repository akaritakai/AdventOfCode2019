package net.akaritakai.aoc2019;

import com.google.common.annotations.VisibleForTesting;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.awt.*;
import java.io.Closeable;
import java.util.List;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneaked;
import static net.akaritakai.aoc2019.Puzzle15.Direction.*;

public class Puzzle15 extends AbstractPuzzle {

  public Puzzle15(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 15;
  }

  @Override
  public String solvePart1() {
    var ship = exploreShip();
    return String.valueOf(ship.distanceToOxygen());
  }

  @Override
  public String solvePart2() {
    var ship = exploreShip();
    return String.valueOf(ship.longestPathFromOxygen());
  }

  private ShipState exploreShip() {
    var droid = new DroidStateImpl(getPuzzleInput());
    var ship = new ShipState(droid);
    ship.exploreShip();
    return ship;
  }

  @VisibleForTesting
  interface DroidState extends Closeable {
    Point STARTING_POSITION = new Point(0, 0);
    State move(Direction direction);
    Point getLocation();
  }

  private static class DroidStateImpl implements DroidState {
    private final BlockingQueue<Long> _input = new LinkedBlockingQueue<>();
    private final BlockingQueue<Long> _output = new LinkedBlockingQueue<>();
    private Point _location = STARTING_POSITION;
    private Thread _vmThread;

    public DroidStateImpl(String puzzleInput) {
      _vmThread = new Thread(() -> {
        var vm = new IntcodeVm(puzzleInput, sneaked(_input::take), _output::add);
        vm.run();
      });
      _vmThread.start();
    }

    public State move(Direction direction) {
      _input.add(direction.getValue());
      var state = State.of(sneaked(_output::take).get());
      if (state != State.WALL) {
        _location = direction.move(_location);
      }
      return state;
    }

    public Point getLocation() {
      return _location;
    }

    @Override
    public synchronized void close() {
      if (_vmThread != null) {
        _vmThread.interrupt();
        _vmThread = null;
      }
    }
  }

  @VisibleForTesting
  static class ShipState {
    private final Graph<Point, DefaultEdge> _ship = new DefaultDirectedGraph<>(DefaultEdge.class);
    private final Stack<Point> _stack = new Stack<>();
    private final Set<Point> _visited = new HashSet<>();
    private final DroidState _droid;
    private Point _oxygen = null;

    ShipState(DroidState droid) {
      _droid = droid;
      _ship.addVertex(droid.getLocation());
      _stack.push(NORTH.move(droid.getLocation()));
      _stack.push(SOUTH.move(droid.getLocation()));
      _stack.push(EAST.move(droid.getLocation()));
      _stack.push(WEST.move(droid.getLocation()));
    }

    private synchronized void exploreShip() {
      while (!_stack.isEmpty()) {
        // Find the next point we need to explore
        var p = _stack.pop();

        // Don't explore previously explored locations
        if (!_visited.add(p)) {
          continue;
        }

        // Travel to our exploration point
        var directions = getDirections(p).iterator();
        State state = null;
        Point previousLocation = null;
        while (directions.hasNext()) {
          var direction = directions.next();
          previousLocation = _droid.getLocation();
          state = _droid.move(direction);
        }

        if (state == State.WALL) {
          continue; // Node is not reachable
        }

        // Place the node into our graph
        _ship.addVertex(_droid.getLocation());
        _ship.addEdge(previousLocation, _droid.getLocation());
        _ship.addEdge(_droid.getLocation(), previousLocation);

        // Mark the oxygen system if we've found it
        if (state == State.OXYGEN_SYSTEM) {
          _oxygen = _droid.getLocation();
        }

        // Enqueue children
        for (var direction : Direction.values()) {
          _stack.push(direction.move(_droid.getLocation()));
        }
      }
      sneaked(_droid::close).run();
    }

    @VisibleForTesting
    synchronized int distanceToOxygen() {
      if (_oxygen == null) {
        exploreShip();
      }
      return new DijkstraShortestPath<>(_ship)
          .getPaths(DroidState.STARTING_POSITION)
          .getPath(_oxygen)
          .getLength();
    }

    @VisibleForTesting
    synchronized int longestPathFromOxygen() {
      if (_oxygen == null) {
        exploreShip();
      }
      var paths = new DijkstraShortestPath<>(_ship).getPaths(_oxygen);
      return _ship.vertexSet().stream()
          .filter(vertex -> !vertex.equals(_oxygen))
          .mapToInt(vertex -> paths.getPath(vertex).getLength())
          .max()
          .orElse(0);
    }

    private List<Direction> getDirections(Point p) {
      // If we're 1 move away, then just go there
      for (var direction : Direction.values()) {
        if (p.equals(direction.move(_droid.getLocation()))) {
          return List.of(direction);
        }
      }

      // If we are more than 1 move away, then find the shortest path to get to any known-good square that's 1 square
      // away, and then just make the last move.
      var djikstra = new DijkstraShortestPath<>(_ship).getPaths(_droid.getLocation());
      return Arrays.stream(Direction.values())
          .filter(direction -> _ship.containsVertex(direction.move(p)))
          .map(direction -> Optional.ofNullable(djikstra.getPath(direction.move(p)))
              .map(GraphPath::getVertexList)
              .map(pointsInPath -> {
                var path = new ArrayList<Direction>();
                for (var i = 0; i < pointsInPath.size() - 1; i++) {
                  path.add(Direction.of(pointsInPath.get(i), pointsInPath.get(i + 1)));
                }
                path.add(direction.opposite());
                return path;
              })
              .orElse(null))
          .filter(Objects::nonNull)
          .min(Comparator.comparingInt(List::size))
          .orElseThrow(() -> new IllegalStateException(String.format("Couldn't find path to %s", p)));
    }
  }


  enum State {
    WALL(0),
    EMPTY(1),
    OXYGEN_SYSTEM(2);

    private final long value;

    State(int value) {
      this.value = value;
    }

    private static State of(long value) {
      return Arrays.stream(State.values())
          .filter(state -> state.value == value)
          .findAny()
          .orElseThrow(() -> new IllegalArgumentException("Unknown state value: " + value));
    }
  }

  enum Direction {
    NORTH(0, 1, 1),
    SOUTH(0, -1, 2),
    WEST(-1, 0, 3),
    EAST(1, 0, 4);

    private final int dx;
    private final int dy;
    private final long value;

    private long getValue() {
      return value;
    }

    Direction(int dx, int dy, long value) {
      this.dx = dx;
      this.dy = dy;
      this.value = value;
    }

    @VisibleForTesting
    Point move(Point point) {
      return new Point(point.x + dx, point.y + dy);
    }

    private Direction opposite() {
      switch (this) {
        case NORTH: return SOUTH;
        case SOUTH: return NORTH;
        case WEST: return EAST;
        case EAST: return WEST;
      }
      throw new IllegalStateException("Invalid direction");
    }

    private static Direction of(Point start, Point end) {
      var dx = end.x - start.x;
      var dy = end.y - start.y;
      return Arrays.stream(Direction.values())
          .filter(direction -> direction.dx == dx && direction.dy == dy)
          .findAny()
          .orElseThrow(() -> new IllegalArgumentException("Points are not adjacent"));
    }
  }
}
