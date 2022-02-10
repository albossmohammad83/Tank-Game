package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.KeyboardReader;

public class PlayerTank extends Tank {

    public PlayerTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);

    }

    @Override
    public void move(GameWorld gameWorld) {
        KeyboardReader keyboard = KeyboardReader.instance();
        if (keyboard.upPressed()) {
            moveForward(Constants.TANK_MOVEMENT_SPEED);
        }
        if (keyboard.downPressed()) {
            moveBackward(Constants.TANK_MOVEMENT_SPEED);
        }
        if (keyboard.leftPressed()) {
            turnLeft(Constants.TANK_TURN_SPEED);
        }
        if (keyboard.rightPressed()) {
            turnRight(Constants.TANK_TURN_SPEED);
        }
        if (keyboard.spacePressed()) {
            if (getCoolDown() == 0) {
                fireShell(gameWorld,
                        getShellX(),
                        getShellY(),
                        getShellAngle());
            }
        }
        if (getCoolDown() != 0) {
            setCoolDown(getCoolDown() - 1);
        }
    }
}
