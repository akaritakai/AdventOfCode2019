package net.akaritakai.aoc2019.intcode;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

@Test
public class TestIntcodeVm {
  @Test
  public void testPuzzle02Part1Example1() {
    var vm = new IntcodeVm("1,9,10,3,2,3,11,0,99,30,40,50");
    vm.run();
    Assert.assertEquals(vm.memory(), IntcodeVm.programMemory("3500,9,10,70,2,3,11,0,99,30,40,50"));
  }

  @Test
  public void testPuzzle02Part1Example2() {
    var vm = new IntcodeVm("1,0,0,0,99");
    vm.run();
    Assert.assertEquals(vm.memory(), IntcodeVm.programMemory("2,0,0,0,99"));
  }

  @Test
  public void testPuzzle02Part1Example3() {
    var vm = new IntcodeVm("2,3,0,3,99");
    vm.run();
    Assert.assertEquals(vm.memory(), IntcodeVm.programMemory("2,3,0,6,99"));
  }

  @Test
  public void testPuzzle02Part1Example4() {
    var vm = new IntcodeVm("2,4,4,5,99,0");
    vm.run();
    Assert.assertEquals(vm.memory(), IntcodeVm.programMemory("2,4,4,5,99,9801"));
  }

  @Test
  public void testPuzzle02Part1Example5() {
    var vm = new IntcodeVm("1,1,1,4,99,5,6,0,99");
    vm.run();
    Assert.assertEquals(vm.memory(), IntcodeVm.programMemory("30,1,1,4,2,5,6,0,99"));
  }

  @Test
  public void testPuzzle05Part1Example1() {
    LongStream.range(-10, 20).forEach(i -> {
      var vm = new IntcodeVm("3,1,99", () -> i, output -> {});
      vm.run();
      Assert.assertEquals((long) vm.memory().get(1L), i);
    });
  }

  @Test
  public void testPuzzle05Part1Example2() {
    LongStream.range(-10, 20).forEach(i -> {
      var output = new AtomicLong();
      var vm = new IntcodeVm("4,3,99," + i, () -> null, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i);
    });
  }

  @Test
  public void testPuzzle05Part1Example3() {
    LongStream.range(-10, 20).forEach(i -> {
      var output = new AtomicLong();
      var vm = new IntcodeVm("3,0,4,0,99", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i);
    });
  }

  @Test
  public void testPuzzle05Part1Example4() {
    var vm = new IntcodeVm("1002,4,3,4,33");
    vm.run();
    Assert.assertEquals(vm.memory(), IntcodeVm.programMemory("1002,4,3,4,99"));
  }

  @Test
  public void testPuzzle05Part1Example5() {
    var vm = new IntcodeVm("1101,100,-1,4,0");
    vm.run();
    Assert.assertEquals(vm.memory(), IntcodeVm.programMemory("1101,100,-1,4,99"));
  }

  @Test
  public void testPuzzle05Part2Example1() {
    LongStream.range(-10, 20).forEach(i -> {
      var output = new AtomicLong();
      var vm = new IntcodeVm("3,9,8,9,10,9,4,9,99,-1,8", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i == 8 ? 1 : 0);
    });
  }

  @Test
  public void testPuzzle05Part2Example2() {
    LongStream.range(-10, 20).forEach(i -> {
      var output = new AtomicLong();
      var vm = new IntcodeVm("3,9,7,9,10,9,4,9,99,-1,8", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i < 8 ? 1 : 0);
    });
  }

  @Test
  public void testPuzzle05Part2Example3() {
    LongStream.range(-10, 20).forEach(i -> {
      var output = new AtomicLong();
      var vm = new IntcodeVm("3,3,1108,-1,8,3,4,3,99", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i == 8 ? 1 : 0);
    });
  }

  @Test
  public void testPuzzle05Part2Example4() {
    LongStream.range(-10, 20).forEach(i -> {
      var output = new AtomicLong();
      var vm = new IntcodeVm("3,3,1107,-1,8,3,4,3,99", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i < 8 ? 1 : 0);
    });
  }

  @Test
  public void testPuzzle05Part2Example5() {
    LongStream.range(-10, 20).forEach(i -> {
      var output = new AtomicLong();
      var vm = new IntcodeVm("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i == 0 ? 0 : 1);
    });
  }

  @Test
  public void testPuzzle05Part2Example6() {
    LongStream.range(-10, 20).forEach(i -> {
      var output = new AtomicLong();
      var vm = new IntcodeVm("3,3,1105,-1,9,1101,0,0,12,4,12,99,1", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i == 0 ? 0 : 1);
    });
  }

  @Test
  public void testPuzzle05Part2Example7() {
    LongStream.range(-10, 20).forEach(i -> {
      var output = new AtomicLong();
      var vm = new IntcodeVm("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,"
          + "125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), (i < 8) ? 999 : ((i == 8) ? 1000 : 1001));
    });
  }

  @Test
  public void testPuzzle09Part1Example1() {
    var output = new ArrayList<Long>();
    var vm = new IntcodeVm("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99", () -> null,
        output::add);
    vm.run();
    Assert.assertEquals(output, List.of(109L, 1L, 204L, -1L, 1001L, 100L, 1L, 100L, 1008L, 100L, 16L, 101L, 1006L, 101L,
        0L, 99L));
  }

  @Test
  public void testPuzzle09Part1Example2() {
    var output = new AtomicLong();
    var vm = new IntcodeVm("1102,34915192,34915192,7,4,7,99,0", () -> null, output::set);
    vm.run();
    Assert.assertEquals(String.valueOf(output.get()).length(), 16);
  }

  @Test
  public void testPuzzle09Part1Example3() {
    var output = new AtomicLong();
    var vm = new IntcodeVm("104,1125899906842624,99", () -> null, output::set);
    vm.run();
    Assert.assertEquals(output.get(), 1125899906842624L);
  }

  @Test
  public void testFizzBuzz() {
    // A FizzBuzz program of my own creation -- assumes that my IntCode simulator is a valid implementation
    // Uses relative base features with as aggressive register re-use as possible to help catch bugs
    // Posted here with notes: https://www.reddit.com/r/adventofcode/comments/e9t52d/fizzbuzz_in_intcode/faoo5pg/
    var program = "109,169,1108,1,101,7,1105,0,165,109,-3,1101,18,0,173,1106,0,24,109,3,1101,49,0,173,1001,3,0,172,"
        + "2007,172,0,33,1105,0,42,2001,172,1,172,1106,0,28,21008,172,0,2,106,0,173,1101,0,0,173,1006,168,68,104,70,"
        + "104,105,104,122,104,122,1001,173,1,173,1006,171,83,104,66,104,117,104,122,104,122,1001,173,1,173,1005,173,"
        + "156,1008,3,100,91,1105,0,141,1007,3,10,98,1105,0,150,1001,3,0,172,1101,0,0,173,1007,172,10,113,1105,0,126,"
        + "1001,172,-10,172,1001,173,1,173,1106,0,108,1001,173,48,173,1001,172,48,172,4,173,4,172,1106,0,156,104,49,"
        + "104,48,104,48,1106,0,156,1001,3,48,172,4,172,104,10,1001,3,1,3,1106,0,2,99,3,-3,0,5,-5";
    var output = new StringBuilder();
    var vm = new IntcodeVm(program, () -> null, c -> output.append(Character.toString(Math.toIntExact(c))));
    vm.run();

    var expected = new StringBuilder();
    for (int i = 1; i <= 100; i++) {
      if (i % 3 == 0 && i % 5 == 0) {
        expected.append("FizzBuzz\n");
      } else if (i % 3 == 0) {
        expected.append("Fizz\n");
      } else if (i % 5 == 0) {
        expected.append("Buzz\n");
      } else {
        expected.append(i).append("\n");
      }
    }

    Assert.assertEquals(output.toString(), expected.toString());
  }
}
