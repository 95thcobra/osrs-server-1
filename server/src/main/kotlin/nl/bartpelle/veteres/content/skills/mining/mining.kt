package nl.bartpelle.veteres.content.skills.mining

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.*
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.model.entity.player.Skills
import nl.bartpelle.veteres.model.item.Item
import nl.bartpelle.veteres.model.map.MapObj
import nl.bartpelle.veteres.script.ScriptRepository
import java.io.File
import java.io.PrintStream
import java.text.DecimalFormat
import kotlin.platform.platformName
import kotlin.platform.platformStatic

/**
 * Created by Bart on 8/28/2015.
 *
 * Handles the mining skill, as well as a few utility functions to add mining-like features elsewhere in the game.
 */
class Mining {
    enum class Rock(val ore: Int, val rockName: String, val level: Int, val difficulty: Int, val xp: Double) {
        COPPER(436, "copper", 1, 75, 17.5),
        IRON(440, "iron", 15, 75, 35.0),
        COAL(453, "coal", 30, 90, 50.0),
        GOLD(444, "gold", 40, 105, 65.0),
        MITHRIL(447, "mithril", 55, 140, 80.0),
        ADAMANT(449, "adamant", 70, 210, 95.0),
        RUNE(451, "rune", 85, 335, 125.0),
    }
    enum class Pickaxe(val id: Int, val points: Int, val anim: Int, val level: Int) {
        BRONZE(1265, 13, 625, 1),
        IRON(1267, 15, 626, 1),
        STEEL(1269, 18, 627, 6),
        BLACK(12297, 21, 3866, 11),
        MITHRIL(1273, 26, 629, 21),
        ADAMANT(1271, 30, 628, 31),
        RUNE(1275, 36, 624, 41),
        DRAGON(11920, 42, 7139, 61),
        DRAGON_OR(12797, 42, 642, 61),
        INFERNAL(13243, 42, 4482, 61),
    }

    companion object {
        fun chance(level: Int, type: Rock, pick: Pickaxe): Int {
            val points = ((level - type.level) + 1 + pick.points.toDouble())
            val denom = type.difficulty.toDouble()
            return (Math.min(0.95, points / denom) * 100).toInt()
        }

        fun find_pickaxe(player: Player): Pickaxe? {
            Pickaxe.values.sortedWith(comparator { p1, p2 -> p2.level - p1.level }).forEach {
                if (player.skills().xpLevel(Skills.MINING) >= it.level && (it.id in player.inventory() || it.id in player.equipment())) {
                    return it
                }
            }

            return null
        }

        @Suspendable fun mine(s: Script, rock: Rock, replId: Int = 10796) {
            val player = s.player()
            val obj: MapObj = s.player().attrib(AttributeKey.INTERACTION_OBJECT)
            val opt: Int = s.player().attrib(AttributeKey.INTERACTION_OPTION)
            val pick: Pickaxe? = find_pickaxe(player)

            if (pick == null) {
                return
            }

            // Is the inventory full? TODO msgbox
            if (player.inventory().full()) {
                return
            }

            if (opt == 1) {
                s.delay(1)
                s.message("You swing your pick at the rock.")

                while (true) {
                    player.animate(pick.anim)
                    s.delay(1)

                    println("Chance to get it is ${Mining.chance(player.skills()[Skills.MINING], rock, pick)}")
                    if (player.world().random(100) <= Mining.chance(player.skills()[Skills.MINING], rock, pick)) {
                        player.inventory() += rock.ore
                        player.skills().addXp(Skills.MINING, rock.xp)
                        s.message("You manage to mine some ${rock.name}.")
                        player.sound(3600, 0)
                        player.animate(-1)

                        val world = player.world()
                        val spawned = MapObj(obj.tile(), replId, obj.type(), obj.rot())
                        world.spawnObj(spawned)
                        s.context = 0
                        s.delay(10)
                        world.removeObjSpawn(obj)
                        return
                    }

                    s.delay(3)
                }
            } else {
                player.lock()
                player.message("You examine the rock for ores...")
                s.delay(4)
                player.message("This rock contains ${rock.name}.")
                player.sound(2661, 0)
                player.unlock()
            }
        }
    }
}

public fun main(args: Array<String>) {
    val type = Mining.Rock.GOLD
    val format = DecimalFormat("###.##")

    var out = PrintStream(File("C:\\Users\\Bart\\Desktop\\mining_"+type.toString().toLowerCase()+".csv"))
    out.print("lvl")
    for (h in Mining.Pickaxe.values())
        out.print("," + h)
    out.println()

    for (i in 1..99) {
        out.print(i)
        for (h in Mining.Pickaxe.values()) {
            out.print("," + format.format(Mining.chance(i, type, h)) + "%")
        }
        out.println()
    }

    out.flush()
}

@ScriptMain fun register_mining(r: ScriptRepository) {
    r.onObject(13708) @Suspendable { Mining.mine(it, Mining.Rock.COPPER) }
    r.onObject(13710) @Suspendable { Mining.mine(it, Mining.Rock.IRON) }
    r.onObject(13714) @Suspendable { Mining.mine(it, Mining.Rock.COAL) }
    r.onObject(13707) @Suspendable { Mining.mine(it, Mining.Rock.GOLD) }
    r.onObject(13718) @Suspendable { Mining.mine(it, Mining.Rock.MITHRIL) }
    r.onObject(13720) @Suspendable { Mining.mine(it, Mining.Rock.ADAMANT) }
    r.onObject(7418) @Suspendable { Mining.mine(it, Mining.Rock.RUNE, 7409) }
    r.onObject(7419) @Suspendable { Mining.mine(it, Mining.Rock.RUNE, 7409) }

    intArrayOf(10796).forEach { r.onObject(it) {it.player().sound(2661, 0); it.message("There is no ore currently available in this rock.")} }
}