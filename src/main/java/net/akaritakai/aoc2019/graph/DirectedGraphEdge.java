package net.akaritakai.aoc2019.graph;

import java.util.Objects;

public class DirectedGraphEdge<T> {
  public final T source;
  public final T target;

  public DirectedGraphEdge(T source, T target) {
    this.source = source;
    this.target = target;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DirectedGraphEdge<?> that = (DirectedGraphEdge<?>) o;
    return Objects.equals(source, that.source) && Objects.equals(target, that.target);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, target);
  }

  @Override
  public String toString() {
    return source + "->" + target;
  }
}
