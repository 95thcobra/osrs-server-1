package plugin.objects

import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.plugin.PluginListener
import nl.bartpelle.veteres.plugin.PluginSignature
import nl.bartpelle.veteres.plugin.impl.ObjectSecondClickPlugin

@PluginSignature(ObjectSecondClickPlugin.class)
final class ObjectSecondClick implements PluginListener<ObjectSecondClickPlugin> {

    @Override
    void execute(Player player, ObjectSecondClickPlugin context) {
    }
}
