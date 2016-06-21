package nl.bartpelle.veteres.aquickaccess.dialogue;

import nl.bartpelle.veteres.model.entity.Player;
import nl.bartpelle.veteres.model.entity.player.Privilege;

/**
 * Created by Sky on 21-6-2016.
 */
public class DialogueAction {

    private void handleOption1(Player player) {
        switch (player.getDialogueAction()) {

            // Teleport wizard
            case 1:
                player.message("Teleport....");
                break;

            // Spellbook altar
            case 2:
                player.message("Change spellbook....");
                player.varps().varbit(4070, 1); // ancients
                player.varps().varbit(4070, 0); // Modern
                player.varps().varbit(4070, 2); // Lunar
                break;
        }
    }

    private void handleOption2(Player player) {
        switch (player.getDialogueAction()) {

            // Teleport wizard
            case 1:
                player.message("1Teleport....");
                break;

            // Spellbook altar
            case 2:
                player.message("2Change spellbook....");
                break;
        }
    }

    //////////////  ///////////////

    public void handleDialog(Player player, int option) {
        switch (option) {
            case 1:
                handleOption1(player);
                break;
            case 2:
                handleOption2(player);
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
        player.interfaces().closeChatDialogue();
    }
}
