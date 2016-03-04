package nl.bartpelle.veteres.content.interfaces

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.*
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.model.entity.player.EquipSlot
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Bart on 8/9/2015.
 */
fun unequipt(it: Script, index: Int) {
    if (it.player().locked())
        return

    val at = it.player().equipment().get(index) ?: return
    val opt: Int = it.player()[AttributeKey.BUTTON_ACTION]

    when (opt) {
        1 -> {
            if (it.player().inventory().add(at, false).success())
                it.player().equipment().set(index, null)
            else
                it.message("You don't have enough free space to do that.")
        }
        10 -> it.message(it.player().world().examineRepository().item(at.id())) // Examine
        else -> it.player().world().server().scriptRepository().triggerEquipmentOption(it.player(), at.id(), index, opt - 1)
    }

    refreshequipstats(it.player())
}

@ScriptMain fun unequipt(repo: ScriptRepository) {
    repo.onButton(387, 6) @Suspendable { unequipt(it, EquipSlot.HEAD)}
    repo.onButton(387, 7) @Suspendable { unequipt(it, EquipSlot.CAPE)}
    repo.onButton(387, 8) @Suspendable { unequipt(it, EquipSlot.AMULET)}
    repo.onButton(387, 9) @Suspendable { unequipt(it, EquipSlot.WEAPON)}
    repo.onButton(387, 10) @Suspendable { unequipt(it, EquipSlot.BODY)}
    repo.onButton(387, 11) @Suspendable { unequipt(it, EquipSlot.SHIELD)}
    repo.onButton(387, 12) @Suspendable { unequipt(it, EquipSlot.LEGS)}
    repo.onButton(387, 13) @Suspendable { unequipt(it, EquipSlot.HANDS)}
    repo.onButton(387, 14) @Suspendable { unequipt(it, EquipSlot.FEET)}
    repo.onButton(387, 15) @Suspendable { unequipt(it, EquipSlot.RING)}
    repo.onButton(387, 16) @Suspendable { unequipt(it, EquipSlot.AMMO)}
}