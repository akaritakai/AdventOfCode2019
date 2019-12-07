package net.akaritakai.aoc2019;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Test
public class TestIntcodeVm {
  @Test
  public void testPuzzle02Part1Example1() {
    var vm = new IntcodeVm("1,9,10,3,2,3,11,0,99,30,40,50");
    vm.run();
    Assert.assertEquals(vm.positions(), List.of(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50));
  }

  @Test
  public void testPuzzle02Part1Example2() {
    var vm = new IntcodeVm("1,0,0,0,99");
    vm.run();
    Assert.assertEquals(vm.positions(), List.of(2, 0, 0, 0, 99));
  }

  @Test
  public void testPuzzle02Part1Example3() {
    var vm = new IntcodeVm("2,3,0,3,99");
    vm.run();
    Assert.assertEquals(vm.positions(), List.of(2, 3, 0, 6, 99));
  }

  @Test
  public void testPuzzle02Part1Example4() {
    var vm = new IntcodeVm("2,4,4,5,99,0");
    vm.run();
    Assert.assertEquals(vm.positions(), List.of(2, 4, 4, 5, 99, 9801));
  }

  @Test
  public void testPuzzle02Part1Example5() {
    var vm = new IntcodeVm("1,1,1,4,99,5,6,0,99");
    vm.run();
    Assert.assertEquals(vm.positions(), List.of(30, 1, 1, 4, 2, 5, 6, 0, 99));
  }

  @Test
  public void testPuzzle05Part1Example1() {
    IntStream.range(-10, 20).forEach(i -> {
      var vm = new IntcodeVm("3,1,99", () -> i, output -> {});
      vm.run();
      Assert.assertEquals((int) vm.positions().get(1), i);
    });
  }

  @Test
  public void testPuzzle05Part1Example2() {
    IntStream.range(-10, 20).forEach(i -> {
      var output = new AtomicInteger();
      var vm = new IntcodeVm("4,3,99," + i, () -> null, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i);
    });
  }

  @Test
  public void testPuzzle05Part1Example3() {
    IntStream.range(-10, 20).forEach(i -> {
      var output = new AtomicInteger();
      var vm = new IntcodeVm("3,0,4,0,99", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i);
    });
  }

  @Test
  public void testPuzzle05Part1Example4() {
    var vm = new IntcodeVm("1002,4,3,4,33");
    vm.run();
    Assert.assertEquals(vm.positions(), List.of(1002, 4, 3, 4, 99));
  }

  @Test
  public void testPuzzle05Part1Example5() {
    var vm = new IntcodeVm("1101,100,-1,4,0");
    vm.run();
    Assert.assertEquals(vm.positions(), List.of(1101, 100, -1, 4, 99));
  }

  @Test
  public void testPuzzle05Part2Example1() {
    IntStream.range(-10, 20).forEach(i -> {
      var output = new AtomicInteger();
      var vm = new IntcodeVm("3,9,8,9,10,9,4,9,99,-1,8", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i == 8 ? 1 : 0);
    });
  }

  @Test
  public void testPuzzle05Part2Example2() {
    IntStream.range(-10, 20).forEach(i -> {
      var output = new AtomicInteger();
      var vm = new IntcodeVm("3,9,7,9,10,9,4,9,99,-1,8", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i < 8 ? 1 : 0);
    });
  }

  @Test
  public void testPuzzle05Part2Example3() {
    IntStream.range(-10, 20).forEach(i -> {
      var output = new AtomicInteger();
      var vm = new IntcodeVm("3,3,1108,-1,8,3,4,3,99", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i == 8 ? 1 : 0);
    });
  }

  @Test
  public void testPuzzle05Part2Example4() {
    IntStream.range(-10, 20).forEach(i -> {
      var output = new AtomicInteger();
      var vm = new IntcodeVm("3,3,1107,-1,8,3,4,3,99", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i < 8 ? 1 : 0);
    });
  }

  @Test
  public void testPuzzle05Part2Example5() {
    IntStream.range(-10, 20).forEach(i -> {
      var output = new AtomicInteger();
      var vm = new IntcodeVm("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i == 0 ? 0 : 1);
    });
  }

  @Test
  public void testPuzzle05Part2Example6() {
    IntStream.range(-10, 20).forEach(i -> {
      var output = new AtomicInteger();
      var vm = new IntcodeVm("3,3,1105,-1,9,1101,0,0,12,4,12,99,1", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), i == 0 ? 0 : 1);
    });
  }

  @Test
  public void testPuzzle05Part2Example7() {
    IntStream.range(-10, 20).forEach(i -> {
      var output = new AtomicInteger();
      var vm = new IntcodeVm("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,"
          + "125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99", () -> i, output::set);
      vm.run();
      Assert.assertEquals(output.get(), (i < 8) ? 999 : ((i == 8) ? 1000 : 1001));
    });
  }
}
