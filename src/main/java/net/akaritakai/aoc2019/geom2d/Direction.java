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
    if (this == NORTH && turn == Turn.LEFT) return WEST;
    if (this == NORTH && turn == Turn.RIGHT) return EAST;
    if (this == SOUTH && turn == Turn.LEFT) return EAST;
    if (this == SOUTH && turn == Turn.RIGHT) return WEST;
    if (this == EAST && turn == Turn.LEFT) return NORTH;
    if (this == EAST && turn == Turn.RIGHT) return SOUTH;
    if (this == WEST && turn == Turn.LEFT) return SOUTH;
    if (this == WEST && turn == Turn.RIGHT) return NORTH;
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
    if (this == NORTH) return SOUTH;
    if (this == SOUTH) return NORTH;
    if (this == EAST) return WEST;
    if (this == WEST) return EAST;
    throw new UnsupportedOperationException("Unknown direction " + this);
  }
}
