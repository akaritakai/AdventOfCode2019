package net.akaritakai.aoc2019;

import com.google.common.annotations.VisibleForTesting;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.math.BigInteger.ONE;

public class Puzzle22 extends AbstractPuzzle {

  private static final String DEAL_INTO_NEW_STACK_INSTRUCTION = "deal into new stack";
  private static final String DEAL_WITH_INCREMENT_INSTRUCTION = "deal with increment (\\d+)";
  private static final String CUT_INSTRUCTION = "cut (-?\\d+)";

  public Puzzle22(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 22;
  }

  @Override
  public String solvePart1() {
    var deck = IntStream.range(0, 10_007).boxed().collect(Collectors.toList());
    var instructions = getPuzzleInput().lines().collect(Collectors.toList());
    for (String instruction : instructions) {
      deck = applyShuffleInstruction(deck, instruction);
    }
    return String.valueOf(deck.indexOf(2019));
  }

  @Override
  public String solvePart2() {
    var size = BigInteger.valueOf(119_315_717_514_047L);
    var f0 = BigInteger.valueOf(2020);
    var f1 = BigInteger.valueOf(inverseShuffle(size.longValue(), f0.longValue()));
    var f2 = BigInteger.valueOf(inverseShuffle(size.longValue(), f1.longValue()));

    // f(i) = A*i + B
    var a = f1.subtract(f2).multiply(f0.subtract(f1).add(size).modInverse(size).mod(size)).mod(size);
    var b = f1.subtract(a.multiply(f0)).mod(size);

    var shuffles = BigInteger.valueOf(101_741_582_076_661L);

    var result = a.modPow(shuffles, size)
        .multiply(f0)
        .add(a.modPow(shuffles, size)
            .subtract(ONE)
            .multiply(a.subtract(ONE).modInverse(size))
            .multiply(b))
        .mod(size);

    return result.toString();
  }

  private long inverseShuffle(long size, long i) {
    var instructions = getPuzzleInput().lines().collect(Collectors.toList());
    Collections.reverse(instructions);
    for (String instruction : instructions) {
      if (instruction.matches(DEAL_INTO_NEW_STACK_INSTRUCTION)) {
        i = reverse(size, i);
      } else if (instruction.matches(DEAL_WITH_INCREMENT_INSTRUCTION)) {
        int n = Integer.parseInt(instruction.replaceAll(DEAL_WITH_INCREMENT_INSTRUCTION, "$1"));
        i = increment(size, i, n);
      } else if (instruction.matches(CUT_INSTRUCTION)) {
        int n = Integer.parseInt(instruction.replaceAll(CUT_INSTRUCTION, "$1"));
        i = cut(size, i, n);
      } else {
        throw new UnsupportedOperationException("Unsupported instruction: " + instruction);
      }
    }
    return i;
  }

  @VisibleForTesting
  static List<Integer> applyShuffleInstruction(List<Integer> deck, String instruction) {
    if (instruction.matches(DEAL_INTO_NEW_STACK_INSTRUCTION)) {
      return reverse(deck);
    } else if (instruction.matches(DEAL_WITH_INCREMENT_INSTRUCTION)) {
      int n = Integer.parseInt(instruction.replaceAll(DEAL_WITH_INCREMENT_INSTRUCTION, "$1"));
      return increment(deck, n);
    } else if (instruction.matches(CUT_INSTRUCTION)) {
      int n = Integer.parseInt(instruction.replaceAll(CUT_INSTRUCTION, "$1"));
      return cut(deck, n);
    } else {
      throw new UnsupportedOperationException("Unsupported instruction: " + instruction);
    }
  }

  private static long reverse(long size, long i) {
    return size - i - 1;
  }

  @VisibleForTesting
  static List<Integer> reverse(List<Integer> deck) {
    Collections.reverse(deck);
    return deck;
  }

  private static long cut(long size, long i, long n) {
    return (i + n + size) % size;
  }

  @VisibleForTesting
  static List<Integer> cut(List<Integer> deck, int n) {
    n = (deck.size() + n) % deck.size();
    var newDeck = new ArrayList<Integer>();
    newDeck.addAll(deck.subList(n, deck.size()));
    newDeck.addAll(deck.subList(0, n));
    return newDeck;
  }

  private static long increment(long size, long i, long n) {
    return BigInteger.valueOf(n)
        .modInverse(BigInteger.valueOf(size))
        .multiply(BigInteger.valueOf(i))
        .mod(BigInteger.valueOf(size))
        .longValue();
  }

  @VisibleForTesting
  static List<Integer> increment(List<Integer> deck, int n) {
    var newDeck = new ArrayList<>(deck);
    for (int i = 0, j = 0; i < deck.size(); i++, j = (j + n) % deck.size()) {
      newDeck.set(j, deck.get(i));
    }
    return newDeck;
  }
}
