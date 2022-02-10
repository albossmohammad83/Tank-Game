package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

public class PowerUpFastReloading extends Entity {

    public PowerUpFastReloading(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    @Override
    public void move(GameWorld gameWorld) {
        turnRight(Constants.POWER_UP_TURN_SPEED);
    }

    @Override
    public void boundsChecking(GameWorld gameWorld) {
    }

    @Override
    public double getYBound() {
        return getY() + 32.0;
    }

    @Override
    public double getXBound() {
        return getX() + 32.0;
    }

}
