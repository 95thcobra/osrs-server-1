package nl.bartpelle.veteres.content.interfaces

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.*
import nl.bartpelle.veteres.net.message.game.InterfaceText
import nl.bartpelle.veteres.script.ScriptRepository
import java.awt.Color

/**
 * Created by bart on 7/19/15.
 */

fun display(script: Script, skill: Int) {
    script.openInterface(214)
    script.player().varps().varbit(4371, skill)
    script.player().varps().varbit(4372, 0)
}

@ScriptMain fun skillguides(repo: ScriptRepository) {
    repo.onButton(320, 1) @Suspendable {display(it, 1)} // Attack
    repo.onButton(320, 2) @Suspendable {display(it, 2)} // Strength
    repo.onButton(320, 3) @Suspendable {display(it, 5)} // Defence
    repo.onButton(320, 4) @Suspendable {display(it, 3)} // Ranged
    repo.onButton(320, 5) @Suspendable {display(it, 7)} // Prayer
    repo.onButton(320, 6) @Suspendable {display(it, 4)} // Magic
    repo.onButton(320, 7) @Suspendable {display(it, 12)} // Runecrafting
    repo.onButton(320, 8) @Suspendable {display(it, 22)} // Construction
    repo.onButton(320, 9) @Suspendable {display(it, 6)} // Hitpoints
    repo.onButton(320, 10) @Suspendable {display(it, 8)} // Agility
    repo.onButton(320, 11) @Suspendable {display(it, 9)} // Herblore
    repo.onButton(320, 12) @Suspendable {display(it, 10)} // Thieving
    repo.onButton(320, 13) @Suspendable {display(it, 11)} // Crafting
    repo.onButton(320, 14) @Suspendable {display(it, 19)} // Fletching
    repo.onButton(320, 15) @Suspendable {display(it, 20)} // Slayer
    repo.onButton(320, 16) @Suspendable {display(it, 23)} // Hunter
    repo.onButton(320, 17) @Suspendable {display(it, 13)} // Mining
    repo.onButton(320, 18) @Suspendable {display(it, 14)} // Smithing
    repo.onButton(320, 19) @Suspendable {display(it, 15)} // Fishing
    repo.onButton(320, 20) @Suspendable {display(it, 16)} // Cooking
    repo.onButton(320, 21) @Suspendable {display(it, 17)} // Firemaking
    repo.onButton(320, 22) @Suspendable {display(it, 18)} // Woodcutting
    repo.onButton(320, 23) @Suspendable {display(it, 21)} // Farming

    for (tab in 11..22) {
        repo.onButton(214, tab) @Suspendable {it.player().varps().varbit(4372, tab - 11)}
    }

    repo.onObject(1286) @Suspendable {
        it.chatPlayer("Hello there.", 588)
        it.chatNpc("What brings you to our holy monument?", 5045, 554)

        when(it.options("Who are you?", "I'm in search of a quest.", "Did you build this?")) {
            1 -> {
                it.chatPlayer("Who are you?", 554)
                it.chatNpc("We are the druids of Guthix. We worship our god at<br>our famous stone circles. " +
                        "You will find them located<br>throughout these lands.", 5045, 590)

                when (it.options("What about the stone circle full of dark wizards?", "So what's so good about Guthix?", "Well, I'll be on my way now.")) {
                    1 -> {
                        it.chatPlayer("What about the stone circle full of dark wizards?", 554)
                        it.chatNpc("That used to be OUR stone circle. Unfortunately,<br>many many years ago, " +
                                "dark wizards cast a wicked spell<br>upon it so that they could corrupt its power " +
                                "for their<br>own evil ends.", 5045, 591)
                        it.chatNpc("When they cursed the rocks for their rituals they made<br>them useless to us and our magics. " +
                                "We require a brave<br>adventurer to go on a quest for us to help purify the<br>circle of Varrock.", 5045, 591)

                        when (it.options("Ok, I will try and help.", "No, that doesn't sound very interesting.", "So... is there anything in this for me?")) {
                            2 -> {
                                it.chatPlayer("No, that doesn't sound very interesting.", 575)
                                it.chatNpc("I will not try and change your mind adventurer. Some<br>day when you have " +
                                        "matured you may reconsider your<br>position. We will wait until then.", 5045, 590)
                            }
                        }
                    }
                    3 -> {
                        it.chatPlayer("Well, I'll be on my way now.")
                        it.chatNpc("Goodbye adventurer. I feel we shall meet again.", 5045)
                    }
                }
            }
            2 -> {}
            3 -> {
                it.chatPlayer("Did you build this?", 554)
                it.chatNpc("What, personally? No, of course I didn't. However, our<br>" +
                        "forefathers did. The first Druids of Guthix built many<br>stone circles across these " +
                        "lands over eight hundred<br>years ago.", 5045, 591)
                it.chatNpc("Unfortunately we only know of two remaining, and of<br>those only one is usable by us anymore.", 611)
            }
        }
    }
}