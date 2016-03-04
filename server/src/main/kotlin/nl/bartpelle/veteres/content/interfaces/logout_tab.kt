package nl.bartpelle.veteres.content.interfaces

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Carl on 2015-08-14.
 */
@ScriptMain fun logout_tab(repo: ScriptRepository) {
    repo.onButton(182, 6, @Suspendable {
        it.player().logout()
    })
}