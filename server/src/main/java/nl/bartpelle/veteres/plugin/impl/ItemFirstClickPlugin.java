package nl.bartpelle.veteres.plugin.impl;

import nl.bartpelle.veteres.model.item.Item;
import nl.bartpelle.veteres.plugin.PluginContext;

/**
 * The plugin context for the first item click message.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemFirstClickPlugin implements PluginContext {

    /**
     * The inventory slot that the player clicked.
     */
    private final int slot;

    /**
     * The item on the inventory slot that the player clicked.
     */
    private final Item item;

    /**
     * Create a new {@link ItemFirstClickPlugin}.
     *
     * @param slot the inventory slot that the player clicked.
     * @param item the item on the inventory slot that the player clicked.
     */
    public ItemFirstClickPlugin(int slot, Item item) {
        this.slot = slot;
        this.item = item;
    }

    /**
     * Gets the inventory slot that the player clicked.
     *
     * @return the inventory slot.
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Gets the item on the inventory slot that the player clicked.
     *
     * @return the item on the inventory slot.
     */
    public Item getItem() {
        return item;
    }
}
