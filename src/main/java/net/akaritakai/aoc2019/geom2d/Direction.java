package net.akaritakai.aoc2019.geom2d;

public enum Direction {
  NORTH (0, 1),
  SOUTH (0, -1),
  EAST (1, 0),
  WEST (-1, 0);

  private int dx;
  private int dy;

  Direction(int dx, int dy) {
    this.dx = dx;
    this.dy = dy;
  }

  public Point move(Point point) {
    return new Point(point.x + dx, point.y + dy);
  }

  public Direction turn(Turn turn) {
    switch (turn) {
      case LEFT:
        switch (this) {
          case NORTH: return WEST;
          case SOUTH: return EAST;
          case EAST: return NORTH;
          case WEST: return SOUTH;
        }
      case RIGHT:
        switch (this) {
          case NORTH: return EAST;
          case SOUTH: return WEST;
          case EAST: return SOUTH;
          case WEST: return NORTH;
        }
    }
    throw new UnsupportedOperationException("Unknown direction " + this + " and turn " + turn);
  }

  public Turn turn(Direction newDirection) {
    if (this == NORTH && newDirection == WEST) return Turn.LEFT;
    if (this == NORTH && newDirection == EAST) return Turn.RIGHT;
    if (this == SOUTH && newDirection == EAST) return Turn.LEFT;
    if (this == SOUTH && newDirection == WEST) return Turn.RIGHT;
    if (this == EAST && newDirection == NORTH) return Turn.LEFT;
    if (this == EAST && newDirection == SOUTH) return Turn.RIGHT;
    if (this == WEST && newDirection == SOUTH) return Turn.LEFT;
    if (this == WEST && newDirection == NORTH) return Turn.RIGHT;
    throw new UnsupportedOperationException("Direction change is not a 90 degree turn (i.e. 0 degrees or 180 degrees)");
  }

  public static Direction fromSegment(Point start, Point end) {
    long dx = end.x - start.x;
    long dy = end.y - start.y;
    if (dx == 0 && dy > 0) return NORTH;
    if (dx == 0 && dy < 0) return SOUTH;
    if (dx > 0 && dy == 0) return EAST;
    if (dx < 0 && dy == 0) return WEST;
    throw new IllegalArgumentException("Line segment is not vertical or horizontal");
  }

  public Direction opposite() {
    switch (this) {
      case NORTH: return SOUTH;
      case SOUTH: return NORTH;
      case EAST: return WEST;
      case WEST: return EAST;
    }
    throw new UnsupportedOperationException("Unknown direction " + this);
  }
}
