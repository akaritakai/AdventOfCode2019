package net.akaritakai.aoc2019;

import com.google.common.annotations.VisibleForTesting;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Puzzle16 extends AbstractPuzzle {

  private static final int[] FFT_PATTERN = {0, 1, 0, -1};

  public Puzzle16(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 16;
  }

  @Override
  public String solvePart1() {
    var signal = getSignal();
    for (int i = 0; i < 100; i++) {
      signal = fft(signal);
    }
    return String.valueOf(sequence(signal, 0, 8));
  }

  @Override
  public String solvePart2() {
    var input = getSignal();
    var offset = sequence(input, 0, 7);
    var signal = Stream.generate(() -> input)
        .limit(10000 - offset / input.size())
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
    offset -= (offset / input.size()) * input.size();
    for (var run = 0; run < 100; run++) {
      var n = signal.get(signal.size() - 1);
      for (var i = signal.size() - 2; i >= offset; i--) {
        n = (n + signal.get(i)) % 10;
        signal.set(i, n);
      }
    }
    return String.valueOf(sequence(signal, offset, 8));
  }

  private List<Integer> getSignal() {
    return getPuzzleInput().trim().chars().boxed().map(i -> i - '0').collect(Collectors.toList());
  }

  private int sequence(List<Integer> signal, int skip, int limit) {
    var result = 0;
    for (var i = skip; i < skip + limit; i++) {
      result = 10 * result + signal.get(i);
    }
    return result;
  }

  @VisibleForTesting
  static List<Integer> fft(List<Integer> signal) {
    return IntStream.rangeClosed(1, signal.size())
        .boxed()
        .map(i -> fft(signal, i))
        .collect(Collectors.toList());
  }

  private static int fft(List<Integer> signal, int length) {
    var it = signal.iterator();
    var sum = IntStream.rangeClosed(1, signal.size())
        .map(i -> it.next() * FFT_PATTERN[(i / length) % 4])
        .sum();
    return Math.abs(sum) % 10;
  }
}
