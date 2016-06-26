package nl.bartpelle.veteres.aquickaccess.combat;

import nl.bartpelle.veteres.event.Event;
import nl.bartpelle.veteres.event.EventContainer;
import nl.bartpelle.veteres.model.AttributeKey;
import nl.bartpelle.veteres.model.Hit;
import nl.bartpelle.veteres.model.HitOrigin;
import nl.bartpelle.veteres.model.Tile;
import nl.bartpelle.veteres.model.entity.Player;
import nl.bartpelle.veteres.model.entity.player.EquipSlot;
import nl.bartpelle.veteres.model.entity.player.WeaponType;
import nl.bartpelle.veteres.model.item.Item;
import nl.bartpelle.veteres.script.TimerKey;
import nl.bartpelle.veteres.util.*;

/**
 * Created by Sky on 25-6-2016.
 */
public class PlayerCombatNew {
    private Player player;
    private Player target;

    public PlayerCombatNew(Player player, Player other) {
        this.player = player;
        this.target = other;
    }

    public void attackPlayer() {
        player.world().getEventHandler().addEvent(player, new Event() {
            @Override
            public void execute(EventContainer container) {
                cycle();
            }

            @Override
            public void stop() {
                player.face(null);
            }
        });
    }

    public void cycle() {
        player.message("attacking " + target.name());
        target.message("getting attacked by " + player.name());

        player.face(target);

        // player.pathQueue().clear();
        Tile currentTile = player.tile();


        // Get weapon data.
        Item weapon = player.equipment().get(EquipSlot.WEAPON);
        int weaponId = weapon == null ? -1 : weapon.id();
        int weaponType = player.world().equipmentInfo().weaponType(weaponId);
        Item ammo = player.equipment().get(EquipSlot.AMMO);
        int ammoId = ammo == null ? -1 : ammo.id();
        String ammoName = ammo == null ? "" : ammo.definition(player.world()).name;

        // Check if players are in wilderness.
        if (!player.inWilderness() || !target.inWilderness()) {
            // stop();
            return;
        }

        // Combat type?
        if (weaponType == WeaponType.BOW || weaponType == WeaponType.CROSSBOW || weaponType == WeaponType.THROWN) {
            player.message("ranging...");
            handleRangeCombat(weaponId, ammoName, currentTile, weaponType);
        } else {
            player.message("meleeeing...");
            handleMeleeCombat(currentTile, weaponId);
        }

        target.putattrib(AttributeKey.LAST_DAMAGER, player);
        target.putattrib(AttributeKey.LAST_DAMAGE, System.currentTimeMillis());
    }

    private void handleMeleeCombat(Tile currentTile, int weaponId) {
        if (!player.touches(target, currentTile) && !player.frozen() && !player.stunned()) {
            currentTile = moveCloser();
            player.message("Moving closer...");
        } else {
            player.message("Attacking...");
            if (player.timers().has(TimerKey.COMBAT_ATTACK)) {
                if (player.varps().varp(Varp.SPECIAL_ENABLED) == 0 || !doMeleeSpecial()) {
                    boolean success = AccuracyFormula.doesHit(player, target, CombatStyle.MELEE);

                    int max = CombatFormula.maximumMeleeHit(player);
                    int hit = player.world().random(max);

                    target.hit(player, success ? hit : 0);

                    player.animate(EquipmentInfo.attackAnimationFor(player));
                    player.timers().register(TimerKey.COMBAT_ATTACK, player.world().equipmentInfo().weaponSpeed(weaponId));


                }
                player.varps().varp(Varp.SPECIAL_ENABLED, 0);
            }
        }
    }

    private void handleRangeCombat(int weaponId, String ammoName, Tile currentTile, int weaponType) {
        // Are we in range?
        if (currentTile.distance(target.tile()) > 7 && !player.frozen() && !player.stunned()) {
            currentTile = moveCloser();
        }

        // Can we shoot?
        if (currentTile.distance(target.tile()) <= 7 && !player.timers().has(TimerKey.COMBAT_ATTACK)) {

            // Do we have ammo?
            if (ammoName.equals("")) {
                player.message("There's no ammo left in your quiver.");
                //container.stop();
                return;
            }

            // Check if ammo is of right type
            if (weaponType == WeaponType.CROSSBOW && !ammoName.contains(" bolts")) {
                player.message("You can't use that ammo with your crossbow.");
                //container.stop();
                return;
            }
            if (weaponType == WeaponType.BOW && !ammoName.contains(" arrow")) {
                player.message("You can't use that ammo with your bow.");
                //container.stop();
                return;
            }

            // Remove the ammo
            Item ammo = player.equipment().get(EquipSlot.AMMO);
            player.equipment().set(EquipSlot.AMMO, new Item(ammo.id(), ammo.amount() - 1));

            player.animate(EquipmentInfo.attackAnimationFor(player));
            int distance = player.tile().distance(target.tile());
            int cyclesPerTile = 5;
            int baseDelay = 32;
            int startHeight = 35;
            int endHeight = 36;
            int curve = 15;
            int graphic = 228;

            if (weaponType == WeaponType.CROSSBOW) {
                cyclesPerTile = 3;
                baseDelay = 40;
                startHeight = 40;
                endHeight = 40;
                curve = 2;
                graphic = 27;
            }

            if (player.varps().varp(Varp.SPECIAL_ENABLED) == 0 || !doRangeSpecial()) {
                player.world().spawnProjectile(player.tile(), target, graphic, startHeight, endHeight, baseDelay, cyclesPerTile * distance, curve, 105);

                long delay = Math.round(Math.floor(baseDelay / 30.0) + (distance * (cyclesPerTile * 0.020) / 0.6));
                boolean success = AccuracyFormula.doesHit(player, target, CombatStyle.RANGE);

                int maxHit = CombatFormula.maximumRangedHit(player);
                int hit = player.world().random(maxHit);

                // target.hit(player, success ? hit : 0, delay).combatStyle(CombatStyle.RANGE);

                target.hit(player, success ? hit : 0, (int) delay).combatStyle(CombatStyle.RANGE);

                // Timer is downtime.
                player.timers().register(TimerKey.COMBAT_ATTACK, player.world().equipmentInfo().weaponSpeed(weaponId));

                // After every attack, reset special.
                player.varps().varp(Varp.SPECIAL_ENABLED, 0);
            }
        }
    }

    private Tile moveCloser() {
        int steps = player.pathQueue().running() ? 2 : 1;
        int otherSteps = target.pathQueue().running() ? 2 : 1;
        Tile otherTile = target.pathQueue().peekAfter(otherSteps).toTile();
        player.stepTowards(target, otherTile, 25);
        return player.pathQueue().peekAfter(steps - 1).toTile();
    }

    private boolean doRangeSpecial() {
        int weaponId = player.equipment().get(EquipSlot.WEAPON).id();

        switch (weaponId) {
            case 861:

                break;
        }

        return false;//TODO
    }

    private boolean doMeleeSpecial() {
        return false;
    }
}
