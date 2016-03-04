package nl.bartpelle.veteres.content.mechanics

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.*
import nl.bartpelle.veteres.content.interfaces.Bank
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by Bart on 9/18/2015.
 */
fun interrupt_depositbox(s: Script, t149: Int, t387: Int) {
    s.player().interfaces().closeMain();
    s.player().invokeScript(915, 3);
    s.player().invokeScript(917, -1, -1)
    s.player().interfaces().send(149, t149 shr 16, t149 and 0xFFFF, false)
    s.player().interfaces().send(387, t387 shr 16, t387 and 0xFFFF, false)
}

@Suspendable fun open_deposit_box(script: Script) {
    script.openInterface(192)

    // Remember where we removed them from
    val t149 = script.player().interfaces().closeById(149)
    val t387 = script.player().interfaces().closeById(387)
    script.player().interfaces().setting(192, 2, 0, 27, 1180734)

    // Define interrupt script
    script.onInterrupt {interrupt_depositbox(it, t149, t387)}

    script.waitForInterfaceClose(192)

    // The interface was closed here.. get rid of the stuff
    interrupt_depositbox(script, t149, t387)
}

@ScriptMain fun deposit_objects(repo: ScriptRepository) {
    repo.onObject(11747) @Suspendable { open_deposit_box(it) }
}