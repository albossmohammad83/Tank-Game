package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

/** Entity class representing all tanks in the game. */
public abstract class Tank extends Entity {
    private int healthPoint = 3;
    private int coolDown = 200;
    private int shellNumber = 0;
    private boolean hasPowerUpFastReloading = false;
    // TODO: Implement. A lot of what's below is relevant to all Entity types, not just Tanks. Move it accordingly to
    //       Entity class.
    public Tank(String id, double x, double y, double angle) {
        super(id,x,y,angle);
    }

    public void fireShell(GameWorld gameWorld, double shellX, double shellY, double shellAngle) {
        Shell shell = new Shell(getId() + "-Shell-" + shellNumber, shellX, shellY, shellAngle);
        gameWorld.addEntity(shell);
        shellNumber++;
        if(hasPowerUpFastReloading){
            coolDown = 50;
        } else{
            coolDown = 200;
        }
    }

    public void setHasPowerUp(boolean hasPowerUp) {
        this.hasPowerUpFastReloading = hasPowerUp;
    }

    @Override
    public void boundsChecking(GameWorld gameWorld){
        if(getX() < Constants.TANK_X_LOWER_BOUND){
            setX(Constants.TANK_X_LOWER_BOUND);
        }
        if(getX() > Constants.TANK_X_UPPER_BOUND){
            setX(Constants.TANK_X_UPPER_BOUND);
        }
        if(getY() < Constants.TANK_Y_LOWER_BOUND){
            setY(Constants.TANK_Y_LOWER_BOUND);
        }
        if(getY() > Constants.TANK_Y_UPPER_BOUND){
            setY(Constants.TANK_Y_UPPER_BOUND);
        }
    }

    // TODO: The methods below are provided so you don't have to do the math for movement. You should call these methods
    //       from the various subclasses of Entity in their implementations of move.

    // The following methods will be useful for determining where a shell should be spawned when it
    // is created by this tank. It needs a slight offset so it appears from the front of the tank,
    // even if the tank is rotated. The shell should have the same angle as the tank.

    protected double getShellX() {
        return getX() + Constants.TANK_WIDTH / 2 + 45.0 * Math.cos(getAngle()) - Constants.SHELL_WIDTH / 2;
    }

    protected double getShellY() {
        return getY() + Constants.TANK_HEIGHT / 2 + 45.0 * Math.sin(getAngle()) - Constants.SHELL_HEIGHT / 2;
    }

    @Override
    public double getYBound() {
        return getY() + Constants.TANK_HEIGHT;
    }

    @Override
    public double getXBound() {
        return getX() + Constants.TANK_WIDTH;
    }

    protected double getShellAngle() {
        return getAngle();
    }

    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }

    public int getHealthPoint() {
        return healthPoint;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    public int getCoolDown() {
        return coolDown;
    }
}
