package nl.bartpelle.veteres.model.entity;

import nl.bartpelle.veteres.model.Area;
import nl.bartpelle.veteres.model.Entity;
import nl.bartpelle.veteres.model.Tile;
import nl.bartpelle.veteres.model.map.MapObj;
import nl.bartpelle.veteres.util.Varp;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Bart Pelle on 8/23/2014.
 */
public class PathQueue {

    private Entity entity;
    private LinkedList<Step> steps = new LinkedList<>();
    private boolean running;
    private Tile lastStep;

    public PathQueue(Entity entity) {
        this.entity = entity;
    }

    public void step(int x, int z) {
        steps.add(new Step(x, z, StepType.REGULAR));
    }

    public void step(int x, int z, StepType type) {
        steps.add(new Step(x, z, type));
    }

    public void interpolate(int tx, int tz) {
        interpolate(tx, tz, StepType.REGULAR);
    }

    public int interpolate(int tx, int tz, StepType type) {
        return interpolate(tx, tz, type, Integer.MAX_VALUE);
    }

    public int interpolate(int tx, int tz, StepType type, int maxSteps) {
        int cx = steps.isEmpty() ? entity.tile().x : steps.getLast().x;
        int cz = steps.isEmpty() ? entity.tile().z : steps.getLast().z;

        int taken = 0;
        while (maxSteps-- > 0) {
            if (cx == tx && cz == tz)
                break;

            if (cx < tx)
                cx++;
            else if (cx > tx)
                cx--;
            if (cz < tz)
                cz++;
            else if (cz > tz)
                cz--;

            step(cx, cz, type);
            taken++;
        }

        return taken;
    }

    public boolean empty() {
        return steps.isEmpty();
    }

    public Step next() {
        lastStep = entity.tile();
        return steps.poll();
    }

    public Step peekNext() {
        return steps.peek();
    }

    public Step peekAfter(int skip) {
        Iterator<Step> iterator = steps.iterator();
        Step current = steps.peek();
        while (skip-- >= 0 && iterator.hasNext()) {
            Step c = iterator.next();
            if (c != null)
                current = c;
        }
        return current;
    }

    public Step peekLast() {
        return steps.peekLast();
    }

    public void clear() {
        steps.clear();
        entity.sync().clearMovement();
    }

    public void trimToSize(int size) {
        while (steps.size() > size)
            steps.removeLast();
    }

    public void removeOutside(Area area) {
        Iterator<Step> it = steps.iterator();
        while (it.hasNext()) {
            Step s = it.next();
            if (!area.contains(s.toTile())) { // Not inside? Prune from here.
                it.remove();

                while (it.hasNext()) {
                    it.next();
                    it.remove();
                }

                return;
            }
        }
    }

    public Tile lastStep() {
        return lastStep;
    }

    /**
     * Gets the tile the entity is walking to. idk if this is good
     *
     * @return
     * @author Simon
     */
    public Tile getTargetTile() {
        PathQueue.Step step = entity.pathQueue().peekLast();
        Tile lastTile;
        if (step == null)
            lastTile = entity.tile();
        else
            lastTile = entity.pathQueue().peekLast().toTile();
        return lastTile;
    }

    public void running(boolean b) {
        running = b;
    }

    public void toggleRunning() {
        if (!entity.isPlayer()) {
            return;
        }
        boolean runningEnabled = ((Player) entity).varps().varp(Varp.RUNNING_ENABLED) == 1;
        ((Player) entity).varps().varp(Varp.RUNNING_ENABLED, (runningEnabled ? 0 : 1));
    }

    public boolean running() {
        if (entity.isPlayer()) {
            return ((Player) entity).varps().varp(Varp.RUNNING_ENABLED) == 1;
        }
        return running;
    }

    public static int calculateDirection(int x1, int z1, int x2, int z2) {
        int dx = x2 - x1;
        int dz = z2 - z1;

        if (dz == 1) {
            if (dx == -1)
                return 0;
            else if (dx == 0)
                return 1;
            else if (dx == 1)
                return 2;
        } else if (dz == 0) {
            if (dx == -1)
                return 3;
            else if (dx == 1)
                return 4;
        } else if (dz == -1) {
            if (dx == -1)
                return 5;
            else if (dx == 0)
                return 6;
            else if (dx == 1)
                return 7;
        }

        return 0;
    }

    public static class Step {
        public int x;
        public int z;
        public StepType type;

        public Step(int x, int z, StepType type) {
            this.x = x;
            this.z = z;
            this.type = type;
        }

        public Tile toTile() {
            return new Tile(x, z);
        }
    }

    public static enum StepType {
        REGULAR, FORCED_WALK, FORCED_RUN
    }

}
