package net.akaritakai.aoc2019;

import net.akaritakai.aoc2019.intcode.IntcodeVm;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Puzzle21 extends AbstractPuzzle {

  public Puzzle21(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 21;
  }

  @Override
  public String solvePart1() {
    // From the puzzle example, we know that after we jump, we land four squares away.
    // It is thus only safe for us to jump when D is solid.
    // We only need to jump when we are in danger, and we are in danger (need to jump) whenever we see a hole in front
    // of us: A, B, or C is not solid.
    // This means J = (!A || !B || !C) && D = !(A && B && C && D) && D
    var program = List.of(
        "OR A J",  // J = A
        "AND B J", // J = A && B
        "AND C J", // J = A && B && C
        "NOT J J", // J = !(A && B && C)
        "AND D J", // J = !(A && B && C) && D
        "WALK").stream().map(i -> i + "\n").collect(Collectors.joining());
    var input = program.chars().mapToLong(i -> i).iterator();
    var output = new AtomicLong();
    var vm = new IntcodeVm(getPuzzleInput(), input::next, output::set);
    vm.run();
    return String.valueOf(output.get());
  }

  @Override
  public String solvePart2() {
    // We can take the solution for part 1 and augment it with our new sensors.
    // After we jump to D, we will either take a step (E must be solid), or we will jump again (H must be solid).
    // This means J = !(A && B && C && D) && D && (E || H)
    var program = List.of(
        "OR A J",  // J = A
        "AND B J", // J = A && B
        "AND C J", // J = A && B && C
        "NOT J J", // J = !(A && B && C)
        "AND D J", // J = !(A && B && C) && D
        "OR E T", // T = E
        "OR H T", // T = E || H
        "AND T J", // J = !(A && B && C) && D && (E || H)
        "RUN").stream().map(i -> i + "\n").collect(Collectors.joining());
    var input = program.chars().mapToLong(i -> i).iterator();
    var output = new AtomicLong();
    var vm = new IntcodeVm(getPuzzleInput(), input::next, output::set);
    vm.run();
    return String.valueOf(output.get());
  }
}
