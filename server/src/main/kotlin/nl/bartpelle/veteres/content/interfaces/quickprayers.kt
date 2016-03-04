package nl.bartpelle.veteres.content.interfaces

import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * @author William Talleur <talleurw@gmail.com>
 * @date November 4, 2015
 */
@ScriptMain fun openquickprayers(repo: ScriptRepository) {
    repo.onButton(160, 14) {
        it.player().interfaces().sendMain(77)
    }
}