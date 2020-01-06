package net.akaritakai.aoc2019;

import net.akaritakai.aoc2019.intcode.IntcodeVm;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Puzzle23 extends AbstractPuzzle {

  public Puzzle23(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 23;
  }

  @Override
  public String solvePart1() {
    var network = new Network(getPuzzleInput());
    while (network.nat.firstPacket == null) {
      Thread.onSpinWait();
    }
    network.halt();
    return String.valueOf(network.nat.firstPacket.y);
  }

  @Override
  public String solvePart2() {
    var network = new Network(getPuzzleInput());
    while (network.nat.firstRepeatedValue == null) {
      Thread.onSpinWait();
    }
    network.halt();
    return String.valueOf(network.nat.firstRepeatedValue);
  }

  private static class Network {
    private final String program;
    private final NAT nat;
    private final List<Machine> machines = new ArrayList<>();
    private final Map<Long, Queue<Packet>> inputQueues = new ConcurrentHashMap<>();

    private Network(String program) {
      this.program = program;
      for (long i = 0; i < 50; i++) {
        var inputQueue = inputQueues.computeIfAbsent(i, s -> new ConcurrentLinkedQueue<>());
        var machine = new Machine(this, i, inputQueue);
        machines.add(machine);
      }
      nat = new NAT(this, inputQueues.computeIfAbsent(255L, s -> new ConcurrentLinkedQueue<>()));
    }

    private String program() {
      return program;
    }

    private void send(long address, Packet packet) {
      inputQueues.computeIfAbsent(address, s -> new ConcurrentLinkedQueue<>()).add(packet);
    }

    private boolean idle() {
      return machines.stream().allMatch(Machine::idle);
    }

    private void halt() {
      machines.forEach(Machine::halt);
      nat.halt();
    }
  }

  private static class Machine {
    private final Network network;
    private final InputBuffer input;
    private final OutputBuffer output;
    private final IntcodeVm vm;

    private Machine(Network network, long address, Queue<Packet> inputQueue) {
      this.network = network;
      input = new InputBuffer(address, inputQueue);
      output = new OutputBuffer(this);
      vm = new IntcodeVm(network.program(), input, output);
      new Thread(vm::run).start();
    }

    private boolean idle() {
      return input.idle();
    }

    private void halt() {
      vm.halt();
    }
  }

  private static class NAT {
    private final Thread vm;
    private final Set<Long> deliveredValues = ConcurrentHashMap.newKeySet();

    private volatile boolean running = true;
    private volatile Packet packet;
    private volatile Packet firstPacket;
    private volatile Long firstRepeatedValue;

    private NAT(Network network, Queue<Packet> inputQueue) {
      vm = new Thread(() -> {
        while (running) {
          drainQueue(inputQueue);
          if (packet != null && network.idle()) {
            drainQueue(inputQueue);
            if (network.idle()) {
              if (firstRepeatedValue == null && !deliveredValues.add(packet.y)) {
                firstRepeatedValue = packet.y;
              }
              network.send(0, packet);
              network.machines.forEach(machine -> machine.input.resetIdleCounter());
            }
          }
        }
      });
      vm.start();
    }

    private void drainQueue(Queue<Packet> inputQueue) {
      while (!inputQueue.isEmpty()) {
        var receivedPacket = inputQueue.poll();
        if (firstPacket == null) {
          firstPacket = receivedPacket;
        }
        packet = receivedPacket;
      }
    }

    private void halt() {
      running = false;
    }
  }

  private static class InputBuffer implements Supplier<Long> {
    private final long address;
    private final Queue<Packet> buffer;

    private volatile boolean receivedAddress;
    private volatile Packet firstPacket; // Set when we receive our first packet
    private volatile Packet packet;
    private volatile long idleCounter = 0;

    private InputBuffer(long address, Queue<Packet> buffer) {
      this.address = address;
      this.buffer = buffer;
    }

    @Override
    public synchronized Long get() {
      // The first time we start, give our machine its address
      if (!receivedAddress) {
        receivedAddress = true;
        return address;
      }

      if (packet == null) {
        // We aren't currently reading a packet
        packet = buffer.poll();
        if (packet == null) {
          // No packet available, so return -1
          idleCounter++;
          return -1L;
        } else {
          // Packet available
          resetIdleCounter();
          if (firstPacket == null) {
            firstPacket = packet;
          }
          // Read X, and the next get() will read Y.
          return packet.x;
        }
      } else {
        // We are currently reading a packet and previously read X, so this get() will read Y.
        var y = packet.y;
        packet = null;
        return y;
      }
    }

    public synchronized void resetIdleCounter() {
      idleCounter = 0;
    }

    public synchronized boolean idle() {
      return idleCounter >= 1_000 && packet == null && buffer.isEmpty();
    }
  }

  private static class OutputBuffer implements Consumer<Long> {
    private final Machine machine;
    private final List<Long> buffer = new ArrayList<>(3);

    private OutputBuffer(Machine machine) {
      this.machine = machine;
    }

    @Override
    public void accept(Long value) {
      buffer.add(value);
      if (buffer.size() == 3) {
        // Finished receiving a packet
        var address = buffer.get(0);
        var x = buffer.get(1);
        var y = buffer.get(2);
        var packet = new Packet(x, y);
        machine.network.send(address, packet);
        machine.input.resetIdleCounter();
        // Clear buffer
        buffer.clear();
      }
    }
  }

  private static class Packet {
    private final long x;
    private final long y;

    private Packet(long x, long y) {
      this.x = x;
      this.y = y;
    }
  }
}
