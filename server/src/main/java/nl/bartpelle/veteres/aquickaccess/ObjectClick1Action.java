package nl.bartpelle.veteres.aquickaccess;

import nl.bartpelle.veteres.aquickaccess.dialogue.DialogueHandler;
import nl.bartpelle.veteres.model.entity.Player;
import nl.bartpelle.veteres.model.map.MapObj;

/**
 * Created by Sky on 21-6-2016.
 */
public class ObjectClick1Action {
    public void handleObjectClick(Player player, MapObj mapObj) {
        switch (mapObj.id()) {

            // Spellbook altar edgeville
            case 6552:
                new DialogueHandler().sendOptionDialogue(
                        player,
                        "Choose your spellbook",
                        "Regular",
                        "Ancient",
                        "Lunar",
                        "Maybe later");
                player.setDialogueAction(2);
                break;

            // Prayer altar edgeville
            case 6817:
                player.skills().restorePrayer();
                player.animate(645);
                player.message("You have recharged your prayer.");
                break;
        }
    }
}
