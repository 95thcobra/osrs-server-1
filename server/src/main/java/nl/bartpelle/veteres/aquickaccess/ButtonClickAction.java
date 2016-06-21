package nl.bartpelle.veteres.aquickaccess;

import nl.bartpelle.veteres.model.Tile;
import nl.bartpelle.veteres.model.entity.Player;
import nl.bartpelle.veteres.net.message.game.InvokeScript;
import nl.bartpelle.veteres.util.SettingsBuilder;
import nl.bartpelle.veteres.util.Varbit;

/**
 * Created by Sky on 21-6-2016.
 */
public class ButtonClickAction {
    private Player player;
    private int interfaceId;
    private int buttonId;
    private int slot;
    private int option;

    public ButtonClickAction(Player player, int interfaceId, int buttonId, int slot, int option) {
        this.player = player;
        this.interfaceId = interfaceId;
        this.buttonId = buttonId;
        this.slot = slot;
        this.option = option;
    }

    // Add buttons here
    public void handleButtonClick() {
        switch (interfaceId) {

            // XP Drops settings
            case 137:
                XPDropToggles();
                break;

            // XP Drops
            case 160:
                setupXPDrops();
                break;

            // Spellbook
            case 218:
                handleSpellBook();
                break;
        }
    }

    ////////////////

    private void XPDropToggles() {
        switch(buttonId) {

            // Position
            case 50:
                player.varps().varbit(Varbit.XP_DROPS_POSITION, slot);
                break;

            // Size
            case 51:
                player.varps().varbit(Varbit.XP_DROPS_SIZE, slot);
                break;

            // Duration
            case 52:
                player.varps().varbit(Varbit.XP_DROPS_DURATION, slot);
                break;

            // Counter
            case 53:
                player.varps().varbit(Varbit.XP_DROPS_COUNTER, slot);
                break;

            // Progressbar
            case 54:
                player.varps().varbit(Varbit.XP_DROPS_PROGRESSBAR, slot);
                break;

            // Color
            case 55:
                player.varps().varbit(Varbit.XP_DROPS_COLOR, slot);
                break;

            // Group
            case 56:
                player.varps().varbit(Varbit.XP_DROPS_GROUP, slot);
                break;

            // Speed
            case 57:
                player.varps().varbit(Varbit.XP_DROPS_SPEED, slot);
                break;
        }
    }

    private void setupXPDrops() {
        // Toggle XP drops

        // Setup XP drops
        if (option == 1) {
            player.write(new InvokeScript(917, -1, -1));
            player.interfaces().sendMain(137);

            SettingsBuilder settingsBuilder = new SettingsBuilder();
            player.interfaces().setting(137, 50, 1, 3, settingsBuilder.option(0));
            player.interfaces().setting(137, 51, 1, 3, settingsBuilder.option(0));
            player.interfaces().setting(137, 52, 1, 4, settingsBuilder.option(0));
            player.interfaces().setting(137, 53, 1, 32, settingsBuilder.option(0));
            player.interfaces().setting(137, 54, 1, 32, settingsBuilder.option(0));
            player.interfaces().setting(137, 55, 1, 8, settingsBuilder.option(0));
            player.interfaces().setting(137, 56, 1, 2, settingsBuilder.option(0));
            player.interfaces().setting(137, 57, 1, 3, settingsBuilder.option(0));
            player.interfaces().setting(137, 16, 0, 24, settingsBuilder.option(0));
        }
    }

    private void handleSpellBook() {
        if (option != 0) {
            return;
        }
        switch (buttonId) {

            // Varrock teleport
            case 16:
                player.message("Teleporting to varrock.");
                player.teleportWithAnimation(Coordinates.VARROCK.getTile());
                break;
        }
    }
}
