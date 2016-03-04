package nl.bartpelle.veteres.content.interfaces

import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.content.*
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.veteres.util.Varp

/**
 * Created by Bart on 8/21/2015.
 */
@ScriptMain fun runtoggle(repo: ScriptRepository) {
    repo.onButton(160, 22) {it.player().varps()[Varp.RUNNING_ENABLED] = if (it.player().varps()[Varp.RUNNING_ENABLED] == 1) 0 else 1}
    repo.onButton(261, 54) {it.player().varps()[Varp.RUNNING_ENABLED] = if (it.player().varps()[Varp.RUNNING_ENABLED] == 1) 0 else 1}
}