package nl.bartpelle.veteres.aquickaccess.combat;

import nl.bartpelle.veteres.model.AttributeKey;
import nl.bartpelle.veteres.model.Entity;

/**
 * Created by Sky on 27-6-2016.
 */
public class NpcCombat extends Combat {
    public NpcCombat(Entity entity, Entity target) {
        super(entity, target);
    }

    @Override
    public void cycle() {
        getTarget().putattrib(AttributeKey.LAST_DAMAGER, getEntity());
        getTarget().putattrib(AttributeKey.LAST_DAMAGE, System.currentTimeMillis());
    }
}
