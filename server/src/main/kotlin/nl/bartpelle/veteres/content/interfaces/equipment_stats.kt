package nl.bartpelle.veteres.content.interfaces

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.get
import nl.bartpelle.veteres.content.message
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.content.set
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.model.entity.player.EquipSlot
import nl.bartpelle.veteres.net.message.game.InterfaceSettings
import nl.bartpelle.veteres.net.message.game.InterfaceText
import nl.bartpelle.veteres.net.message.game.InvokeScript
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.veteres.util.CombatFormula
import nl.bartpelle.veteres.util.EquipmentInfo
import nl.bartpelle.veteres.util.Varp

/**
 * @author William Talleur <talleurw@gmail.com>
 * @date October 25, 2015
 */
@ScriptMain fun openequipstats(repo: ScriptRepository) {
    /* Open equipment stats */
    repo.onButton(387, 17) {
        it.player().interfaces().sendMain(84)
        refreshequipstats(it.player())
    }
}

fun refreshequipstats(p : Player) {
    val playerBonuses = CombatFormula.totalBonuses(p, p.world().equipmentInfo())

    p.write(InterfaceText(84, 23, "Stab: " + format(playerBonuses.stab)))
    p.write(InterfaceText(84, 24, "Slash: " + format(playerBonuses.slash)))
    p.write(InterfaceText(84, 25, "Crush: " + format(playerBonuses.crush)))
    p.write(InterfaceText(84, 26, "Magic: " + format(playerBonuses.mage)))
    p.write(InterfaceText(84, 27, "Range: " + format(playerBonuses.range)))

    p.write(InterfaceText(84, 29, "Stab: " + format(playerBonuses.stabdef)))
    p.write(InterfaceText(84, 30, "Slash: " + format(playerBonuses.slashdef)))
    p.write(InterfaceText(84, 31, "Crush: " + format(playerBonuses.crushdef)))
    p.write(InterfaceText(84, 32, "Magic: " + format(playerBonuses.magedef)))
    p.write(InterfaceText(84, 33, "Range: " + format(playerBonuses.rangedef)))

    p.write(InterfaceText(84, 35, "Melee strength: " + format(playerBonuses.str)))
    p.write(InterfaceText(84, 36, "Ranged strength: " + format(playerBonuses.rangestr)))
    p.write(InterfaceText(84, 37, "Magic damage: " + format(playerBonuses.magestr) + "%"))
    p.write(InterfaceText(84, 38, "Prayer: " + format(playerBonuses.pray)))

    p.write(InterfaceText(84, 40, "Undead: 0%"))
    p.write(InterfaceText(84, 41, "Slayer: 0%"))
}

fun format(bonus : Int) : String {
    val prefix : String

    if (bonus.toString().startsWith("-") || bonus == 0)
        prefix = "";
    else
        prefix = "+";

    return prefix + bonus.toString()
}

fun unequip(it: Script, index: Int) {
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
}

@ScriptMain fun unequip(repo: ScriptRepository) {
    repo.onButton(84, 11) @Suspendable { unequip(it, EquipSlot.HEAD)}
    repo.onButton(84, 12) @Suspendable { unequip(it, EquipSlot.CAPE)}
    repo.onButton(84, 13) @Suspendable { unequip(it, EquipSlot.AMULET)}
    repo.onButton(84, 14) @Suspendable { unequip(it, EquipSlot.WEAPON)}
    repo.onButton(84, 15) @Suspendable { unequip(it, EquipSlot.BODY)}
    repo.onButton(84, 16) @Suspendable { unequip(it, EquipSlot.SHIELD)}
    repo.onButton(84, 17) @Suspendable { unequip(it, EquipSlot.LEGS)}
    repo.onButton(84, 18) @Suspendable { unequip(it, EquipSlot.HANDS)}
    repo.onButton(84, 19) @Suspendable { unequip(it, EquipSlot.FEET)}
    repo.onButton(84, 20) @Suspendable { unequip(it, EquipSlot.RING)}
    repo.onButton(84, 21) @Suspendable { unequip(it, EquipSlot.AMMO)}
}