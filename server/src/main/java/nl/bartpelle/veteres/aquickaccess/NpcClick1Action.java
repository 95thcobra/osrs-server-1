package nl.bartpelle.veteres.aquickaccess;

import nl.bartpelle.veteres.aquickaccess.dialogue.DialogueHandler;
import nl.bartpelle.veteres.model.entity.Player;

/**
 * Created by Sky on 21-6-2016.
 */
public class NpcClick1Action {
    public void handleNpcClick(Player player, int npcId) {
        switch (npcId) {
            case 4400:
                new DialogueHandler().sendOptionDialogue(
                        player,
                        "Where would you like to teleport to?",
                        "Edgeville",
                        "Varrock",
                        "Falador");
                break;
        }
    }
}