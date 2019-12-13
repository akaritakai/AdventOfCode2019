package net.akaritakai.aoc2019;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class Puzzle13 extends AbstractPuzzle {

  public Puzzle13(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 13;
  }

  @Override
  public String solvePart1() {
    var state = new GameState();
    var vm = new IntcodeVm(getPuzzleInput(), () -> null, state::onOutput);
    vm.run();
    long count = state.getScreen().values().stream().filter(tile -> tile == TileType.BLOCK).count();
    return String.valueOf(count);
  }

  @Override
  public String solvePart2() {
    var state = new GameState();
    var vm = new IntcodeVm(getPuzzleInput(), state.getInput(), state::onOutput);
    vm.memory().put(0L, 2L);
    vm.run();
    return String.valueOf(state.getScore());
  }

  private static class GameState {
    private final Map<Point, TileType> _screen = new ConcurrentHashMap<>();
    private final List<Long> _gameOutput = new ArrayList<>();
    private long _score = 0;

    synchronized void onOutput(long value) {
      _gameOutput.add(value);
      if (_gameOutput.size() == 3) {
        long x = _gameOutput.get(0);
        long y = _gameOutput.get(1);
        TileType tile = TileType.of(_gameOutput.get(2));
        if (x == -1 && y == 0 && tile == null) {
          _score = _gameOutput.get(2);
        } else {
          Point position = new Point((int) x, (int) y);
          _screen.put(position, tile);
        }
        _gameOutput.clear();
      }
    }

    Map<Point, TileType> getScreen() {
      return _screen;
    }

    long getScore() {
      return _score;
    }

    Supplier<Long> getInput() {
      return () -> {
        var ballPos = _screen.entrySet().stream()
            .filter(e -> e.getValue() == TileType.BALL)
            .mapToLong(e -> e.getKey().x)
            .findAny()
            .orElse(0);
        var paddlePos = _screen.entrySet().stream()
            .filter(e -> e.getValue() == TileType.HORIZONTAL_PADDLE)
            .mapToLong(e -> e.getKey().x)
            .findAny()
            .orElse(0);
        return (long) Long.compare(ballPos, paddlePos);
      };
    }
  }

  private enum TileType {
    EMPTY (0),
    WALL (1),
    BLOCK (2),
    HORIZONTAL_PADDLE (3),
    BALL (4);

    private final long _id;

    TileType(long id) {
      _id = id;
    }

    public long getId() {
      return _id;
    }

    public static TileType of(long id) {
      return Arrays.stream(TileType.values())
          .filter(color -> color.getId() == id)
          .findAny()
          .orElse(null);
    }
  }
}
