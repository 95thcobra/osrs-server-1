package nl.bartpelle.veteres.content.items.teleport

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.Tile
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.model.item.Item
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Bart on 8/25/2015.
 */
@ScriptMain fun teletabs(repo: ScriptRepository) {
    repo.onItemOption1(8007) @Suspendable { tab(it, it.player(), Tile(3212, 3423), 2) } // Varrock
    repo.onItemOption1(8008) @Suspendable { tab(it, it.player(), Tile(3222, 3218), 2) } // Lumbridge
    repo.onItemOption1(8009) @Suspendable { tab(it, it.player(), Tile(2965, 3380), 3) } // Falador
    repo.onItemOption1(8010) @Suspendable { tab(it, it.player(), Tile(2757, 3479), 2) } // Camelot
    repo.onItemOption1(8011) @Suspendable { tab(it, it.player(), Tile(2662, 3307), 2) } // Ardougne
}

@Suspendable fun tab(script: Script, player: Player, target: Tile, radius: Int) {
    val used = player.inventory()[player.attrib(AttributeKey.ITEM_SLOT)]
    player.lock()
    player.sound(965, 15)
    player.animate(4069, 16)
    script.delay(2)
    player.inventory().remove(Item(used.id(), 1), true)
    player.animate(4071)
    player.graphic(678)
    script.delay(2)
    player.teleport(player.world().randomTileAround(target, radius))
    player.animate(-1)
    player.graphic(-1)
    player.unlock()
}

