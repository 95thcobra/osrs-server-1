package nl.bartpelle.veteres.content.combat

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.message
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.content.skills.prayer.Bone
import nl.bartpelle.veteres.content.skills.prayer.bury
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.model.entity.player.Skills
import nl.bartpelle.veteres.model.item.Item
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.veteres.script.TimerKey

/**
 * Created by Carl on 2015-08-13.
 */

enum class Food(val itemId: Int, val heal: Int, val replacement: Int = -1, val effect: Boolean = false) {
    SHRIMP(315, 3),
    COOKED_CHICKEN(2140, 4),
    SARDINE(325, 4),
    COOKED_MEAT(2142, 4),
    BREAD(2309, 5),
    HERRING(347, 5),
    MACKEREL(355, 6),
    TROUT(333, 7),
    SALMON(329, 9),
    TUNA(361, 10),
    CAKE(1891, 4, 1893),
    TWO_THIRDS_CAKE(1893, 4, 1895),
    ONE_THIRD_CAKE(1895, 4),
    LOBSTER(379, 12),
    BASS(365, 13),
    SWORDFISH(373, 14),
    MONKFISH(7946, 16),
    ANCHOVY_PIZZA(2297, 9, 2299),
    HALF_ANCHOVY_PIZZA(2299, 9),
    SHARK(385, 20),
    SEA_TURTLE(397, 21),
    MANTA_RAY(391, 22),
    TUNA_POTATO(7060, 22),
    DARK_CRAB(11936, 22),
    REDBERRY_PIE(2325, 5, 2333),
    HALF_REDBERRY_PIE(2333, 5, 2313),
    MEAT_PIE(2327, 6, 2331),
    HALF_MEAT_PIE(2331, 6, 2313),
    APPLE_PIE(2323, 7, 2335),
    HALF_APPLE_PIE(2335, 7, 2313),
    GARDEN_PIE(7178, 6, 7180),
    HALF_GARDEN_PIE(7180, 6, 2313),
    FISH_PIE(7188, 6, 7190),
    HALF_FISH_PIE(7190, 6, 2313),
    ADMIRAL_PIE(7198, 8, 7200),
    HALF_ADMIRAL_PIE(7200, 8, 2313),
    WILD_PIE(7208, 11, 7210),
    HALF_WILD_PIE(7210, 11, 2313),
    SUMMER_PIE(7218, 11, 7220),
    HALF_SUMMER_PIE(7220, 11, 2313),

    BEER(1917, 1, 1919, true),
}

@ScriptMain fun food(repo: ScriptRepository) {
    Food.values().forEach { food -> repo.onItemOption1(food.itemId) @Suspendable { eat(it, food) } }
}

@Suspendable fun eat(script: Script, food: Food) {
    if (script.player().timers().has(TimerKey.FOOD))
        return

    val foodItem = Item(food.itemId)
    val healed = script.player().hp() < script.player().maxHp()
    val name = foodItem.definition(script.player().world()).name.toLowerCase()

    script.player().inventory().remove(foodItem, true, script.player().attrib(AttributeKey.ITEM_SLOT, 0))
    if (food.replacement > -1)
        script.player().inventory().add(Item(food.replacement), true)

    script.player().timers().register(TimerKey.FOOD, 3)
    script.player().timers().extendOrRegister(TimerKey.COMBAT_ATTACK, 3)

    heal(script.player(), food.heal)
    script.player().animate(829)
    if (!food.effect) {
        script.message("You eat the $name.")

        if (healed)
            script.message("It heals some health.")
    } else {
        if (food == Food.BEER) {
            script.message("You drink the beer. You feel slightly reinvigorated...")
            script.message("...and slightly dizzy too.")
        }
    }
}

fun heal(player: Player, amount: Int) {
    player.hp(player.hp() + amount, 0)
}