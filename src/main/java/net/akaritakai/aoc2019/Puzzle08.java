package net.akaritakai.aoc2019;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle08 extends AbstractPuzzle {

  private static final int IMAGE_HEIGHT = 6;
  private static final int IMAGE_WIDTH = 25;
  private static final int IMAGE_SIZE = IMAGE_HEIGHT * IMAGE_WIDTH;

  public Puzzle08(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 8;
  }

  @Override
  public String solvePart1() {
    var layers = getLayers();
    var count = layers.stream()
        .map(layer -> layer.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())))
        .min(Comparator.comparing(layer -> layer.get(0)))
        .orElseThrow(() -> new IllegalStateException("No layers returned"));
    return String.valueOf(count.get(1) * count.get(2));
  }

  @Override
  public String solvePart2() {
    var layers = getLayers();
    var pixels = IntStream.range(0, IMAGE_SIZE).boxed()
        .map(p -> layers.stream()
            .map(layer -> layer.get(p))
            .reduce((above, below) -> above == 2 ? below : above)
            .orElseThrow(() -> new IllegalStateException("No pixel data returned")))
        .collect(Collectors.toList());
    return "\n" + renderImage(pixels);
  }

  private String renderImage(List<Integer> pixels) {
    var it = pixels.iterator();
    return IntStream.range(0, IMAGE_HEIGHT).mapToObj(y ->
        IntStream.range(0, IMAGE_WIDTH).mapToObj(x -> {
          switch (it.next()) {
            case 0: return " ";
            case 1: return "#";
            default: throw new IllegalStateException("Invalid color");
          }
        }).collect(Collectors.joining()))
        .collect(Collectors.joining("\n"));
  }

  private List<List<Integer>> getLayers() {
    var input = getPuzzleInput().trim();
    return IntStream.range(0, input.length() / IMAGE_SIZE)
        .mapToObj(layerNum -> input.substring((IMAGE_SIZE * layerNum), (IMAGE_SIZE * (layerNum + 1))))
        .map(layerStr -> layerStr.chars().boxed().map(c -> c - '0').collect(Collectors.toList()))
        .collect(Collectors.toList());
  }
}
