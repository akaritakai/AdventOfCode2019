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

  private volatile boolean _shutdown = false;

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
      while (!_shutdown) {
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

  public void halt() {
    _shutdown = true;
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
    ADD,
    MULTIPLY,
    INPUT,
    OUTPUT,
    JUMP_IF_TRUE,
    JUMP_IF_FALSE,
    LESS_THAN,
    EQUALS,
    ADJUST_RELATIVE_BASE,
    HALT;

    private static OpCode of(long code) {
      switch (Math.toIntExact(code)) {
        case 1: return ADD;
        case 2: return MULTIPLY;
        case 3: return INPUT;
        case 4: return OUTPUT;
        case 5: return JUMP_IF_TRUE;
        case 6: return JUMP_IF_FALSE;
        case 7: return LESS_THAN;
        case 8: return EQUALS;
        case 9: return ADJUST_RELATIVE_BASE;
        case 99: return HALT;
      }
      throw new UnsupportedOperationException("Unknown opcode: " + code);
    }
  }

  private OpCode opcode() {
    return OpCode.of(_memory.get(_ip) % 100);
  }

  private enum Mode {
    POSITION,
    IMMEDIATE,
    RELATIVE;

    private static Mode of(long code) {
      switch (Math.toIntExact(code)) {
        case 0: return POSITION;
        case 1: return IMMEDIATE;
        case 2: return RELATIVE;
      }
      throw new UnsupportedOperationException("Unknown mode: " + code);
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
