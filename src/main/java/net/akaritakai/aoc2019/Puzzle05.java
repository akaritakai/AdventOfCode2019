package net.akaritakai.aoc2019;

import net.akaritakai.aoc2019.intcode.IntcodeVm;

import java.util.concurrent.atomic.AtomicLong;


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
    var output = new AtomicLong();
    var vm = new IntcodeVm(getPuzzleInput(), () -> 1L, output::set);
    vm.run();
    return String.valueOf(output.get());
  }

  @Override
  public String solvePart2() {
    var output = new AtomicLong();
    var vm = new IntcodeVm(getPuzzleInput(), () -> 5L, output::set);
    vm.run();
    return String.valueOf(output.get());
  }
}
