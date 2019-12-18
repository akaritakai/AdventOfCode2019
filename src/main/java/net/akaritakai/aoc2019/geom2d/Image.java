package net.akaritakai.aoc2019.geom2d;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class Image {

  public static <T> String renderImage(Collection<T> objects, Function<T, String> renderer, long width, long height) {
    var sb = new StringBuilder();
    var it = objects.iterator();
    for (var y = 0L; y < height; y++) {
      for (var x = 0L; x < width; x++) {
        T element = it.hasNext() ? it.next() : null;
        sb.append(renderer.apply(element));
      }
      if (y + 1 < height) sb.append('\n');
    }
    return sb.toString();
  }

  public static String renderImage(Set<Point> points, Function<Point, String> renderer) {
    var imageSize = imageSize(points);
    return renderImage(imageSize, renderer);
  }

  public static String renderImage(Rectangle imageSize, Function<Point, String> renderer) {
    var sb = new StringBuilder();
    for (var y = imageSize.y; y < imageSize.y + imageSize.height; y++) {
      for (var x = imageSize.x; x < imageSize.x + imageSize.width; x++) {
        var p = new Point(x, y);
        sb.append(renderer.apply(p));
      }
      if (y + 1 < imageSize.y + imageSize.height) sb.append('\n');
    }
    return sb.toString();
  }

  public static String renderImageUpsideDown(Set<Point> points, Function<Point, String> renderer) {
    var imageSize = imageSize(points);
    return renderImageUpsideDown(imageSize, renderer);
  }

  public static String renderImageUpsideDown(Rectangle imageSize, Function<Point, String> renderer) {
    var sb = new StringBuilder();
    for (var y = imageSize.y + imageSize.height - 1; y >= imageSize.y; y--) {
      for (var x = imageSize.x; x < imageSize.x + imageSize.width; x++) {
        var p = new Point(x, y);
        sb.append(renderer.apply(p));
      }
      if (y - 1 >= imageSize.y) sb.append('\n');
    }
    return sb.toString();
  }

  public static Rectangle imageSize(Set<Point> visiblePoints) {
    return imageSize(visiblePoints, point -> true);
  }

  public static Rectangle imageSize(Set<Point> points, Predicate<Point> isVisible) {
    var minHeight = points.stream().filter(isVisible).mapToLong(p -> p.y).min().orElseThrow();
    var maxHeight = points.stream().filter(isVisible).mapToLong(p -> p.y).max().orElseThrow();
    var minWidth = points.stream().filter(isVisible).mapToLong(p -> p.x).min().orElseThrow();
    var maxWidth = points.stream().filter(isVisible).mapToLong(p -> p.x).max().orElseThrow();
    return new Rectangle(minWidth, minHeight, maxWidth - minWidth + 1, maxHeight - minHeight + 1);
  }
}
