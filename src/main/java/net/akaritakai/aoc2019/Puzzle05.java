package net.akaritakai.aoc2019;

import java.util.concurrent.atomic.AtomicInteger;


public class Puzzle05 extends AbstractPuzzle {

  public Puzzle05(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 5;
  }

  @Override
  public String solvePart1() {
    var output = new AtomicInteger();
    var vm = new IntcodeVm(getPuzzleInput(), () -> 1, output::set);
    vm.run();
    return String.valueOf(output.get());
  }

  @Override
  public String solvePart2() {
    var output = new AtomicInteger();
    var vm = new IntcodeVm(getPuzzleInput(), () -> 5, output::set);
    vm.run();
    return String.valueOf(output.get());
  }
}
