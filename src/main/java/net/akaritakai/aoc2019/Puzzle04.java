package net.akaritakai.aoc2019;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.annotations.VisibleForTesting;


public class Puzzle04 extends AbstractPuzzle {

  public Puzzle04(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 4;
  }

  @Override
  public String solvePart1() {
    var count = IntStream.rangeClosed(getLowerBound(), getUpperBound()).filter(Puzzle04::meetsPart1Criteria).count();
    return String.valueOf(count);
  }

  @Override
  public String solvePart2() {
    var count = IntStream.rangeClosed(getLowerBound(), getUpperBound()).filter(Puzzle04::meetsPart2Criteria).count();
    return String.valueOf(count);
  }

  @VisibleForTesting
  static boolean meetsPart1Criteria(int n) {
    return digitsMonotonicallyIncreasing(n) && digitCounts(n).values().stream().anyMatch(i -> i >= 2);
  }

  @VisibleForTesting
  static boolean meetsPart2Criteria(int n) {
    return digitsMonotonicallyIncreasing(n) && digitCounts(n).values().stream().anyMatch(i -> i == 2);
  }

  private static boolean digitsMonotonicallyIncreasing(int n) {
    String s = String.valueOf(n);
    String monotonic = s.chars().sorted().mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining());
    return s.equals(monotonic);
  }

  private static Map<Integer, Long> digitCounts(int n) {
    return String.valueOf(n).chars().mapToObj(c -> c - '0')
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }

  private int getLowerBound() {
    return Integer.parseInt(getPuzzleInput().trim().split("-")[0]);
  }

  private int getUpperBound() {
    return Integer.parseInt(getPuzzleInput().trim().split("-")[1]);
  }
}
