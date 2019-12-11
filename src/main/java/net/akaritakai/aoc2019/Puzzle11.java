package net.akaritakai.aoc2019;

import com.google.common.annotations.VisibleForTesting;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneaked;
import static net.akaritakai.aoc2019.Puzzle11.Color.DEFAULT;

public class Puzzle11 extends AbstractPuzzle {

  public Puzzle11(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 11;
  }

  @Override
  public String solvePart1() {
    var input = new LinkedBlockingQueue<Long>();
    var state = new RobotState(new HashMap<>(), input);
    var vm = new IntcodeVm(getPuzzleInput(), sneaked(input::take), state::onOutput);
    vm.run();
    return String.valueOf(state.getPaintedSquares().size());
  }

  @Override
  public String solvePart2() {
    var hull = new HashMap<Point, Color>();
    hull.put(RobotState.ORIGIN, Color.WHITE); // starting panel is white
    var input = new LinkedBlockingQueue<Long>();
    var state = new RobotState(hull, input);
    var vm = new IntcodeVm(getPuzzleInput(), sneaked(input::take), state::onOutput);
    vm.run();
    return "\n" + renderImage(hull);
  }

  private String renderImage(Map<Point, Color> hull) {
    var area = getVisibleDimensions(hull);
    return IntStream.rangeClosed(area.y, area.y + area.height).boxed()
        .sorted(Collections.reverseOrder()) // Hull image is drawn upside down
        .map(y -> IntStream.rangeClosed(area.x, area.x + area.width).boxed()
            .map(x -> new Point(x, y))
            .map(point -> hull.getOrDefault(point, DEFAULT))
            .map(color -> color == DEFAULT ? " " : "#")
            .collect(Collectors.joining()))
        .collect(Collectors.joining("\n"));
  }

  private Rectangle getVisibleDimensions(Map<Point, Color> hull) {
    /* Points are visible if they "stand out" from the hull's default color (i.e. are not the default color) */
    Predicate<Map.Entry<Point, Color>> isVisible = e -> e.getValue() != DEFAULT;
    Supplier<Stream<Point>> visiblePoints = () -> hull.entrySet().stream().filter(isVisible).map(Map.Entry::getKey);

    var minHeight = visiblePoints.get().mapToInt(point -> point.y).min().orElseThrow();
    var maxHeight = visiblePoints.get().mapToInt(point -> point.y).max().orElseThrow();
    var minWidth = visiblePoints.get().mapToInt(point -> point.x).min().orElseThrow();
    var maxWidth = visiblePoints.get().mapToInt(point -> point.x).max().orElseThrow();
    return new Rectangle(minWidth, minHeight, maxWidth - minWidth, maxHeight - minHeight);
  }

  @VisibleForTesting
  static class RobotState {
    @VisibleForTesting
    static final Direction STARTING_DIRECTION = Direction.UP;
    @VisibleForTesting
    static final Point ORIGIN = new Point(0, 0);

    private final BlockingQueue<Long> _robotInput;
    private final List<Long> _robotOutput = new ArrayList<>();
    private final Map<Point, Color> _hull;
    private final Set<Point> _painted = ConcurrentHashMap.newKeySet();

    private Point _position = ORIGIN;
    private Direction _direction = STARTING_DIRECTION;

    @VisibleForTesting
    RobotState(Map<Point, Color> hull, BlockingQueue<Long> robotInput) {
      _hull = hull;
      _robotInput = robotInput;
      robotInput.add(_hull.getOrDefault(ORIGIN, DEFAULT).getValue()); // seed robot with color of its position
    }

    @VisibleForTesting
    synchronized void onOutput(long value) {
      _robotOutput.add(value);
      if (_robotOutput.size() == 2) {
        _hull.put(_position, Color.of(_robotOutput.get(0))); // color the hull
        _painted.add(_position); // mark that we colored this position
        _direction = _direction.turn(Turn.of(_robotOutput.get(1))); // turn the robot
        _position = _direction.move(_position); // move forward one space
        _robotInput.add(_hull.getOrDefault(_position, DEFAULT).getValue()); // signal the program
        _robotOutput.clear();
      }
    }

    @VisibleForTesting
    Set<Point> getPaintedSquares() {
      return Collections.unmodifiableSet(_painted);
    }

    @VisibleForTesting
    Direction getRobotDirection() {
      return _direction;
    }

    @VisibleForTesting
    Point getRobotPosition() {
      return _position;
    }
  }

  enum Color {
    BLACK (0),
    WHITE (1);

    static final Color DEFAULT = BLACK;

    private final long _value;

    Color(long value) {
      _value = value;
    }

    long getValue() {
      return _value;
    }

    private static Color of(long value) {
      return Arrays.stream(Color.values())
          .filter(color -> color.getValue() == value)
          .findAny()
          .orElseThrow(()  -> new IllegalArgumentException("Unknown color value: " + value));
    }
  }

  @VisibleForTesting
  enum Direction {
    UP (0, 1),
    DOWN (0, -1),
    LEFT (-1, 0),
    RIGHT (1, 0);

    private final int _dx;
    private final int _dy;

    Direction(int dx, int dy) {
      _dx = dx;
      _dy = dy;
    }

    private Point move(Point point) {
      return new Point(point.x + _dx, point.y + _dy);
    }

    private Direction turn(Turn turn) {
      switch (turn) {
        case LEFT:
          switch (this) {
            case UP: return LEFT;
            case DOWN: return RIGHT;
            case LEFT: return DOWN;
            case RIGHT: return UP;
          }
          break;
        case RIGHT:
          switch (this) {
            case UP: return RIGHT;
            case DOWN: return LEFT;
            case LEFT: return UP;
            case RIGHT: return DOWN;
          }
          break;
      }
      throw new IllegalArgumentException("Unknown turn signal");
    }
  }

  public enum Turn {
    LEFT (0),
    RIGHT (1);

    public final long _value;

    Turn(long value) {
      _value = value;
    }

    public long getValue() {
      return _value;
    }

    public static Turn of(long value) {
      return Arrays.stream(Turn.values())
          .filter(color -> color.getValue() == value)
          .findAny()
          .orElseThrow(()  -> new IllegalArgumentException("Unknown turn value: " + value));
    }
  }
}
