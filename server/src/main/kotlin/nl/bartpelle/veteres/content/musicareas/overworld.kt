package nl.bartpelle.veteres.content.musicareas

import nl.bartpelle.veteres.content.interfaces.varpForIndex
import nl.bartpelle.veteres.content.*
import nl.bartpelle.veteres.content.interfaces.resolveId
import nl.bartpelle.veteres.fs.EnumDefinition
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.net.message.game.PlayMusic
import nl.bartpelle.veteres.script.ScriptRepository
import nl.bartpelle.skript.ScriptMain

/**
 * Created by Bart on 8/11/2015.
 */

/*fun unlockAndPlay(script: Script, slot: Int) {
    val player = script.player()
    val def = player.world().definitions().get(javaClass<EnumDefinition>(), 812)
    val bitdef = player.world().definitions().get(javaClass<EnumDefinition>(), 819)
    val index = bitdef.getInt(slot) shr 14
    val bit = bitdef.getInt(slot) and 16383
    val varpid = varpForIndex(index)
    val name = def.getString(slot)

    if (varpid != -1 && (player.varps()[varpid] and (1 shl bit) == 0)) {
        player.message("You have unlocked a new music track: %s.".red(), name)

        var current = player.varps()[varpid]
        current = current or (1 shl bit)
        player.varps()[varpid] = current
    }

    val id = resolveId(player.world().server().store(), name)
    player.write(PlayMusic(id))
}

@ScriptMain fun script(repo: ScriptRepository) {
    repo.onRegionEnter(12849) {unlockAndPlay(it, 177)} // Southern Lumbridge: Yesteryear
    repo.onRegionEnter(12850) {unlockAndPlay(it, 6)} // Lumbridge castle: Harmony
    repo.onRegionEnter(12851) {unlockAndPlay(it, 9)} // Norhtern lumbridge: Autumn Voyage
    repo.onRegionEnter(12593) {unlockAndPlay(it, 16)} // Lumbridge swamp: Book of Spells
    repo.onRegionEnter(12337) {unlockAndPlay(it, 39)} // Wizard's Tower: Vision
    repo.onRegionEnter(12338) {unlockAndPlay(it, 38)} // Draynor Village: Unknown Land
    repo.onRegionEnter(12595) {unlockAndPlay(it, 14)} // North-west Lumbridge: Flute Salad
    repo.onRegionEnter(12594) {unlockAndPlay(it, 15)} // Lumbridge-Draynor: Dream
    repo.onRegionEnter(12339) {unlockAndPlay(it, 37)} // Northern Draynor: Start
    repo.onRegionEnter(12340) {unlockAndPlay(it, 36)} // Draynor Manor: Spooky
    repo.onRegionEnter(12341) {unlockAndPlay(it, 35)} // Barbarian village: Barbarianism
    repo.onRegionEnter(12596) {unlockAndPlay(it, 20)} // River Lum: Greatness
    repo.onRegionEnter(12852) {unlockAndPlay(it, 26)} // South Varrock entrance: Expanse
    repo.onRegionEnter(12853) {unlockAndPlay(it, 25)} // Varrock Square: Garden
    repo.onRegionEnter(12597) {unlockAndPlay(it, 18)} // Varrock West: Spirit
    repo.onRegionEnter(11828) {unlockAndPlay(it, 46)} // Falador Center: Fanfare
    repo.onRegionEnter(12084) {unlockAndPlay(it, 163)} // Falador East: Workshop
    repo.onRegionEnter(11827) {unlockAndPlay(it, 52)} // South of Falador: Nightfall
    repo.onRegionEnter(11829) {unlockAndPlay(it, 175)} // Outside the Falador entrance: Scape Soft
    repo.onRegionEnter(11572) {unlockAndPlay(it, 45)} // West of Falador: Arrival
    repo.onRegionEnter(11571) {unlockAndPlay(it, 47)} // Crafting guild: Miles Away
    repo.onRegionEnter(12083) {unlockAndPlay(it, 49)} // North of port sarim: Wander
    repo.onRegionEnter(13107) {unlockAndPlay(it, 31)} // Al Kharid mine: Arabian 2
    repo.onRegionEnter(13106) {unlockAndPlay(it, 32)} // North Al Kharid: Arabian
    repo.onRegionEnter(13105) {unlockAndPlay(it, 33)} // Al Kharid center: Al Kharid
    repo.onRegionEnter(12082) {unlockAndPlay(it, 40)} // Northern Port Sarim: Sea SHanty 2
    repo.onRegionEnter(12081) {unlockAndPlay(it, 418)} // Southern Port Sarim: Tomorrow
    repo.onRegionEnter(11826) {unlockAndPlay(it, 148)} // Rimmington mines: Long Way Home
    repo.onRegionEnter(11825) {unlockAndPlay(it, 112)} // Southern Rimmington: Attention
    repo.onRegionEnter(11570) {unlockAndPlay(it, 115)} // West Rimmington: Emperor
    repo.onRegionEnter(13617) {unlockAndPlay(it, 32)} // East of clan wars: Arabian
    repo.onRegionEnter(13362) {unlockAndPlay(it, 179)} // Duel arena: Duel Arena
    repo.onRegionEnter(13363) {unlockAndPlay(it, 181)} // Mage training arena: Shine
    repo.onRegionEnter(11573) {unlockAndPlay(it, 96)} // South Taverley: Horizon
    repo.onRegionEnter(11574) {unlockAndPlay(it, 55)} // North Taverley: Splendour
    repo.onRegionEnter(11575) {unlockAndPlay(it, 193)} // Burthorpe: Principality
    repo.onRegionEnter(11831) {unlockAndPlay(it, 129)} // Westmost part of the wilderness ditch: Wonder
    repo.onRegionEnter(12086) {unlockAndPlay(it, 50)} // Edgeville monastery: Alone
    repo.onRegionEnter(12087) {unlockAndPlay(it, 107)} // Western part of the wilderness ditch: Inspiration
    repo.onRegionEnter(12343) {unlockAndPlay(it, 114)} // Western part of the wilderness ditch: Dangerous
    repo.onRegionEnter(12599) {unlockAndPlay(it, 323)} // North of edgeville: Lightness
    repo.onRegionEnter(12855) {unlockAndPlay(it, 126)} // North of varrock palace: Crystal Sword
    repo.onRegionEnter(12600) {unlockAndPlay(it, 102)} // Wilderness: Moody
    repo.onRegionEnter(12344) {unlockAndPlay(it, 109)} // Wilderness: Wildwood
    repo.onRegionEnter(12088) {unlockAndPlay(it, 108)} // Wilderness: Army of Darkness
    repo.onRegionEnter(8751)  {unlockAndPlay(it, 500)} // Zul-Andra: Thrall of the Serpent
    repo.onRegionEnter(9007)  {unlockAndPlay(it, 499)} // Zulrah: Coil
}*/