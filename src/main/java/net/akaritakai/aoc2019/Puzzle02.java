package net.akaritakai.aoc2019;

public class Puzzle02 extends AbstractPuzzle {

  public Puzzle02(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 2;
  }

  @Override
  public String solvePart1() {
    var vm = new IntcodeVm(getPuzzleInput());
    vm.memory().put(1L, 12L);
    vm.memory().put(2L, 2L);
    vm.run();
    return String.valueOf(vm.memory().get(0L));
  }

  @Override
  public String solvePart2() {
    for (long noun = 0; noun < 100; noun++) {
      for (long verb = 0; verb < 100; verb++) {
        var vm = new IntcodeVm(getPuzzleInput());
        vm.memory().put(1L, noun);
        vm.memory().put(2L, verb);
        vm.run();
        if (vm.memory().get(0L) == 19690720) {
          return String.valueOf(100 * noun + verb);
        }
      }
    }
    throw new IllegalStateException("Unable to find noun and verb");
  }
}
