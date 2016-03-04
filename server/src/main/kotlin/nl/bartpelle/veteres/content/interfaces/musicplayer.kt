package nl.bartpelle.veteres.content.interfaces

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.dawnguard.DataStore
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.*
import nl.bartpelle.veteres.fs.EnumDefinition
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.World
import nl.bartpelle.veteres.net.message.game.PlayMusic
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Carl on 2015-08-23.
 */


@ScriptMain fun musicplayer(repo: ScriptRepository) {
    repo.onButton(239, 1) @Suspendable {
        val slot: Int = it.player().attrib(AttributeKey.BUTTON_SLOT)
        val def = it.player().world().definitions().get(javaClass<EnumDefinition>(), 812)
        val bitdef = it.player().world().definitions().get(javaClass<EnumDefinition>(), 819)

        val index = bitdef.getInt(slot) shr 14
        val bit = bitdef.getInt(slot) and 16383
        val varpid = varpForIndex(index)

        if (varpid != -1 && (it.player().varps()[varpid] and (1 shl bit) == 0)) {
            it.message("You have not unlocked this piece of music yet!")
        } else {
            val name = def.getString(slot)
            val id = resolveId(it.player().world().server().store(), name)

            it.player().write(PlayMusic(id))
        }
    }
}

fun resolveId(store: DataStore, name: String): Int =
    when (name) {
        "Thrall of the Serpent" -> 433
        else -> store.getIndex(6).getContainerByName(name).getId()
    }

fun varpForIndex(idx: Int): Int = when (idx) {
    1 -> 20
    2 -> 21
    3 -> 22
    4 -> 23
    5 -> 24
    6 -> 25
    7 -> 298
    8 -> 311
    9 -> 346
    10 -> 414
    11 -> 464
    12 -> 598
    13 -> 662
    14 -> 721
    15 -> 906
    16 -> 1009
    else -> -1
}
