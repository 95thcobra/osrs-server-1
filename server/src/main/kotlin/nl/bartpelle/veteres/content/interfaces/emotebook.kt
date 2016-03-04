package nl.bartpelle.veteres.content.interfaces

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.animate
import nl.bartpelle.veteres.content.get
import nl.bartpelle.veteres.content.message
import nl.bartpelle.veteres.content.player
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.entity.player.EquipSlot
import nl.bartpelle.veteres.script.ScriptRepository

/**
 * Created by bart on 7/19/15.
 */
@ScriptMain fun script(repo: ScriptRepository) { //TODO locking
    repo.onButton(216, 1) @Suspendable {

        val button: Int = it.player().attrib(AttributeKey.BUTTON_SLOT)

        when(button) {
            0 -> { it.animate(855) } // Yes
            1 -> { it.animate(856) } // No
            2 -> { it.animate(858) } // Bow
            3 -> { it.animate(859) } // Angry
            4 -> { it.animate(857) } // Think
            5 -> { it.animate(863) } // Wave
            6 -> { it.animate(2113) } // Shrug
            7 -> { it.animate(862) } // Cheer
            8 -> { it.animate(864) } // Beckon
            9 -> { it.animate(861) } // Laugh
            10 -> { it.animate(2109) } // Jump for joy
            11 -> { it.animate(2111) } // Yawn
            12 -> { it.animate(866) } // Dance
            13 -> { it.animate(2106) } // Jig
            14 -> { it.animate(2107) } // Spin
            15 -> { it.animate(2108) } // Headbang
            16 -> { it.animate(860) } // Cry
            17 -> { it.animate(1374); it.player().graphic(574) } // Blow kiss
            18 -> { it.animate(2105) } // Panic
            19 -> { it.animate(2110) } // Raspberry
            20 -> { it.animate(865) } // Clap
            21 -> { it.animate(2112) } // Salute
            22 -> { it.animate(2127) } // Goblin bow
            23 -> { it.animate(2128) } // Goblin salute
            24 -> { it.animate(1131) } // Glass box
            25 -> { it.animate(1130) } // Climb rope
            26 -> { it.animate(1129) } // Lean
            27 -> { it.animate(1128) } // Glass wall
            28 -> { it.animate(4275) } // Idea
            29 -> { it.animate(1745) } // Stomp
            30 -> { it.animate(4280) } // Flap
            31 -> { it.animate(4276) } // Slap head
            32 -> { it.animate(3544) } // Zombie walk
            33 -> { it.animate(3543) } // Zombie dance
            34 -> { it.animate(2836) } // Scared
            35 -> { it.animate(6111) } // Rabbit hop

            37 -> { //Skillcapes
                val cape = it.player().equipment().get(EquipSlot.CAPE).id()

                it.message("$cape")

                when (cape) {
                    9747, 9748 -> {it.animate(4959); it.player().graphic(823);} //Attack cape
                    9753, 9754 -> {it.animate(4961); it.player().graphic(824);} //Defence cape
                    9750, 9751 -> {it.animate(4981); it.player().graphic(828);} //Strength cape
                    9768, 9769 -> {it.animate(4971); it.player().graphic(833);} //Hitpoints cape
                    9756, 9757 -> {it.animate(4973); it.player().graphic(832);} //Ranged cape
                    9762, 9763 -> {it.animate(4939); it.player().graphic(813);} //Magic cape
                    9759, 9760 -> {it.animate(4979); it.player().graphic(829);} //Prayer cape
                    9801, 9802 -> {it.animate(4955); it.player().graphic(821);} //Cooking cape
                    9807, 9808 -> {it.animate(4957); it.player().graphic(822);} //Woodcutting cape
                    9783, 9784 -> {it.animate(4937); it.player().graphic(812);} //Fletching cape
                    9798, 9799 -> {it.animate(4951); it.player().graphic(819);} //Fishing cape
                    9804, 9805 -> {it.animate(4975); it.player().graphic(8831);} //Firemaking cape
                    9780, 9781 -> {it.animate(4949); it.player().graphic(818);} //Crafting cape
                    9795, 9796 -> {it.animate(4943); it.player().graphic(815);} //Smithing cape
                    9792, 9793 -> {it.animate(4941); it.player().graphic(814);} //Mining cape
                    9774, 9775 -> {it.animate(4969); it.player().graphic(835);} //Herblore cape
                    9771, 9772 -> {it.animate(4977); it.player().graphic(830);} //Agility cape
                    9777, 9778 -> {it.animate(4965); it.player().graphic(826);} //Thieving cape
                    9786, 9787 -> {it.animate(4967); it.player().graphic(1656);} //Slayer cape
                    9810, 9811 -> {it.animate(4963); it.player().graphic(826);} //Farming cape
                    9765, 9766 -> {it.animate(4947); it.player().graphic(817);} //Runecrafting cape
                    9789, 9790 -> {it.animate(4953); it.player().graphic(820);} //Construction cape
                    9948, 9949 -> {it.animate(5158); it.player().graphic(907);} //Hunter cape
                    9813 -> {it.animate(4945); it.player().graphic(816);} //Quest cape
                }

            }
        }
    }
}