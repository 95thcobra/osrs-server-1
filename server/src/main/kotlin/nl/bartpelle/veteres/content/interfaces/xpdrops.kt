package nl.bartpelle.veteres.content.interfaces

import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.net.message.game.InterfaceSettings
import nl.bartpelle.veteres.net.message.game.InvokeScript
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.veteres.util.SettingsBuilder
import nl.bartpelle.veteres.util.Varbit

/**
 * Created by Bart on 9/5/2015.
 */
@ScriptMain fun xpdrops(r: ScriptRepository) {
    // Toggle button
    r.onButton(160, 1) {
        when (it.player().attrib<Int>(AttributeKey.BUTTON_ACTION)) {
            1 -> {}
            2 -> open_xpdrops(it.player())
        }
    }

    // Speed toggle
    r.onButton(137, 57) {
        it.player().varps().varbit(Varbit.XP_DROPS_SPEED, it.player().attrib<Int>(AttributeKey.BUTTON_SLOT) - 1);
    }
    // Size toggle
    r.onButton(137, 51) {
        it.player().varps().varbit(Varbit.XP_DROPS_SIZE, it.player().attrib<Int>(AttributeKey.BUTTON_SLOT) - 1);
    }
    // Position toggle
    r.onButton(137, 50) {
        it.player().varps().varbit(Varbit.XP_DROPS_POSITION, it.player().attrib<Int>(AttributeKey.BUTTON_SLOT) - 1);
    }
    // Duration toggle
    r.onButton(137, 52) {
        it.player().varps().varbit(Varbit.XP_DROPS_DURATION, it.player().attrib<Int>(AttributeKey.BUTTON_SLOT) - 1);
    }
    // Progressbar toggle
    r.onButton(137, 54) {
        it.player().varps().varbit(Varbit.XP_DROPS_PROGRESSBAR, it.player().attrib<Int>(AttributeKey.BUTTON_SLOT) - 1);
    }
    // Counter toggle
    r.onButton(137, 53) {
        it.player().varps().varbit(Varbit.XP_DROPS_COUNTER, it.player().attrib<Int>(AttributeKey.BUTTON_SLOT) - 1);
    }
    // Color toggle
    r.onButton(137, 55) {
        it.player().varps().varbit(Varbit.XP_DROPS_COLOR, it.player().attrib<Int>(AttributeKey.BUTTON_SLOT) - 1);
    }
    // Color toggle
    r.onButton(137, 56) {
        it.player().varps().varbit(Varbit.XP_DROPS_GROUP, it.player().attrib<Int>(AttributeKey.BUTTON_SLOT) - 1);
    }
}

fun open_xpdrops(player: Player) {
    player.write(InvokeScript(917, -1, -1))
    player.interfaces().sendMain(137)

    player.interfaces().setting(137, 50, 1, 3, SettingsBuilder().option(0))
    player.interfaces().setting(137, 51, 1, 3, SettingsBuilder().option(0))
    player.interfaces().setting(137, 52, 1, 4, SettingsBuilder().option(0))
    player.interfaces().setting(137, 53, 1, 32, SettingsBuilder().option(0))
    player.interfaces().setting(137, 54, 1, 32, SettingsBuilder().option(0))
    player.interfaces().setting(137, 55, 1, 8, SettingsBuilder().option(0))
    player.interfaces().setting(137, 56, 1, 2, SettingsBuilder().option(0))
    player.interfaces().setting(137, 57, 1, 3, SettingsBuilder().option(0))
    player.interfaces().setting(137, 16, 0, 24, SettingsBuilder().option(0))
}