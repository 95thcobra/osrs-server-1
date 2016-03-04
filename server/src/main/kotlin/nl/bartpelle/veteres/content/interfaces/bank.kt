package nl.bartpelle.veteres.content.interfaces

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.*
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.model.entity.player.Privilege
import nl.bartpelle.veteres.model.item.Item
import nl.bartpelle.veteres.net.message.game.InterfaceSettings
import nl.bartpelle.veteres.net.message.game.InvokeScript
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Bart on 8/23/2015.
 */
@ScriptMain fun bank(repo: ScriptRepository) {

    // Item withdrawing
    repo.onButton(15, 3) @Suspendable {
        val player = it.player()
        val slot: Int = player.attrib(AttributeKey.BUTTON_SLOT)
        val option: Int = player.attrib(AttributeKey.BUTTON_ACTION)
        val item = player.inventory()[slot]

        if (item != null) {
            val num = when (option) {
                2 -> 1
                3 -> 5
                4 -> 10
                7 -> player.inventory().count(item.id()) // All items
                else -> 1
            }

            val inInv = player.inventory().count(item.id())

            if (item.definition(player.world()).stackable() || num == 1) {
                // Stackable is the easiest. Remove & add.
                var toadd =  if (item.definition(player.world()).noteModel == 0)
                                Item(item, Math.min(inInv, num))
                             else
                                Item(item.definition(player.world()).unnotedID, Math.min(inInv, num)) // Params do not exist on paramable items

                val result = player.bank().add(toadd, true)
                if (result.completed() == item.amount()) {
                    // Did it fully deposit our stack there?
                    player.inventory()[slot] = null
                } else {
                    // Not all could be stored, so we just remove some from out stack
                    player.inventory()[slot] = Item(item.id(), item.amount() - result.completed())
                }
            } else {
                // Non-stackable works different due to item attribs. Must be iterated.
                var remaining = num
                for (i in 0..27) {
                    if (remaining < 1)
                        break;

                    val itemAt = player.inventory()[i] ?: continue // Get item or continue

                    // Is this a candidate?
                    if (itemAt.id() == item.id()) {
                        val result = player.bank().add(itemAt, true)
                        if (result.success()) {
                            player.inventory()[i] = null
                            remaining--
                        }
                    }
                }
            }
        }
    }


    // Deposit inventory
    repo.onButton(12, 27) {
        val player = it.player()
        for (i in 0..27) {
            val itemAt = player.inventory()[i] ?: continue // Get item or continue

            var toadd =  if (itemAt.definition(player.world()).noteModel == 0)
                itemAt
            else
                Item(itemAt.definition(player.world()).unnotedID, itemAt.amount()) // Params do not exist on paramable items

            // Is this a candidate?
            val result = player.bank().add(toadd, true)
            if (result.success()) {
                player.inventory()[i] = null
            }
        }
    }


    // Deposit equipment
    repo.onButton(12, 29) {
        val player = it.player()
        for (i in 0..13) {
            val itemAt = player.equipment()[i] ?: continue // Get item or continue

            // Is this a candidate?
            val result = player.bank().add(itemAt, true)
            if (result.success()) {
                player.equipment()[i] = null
            }
        }
    }


    // Depositing
    repo.onButton(12, 12) @Suspendable {
        val slot: Int = it.player().attrib(AttributeKey.BUTTON_SLOT)
        val option: Int = it.player().attrib(AttributeKey.BUTTON_ACTION)
        val item = it.player().bank()[slot]

        if (item != null) {
            val num = when (option) {
                1 -> 1
                2 -> 5
                3 -> 10
                6 -> it.player().bank().count(item.id()) // All items
                7 -> it.player().bank().count(item.id()) - 1 // All items except one
                else -> 1
            }

            if (num != 0) {
                // Check if we would withdraw at least 1
                val numinbank = it.player().bank().count(item.id())
                val numtake = Math.min(num, numinbank)
                val result = it.player().inventory().add(Item(item, numtake), true)

                if (result.completed() == numinbank) {
                    // Exhaust stack?
                    it.player().bank()[slot] = null
                } else if (result.completed() > 0) {
                    // At least any change?
                    it.player().bank()[slot] = Item(item, item.amount() - result.completed())
                }
            }
        }
    }
}

class Bank {
    companion object {
        @JvmStatic fun open(p: Player) {
            p.write(InvokeScript(917, -1, -2147483648))
            p.interfaces().sendMain(12, false)
            p.interfaces().send(15, p.interfaces().activeRoot(), if (p.interfaces().resizable()) 56 else 60, false)

            p.write(InterfaceSettings(12, 12, 0, 799, 1311998))
            p.write(InterfaceSettings(12, 12, 809, 817, 2))
            p.write(InterfaceSettings(12, 12, 818, 827, 1048576))
            p.write(InterfaceSettings(12, 10, 10, 10, 1048578))
            p.write(InterfaceSettings(12, 10, 11, 19, 1179714))
            p.write(InterfaceSettings(15, 3, 0, 27, 1181438))
            p.write(InterfaceSettings(15, 12, 0, 27, 1054))
            p.write(InterfaceSettings(12, 32, 0, 3, 2))

            p.bank().makeDirty()
        }
    }
}