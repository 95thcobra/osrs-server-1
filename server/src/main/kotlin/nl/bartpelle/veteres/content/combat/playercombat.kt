package nl.bartpelle.veteres.content.combat

import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.ScriptMain
import nl.bartpelle.veteres.content.*
import nl.bartpelle.veteres.fs.ItemDefinition
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.Entity
import nl.bartpelle.veteres.model.Hit
import nl.bartpelle.veteres.model.Tile
import nl.bartpelle.veteres.model.entity.PathQueue
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.model.entity.player.EquipSlot
import nl.bartpelle.veteres.model.entity.player.Skills
import nl.bartpelle.veteres.model.entity.player.WeaponType
import nl.bartpelle.veteres.model.item.Item
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.veteres.script.TimerKey
import nl.bartpelle.veteres.util.*

/**
 * Created by Bart on 8/12/2015.
 */
@ScriptMain fun spectrigger(repo: ScriptRepository) {
    repo.onButton(593, 30) @Suspendable {

        it.player().varps()[Varp.SPECIAL_ENABLED] = if (it.player().varps()[Varp.SPECIAL_ENABLED] == 1) 0 else 1

        if (it.player().varps()[Varp.SPECIAL_ENABLED] == 1) {
            val weapon = it.player().equipment()[EquipSlot.WEAPON]?.id() ?: -1

            when (weapon) {
                35 -> {
                    // Dragon long
                    if (takeSpecialEnergy(it.player(), 100)) {
                        it.player().skills().alterSkill(Skills.DEFENCE, 8, true)
                        it.animate(1168)
                        it.player().graphic(247)
                        it.player().timers()[TimerKey.COMBAT_ATTACK] = 5
                    }
                    it.player().varps()[Varp.SPECIAL_ENABLED] = 0;
                }
            }
        }
    }
}

@ScriptMain fun styleswitching(repo: ScriptRepository) {
    repo.onButton(593, 3) { it.player().varps()[43] = 0 }
    repo.onButton(593, 7) { it.player().varps()[43] = 1 }
    repo.onButton(593, 11) { it.player().varps()[43] = 2 }
    repo.onButton(593, 15) { it.player().varps()[43] = 3 }
}

@ScriptMain fun spells(repo: ScriptRepository) {
    repo.onSpellOnPlayer(218, 74) @Suspendable { spellOnPlayer(it, 1978, -1, 12, 385, 14) } // Smoke rush TODO poison
    repo.onSpellOnPlayer(218, 78) @Suspendable { spellOnPlayer(it, 1978, -1, 12, 379, 15) } // Shadow rush
    repo.onSpellOnPlayer(218, 70) @Suspendable { spellOnPlayer(it, 1978, -1, 12, 373, 16) } // Blood rush
    repo.onSpellOnPlayer(218, 66) @Suspendable { spellOnPlayer(it, 1978, 360, 12, 361, 17) } // Ice rush
    repo.onSpellOnPlayer(218, 80) @Suspendable { spellOnPlayer(it, 1979, -1, 12, 382, 19) } // Shadow burst
    repo.onSpellOnPlayer(218, 72) @Suspendable { spellOnPlayer(it, 1979, -1, 12, 376, 21) } // Blood burst
    repo.onSpellOnPlayer(218, 68) @Suspendable { spellOnPlayer(it, 1979, -1, 12, 363, 22) } // Ice burst
    repo.onSpellOnPlayer(218, 75) @Suspendable { spellOnPlayer(it, 1978, -1, 12, 387, 23) } // Smoke blitz
    repo.onSpellOnPlayer(218, 79) @Suspendable { spellOnPlayer(it, 1978, -1, 12, 381, 24) } // Shadow blitz
    repo.onSpellOnPlayer(218, 71) @Suspendable { spellOnPlayer(it, 1978, -1, 12, 375, 25) } // Blood blitz
    repo.onSpellOnPlayer(218, 67) @Suspendable { spellOnPlayer(it, 1978, -1, 12, 367, 26) } // Ice blitz
    repo.onSpellOnPlayer(218, 77) @Suspendable { spellOnPlayer(it, 1979, -1, 12, 391, 27) } // Smoke barrage
    repo.onSpellOnPlayer(218, 81) @Suspendable { spellOnPlayer(it, 1979, -1, 12, 383, 28) } // Shadow barrage
    repo.onSpellOnPlayer(218, 73) @Suspendable { spellOnPlayer(it, 1979, -1, 12, 377, 29) } // Blood barrage
    repo.onSpellOnPlayer(218, 69) @Suspendable { spellOnPlayer(it, 1979, 366, 12, 369, 30) } // Ice barrage
}

@Suspendable fun spellOnPlayer(script: Script, animation: Int, projectile: Int, speed: Int, gfx: Int, maxhit: Int) {
    val index: Int = script.player()[AttributeKey.TARGET]
    var target: Player? = script.player().world().players()[index]
    val player: Player = script.player()

    if (target != null)
        player.face(target)

    player.pathQueue().clear()

    while (target != null && !target.dead() && !player.dead()) {
        var tile = player.tile()

        if (!player.inWilderness() || !target.inWilderness())
            break

        if (tile.distance(target.tile()) > 10 && !player.frozen() && !player.stunned()) {
            // Are we in range distance?
            tile = moveCloser(player, target, tile)
        }

        if (tile.distance(target.tile()) <= 10) {
            // Recheck
            if (TimerKey.COMBAT_ATTACK !in player.timers()) {
                doMagicSpell(player, target, tile, animation, projectile, speed, gfx, player.world().random(maxhit))
                break
            }
        }

        script.delay(1)
        target = player.world().players()[index]
        target.putattrib(AttributeKey.LAST_DAMAGER, player)
        target.putattrib(AttributeKey.LAST_DAMAGE, System.currentTimeMillis())
    }
}

@Suspendable fun doMagicSpell(player: Player, target: Entity, tile: Tile, animation: Int, projectile: Int, speed: Int, gfx: Int, hit: Int) {
    val tileDist = tile distance target.tile()
    player.animate(animation)

    if (projectile > 0 && !target.pathQueue().empty())
        player.world().spawnProjectile(player.tile(), target, projectile, 100, 0, 36, speed * tileDist, 50, 80)

    val delay = 1 + Math.floor(tileDist.toDouble()) / 2.0
    player.timers()[TimerKey.COMBAT_ATTACK] = 6
    val success = AccuracyFormula.doesHit(player, target as Player, CombatStyle.MAGIC)

    if (success) {
        target.hit(player, hit, delay.toInt()).graphic(gfx).combatStyle(CombatStyle.MAGIC)

        if (gfx == 369)
            target.freeze(33) // 20 second freeze timer
        else if (gfx == 377 || gfx == 373 || gfx == 376 || gfx == 375)
            player.heal(hit / 4) // Heal for 25% with blood barrage
        else if ((gfx == 379 || gfx == 382) && target.isPlayer())
            target.skills().alterSkill(Skills.ATTACK, -(target.skills().level(Skills.ATTACK) * 0.1).toInt(), false)
        else if (gfx == 361)
            target.freeze(8)
        else if (gfx == 363)
            target.freeze(16)
        else if ((gfx == 381 || gfx == 383) && target.isPlayer())
            target.skills().alterSkill(Skills.ATTACK, -(target.skills().level(Skills.ATTACK) * 0.15).toInt(), false)
        else if (gfx == 367) {
            target.freeze(25) //15 second freeze timer
            player.graphic(366)
        }
    } else {
        target.hit(player, 0, delay.toInt()).graphic(85)
    }
}

@Suspendable class PlayerCombat {
    @Suspendable companion object {
        public val script: Function1<Script, Unit> = @Suspendable {
            val index: Int = it.player()[AttributeKey.TARGET]
            var target: Player? = it.player().world().players()[index]
            val player: Player = it.player()

            if (target != null)
                player.face(target)

            while (target != null && !target.dead() && !player.dead() && canAttack(player, target)) {
                // Begin by clearing the path queue
                player.pathQueue().clear()

                var tile = player.tile() // Grab base move tile

                val weaponId = player.equipment()[EquipSlot.WEAPON]?.id() ?: -1;
                val ammoId = player.equipment()[EquipSlot.AMMO]?.id() ?: -1;
                val weaponType = player.world().equipmentInfo().weaponType(weaponId)
                val ammoName = Item(ammoId).definition(player.world())?.name ?: ""

                if (!player.inWilderness() || !target.inWilderness())
                    break

                // Range, mage or melee?
                if (weaponType == WeaponType.BOW || weaponType == WeaponType.CROSSBOW || weaponType == WeaponType.THROWN) {
                    if (tile.distance(target.tile()) > 7 && !player.frozen() && !player.stunned()) {
                        // Are we in range distance?
                        tile = moveCloser(player, target, tile)
                    }

                    // Can we shoot?
                    if (tile.distance(target.tile()) <= 7 && TimerKey.COMBAT_ATTACK !in player.timers()) {
                        // Do we even have ammo?
                        if (ammoName == "") {
                            player.message("There's no ammo left in your quiver.")
                            break
                        }

                        // Check if we use the right type of ammo first
                        if (weaponType == WeaponType.CROSSBOW && " bolts" !in ammoName) {
                            player.message("You can't use that ammo with your crossbow.")
                            break
                        }
                        if (weaponType == WeaponType.BOW && " arrow" !in ammoName) {
                            player.message("You can't use that ammo with your bow.")
                            break
                        }

                        // Remove the ammo :)
                        val ammo = player.equipment()[EquipSlot.AMMO]
                        player.equipment()[EquipSlot.AMMO] = Item(ammo.id(), ammo.amount() - 1)

                        player.animate(EquipmentInfo.attackAnimationFor(player))
                        val tileDist = tile distance target.tile()
                        var cyclesPerTile = 5
                        var baseDelay = 32
                        var startHeight = 35
                        var endHeight = 36
                        var curve = 15
                        var graphic = 228

                        if (weaponType == WeaponType.CROSSBOW) {
                            cyclesPerTile = 3
                            baseDelay = 40
                            startHeight = 40
                            endHeight = 40
                            curve = 2
                            graphic = 27
                        }

                        if (player.varps()[Varp.SPECIAL_ENABLED] == 0 || !doRangeSpecial(it, player, target, tileDist)) {
                            player.world().spawnProjectile(player.tile(), target, graphic, startHeight, endHeight, baseDelay, cyclesPerTile * tileDist, curve, 105)

                            val delay = Math.round(Math.floor(baseDelay / 30.0) + (tileDist.toDouble() * (cyclesPerTile.toDouble() * 0.020) / 0.6))
                            val success = AccuracyFormula.doesHit(player, target, CombatStyle.RANGE)

                            if (success) {
                                val max = CombatFormula.maximumRangedHit(player)
                                val hit = player.world().random(max)
                                target.hit(player, hit, delay.toInt()).combatStyle(CombatStyle.RANGE)
                            } else {
                                target.hit(player, 0, delay.toInt()).combatStyle(CombatStyle.RANGE)
                            }

                            player.timers()[TimerKey.COMBAT_ATTACK] = player.world().equipmentInfo().weaponSpeed(weaponId)
                        }

                        // After every attack, reset the special mode.
                        player.varps()[Varp.SPECIAL_ENABLED] = 0
                    }
                } else {
                    // Ok seems like we're melee.
                    if (!player.touches(target, tile) && !player.frozen() && !player.stunned()) {
                        // Not touching? Shit son, go close!
                        tile = moveCloser(player, target, tile)
                    }

                    // Do we finally touch the enemy now?
                    if (player.touches(target, tile)) {
                        // Check if we may deal a blow :)
                        if (TimerKey.COMBAT_ATTACK !in player.timers()) {
                            if (player.varps()[Varp.SPECIAL_ENABLED] == 0 || !doSpecial(it, target)) {
                                val success = AccuracyFormula.doesHit(player, target, CombatStyle.MELEE)

                                if (success) {
                                    val max = CombatFormula.maximumMeleeHit(player)
                                    val hit = player.world().random(max)

                                    target.hit(player, hit)
                                } else {
                                    target.hit(player, 0) // Uh-oh, that's a miss.
                                }

                                it.animate(EquipmentInfo.attackAnimationFor(player))
                                player.timers()[TimerKey.COMBAT_ATTACK] = player.world().equipmentInfo().weaponSpeed(weaponId)
                            }

                            // After every attack, reset the special mode.
                            player.varps()[Varp.SPECIAL_ENABLED] = 0
                        }
                    }
                }

                target.putattrib(AttributeKey.LAST_DAMAGER, player)
                target.putattrib(AttributeKey.LAST_DAMAGE, System.currentTimeMillis())

                it.delay(1)
                target = player.world().players()[index]
            }

            // At the end of combat, face nobody. Not even yourself.
            it.delay(1)
            player.face(null)
        }

        @Suspendable fun doRangeSpecial(it: Script, player: Player, target: Player, tileDist: Int): Boolean {
            val weapon = it.player().equipment()[EquipSlot.WEAPON]?.id() ?: -1

            when (weapon) {
                861 -> {
                    if (!takeSpecialEnergy(it.player(), 55))
                        return false

                    player.graphic(256, 90, 0)
                    player.animate(1074)
                    player.world().spawnProjectile(player.tile(), target, 249, 35, 36, 32, 5 * tileDist, 15, 105)
                    player.world().spawnProjectile(player.tile(), target, 249, 35, 36, 42, 5 * tileDist, 15, 105)

                    val max = CombatFormula.maximumRangedHit(player)
                    val hit = player.world().random(max)
                    val hit2 = player.world().random(max)
                    val delay = Math.round(Math.floor(32 / 30.0) + (tileDist.toDouble() * (5 * 0.020) / 0.6))

                    target.hit(player, hit, delay.toInt()).combatStyle(CombatStyle.RANGE)
                    target.hit(player, hit2, delay.toInt()).combatStyle(CombatStyle.RANGE)

                    player.timers()[TimerKey.COMBAT_ATTACK] = 5
                    return true
                }
            }

            return false
        }

        @Suspendable fun doSpecial(it: Script, target: Player): Boolean {
            val weapon = it.player().equipment()[3]

            if (weapon != null) {
                when (weapon.id()) {
                    1305 -> {
                        // Dragon long
                        if (!takeSpecialEnergy(it.player(), 25))
                            return false

                        it.animate(1058)
                        it.player().graphic(248, 92, 0)
                        target.hit(it.player(), it.player().world().random().nextInt(12))
                        it.player().timers()[TimerKey.COMBAT_ATTACK] = 5
                        return true
                    }
                    1215, 5698 -> {
                        // Dragon dagger spec
                        if (!takeSpecialEnergy(it.player(), 25))
                            return false

                        it.animate(1062)
                        it.player().graphic(252, 92, 0)

                        val max = CombatFormula.maximumMeleeHit(it.player()) * 1.1
                        val hit = it.player().world().random().nextInt(max.toInt())
                        val hit2 = it.player().world().random().nextInt(max.toInt())

                        target.hit(it.player(), hit.toInt())
                        target.hit(it.player(), hit2.toInt())
                        it.player().timers()[TimerKey.COMBAT_ATTACK] = 5
                        return true
                    }
                    11802 -> {
                        // AGS
                        if (!takeSpecialEnergy(it.player(), 50))
                            return false

                        it.animate(7061)
                        it.player().graphic(1211)

                        target.blockHit()
                        val max = (CombatFormula.maximumMeleeHit(it.player()) * 1.25).toInt()
                        target.hit(it.player(), it.player().world().random().nextInt(max), 0).block(false)

                        it.player().timers()[TimerKey.COMBAT_ATTACK] = 5
                        return true
                    }
                    11804 -> {
                        // BGS
                        if (!takeSpecialEnergy(it.player(), 65))
                            return false

                        it.animate(7060)
                        it.player().graphic(1212)

                        // TODO drain opponent stats
                        target.blockHit()
                        val max = (CombatFormula.maximumMeleeHit(it.player()) * 1.21).toInt()
                        target.hit(it.player(), it.player().world().random().nextInt(max), 1).block(false)

                        it.player().timers()[TimerKey.COMBAT_ATTACK] = 5
                        return true
                    }
                    11806 -> {
                        // SGS
                        if (!takeSpecialEnergy(it.player(), 50))
                            return false

                        it.animate(7058)
                        it.player().graphic(1209)

                        // TODO drain opponent stats
                        val max = (CombatFormula.maximumMeleeHit(it.player()) * 1.10).toInt()
                        val hit = it.player().world().random().nextInt(max)

                        // Heal the player (TODO restore prayer) and hit the enemy
                        target.blockHit()
                        it.player().heal(Math.max(10, hit / 2)) // Min heal is 10
                        target.hit(it.player(), hit, 1).block(false)

                        it.player().timers()[TimerKey.COMBAT_ATTACK] = 5
                        return true
                    }
                    11808 -> {
                        // ZGS
                        if (!takeSpecialEnergy(it.player(), 60))
                            return false

                        it.animate(7057)
                        it.player().graphic(1210)

                        // TODO drain opponent stats
                        val max = (CombatFormula.maximumMeleeHit(it.player()) * 1.10).toInt()
                        val hit = it.player().world().random().nextInt(max)

                        target.blockHit()
                        target.hit(it.player(), hit, 1).block(false) // Manual block animation
                        target.graphic(369)
                        target.freeze(33) // 20 second freeze timer

                        it.player().timers()[TimerKey.COMBAT_ATTACK] = 5
                        return true
                    }
                    4587 -> {
                        if (!takeSpecialEnergy(it.player(), 55))
                            return false
                        it.animate(1872)
                        it.player().graphic(347, 100, 0)

                        val max = (CombatFormula.maximumMeleeHit(it.player())).toInt()
                        target.hit(it.player(), it.player().world().random().nextInt(max), 1).block(false)

                        it.player().timers()[TimerKey.COMBAT_ATTACK] = 5
                        return true;
                    }
                    1249 -> {
                        if (!takeSpecialEnergy(it.player(), 25))
                            return false
                        target.stun(5)
                        it.animate(1064)
                        it.player().graphic(253, 100, 0)
                        target.graphic(254, 100, 0)

                        it.player().timers()[TimerKey.COMBAT_ATTACK] = 5
                        return true;
                    }
                }
            }

            return false
        }
    }
}

fun moveCloser(player: Player, target: Player, tile: Tile): Tile {
    val steps = if (player.pathQueue().running()) 2 else 1
    val otherSteps = if (target.pathQueue().running()) 2 else 1

    val otherTile = target.pathQueue().peekAfter(otherSteps)?.toTile() ?: target.tile()
    player.stepTowards(target, otherTile, 25)
    return player.pathQueue().peekAfter(steps - 1)?.toTile() ?: tile
}

fun takeSpecialEnergy(player: Player, num: Int): Boolean {
    if (player.varps()[Varp.SPECIAL_ENERGY] < num * 10) {
        player.message("You don't have enough power left.")
        return false
    }
    player.varps()[Varp.SPECIAL_ENERGY] = player.varps()[Varp.SPECIAL_ENERGY] - num * 10
    return true
}


fun canAttack(player: Player, other: Player): Boolean {
    val targetLastTime = System.currentTimeMillis() - other.attrib<Long>(AttributeKey.LAST_DAMAGE, 0.toLong())
    val targetLastAttacker = other.attrib<Player>(AttributeKey.LAST_DAMAGER)

    val myLastTime = System.currentTimeMillis() - player.attrib<Long>(AttributeKey.LAST_DAMAGE, 0.toLong())
    val myLastAttacker = player.attrib<Player>(AttributeKey.LAST_DAMAGER)

    if (myLastTime < 10000 && myLastAttacker != null && myLastAttacker !== other) {
        player.message("You are already fighting someone else.")
        return false
    }

    if (targetLastTime < 10000 && targetLastAttacker != null && targetLastAttacker !== player) {
        player.message("That player is already in combat.")
        return false
    }

    return true
}