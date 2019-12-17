package net.akaritakai.aoc2019;

import com.google.common.collect.Maps;
import com.google.common.math.LongMath;
import com.google.common.primitives.Longs;

import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Puzzle14 extends AbstractPuzzle {

  private final Map<String, ChemicalProduct> equations = getEquations();

  public Puzzle14(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 14;
  }

  @Override
  public String solvePart1() {
    var requirements = new HashMap<String, Long>();
    requirements.put("FUEL", 1L);
    var ores = oresNeeded(requirements, new HashMap<>());
    return String.valueOf(ores);
  }

  @Override
  public String solvePart2() {
    var fuel = 1L;
    var target = 1_000_000_000_000L;
    while (true) {
      var ore = oresNeededForFuel(fuel + 1);
      if (ore > target) {
        return String.valueOf(fuel);
      } else {
        fuel = Math.max(fuel + 1, (fuel + 1) * target / ore);
      }
    }
  }

  private long oresNeededForFuel(long quantity) {
    var requirements = new HashMap<String, Long>();
    requirements.put("FUEL", quantity);
    return oresNeeded(requirements, new HashMap<>());
  }

  private long oresNeeded(Map<String, Long> requirements, Map<String, Long> inventory) {
    var ores = new AtomicLong(0L);
    while (!requirements.isEmpty()) {
      // Determine our requirement
      var chemical = requirements.keySet().iterator().next();
      var needed = requirements.remove(chemical);

      // See if we have any of the chemical in our inventory
      var available = inventory.getOrDefault(chemical, 0L);
      if (available > 0) {
        inventory.put(chemical, Longs.constrainToRange(available - needed, 0L, available));
        needed = Longs.constrainToRange(needed - available, 0L, needed);
      }

      // Find our requirement in the equations
      var product = equations.get(chemical);
      // Determine how many chemical reactions we need to run to satisfy the requirement
      var factor = LongMath.divide(needed, product.quantity, RoundingMode.CEILING);
      // Store any excess back in our inventory
      inventory.put(chemical, inventory.getOrDefault(chemical, 0L) + (product.quantity * factor) - needed);

      // Handle the reagents
      product.reagents.forEach((reagentName, reagentQuantity) -> {
        var amount = reagentQuantity * factor;
        if (reagentName.equals("ORE")) {
          ores.addAndGet(amount);
        } else {
          requirements.put(reagentName, requirements.getOrDefault(reagentName, 0L) + amount);
        }
      });
    }
    return ores.get();
  }

  private Map<String, ChemicalProduct> getEquations() {
    Function<String, String> chemicalName = token -> token.split(" ")[1];
    Function<String, Long> chemicalQuantity = token -> Long.parseLong(token.split(" ")[0]);
    return getPuzzleInput().trim().lines()
        .map(line -> {
          var sides = line.split(" => ");
          var name = chemicalName.apply(sides[1]);
          var quantity = chemicalQuantity.apply(sides[1]);
          var reagents = Arrays.stream(sides[0].split(", "))
              .map(token -> Maps.immutableEntry(chemicalName.apply(token), chemicalQuantity.apply(token)))
              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
          return Maps.immutableEntry(name, new ChemicalProduct(quantity, reagents));
        })
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private static class ChemicalProduct {
    private final long quantity;
    private final Map<String, Long> reagents;

    public ChemicalProduct(long quantity, Map<String, Long> reagents) {
      this.quantity = quantity;
      this.reagents = reagents;
    }
  }
}
