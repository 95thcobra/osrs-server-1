package nl.bartpelle.veteres.event;

import nl.bartpelle.veteres.model.entity.Player;

/**
 * Created by Sky on 3-3-2016.
 */
public abstract class Event {

    public abstract void execute(EventContainer container);

    public void stop() {

    }
}