package nl.bartpelle.veteres.aquickaccess.combat;

import nl.bartpelle.veteres.event.Event;
import nl.bartpelle.veteres.event.EventContainer;
import nl.bartpelle.veteres.model.Entity;
import nl.bartpelle.veteres.model.Tile;
import nl.bartpelle.veteres.model.entity.Player;
import nl.bartpelle.veteres.script.TimerKey;
import nl.bartpelle.veteres.util.AccuracyFormula;
import nl.bartpelle.veteres.util.CombatFormula;
import nl.bartpelle.veteres.util.CombatStyle;
import nl.bartpelle.veteres.util.EquipmentInfo;

/**
 * Created by Sky on 27-6-2016.
 */
public abstract class Combat {

    private Entity entity;
    private Entity target;

    public Combat(Entity entity, Entity target) {
        this.entity = entity;
        this.target = target;
    }

    public Entity getEntity() {
        return entity;
    }

    public Entity getTarget() {
        return target;
    }

    public void start() {
        entity.world().getEventHandler().addEvent(entity, new Event() {
            @Override
            public void execute(EventContainer container) {
                if (entity.dead() || entity.locked() || target.dead() || target.locked()) {
                    container.stop();
                    return;
                }
                cycle();
            }
        });
    }

    public abstract void cycle();


    public Tile moveCloser() {
        entity.pathQueue().clear();

        int steps = entity.pathQueue().running() ? 2 : 1;
        int otherSteps = target.pathQueue().running() ? 2 : 1;

        Tile otherTile = target.pathQueue().peekAfter(otherSteps) == null ? target.tile() : target.pathQueue().peekAfter(otherSteps).toTile();
        entity.stepTowards(target, otherTile, 25);
        return entity.pathQueue().peekAfter(steps - 1) == null ? entity.tile() : entity.pathQueue().peekAfter(steps - 1).toTile();
    }

    public void handleMeleeCombat(int weaponId) {
        Tile currentTile = getEntity().tile();

        // Move closer if out of range.
        if (!entity.touches(getTarget(), currentTile) && !getEntity().frozen() && !getEntity().stunned()) {
            currentTile = moveCloser();
            return;
        }

        // Check timer.
        if (!getEntity().timers().has(TimerKey.COMBAT_ATTACK)) {

            if (entity instanceof Player && ((Player) entity).isSpecialAttackEnabled()) {
                doMeleeSpecial((Player) entity, weaponId);
                return;
            }

            boolean success = AccuracyFormula.doesHit(((Player) getEntity()), getTarget(), CombatStyle.MELEE);

            int max = CombatFormula.maximumMeleeHit(((Player) getEntity()));
            int hit = getEntity().world().random(max);

            getTarget().hit(getEntity(), success ? hit : 0);

            getEntity().animate(EquipmentInfo.attackAnimationFor(((Player) getEntity())));
            getEntity().timers().register(TimerKey.COMBAT_ATTACK, getEntity().world().equipmentInfo().weaponSpeed(weaponId));
        }
    }

    private void doMeleeSpecial(Player player, int weaponId) {
        switch (weaponId) {

            // dds
            case 5698:
                player.timers().register(TimerKey.COMBAT_ATTACK, 5);

                if (!drainSpecialEnergy(player, 25)) {
                    return;
                }

                player.animate(1062);
                player.graphic(252, 92, 0);

                double max = CombatFormula.maximumMeleeHit(player) * 1.1;
                int hit = player.world().random().nextInt((int)Math.round(max));
                int hit2 = player.world().random().nextInt((int)Math.round(max));

                target.hit(player, hit);
                target.hit(player, hit2);
                break;
        }


    }

    private boolean drainSpecialEnergy(Player player, int amount) {
        player.toggleSpecialAttack();
        if (player.getSpecialEnergyAmount() < amount * 10) {
            player.message("You do not have enough special energy.");
            return false;
        }
        player.setSpecialEnergyAmount(player.getSpecialEnergyAmount() - (amount * 10));
        return true;
    }
}
