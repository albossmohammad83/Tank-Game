package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

public class DumbAiTank extends Tank {
    public DumbAiTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    @Override
    public void move(GameWorld gameWorld) {
        moveBackward(Constants.TANK_MOVEMENT_SPEED);
        if(getCoolDown() % 4 == 0)
        turnLeft(Constants.TANK_TURN_SPEED);
        if(getCoolDown() == 0){
           fireShell(gameWorld,
                   getShellX(),
                   getShellY(),
                   getShellAngle());
        } else {
            setCoolDown(getCoolDown() - 1);
        }
    }
}