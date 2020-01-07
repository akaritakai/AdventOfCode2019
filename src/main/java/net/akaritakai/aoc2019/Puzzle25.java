package net.akaritakai.aoc2019;

import com.google.common.collect.Sets;
import net.akaritakai.aoc2019.geom2d.Direction;
import net.akaritakai.aoc2019.intcode.IntcodeVm;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneaked;

public class Puzzle25 extends AbstractPuzzle {

  public Puzzle25(String puzzleInput) {
    super(puzzleInput);
  }

  @Override
  public int getDay() {
    return 25;
  }

  @Override
  public String solvePart1() {
    var droid = new Droid(getPuzzleInput());
    var ship = new Ship(droid);
    return ship.securityToken();
  }

  @Override
  public String solvePart2() {
    return "Day 25 has no part 2";
  }

  private static class Ship {
    private final Graph<Room, DoorEdge> layout = new DefaultDirectedGraph<>(DoorEdge.class);
    private final Set<String> restrictedItems = Set.of(
        "escape pod", // Taking the escape pod leaves the ship
        "molten lava", // Taking the molten lava causes us to melt
        "giant electromagnet", // Taking the giant electromagnet causes you to get stuck
        "photons", // Taking the photons causes it to get dark and we are eaten by a grue
        "infinite loop" // Taking the infinite loop causes us to get stuck in an infinite loop
    );
    private final Droid droid;
    private Door pressureSensitiveDoor;

    public Ship(Droid droid) {
      this.droid = droid;
    }

    public String securityToken() {
      var stack = new Stack<Door>();
      var visited = new HashSet<Door>();
      var items = new HashSet<String>();
      var inventory = new HashSet<String>();

      // Visit first room
      var room = droid.room();
      layout.addVertex(room);
      for (var direction : room.doors) {
        stack.add(new Door(room, direction));
      }

      // Explore the layout prior to the security checkpoint
      while (!stack.isEmpty()) {
        // Find our next door to explore
        var door = stack.pop();

        // Skip doors we've already explored
        if (!visited.add(door)) {
          continue;
        }

        // Send the droid through the door
        for (var direction : getDirections(door)) {
          droid.command(direction.name().toLowerCase());
        }

        // Add our room to the graph and pick up all items in the room (visit it)
        room = droid.room();
        for (var item : room.items) {
          if (restrictedItems.contains(item)) {
            continue; // Taking restricted items has undesirable consequences
          }
          droid.command("take " + item);
          items.add(item);
          inventory.add(item);
        }

        // If we didn't make any progress (happens when trying to cross from the security checkpoint to the
        // pressure-sensitive floor), record the event.
        if (room.equals(door.room)) {
          pressureSensitiveDoor = door;
          continue;
        }

        layout.addVertex(room);
        layout.addEdge(door.room, room, new DoorEdge(door.direction));

        // Add the next doors to explore
        for (var direction : room.doors) {
          stack.add(new Door(room, direction));
        }
      }

      // Now we have explored the up the security checkpoint
      if (pressureSensitiveDoor != null) {
        // Head back to the security checkpoint
        for (Direction direction : getDirections(pressureSensitiveDoor.room)) {
          droid.command(direction.name().toLowerCase());
        }

        // Try all combinations of elements
        boolean foundCombination = false;
        for (var size = items.size() - 1; !foundCombination; size--) {
          for (Set<String> combination : Sets.combinations(items, size)) {
            //System.out.println("TRYING: " + combination);
            // Set our inventory to be the combination
            for (var drop : new HashSet<>(Sets.difference(inventory, combination))) {
              droid.command("drop " + drop);
              inventory.remove(drop);
            }
            for (var pickUp : new HashSet<>(Sets.difference(combination, inventory))) {
              droid.command("take " + pickUp);
              inventory.add(pickUp);
            }
            // Once we have the combination, try to walk through the door
            droid.command(pressureSensitiveDoor.direction.name().toLowerCase());
            if (droid.room().name.equals("Pressure-Sensitive Floor")) {
              // We passed through!
              foundCombination = true;
              break;
            }
          }
        }
      }

      return droid.output().lines()
          .filter(line -> line.matches("^.*You should be able to get in by typing (\\S+) on the keypad at the "
              + "main airlock.*$"))
          .map(line -> line.replaceAll("^.*You should be able to get in by typing (\\S+) on the keypad at the "
              + "main airlock.*$", "$1"))
          .findAny()
          .orElseThrow();
    }

    private List<Direction> getDirections(Door door) {
      // Get directions to the room
      var directions = getDirections(door.room);
      // Then take the door
      directions.add(door.direction);

      return directions;
    }

    private List<Direction> getDirections(Room room) {
      // If we're in the room already, we don't need to move
      var droidPosition = droid.room();
      if (room.equals(droid.room())) {
        return new ArrayList<>();
      }

      // If we aren't in the same room, then find a path to get to the room
      var path = new DijkstraShortestPath<>(layout).getPath(droidPosition, room);
      return path.getEdgeList().stream().map(edge -> edge.direction).collect(Collectors.toList());
    }
  }

  private static class Door {
    private final Room room;
    private final Direction direction;

    public Door(Room room, Direction direction) {
      this.room = room;
      this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Door door = (Door) o;
      return Objects.equals(room, door.room) && direction == door.direction;
    }

    @Override
    public int hashCode() {
      return Objects.hash(room, direction);
    }
  }

  private static class Room {
    private final String name;
    private final String description;
    private final Set<Direction> doors;
    private final Set<String> items;

    public Room(String roomData) {
      var lines = roomData.lines().collect(Collectors.toList());

      // Find last room header
      var i = lines.size() - 1;
      while (i >= 0) {
        var line = lines.get(i);
        if (line.startsWith("==")) {
          break;
        }
        i--;
      }
      i = Math.max(i, 0);
      lines = lines.subList(i, lines.size());

      name = lines.get(0).replace("==", "").trim();
      description = lines.get(1).trim();
      doors = findDoors(lines);
      items = findItems(lines);
    }

    private static Set<Direction> findDoors(List<String> lines) {
      var doors = new HashSet<Direction>();
      for (var i = 2; i < lines.size(); i++) {
        var line = lines.get(i);
        if (line.equals("Doors here lead:")) {
          for (var j = i + 1; j < lines.size(); j++) {
            line = lines.get(j);
            if (line.startsWith("- ")) {
              var direction = line.substring(2);
              switch (direction) {
                case "north":
                  doors.add(Direction.NORTH);
                  break;
                case "south":
                  doors.add(Direction.SOUTH);
                  break;
                case "east":
                  doors.add(Direction.EAST);
                  break;
                case "west":
                  doors.add(Direction.WEST);
                  break;
                default: throw new UnsupportedOperationException("Unknown direction " + direction);
              }
            } else {
              break;
            }
          }
          return doors;
        }
      }
      return doors;
    }

    private static Set<String> findItems(List<String> lines) {
      var items = new HashSet<String>();
      for (var i = 2; i < lines.size(); i++) {
        var line = lines.get(i);
        if (line.equals("Items here:")) {
          for (int j = i + 1; j < lines.size(); j++) {
            line = lines.get(j);
            if (line.startsWith("- ")) {
              items.add(line.substring(2));
            } else {
              break;
            }
          }
          return items;
        }
      }
      return items;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Room room = (Room) o;
      return Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name);
    }
  }

  private static class Droid {
    private final BlockingQueue<Long> input = new LinkedBlockingQueue<>(); // commands to droid
    private final List<String> output = new CopyOnWriteArrayList<>(); // output from droid
    private final IntcodeVm vm;
    private final Thread thread;
    private String result;
    private Room room;

    public Droid(String program) {
      vm = new IntcodeVm(program, sneaked(input::take), i -> output.add(Character.toString(Math.toIntExact(i))));
      thread = new Thread(vm::run);
      thread.start();
    }

    public synchronized void command(String command) {
      output.clear();
      result = null;
      switch (command) {
        case "north":
        case "south":
        case "east":
        case "west":
          room = null;
          break;
        default: break;
      }
      command.chars().asLongStream().forEachOrdered(input::add);
      input.add((long) '\n');
      result = processOutput(); // Wait for response
    }

    private synchronized String processOutput() {
      if (result == null) {
        while (true) {
          result = String.join("", output).trim();
          if (result.endsWith("Command?") || result.endsWith("on the keypad at the main airlock.\"")) {
            break;
          }
        }
      }
      return result;
    }

    public synchronized String output() {
      return result;
    }

    public synchronized Room room() {
      if (room == null) {
        room = new Room(processOutput());
      }
      return room;
    }
  }

  private static class DoorEdge extends DefaultEdge {
    private final Direction direction;

    public DoorEdge(Direction direction) {
      this.direction = direction;
    }
  }
}
