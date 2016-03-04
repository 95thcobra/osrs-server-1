package plugin

import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.plugin.PluginListener
import nl.bartpelle.veteres.plugin.PluginSignature
import nl.bartpelle.veteres.plugin.impl.LoginPlugin

@PluginSignature(LoginPlugin.class)
final class Login implements PluginListener<LoginPlugin> {

    @Override
    void execute(Player player, LoginPlugin plugin) {
        player.message("Welcome to Guthix!")
        player.message("This is sent by Login.groovy.")

    }
}