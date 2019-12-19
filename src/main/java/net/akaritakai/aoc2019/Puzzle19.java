package net.akaritakai.aoc2019;

import net.akaritakai.aoc2019.geom2d.Point;
import net.akaritakai.aoc2019.intcode.IntcodeVm;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Puzzle19 extends AbstractPuzzle {

  private final Map<Point, Boolean> _memo = new ConcurrentHashMap<>();

  public Puzzle19(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 19;
  }

  @Override
  public String solvePart1() {
    var affected = 0;
    var x = 0;
    var min = 0;
    for (var y = 0; y < 50; y++) {
      while (x < 50 && !pulled(x, y)) {
        x++;
      }
      min = pulled(x, y) ? x : min;
      while (x < 50 && pulled(x, y)) {
        affected++;
        x++;
      }
      x = min;
    }
    return String.valueOf(affected);
  }

  @Override
  public String solvePart2() {
    var x = 0;
    var y = 99;
    while (true) {
      while (!pulled(x, y)) {
        x++;
      }
      if (pulled(x, y - 99)
          && pulled(x, y)
          && pulled(x + 99, y - 99)
          && pulled(x + 99, y)) {
        return String.valueOf(10_000 * x + (y - 99));
      }
      y++;
    }
  }

  private boolean pulled(long x, long y) {
    if (x < 0 || y < 0) {
      return false;
    }
    return _memo.computeIfAbsent(new Point(x, y), s -> {
      var input = List.of(x, y).iterator();
      var output = new AtomicLong();
      var vm = new IntcodeVm(getPuzzleInput(), input::next, output::set);
      vm.run();
      return output.get() == 1;
    });
  }
}
