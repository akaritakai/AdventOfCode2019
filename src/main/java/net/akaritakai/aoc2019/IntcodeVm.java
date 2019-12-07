package net.akaritakai.aoc2019;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Simulates an Intcode machine.
 */
public class IntcodeVm {

  private final List<Integer> _positions;
  private final Supplier<Integer> _input;
  private final Consumer<Integer> _output;

  /**
   * Constructor which accepts the program to run.
   */
  public IntcodeVm(String program) {
    this(program, () -> null, i -> {});
  }

  /**
   * Constructor which accepts the program to run, and suppliers and consumers to handle the VM's input and output.
   */
  public IntcodeVm(String program, Supplier<Integer> input, Consumer<Integer> output) {
    _positions = Arrays.stream(program.trim().split(",")).map(Integer::parseInt).collect(Collectors.toList());
    _input = input;
    _output = output;
  }

  /**
   * Runs the VM until it halts.
   */
  public void run() {
    var ip = 0;
    while (true) {
      var instruction = new Instruction(ip);
      var param1 = instruction.param1;
      var param2 = instruction.param2;
      switch (instruction.opCode) {
        case ADD -> {
          _positions.set(_positions.get(ip + 3), param1 + param2);
          ip += 4;
        }
        case MULTIPLY -> {
          _positions.set(_positions.get(ip + 3), param1 * param2);
          ip += 4;
        }
        case INPUT -> {
          _positions.set(_positions.get(ip + 1), _input.get());
          ip += 2;
        }
        case OUTPUT -> {
          _output.accept(param1);
          ip += 2;
        }
        case JUMP_IF_TRUE -> ip = param1 != 0 ? param2 : ip + 3;
        case JUMP_IF_FALSE -> ip = param1 == 0 ? param2 : ip + 3;
        case LESS_THAN -> {
          _positions.set(_positions.get(ip + 3), param1 < param2 ? 1 : 0);
          ip += 4;
        }
        case EQUALS -> {
          _positions.set(_positions.get(ip + 3), param1.equals(param2) ? 1 : 0);
          ip += 4;
        }
        case HALT -> {
          return;
        }
      }
    }
  }

  /**
   * Gets the VM's memory.
   */
  public List<Integer> positions() {
    return _positions;
  }

  private final class Instruction {
    private final OpCode opCode;
    private final Integer param1;
    private final Integer param2;
    private final Integer param3;

    private Instruction(int ip) {
      var code = _positions.get(ip);
      opCode = OpCode.of(code % 100);
      param1 = getParam(Mode.of((code / 100) % 2), ip + 1);
      param2 = getParam(Mode.of((code / 1000) % 2), ip + 2);
      param3 = getParam(Mode.of((code / 10000) % 2), ip + 3);
    }

    private Integer getParam(Mode mode, int offset) {
      if (offset >= _positions.size()) {
        return null;
      }
      int ref = _positions.get(offset);
      return switch (mode) {
        case IMMEDIATE -> ref;
        case POSITION -> ref >= 0 && ref < _positions.size() ? _positions.get(ref) : null;
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
