package edu.csc413.tankgame;

import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class GameDriver {
    boolean win = false;
    boolean pauseSet = false;
    boolean endGame = false;
    int AICounter = 0;
    int powerUpCounter = 1000;
    int FastReloadingCounter = 0;
    boolean hasFastReloading = false;
    boolean PowerUpHealthFlag = false;
    boolean powerUpFastReloadingFlag = false;
    private final MainView mainView;
    private final RunGameView runGameView;
    private final GameWorld gameWorld;
    public GameDriver() {
        mainView = new MainView(this::startMenuActionPerformed, this::pauseMenuActionPerformed);
        runGameView = mainView.getRunGameView();
        gameWorld = new GameWorld();
    }

    public void start() {
        mainView.setScreen(MainView.Screen.START_GAME_SCREEN);
    }

    private void startMenuActionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case StartMenuView.START_BUTTON_ACTION_COMMAND -> runGame();
            case StartMenuView.EXIT_BUTTON_ACTION_COMMAND -> mainView.closeGame();
            default -> throw new RuntimeException("Unexpected action command: " + actionEvent.getActionCommand());
        }
    }

    private void pauseMenuActionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case PauseMenuView.RESUME_BUTTON_ACTION_COMMAND -> {
                mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
                pauseSet = false;
            }
            case StartMenuView.EXIT_BUTTON_ACTION_COMMAND -> mainView.closeGame();
            default -> throw new RuntimeException("Unexpected action command: " + actionEvent.getActionCommand());
        }
    }


    private void runGame() {
        mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
        Runnable gameRunner = () -> {
            setUpGame();
            while (updateGame()) {
                runGameView.repaint();
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
            }
            if(win){
                mainView.setScreen(MainView.Screen.WIN_END_MENU_SCREEN);
            } else {
                mainView.setScreen(MainView.Screen.LOSE_END_MENU_SCREEN);
            }
            resetGame();

        };
        new Thread(gameRunner).start();
    }

    /**
     * setUpGame is called once at the beginning when the game is started. Entities that are present from the start
     * should be initialized here, with their corresponding sprites added to the RunGameView.
     */
    private void setUpGame() {
        // TODO: Implement.

        // adding walls
        List<WallInformation> wallInformationList = WallInformation.readWalls();
        int wallNumber = 0;
        for (WallInformation wallInformation: wallInformationList){
            Entity walls = new Walls(wallInformation.getImageFile() + wallNumber,
                    wallInformation.getX(),
                    wallInformation.getY(),
                    0);
            wallNumber++;
            gameWorld.addEntity(walls);
            runGameView.addSprite(walls.getId(),
                    wallInformation.getImageFile(),
                    walls.getX(),
                    walls.getY(),
                    walls.getAngle());

        }


        // creating Tanks entities
        Entity playerTank =
                new PlayerTank(
                        Constants.PLAYER_TANK_ID,
                        Constants.PLAYER_TANK_INITIAL_X,
                        Constants.PLAYER_TANK_INITIAL_Y,
                        Constants.PLAYER_TANK_INITIAL_ANGLE);
        Entity dumbAiTank =
                new DumbAiTank(
                        Constants.AI_TANK_1_ID,
                        Constants.AI_TANK_1_INITIAL_X,
                        Constants.AI_TANK_1_INITIAL_Y,
                        Constants.AI_TANK_1_INITIAL_ANGLE);
        Entity smartAiTank =
                new SmartAiTank(
                        Constants.AI_TANK_2_ID,
                        Constants.AI_TANK_2_INITIAL_X,
                        Constants.AI_TANK_2_INITIAL_Y,
                        Constants.AI_TANK_2_INITIAL_ANGLE);
        // creating health bar status
        HealthBar  healthBar =
                new HealthBar(
                        Constants.HEALTH_BAR_ID,
                        Constants.HEALTH_BAR_INITIAL_X,
                        Constants.HEALTH_BAR_INITIAL_Y,
                        Constants.HEALTH_BAR_INITIAL_ANGLE);

        PowerUpBar  powerUpBar =
                new PowerUpBar(
                        Constants.POWER_UP_BAR_ID,
                        Constants.POWER_UP_BAR_INITIAL_X,
                        Constants.POWER_UP_BAR_INITIAL_Y,
                        Constants.POWER_UP_BAR_INITIAL_ANGLE);

        // adding the entities to the gameWorld
        gameWorld.addEntity(playerTank);
        gameWorld.addEntity(dumbAiTank);
        gameWorld.addEntity(smartAiTank);

        // adding the sprites for our entities
        runGameView.addSprite(
                playerTank.getId(),
                RunGameView.PLAYER_TANK_IMAGE_FILE,
                playerTank.getX(),
                playerTank.getY(),
                playerTank.getAngle());

        runGameView.addSprite(
                dumbAiTank.getId(),
                RunGameView.AI_TANK_IMAGE_FILE,
                dumbAiTank.getX(),
                dumbAiTank.getY(),
                dumbAiTank.getAngle());

        runGameView.addSprite(
                smartAiTank.getId(),
                RunGameView.AI_TANK_IMAGE_FILE,
                smartAiTank.getX(),
                smartAiTank.getY(),
                smartAiTank.getAngle());

        runGameView.addSprite(
                healthBar.getId(),
                RunGameView.Health_Point_3_IMAGE_FILE,
                healthBar.getX(),
                healthBar.getY(),
                healthBar.getAngle());

        runGameView.addSprite(
                powerUpBar.getId(),
                RunGameView.POWER_UP_BAR_IMAGE_FILE,
                powerUpBar.getX(),
                powerUpBar.getY(),
                powerUpBar.getAngle());

        // merging the new added entities with the original entities
        gameWorld.mergeEntities();
    }

    /**
     * updateGame is repeatedly called in the gameplay loop. The code in this method should run a single frame of the
     * game. As long as it returns true, the game will continue running. If the game should stop for whatever reason
     * (e.g. the player tank being destroyed, escape being pressed), it should return false.
     */
    private boolean updateGame() {
        // TODO: Implement.

        KeyboardReader keyboard = KeyboardReader.instance();

        // if the game is not paused update the game otherwise don't update "pause"
        if(!pauseSet){


        if(keyboard.escapePressed() && !pauseSet){
            pauseSet = true;
            mainView.setScreen(MainView.Screen.PAUSE_MENU_SCREEN);
        }

        // power up logic
        if(powerUpCounter <= 0 && !powerUpFastReloadingFlag) {
            powerUpFastReloadingFlag = true;
            Entity powerUpFastReloading =
                    new PowerUpFastReloading(
                            Constants.FAST_RELOADING_ID,
                            Constants.FAST_RELOADING_INITIAL_X,
                            Constants.FAST_RELOADING_INITIAL_Y,
                            Constants.FAST_RELOADING_INITIAL_ANGLE);
            gameWorld.addEntity(powerUpFastReloading);
            runGameView.addSprite(
                    powerUpFastReloading.getId(),
                    RunGameView.Fast_Reloading_IMAGE_FILE,
                    powerUpFastReloading.getX(),
                    powerUpFastReloading.getY(),
                    powerUpFastReloading.getAngle());

            gameWorld.mergeEntities();
        }
            if(powerUpCounter <= 0 && !PowerUpHealthFlag) {
                PowerUpHealthFlag = true;
                Entity powerUpHealth =
                        new PowerUpHealth(
                                Constants.HEALTH_POINT_ID,
                                Constants.HEALTH_POINT_INITIAL_X,
                                Constants.HEALTH_POINT_INITIAL_Y,
                                Constants.HEALTH_POINT_INITIAL_ANGLE);
                gameWorld.addEntity(powerUpHealth);
                runGameView.addSprite(
                        powerUpHealth.getId(),
                        RunGameView.Health_Point_1_IMAGE_FILE,
                        powerUpHealth.getX(),
                        powerUpHealth.getY(),
                        powerUpHealth.getAngle());

                gameWorld.mergeEntities();
            }

            powerUpCounter--;


        // power up status bar
            if(hasFastReloading) {
                runGameView.addSprite(
                        Constants.FAST_RELOADING_ID + "-bar-" + FastReloadingCounter,
                        RunGameView.Fast_Reloading_IMAGE_FILE,
                        Constants.POWER_UP_BAR_INITIAL_X + 30.0,
                        Constants.POWER_UP_BAR_INITIAL_Y + 30.0,
                        Constants.POWER_UP_BAR_INITIAL_ANGLE);
                hasFastReloading = false;
                FastReloadingCounter++;
            }
        // Health Bar for player tank
        Entity tank = gameWorld.getEntity(Constants.PLAYER_TANK_ID);
        if(((Tank) tank).getHealthPoint() == 3) {
            runGameView.removeSprite(Constants.HEALTH_BAR_ID);
            runGameView.addSprite(Constants.HEALTH_BAR_ID,
                    RunGameView.Health_Point_3_IMAGE_FILE,
                    Constants.HEALTH_BAR_INITIAL_X,
                    Constants.HEALTH_BAR_INITIAL_Y,
                    Constants.HEALTH_BAR_INITIAL_ANGLE);
        } else if(((Tank) tank).getHealthPoint() == 2){
            runGameView.removeSprite(Constants.HEALTH_BAR_ID);
            runGameView.addSprite(Constants.HEALTH_BAR_ID,
                    RunGameView.Health_Point_2_IMAGE_FILE,
                    Constants.HEALTH_BAR_INITIAL_X,
                    Constants.HEALTH_BAR_INITIAL_Y,
                    Constants.HEALTH_BAR_INITIAL_ANGLE);
        }else if(((Tank) tank).getHealthPoint() == 1){
            runGameView.removeSprite(Constants.HEALTH_BAR_ID);
            runGameView.addSprite(Constants.HEALTH_BAR_ID,
                    RunGameView.Health_Point_1_IMAGE_FILE,
                    Constants.HEALTH_BAR_INITIAL_X,
                    Constants.HEALTH_BAR_INITIAL_Y,
                    Constants.HEALTH_BAR_INITIAL_ANGLE);
        }

        for(Entity entity: gameWorld.getEntities()){
            entity.move(gameWorld);
            }

            // adding shells when they are fired from the tanks
        for(int i = 0; i < gameWorld.getNewEntities().size(); i++){
            runGameView.addSprite(
                        gameWorld.getNewEntities().get(i).getId(),
                        RunGameView.SHELL_IMAGE_FILE,
                        gameWorld.getNewEntities().get(i).getX(),
                        gameWorld.getNewEntities().get(i).getY(),
                        gameWorld.getNewEntities().get(i).getAngle());
        }
        gameWorld.mergeEntities();

        // handling Collision
        for (int i = 0; i < gameWorld.getEntities().size(); i++) {
            for (int j = i + 1; j < gameWorld.getEntities().size(); j++) {
                if(entitiesOverlap(gameWorld.getEntities().get(i), gameWorld.getEntities().get(j))){
                    handleCollision(gameWorld.getEntities().get(i), gameWorld.getEntities().get(j));
                        }
                    }
                }

        for(Entity entity: gameWorld.getEntities()){
            entity.boundsChecking(gameWorld);
        }

        // removing Entities from the runGameView using IDs
        for (Entity entity: gameWorld.getRemovedEntities()) {
            runGameView.removeSprite(entity.getId());
        }
        gameWorld.deleteEntities();

        for(Entity entity: gameWorld.getEntities()){
            runGameView.setSpriteLocationAndAngle(
                    entity.getId(),
                    entity.getX(),
                    entity.getY(),
                    entity.getAngle());
        }

        return !endGame;
        }
        return !endGame;
    }

    /**
     * resetGame is called at the end of the game once the gameplay loop exits. This should clear any existing data from
     * the game so that if the game is restarted, there aren't any things leftover from the previous run.
     */
    private void resetGame() {
        // TODO: Implement.
        endGame = false;
        AICounter = 0;
        powerUpCounter = 1000;
        FastReloadingCounter = 0;
        hasFastReloading = false;
        PowerUpHealthFlag = false;
        powerUpFastReloadingFlag = false;
        gameWorld.removeAllEntity();
        runGameView.reset();
    }
    private boolean entitiesOverlap(Entity entity1, Entity entity2){
        return entity1.getX() < entity2.getXBound()
                && entity1.getXBound() > entity2.getX()
                && entity1.getY() < entity2.getYBound()
                && entity1.getYBound() > entity2.getY();
    }

    private void handleCollision(Entity entity1, Entity entity2) {
        if (entity1 instanceof Tank && entity2 instanceof Tank) {
            double moveLeft = entity1.getXBound() - entity2.getX();
            double moveRight = entity2.getXBound() - entity1.getX();
            double moveUp = entity1.getYBound() - entity2.getY();
            double moveDown = entity2.getYBound() - entity1.getY();
            if(moveLeft < moveRight && moveLeft < moveUp && moveLeft < moveDown){
                entity1.setX(entity1.getX() - moveLeft / 2);
                entity2.setX(entity2.getX() + moveLeft / 2);
            } else if(moveRight < moveLeft && moveRight < moveUp && moveRight < moveDown){
                entity1.setX(entity1.getX() + moveRight / 2);
                entity2.setX(entity2.getX() - moveRight / 2);
            } else if(moveUp < moveLeft && moveUp < moveRight && moveUp < moveDown){
                entity1.setY(entity1.getY() - moveUp / 2);
                entity2.setY(entity2.getY() + moveUp / 2);
            } else if(moveDown < moveLeft &&  moveDown < moveRight && moveDown < moveUp){
                entity1.setY(entity1.getY() + moveDown / 2);
                entity2.setY(entity2.getY() - moveDown / 2);
            }
        } else if (entity1 instanceof Shell && entity2 instanceof Shell) {
            gameWorld.removeEntity(entity1.getId());
            gameWorld.removeEntity(entity2.getId());
            runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION,RunGameView.SHELL_EXPLOSION_FRAME_DELAY,entity1.getX(),entity2.getY());
        } else if (entity1 instanceof Tank && entity2 instanceof Shell) {
            gameWorld.removeEntity(entity2.getId());
            runGameView.addAnimation(RunGameView.BIG_EXPLOSION_ANIMATION,RunGameView.BIG_EXPLOSION_FRAME_DELAY,entity1.getX(),entity1.getY());
            ((Tank) entity1).setHealthPoint(((Tank) entity1).getHealthPoint() - 1);
            if(((Tank) entity1).getHealthPoint() <= 0){
               gameWorld.removeEntity(entity1.getId());
               if(entity1.getId().equals(Constants.PLAYER_TANK_ID)){
                   win = false;
                   endGame = true;
               } else {
                   AICounter ++;
               } // show END_MENU_SCREEN when all AI tanks die
                    if(AICounter == 2){
                        win = true;
                        endGame = true;
                    }
            }
        } else if (entity1 instanceof Walls && entity2 instanceof Tank) {
            double moveLeft = entity2.getXBound() - entity1.getX();
            double moveRight = entity1.getXBound() - entity2.getX();
            double moveUp = entity2.getYBound() - entity1.getY();
            double moveDown = entity1.getYBound() - entity2.getY();
            if(moveLeft < moveRight && moveLeft < moveUp && moveLeft < moveDown){
                entity2.setX(entity2.getX() - moveLeft);

            } else if(moveRight < moveLeft && moveRight < moveUp && moveRight < moveDown){
                entity2.setX(entity2.getX() + moveRight);
            } else if(moveUp < moveLeft && moveUp < moveRight && moveUp < moveDown){
                entity2.setY(entity2.getY() - moveUp);
            } else if(moveDown < moveLeft &&  moveDown < moveRight && moveDown < moveUp){
                entity2.setY(entity2.getY() + moveDown);
            }
        } else if (entity1 instanceof Walls && entity2 instanceof Shell) {
            gameWorld.removeEntity(entity2.getId());
            runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION,RunGameView.SHELL_EXPLOSION_FRAME_DELAY,entity1.getX(),entity1.getY());
            ((Walls) entity1).setHealthPoint(((Walls) entity1).getHealthPoint() - 1);
            if(((Walls) entity1).getHealthPoint() <= 0){
                gameWorld.removeEntity(entity1.getId());
            }

        } else if (entity1 instanceof Tank && entity2 instanceof PowerUpFastReloading) {
            ((Tank) entity1).setHasPowerUp(true);
            powerUpFastReloadingFlag = false;
            powerUpCounter = 1000;
            if(entity1.getId().equals(Constants.PLAYER_TANK_ID)){
                hasFastReloading = true;
            }
            gameWorld.removeEntity(entity2.getId());
            runGameView.removeSprite(entity2.getId());

        } else if (entity1 instanceof Tank && entity2 instanceof PowerUpHealth) {
            PowerUpHealthFlag = false;
            powerUpCounter = 1000;
            ((Tank) entity1).setHealthPoint(3);
            gameWorld.removeEntity(entity2.getId());
        }
    }

    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }
}
