package nl.bartpelle.veteres.content.mechanics

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.interfaces.Bank
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Bart on 8/24/2015.
 */
@ScriptMain fun bankobjs(repo: ScriptRepository) {
    repo.onObject(11744) @Suspendable { Bank.open(it.player()) }
    repo.onObject(11748) @Suspendable { Bank.open(it.player()) }
    repo.onObject(24101) @Suspendable { Bank.open(it.player()) }
}