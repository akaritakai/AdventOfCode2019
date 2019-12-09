package net.akaritakai.aoc2019;

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
      Assert.assertEquals((long) vm.memory().get(1), i);
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
}
