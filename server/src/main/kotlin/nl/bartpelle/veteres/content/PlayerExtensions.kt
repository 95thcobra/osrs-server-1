package nl.bartpelle.veteres.content

import co.paralleluniverse.fibers.Fiber
import co.paralleluniverse.fibers.Suspendable
import nl.bartpelle.skript.Script
import nl.bartpelle.skript.WaitReason
import nl.bartpelle.veteres.fs.NpcDefinition
import nl.bartpelle.veteres.model.AttributeKey
import nl.bartpelle.veteres.model.Entity
import nl.bartpelle.veteres.model.Tile
import nl.bartpelle.veteres.model.entity.Npc
import nl.bartpelle.veteres.model.entity.Player
import nl.bartpelle.veteres.model.entity.player.Skills
import nl.bartpelle.veteres.model.entity.player.Varps
import nl.bartpelle.veteres.model.item.Item
import nl.bartpelle.veteres.model.item.ItemContainer
import nl.bartpelle.veteres.net.message.game.*
import nl.bartpelle.veteres.script.TimerKey
import nl.bartpelle.veteres.script.TimerRepository

/**
 * Created by Bart on 7/9/2015.
 */
val dialogueInterruptCall = { s: Script ->
    s.player().interfaces().close(162, 546)
}

@Suspendable fun Script.message(msg: String) {
    this.ctx<Player>().message(msg)
}

@Suspendable fun Script.message(msg: String, vararg args: Any) {
    this.ctx<Player>().message(msg, *args)
}

@Suspendable fun Script.chatPlayer(msg: String, anim: Int = 588) {
    player().interfaces().send(217, 162, 546, false)
    player().write(InterfaceText(217, 1, player().name()))
    player().write(InterfaceText(217, 2, "Click here to continue"))
    player().write(InterfaceText(217, 3, msg))
    player().write(PlayerOnInterface(217, 0))
    player().write(AnimateInterface(217, 0, anim))
    player().write(InvokeScript(600, 1, 1, 16, 14221315))
    player().write(InterfaceSettings(217, 2, -1, -1, 1))

    waitReason = WaitReason.DIALOGUE
    interruptCall = dialogueInterruptCall
    Fiber.park()

    player().interfaces().close(162, 546)
}

@Suspendable fun Script.chatNpc(msg: String, npc: Int, anim: Int = 588) {
    val def: NpcDefinition = player().world().definitions().get(javaClass<NpcDefinition>(), npc)

    player().interfaces().send(231, 162, 546, false)
    player().write(InterfaceText(231, 1, def.name))
    player().write(InterfaceText(231, 2, "Click here to continue"))
    player().write(InterfaceText(231, 3, msg))
    player().write(NpcOnInterface(231, 0, npc))
    player().write(AnimateInterface(231, 0, anim))
    player().write(InvokeScript(600, 1, 1, 16, 15138819))
    player().write(InterfaceSettings(231, 2, -1, -1, 1))

    waitReason = WaitReason.DIALOGUE
    interruptCall = dialogueInterruptCall
    Fiber.park()

    player().interfaces().close(162, 546)
}

@Suspendable fun Script.options(vararg options: String, title: String = "Select an Option"): Int {
    player().interfaces().send(219, 162, 546, false)
    player().write(InvokeScript(58, title, options.joinToString("|")))
    player().write(InterfaceSettings(219, 0, 1, 5, 1))

    waitReason = WaitReason.DIALOGUE
    interruptCall = dialogueInterruptCall
    Fiber.park()

    player().interfaces().close(162, 546)
    return waitReturnVal as Int
}

@Suspendable fun Script.messagebox(message: String) {
    player().interfaces().send(229, 162, 546, false)
    player().write(InterfaceText(229, 0, message))
    player().write(InterfaceText(229, 1, "Click here to continue"))
    player().write(InvokeScript(600, 1, 1, 31, 15007744))
    player().write(InterfaceSettings(229, 1, -1, -1, 1))

    waitReason = WaitReason.DIALOGUE
    interruptCall = dialogueInterruptCall
    Fiber.park()

    player().interfaces().close(162, 546)
}

@Suspendable fun Script.animate(anim: Int) {
    this.ctx<Player>().animate(anim)
}

@Suspendable fun Script.openInterface(id: Int) {
    this.ctx<Player>().interfaces().sendMain(id, false);
}

@Suspendable fun Script.openRoot(id: Int) {
    this.ctx<Player>().interfaces().sendRoot(id);
}

@Suspendable fun Script.player(): Player {
    return this.ctx<Player>();
}

@Suspendable fun Script.npc(): Npc {
    return this.ctx<Npc>();
}

@Suspendable fun Script.waitForTimer(name: TimerKey) {
    val player = player()
    while (player.timers().has(name))
        delay(1)
}

@Suspendable fun Script.waitForTile(tile: Tile) {
    val player = player()
    while (player.tile().x != tile.x || player.tile().z != tile.z)
        delay(1)
}

@Suspendable fun Script.waitForInterfaceClose(id: Int) {
    val player = player()
    while (player.interfaces().visible(id))
        delay(1)
}

fun ItemContainer.contains(id: Int): Boolean {
    return has(id)
}

fun ItemContainer.contains(item: Item?): Boolean {
    return item != null && has(item.id())
}

fun Varps.get(i: Int): Int {
    return varp(i)
}

fun Varps.set(i: Int, j: Int) {
    varp(i, j)
}

fun TimerRepository.set(key: TimerKey, v: Int) {
    register(key, v)
}

fun TimerRepository.contains(key: TimerKey): Boolean {
    return has(key)
}

fun <T> Entity.get(i: AttributeKey): T {
    return attrib(i)
}

fun ItemContainer.minusAssign(item: Item) {
    remove(item, true)
}

fun ItemContainer.minusAssign(item: Int) {
    remove(Item(item), true)
}

fun ItemContainer.plusAssign(item: Item) {
    add(item, true)
}

fun ItemContainer.plusAssign(item: Int) {
    add(Item(item), true)
}

fun Skills.get(i: Int): Int {
    return level(i)
}

fun String.red(): String {
    return "<col=ff0000>".concat(this);
}

fun String.col(col: String): String {
    return StringBuilder("<col=").append(col).append(">").append(this).toString()
}