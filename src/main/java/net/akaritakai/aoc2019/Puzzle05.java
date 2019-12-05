package net.akaritakai.aoc2019;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;


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
    var input = new Stack<Integer>();
    var output = new Stack<Integer>();
    input.push(1);
    runProgram(programSupplier().get(), input, output);
    return String.valueOf(output.pop());
  }

  @Override
  public String solvePart2() {
    var input = new Stack<Integer>();
    var output = new Stack<Integer>();
    input.push(5);
    runProgram(programSupplier().get(), input, output);
    return String.valueOf(output.pop());
  }

  @VisibleForTesting
  static void runProgram(List<Integer> program, Stack<Integer> input, Stack<Integer> output) {
    var ip = 0;
    while (true) {
      var instruction = Instruction.of(program, ip);
      var param1 = instruction.param1;
      var param2 = instruction.param2;
      switch (instruction.opCode) {
        case ADD -> {
          program.set(program.get(ip + 3), param1 + param2);
          ip += 4;
        }
        case MULTIPLY -> {
          program.set(program.get(ip + 3), param1 * param2);
          ip += 4;
        }
        case INPUT -> {
          program.set(program.get(ip + 1), input.pop());
          ip += 2;
        }
        case OUTPUT -> {
          output.push(param1);
          ip += 2;
        }
        case JUMP_IF_TRUE -> ip = param1 != 0 ? param2 : ip + 3;
        case JUMP_IF_FALSE -> ip = param1 == 0 ? param2 : ip + 3;
        case LESS_THAN -> {
          program.set(program.get(ip + 3), param1 < param2 ? 1 : 0);
          ip += 4;
        }
        case EQUALS -> {
          program.set(program.get(ip + 3), param1.equals(param2) ? 1 : 0);
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

  private static class Instruction {
    private final OpCode opCode;
    private final Integer param1;
    private final Integer param2;
    private final Integer param3;

    private Instruction(OpCode opCode, Integer param1, Integer param2, Integer param3) {
      this.opCode = opCode;
      this.param1 = param1;
      this.param2 = param2;
      this.param3 = param3;
    }

    private static Instruction of(List<Integer> program, int ip) {
      var code = program.get(ip);
      var opCode = OpCode.of(code % 100);
      var param1 = getParam(program, Mode.of((code / 100) % 2), ip + 1);
      var param2 = getParam(program, Mode.of((code / 1000) % 2), ip + 2);
      var param3 = getParam(program, Mode.of((code / 10000) % 2), ip + 3);
      return new Instruction(opCode, param1, param2, param3);
    }

    private static Integer getParam(List<Integer> program, Mode mode, int offset) {
      if (offset >= program.size()) {
        return null;
      }
      int ref = program.get(offset);
      return switch (mode) {
        case IMMEDIATE -> ref;
        case POSITION -> ref >= 0 && ref < program.size() ? program.get(ref) : null;
      };
    }
  }

  private enum Mode {
    POSITION,
    IMMEDIATE;

    private static Mode of(int code) {
      return switch (code) {
        case 0 -> POSITION;
        case 1 -> IMMEDIATE;
        default -> throw new UnsupportedOperationException("Unknown mode: " + code);
      };
    }
  }

  private enum OpCode {
    ADD,
    MULTIPLY,
    INPUT,
    OUTPUT,
    JUMP_IF_TRUE,
    JUMP_IF_FALSE,
    LESS_THAN,
    EQUALS,
    HALT;

    private static OpCode of(int code) {
      return switch (code) {
        case 1 -> ADD;
        case 2 -> MULTIPLY;
        case 3 -> INPUT;
        case 4 -> OUTPUT;
        case 5 -> JUMP_IF_TRUE;
        case 6 -> JUMP_IF_FALSE;
        case 7 -> LESS_THAN;
        case 8 -> EQUALS;
        case 99 -> HALT;
        default -> throw new UnsupportedOperationException("Unknown opcode: " + code);
      };
    }
  }
}
