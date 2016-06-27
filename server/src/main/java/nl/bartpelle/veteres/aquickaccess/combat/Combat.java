package nl.bartpelle.veteres.aquickaccess.combat;

import nl.bartpelle.veteres.event.Event;
import nl.bartpelle.veteres.event.EventContainer;
import nl.bartpelle.veteres.model.Entity;
import nl.bartpelle.veteres.model.Tile;
import nl.bartpelle.veteres.model.entity.Player;

/**
 * Created by Sky on 27-6-2016.
 */
public abstract class Combat {

    private Entity entity;
    private Entity target;

    public Combat(Entity entity, Entity target) {
        this.entity = entity;
        this.target = target;
    }

    public Entity getEntity() {
        return entity;
    }

    public Entity getTarget() {
        return target;
    }

    public void start() {
        entity.world().getEventHandler().addEvent(entity, new Event() {
            @Override
            public void execute(EventContainer container) {
                if (target.dead() || target.locked()) {
                    container.stop();
                    return;
                }
                cycle();
            }
        });
    }

    public abstract void cycle();



    public Tile moveCloser() {
        entity.pathQueue().clear();

        int steps = entity.pathQueue().running() ? 2 : 1;
        int otherSteps = target.pathQueue().running() ? 2 : 1;

        Tile otherTile = target.pathQueue().peekAfter(otherSteps) == null ? target.tile() : target.pathQueue().peekAfter(otherSteps).toTile();
        entity.stepTowards(target, otherTile, 25);
        return entity.pathQueue().peekAfter(steps - 1) == null ? entity.tile() : entity.pathQueue().peekAfter(steps - 1).toTile();
    }
}
