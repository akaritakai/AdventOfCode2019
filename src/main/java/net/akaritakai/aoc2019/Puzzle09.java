package net.akaritakai.aoc2019;

import java.util.concurrent.atomic.AtomicLong;

public class Puzzle09 extends AbstractPuzzle {

  public Puzzle09(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 9;
  }

  @Override
  public String solvePart1() {
    var output = new AtomicLong();
    var vm = new IntcodeVm(getPuzzleInput(), () -> 1L, output::set);
    vm.run();
    return String.valueOf(output.get());
  }

  @Override
  public String solvePart2() {
    var output = new AtomicLong();
    var vm = new IntcodeVm(getPuzzleInput(), () -> 2L, output::set);
    vm.run();
    return String.valueOf(output.get());
  }
}
