package nl.bartpelle.veteres.aquickaccess;

import nl.bartpelle.veteres.model.Tile;

/**
 * Created by Sky on 21-6-2016.
 */
public enum Coordinates {
    VARROCK(3210, 3424);

    private int x;
    private int y;

    Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Tile getTile() {
        return new Tile(x, y);
    }
}
