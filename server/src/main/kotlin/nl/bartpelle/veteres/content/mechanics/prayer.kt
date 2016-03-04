package nl.bartpelle.veteres.content.mechanics

import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.*
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.model.entity.player.Skills
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.veteres.script.TimerKey
import nl.bartpelle.veteres.util.Varbit

/**
 * Created by Bart on 8/24/2015.
 */
@ScriptMain fun prayers(repo: ScriptRepository) {
    repo.onLogin { it.player().timers()[TimerKey.PRAYER_TICK] = 1 }
    repo.onTimer(TimerKey.PRAYER_TICK) {
        it.player().timers()[TimerKey.PRAYER_TICK] = 1

        var prayvalue: Int = it.player().attrib(AttributeKey.PRAYERINCREMENT, 0)

        if (it.player().skills().level(Skills.PRAYER) > 0) {
            if (it.player().varps().varbit(4118) == 1)
                prayvalue += 20

            if (prayvalue > 100) {
                val points = prayvalue / 100
                it.player().skills().levels()[Skills.PRAYER] = Math.max(0, it.player().skills().level(Skills.PRAYER) - points)
                it.player().skills().update(Skills.PRAYER)
                if (it.player().skills().level(Skills.PRAYER) < 1) {
                    Prayers.disableAllPrayers(it.player())
                    it.message("You have run out of prayer points, you must recharge at an altar.")
                }
                prayvalue %= 100
            }
        }

        it.player().putattrib(AttributeKey.PRAYERINCREMENT, prayvalue)
    }

    // Prayer toggles
    repo.onButton(271, 16) {togglePrayer(it, Varbit.PROTECT_FROM_MAGIC, 37, Varbit.PROTECT_FROM_MELEE, Varbit.PROTECT_FROM_MISSILES)}
    repo.onButton(271, 17) {togglePrayer(it, Varbit.PROTECT_FROM_MISSILES, 40, Varbit.PROTECT_FROM_MAGIC, Varbit.PROTECT_FROM_MELEE)}
    repo.onButton(271, 18) {togglePrayer(it, Varbit.PROTECT_FROM_MELEE, 43, Varbit.PROTECT_FROM_MISSILES, Varbit.PROTECT_FROM_MAGIC)}
}

fun togglePrayer(script: Script, varbit: Int, levelreq: Int, vararg disable: Int) {
    if (script.player().skills().xpLevel(Skills.PRAYER) >= levelreq && script.player().skills().level(Skills.PRAYER) > 0) {
        val state = script.player().varps().varbit(varbit)
        script.player().varps().varbit(varbit, if (state == 1) 0 else 1)

        for (v in disable) // Disable the ones we must toggle off
            script.player().varps().varbit(v, 0)
    } else {
        script.player().varps().varbit(varbit, 0)
    }
}

object Prayers {
    @JvmStatic fun disableAllPrayers(player: Player) {
        player.varps().varbit(Varbit.PROTECT_FROM_MAGIC, 0)
        player.varps().varbit(Varbit.PROTECT_FROM_MELEE, 0)
        player.varps().varbit(Varbit.PROTECT_FROM_MISSILES, 0)
    }
}