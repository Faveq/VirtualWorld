package General;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Point point) {
        return this.x == point.x && this.y == point.y;
    }

    public boolean notEquals(Point point) {
        return !equals(point);
    }

    public Point add(Point point) {
        return new Point(this.x + point.x, this.y + point.y);
    }

    public Point subtract(Point point) {
        return new Point(this.x - point.x, this.y - point.y);
    }

    public void addInPlace(Point point) {
        this.x += point.x;
        this.y += point.y;
    }

    public boolean beyondBorders(int width, int height) {
        return x < 0 || y < 0 || x >= width || y >= height;
    }
}
