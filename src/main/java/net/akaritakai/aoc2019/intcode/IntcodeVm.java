package net.akaritakai.aoc2019.intcode;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.apache.commons.math3.util.ArithmeticUtils.pow;

/**
 * Simulates an Intcode machine.
 */
public class IntcodeVm {
  private static final Logger LOG = LoggerFactory.getLogger(IntcodeVm.class);

  private final Map<Long, Long> _memory;
  private final Supplier<Long> _input;
  private final Consumer<Long> _output;
  private long _ip = 0;
  private long _base = 0;

  /**
   * Constructor which accepts the program to run.
   */
  public IntcodeVm(String program) {
    this(program, () -> null, i -> {});
  }

  /**
   * Constructor which accepts the program to run, and suppliers and consumers to handle the VM's input and output.
   */
  public IntcodeVm(String program, Supplier<Long> input, Consumer<Long> output) {
    _memory = programMemory(program);
    _input = input;
    _output = output;
  }

  /**
   * Runs the VM until it halts.
   */
  public void run() {
    try {
      while (true) {
        switch (opcode()) {
          case ADD: {
            _memory.put(param(3).address, param(1).value + param(2).value);
            _ip += 4;
            break;
          }
          case MULTIPLY: {
            _memory.put(param(3).address, param(1).value * param(2).value);
            _ip += 4;
            break;
          }
          case INPUT: {
            _memory.put(param(1).address, _input.get());
            _ip += 2;
            break;
          }
          case OUTPUT: {
            _output.accept(param(1).value);
            _ip += 2;
            break;
          }
          case JUMP_IF_TRUE: {
            _ip = param(1).value != 0 ? param(2).value : _ip + 3;
            break;
          }
          case JUMP_IF_FALSE: {
            _ip = param(1).value == 0 ? param(2).value : _ip + 3;
            break;
          }
          case LESS_THAN: {
            _memory.put(param(3).address, param(1).value < param(2).value ? 1L : 0L);
            _ip += 4;
            break;
          }
          case EQUALS: {
            _memory.put(param(3).address, param(1).value == param(2).value ? 1L : 0L);
            _ip += 4;
            break;
          }
          case ADJUST_RELATIVE_BASE: {
            _base += param(1).value;
            _ip += 2;
            break;
          }
          case HALT: {
            return;
          }
        }
      }
    } catch (Exception e) {
      LOG.warn("IntCode machine ran into an exception", e);
    }
  }

  /**
   * Gets the VM's memory.
   */
  public Map<Long, Long> memory() {
    return _memory;
  }

  @VisibleForTesting
  static Map<Long, Long> programMemory(String program) {
    Map<Long, Long> memory = new ConcurrentHashMap<>();
    var it = Arrays.stream(program.trim().split(",")).map(Long::parseLong).iterator();
    for (long i = 0; it.hasNext(); i++) {
      memory.put(i, it.next());
    }
    return memory;
  }

  private enum OpCode {
    ADD (1),
    MULTIPLY (2),
    INPUT (3),
    OUTPUT (4),
    JUMP_IF_TRUE (5),
    JUMP_IF_FALSE (6),
    LESS_THAN (7),
    EQUALS (8),
    ADJUST_RELATIVE_BASE (9),
    HALT (99);

    private final long _code;

    OpCode(long code) {
      _code = code;
    }

    private static OpCode of(long code) {
      return Arrays.stream(OpCode.values())
          .filter(opcode -> opcode._code == code)
          .findAny()
          .orElseThrow(() -> new UnsupportedOperationException("Unknown opcode: " + code));
    }
  }

  private OpCode opcode() {
    return OpCode.of(_memory.get(_ip) % 100);
  }

  private enum Mode {
    POSITION (0),
    IMMEDIATE (1),
    RELATIVE (2);

    private final long _code;

    Mode(long code) {
      _code = code;
    }

    private static Mode of(long code) {
      return Arrays.stream(Mode.values())
          .filter(mode -> mode._code == code)
          .findAny()
          .orElseThrow(() -> new UnsupportedOperationException("Unknown mode: " + code));
    }
  }

  private Mode mode(long param) {
    var ref = _memory.getOrDefault(_ip, 0L);
    return Mode.of((ref / pow(10, (int) param + 1)) % 10);
  }

  private final class Parameter {
    private final long address;
    private final long value;

    private Parameter(int param) {
      switch (mode(param)) {
        case POSITION:
          address = _memory.getOrDefault(_ip + param, 0L);
          break;
        case IMMEDIATE:
          address = _ip + param;
          break;
        case RELATIVE:
          address = _memory.getOrDefault(_ip + param, 0L) + _base;
          break;
        default: throw new UnsupportedOperationException("Unknown mode: " + mode(param));
      }
      value = _memory.getOrDefault(address, 0L);
    }
  }

  private Parameter param(int i) {
    return new Parameter(i);
  }
}
