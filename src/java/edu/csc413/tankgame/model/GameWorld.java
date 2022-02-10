package edu.csc413.tankgame.model;

import edu.csc413.tankgame.view.RunGameView;

import java.util.*;

/**
 * GameWorld holds all of the model objects present in the game. GameWorld tracks all moving entities like tanks and
 * shells, and provides access to this information for any code that needs it (such as GameDriver or entity classes).
 */
public class GameWorld {
    // TODO: Implement. There's a lot of information the GameState will need to store to provide contextual information.
    //       Add whatever instance variables, constructors, and methods are needed.
    private final List<Entity> entities;
    private final List<Entity> removedEntities;
    private final List<Entity> newEntities;
    public GameWorld() {
        // TODO: Implement.
        entities = new ArrayList<>();
        removedEntities = new ArrayList<>();
        newEntities = new ArrayList<>();
    }

    /** Returns a list of all entities in the game. */
    public List<Entity> getEntities() {
        // TODO: Implement.
        return entities;
    }

    /** Adds a new entity to the game. */
    public void addEntity(Entity entity) {
        // TODO: Implement.
        newEntities.add(entity);
    }

    public List<Entity> getRemovedEntities() {
        return removedEntities;
    }

    public List<Entity> getNewEntities() {
        return newEntities;
    }

    /** Returns the Entity with the specified ID. */
    public Entity getEntity(String id) {
        // TODO: Implement.
        for (Entity entity : entities) {
            if (entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }

    /** Removes the entity with the specified ID from the game. */
    public void removeEntity(String id) {
        // TODO: Implement.
        for (Entity entity : entities) {
            if (entity.getId().equals(id)) {
                removedEntities.add(entity);
            }
        }
    }

    public void deleteEntities(){
        for(Entity entity: removedEntities){
            entities.remove(entity);
        }
        removedEntities.clear();
    }

    public void mergeEntities(){
        entities.addAll(newEntities);
        newEntities.clear();
    }

    public void removeAllEntity() {
        entities.clear();
        newEntities.clear();
        removedEntities.clear();
    }

}
