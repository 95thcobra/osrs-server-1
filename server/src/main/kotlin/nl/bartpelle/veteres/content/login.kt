package nl.bartpelle.veteres.content

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Bart on 7/9/2015.
 */
@ScriptMain fun login(repo: ScriptRepository) {
    repo.onLogin(@Suspendable  {
        it.player().message("Welcome to Guthix(ALPHA).")
        it.player().message("Please report any bug you encounter, and post suggestions on the forums.")
    })
}