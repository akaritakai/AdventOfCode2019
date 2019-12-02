package net.akaritakai.aoc2019;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;


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
    var program = programSupplier().get();
    program.set(1, 12);
    program.set(2, 2);
    runProgram(program);
    return String.valueOf(program.get(0));
  }

  @Override
  public String solvePart2() {
    for (var noun = 0; noun < 100; noun++) {
      for (var verb = 0; verb < 100; verb++) {
        var program = programSupplier().get();
        program.set(1, noun);
        program.set(2, verb);
        runProgram(program);
        if (program.get(0) == 19690720) {
          return String.valueOf(100 * noun + verb);
        }
      }
    }
    throw new IllegalStateException("Unable to find noun and verb");
  }

  @VisibleForTesting
  static void runProgram(List<Integer> program) {
    var ip = 0;
    while (true) {
      var opcode = OpCode.of(program.get(ip));
      switch (opcode) {
        case ADD -> {
          var operand1 = program.get(program.get(ip + 1));
          var operand2 = program.get(program.get(ip + 2));
          program.set(program.get(ip + 3), operand1 + operand2);
          ip += 4;
        }
        case MULTIPLY -> {
          var operand1 = program.get(program.get(ip + 1));
          var operand2 = program.get(program.get(ip + 2));
          program.set(program.get(ip + 3), operand1 * operand2);
          ip += 4;
        }
        case HALT -> {
          return;
        }
      }
    }
  }

  private Supplier<List<Integer>> programSupplier() {
    return () -> Arrays.stream(getPuzzleInput().trim().split(","))
        .map(Integer::parseInt)
        .collect(Collectors.toList());
  }

  private enum OpCode {
    ADD,
    MULTIPLY,
    HALT;

    private static OpCode of(int code) {
      return switch (code) {
        case 1 -> ADD;
        case 2 -> MULTIPLY;
        case 99 -> HALT;
        default -> throw new UnsupportedOperationException("Unknown opcode");
      };
    }
  }
}
