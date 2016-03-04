package nl.bartpelle.veteres.content.interfaces

import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.*
import nl.bartpelle.veteres.net.message.game.InvokeScript
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.veteres.util.Varp

/**
 * Created by Bart on 9/6/2015.
 */
@ScriptMain fun settings(r: ScriptRepository) {
    // Brightness settings
    r.onButton(261, 15) {it.player().varps()[Varp.BRIGHTNESS] = 1}
    r.onButton(261, 16) {it.player().varps()[Varp.BRIGHTNESS] = 2}
    r.onButton(261, 17) {it.player().varps()[Varp.BRIGHTNESS] = 3}
    r.onButton(261, 18) {it.player().varps()[Varp.BRIGHTNESS] = 4}

    // Music volume
    r.onButton(261, 24) {it.player().varps()[Varp.MUSIC_VOLUME] = 4}
    r.onButton(261, 25) {it.player().varps()[Varp.MUSIC_VOLUME] = 3}
    r.onButton(261, 26) {it.player().varps()[Varp.MUSIC_VOLUME] = 2}
    r.onButton(261, 27) {it.player().varps()[Varp.MUSIC_VOLUME] = 1}
    r.onButton(261, 28) {it.player().varps()[Varp.MUSIC_VOLUME] = 0}

    // Sound effects volume
    r.onButton(261, 30) {it.player().varps()[Varp.SFX_VOLUME] = 4}
    r.onButton(261, 31) {it.player().varps()[Varp.SFX_VOLUME] = 3}
    r.onButton(261, 32) {it.player().varps()[Varp.SFX_VOLUME] = 2}
    r.onButton(261, 33) {it.player().varps()[Varp.SFX_VOLUME] = 1}
    r.onButton(261, 34) {it.player().varps()[Varp.SFX_VOLUME] = 0}

    // Area sound volume
    r.onButton(261, 36) {it.player().varps()[Varp.AREA_VOLUME] = 4}
    r.onButton(261, 37) {it.player().varps()[Varp.AREA_VOLUME] = 3}
    r.onButton(261, 38) {it.player().varps()[Varp.AREA_VOLUME] = 2}
    r.onButton(261, 39) {it.player().varps()[Varp.AREA_VOLUME] = 1}
    r.onButton(261, 40) {it.player().varps()[Varp.AREA_VOLUME] = 0}

    // Chat effects
    r.onButton(261, 42) {it.player().varps()[Varp.CHAT_EFFECTS] = if (it.player().varps()[Varp.CHAT_EFFECTS] == 1) 0 else 1}

    // Private chat toggle
    r.onButton(261, 44) {
        it.player().varps()[Varp.SPLIT_PRIVATE_CHAT] = if (it.player().varps()[Varp.SPLIT_PRIVATE_CHAT] == 1) 0 else 1
        it.player().write(InvokeScript(83))
    }

    r.onButton(261, 21) {
        it.player().interfaces().sendMain(60)
    }
}