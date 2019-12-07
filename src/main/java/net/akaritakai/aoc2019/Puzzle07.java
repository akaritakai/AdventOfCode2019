package net.akaritakai.aoc2019;

import com.google.common.collect.Collections2;
import com.rainerhahnekamp.sneakythrow.functional.SneakyConsumer;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneaked;

public class Puzzle07 extends AbstractPuzzle {

  public Puzzle07(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 7;
  }

  @Override
  public String solvePart1() {
    var maxSignal = Collections2.permutations(List.of(0, 1, 2, 3, 4))
        .stream()
        .mapToInt(this::runChainedAmplifiers)
        .max()
        .orElseThrow(() -> new IllegalStateException("No amplifier outputs returned"));
    return String.valueOf(maxSignal);
  }

  @Override
  public String solvePart2() {
    var maxSignal = Collections2.permutations(List.of(5, 6, 7, 8, 9))
        .stream()
        .mapToInt(this::runChainedLoopedAmplifiers)
        .max()
        .orElseThrow(() -> new IllegalStateException("No amplifier outputs returned"));
    return String.valueOf(maxSignal);
  }

  /**
   * Runs a set of amplifiers in series, applying the phase settings provided to each of them.
   * Returns the output of the last amplifier.
   */
  int runChainedAmplifiers(List<Integer> phaseSettings) {
    AtomicInteger input = new AtomicInteger(0);
    IntStream.range(0, phaseSettings.size()).forEach(i -> {
      var phaseSetting = phaseSettings.get(i);
      var output = runAmplifier(phaseSetting, input.get());
      input.set(output); // pass output to the next amplifier
    });
    return input.get();
  }

  /**
   * Returns the output of an amplifier that accepts a phase setting and then a single input value.
   */
  int runAmplifier(int phaseSetting, int inputValue) {
    var input = List.of(phaseSetting, inputValue).iterator();
    var output = new AtomicInteger();
    var vm = new IntcodeVm(getPuzzleInput(), input::next, output::set);
    vm.run();
    return output.get();
  }

  /**
   * Runs a set of amplifiers in a chained feedback loop, applying the phase settings provided to each of them.
   * Returns the last unused output of the last amplifier after all amplifiers halt.
   */
  int runChainedLoopedAmplifiers(List<Integer> phaseSettings) {
    // Create the i/o streams
    var streams = Stream.generate((Supplier<LinkedBlockingQueue<Integer>>) LinkedBlockingQueue::new)
        .limit(phaseSettings.size())
        .collect(Collectors.toList());
    // Create and start the amplifiers
    var amplifiers = IntStream.range(0, phaseSettings.size())
        .mapToObj(i -> {
          var phaseSetting = phaseSettings.get(i);
          var input = streams.get(i);
          var output = streams.get((i + 1) % phaseSettings.size());
          return runLoopedAmplifier(phaseSetting, input, output);
        })
        .collect(Collectors.toList());
    streams.get(0).add(0); // send 0 to the first amplifier
    amplifiers.forEach(sneaked((SneakyConsumer<Thread, Exception>) Thread::join)); // wait for the amplifiers to halt
    return streams.get(0).remove();
  }

  /**
   * Returns a thread for an amplifier that accepts a phase setting and an input stream. Amplifier outputs will be sent
   * to the output stream.
   */
  Thread runLoopedAmplifier(int phaseSetting, BlockingQueue<Integer> inputStream, BlockingQueue<Integer> outputStream) {
    sneaked(() -> inputStream.put(phaseSetting)).run();  // enqueue the phaseSetting to the amplifier's input stream
    var thread = new Thread(() -> {
      var program = getPuzzleInput();
      var input = sneaked(inputStream::take);
      var output = sneaked(outputStream::put);
      var vm = new IntcodeVm(program, input, output);
      vm.run();
    });
    thread.start();
    return thread;
  }
}