package net.akaritakai.aoc2019;

import java.util.stream.IntStream;

public class Puzzle01 extends AbstractPuzzle {

  public Puzzle01(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 1;
  }

  @Override
  public String solvePart1() {
    var fuelRequirement = getInput().map(this::getFuelWeight).sum();
    return String.valueOf(fuelRequirement);
  }

  @Override
  public String solvePart2() {
    var fuelRequirement = getInput().map(this::getRecursiveFuelWeight).sum();
    return String.valueOf(fuelRequirement);
  }

  private int getFuelWeight(int payloadWeight) {
    return payloadWeight / 3 - 2;
  }

  private int getRecursiveFuelWeight(int payloadWeight) {
    var fuelWeight = getFuelWeight(payloadWeight);
    if (fuelWeight <= 0) {
      return 0;
    }
    return fuelWeight + getRecursiveFuelWeight(fuelWeight);
  }

  private IntStream getInput() {
    return getPuzzleInput().lines().mapToInt(Integer::parseInt);
  }
}
