package net.akaritakai.aoc2019;

import com.google.common.annotations.VisibleForTesting;
import net.akaritakai.aoc2019.geom2d.Direction;
import net.akaritakai.aoc2019.geom2d.Image;
import net.akaritakai.aoc2019.geom2d.Point;
import net.akaritakai.aoc2019.geom2d.Turn;
import net.akaritakai.aoc2019.intcode.IntcodeVm;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import java.util.function.Predicate;

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
    Function<Point, String> renderer = point -> hull.get(point) == DEFAULT ? " " : "#";
    Predicate<Point> isVisible = point -> hull.get(point) != DEFAULT; // Non-default hull squares stand out
    return Image.renderImageUpsideDown(Image.imageSize(hull.keySet(), isVisible), renderer);
  }

  @VisibleForTesting
  static class RobotState {
    @VisibleForTesting
    static final Direction STARTING_DIRECTION = Direction.NORTH;
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
        _direction = _direction.turn(turn(_robotOutput.get(1))); // turn the robot
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

    private final long value;

    Color(long value) {
      this.value = value;
    }

    long getValue() {
      return value;
    }

    private static Color of(long value) {
      for (Color color : Color.values()) {
        if (value == color.value) {
          return color;
        }
      }
      throw new IllegalArgumentException("Unknown color value: " + value);
    }
  }

  private static Turn turn(long value) {
    switch (Math.toIntExact(value)) {
      case 0: return Turn.LEFT;
      case 1: return Turn.RIGHT;
    }
    throw new IllegalArgumentException("Unknown turn value: " + value);
  }
}
