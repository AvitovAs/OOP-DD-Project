package game.utils;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double distance(Position pos) {
        double x_distance = Math.pow(pos.x - this.x, 2);
        double y_distance = Math.pow(pos.y - this.y, 2);

        return Math.sqrt(x_distance + y_distance);
    }
}