package edu.csc413.tankgame.model;

public class GameStatusBar {
    private final String id;
    private final double x;
    private final double y;
    private final double angle;

    public GameStatusBar(String id, double x, double y, double angle) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public String getId() {
        return id;
    }

    public double getAngle() {
        return angle;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
