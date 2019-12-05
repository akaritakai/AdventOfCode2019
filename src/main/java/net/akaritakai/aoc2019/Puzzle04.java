package net.akaritakai.aoc2019;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Ordering;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


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
    return digitsMonotonicallyIncreasing(n) && digitFrequencyCounts(n).anyMatch(i -> i >= 2);
  }

  @VisibleForTesting
  static boolean meetsPart2Criteria(int n) {
    return digitsMonotonicallyIncreasing(n) && digitFrequencyCounts(n).anyMatch(i -> i == 2);
  }

  private static boolean digitsMonotonicallyIncreasing(int n) {
    Iterable<Integer> it = () -> digits(n).iterator();
    return Ordering.natural().isOrdered(it);
  }

  private static Stream<Long> digitFrequencyCounts(int n) {
    return digits(n).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).values().stream();
  }

  private static Stream<Integer> digits(int n) {
    return String.valueOf(n).chars().map(i -> i - '0').boxed();
  }

  private int getLowerBound() {
    return Integer.parseInt(getPuzzleInput().trim().split("-")[0]);
  }

  private int getUpperBound() {
    return Integer.parseInt(getPuzzleInput().trim().split("-")[1]);
  }
}
