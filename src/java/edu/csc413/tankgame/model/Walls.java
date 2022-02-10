package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

public class Walls extends Entity {
    private int healthPoint = 3;
    public Walls(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }
    @Override
    public void move(GameWorld gameWorld) {
    }
    @Override
    public void boundsChecking(GameWorld gameWorld) {
    }

    @Override
    public double getYBound() {
        return getY() + Constants.WALL_HEIGHT;
    }

    @Override
    public double getXBound() {
        return getX() + Constants.WALL_WIDTH;
    }

    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }

    public int getHealthPoint() {
        return healthPoint;
    }
}
