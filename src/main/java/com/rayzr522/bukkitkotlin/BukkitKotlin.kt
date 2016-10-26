package com.rayzr522.bukkitkotlin

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerGameModeChangeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.plugin.java.JavaPlugin

class BukkitKotlin : JavaPlugin(), Listener {

    fun Location.isSolidBelow(): Boolean {
        return this.block.getRelative(BlockFace.DOWN).type.isSolid
    }

    private var  warpManager: WarpManager

    init {
        warpManager = WarpManager(this)
    }

    override fun onEnable() {

        server.pluginManager.registerEvents(this, this)
        server.pluginManager.registerEvents(PlayerData, this)

        getCommand("speed").executor = CommandFly()
        getCommand("doublejump").executor = CommandDoubleJump()

        warpManager.enable()

        server.onlinePlayers.forEach { PlayerData(it.uniqueId, it.name) }

        logger.info("BukkitKotlin has been enabled! Woot woot!")

        loadConfig()

    }

    private fun loadConfig() {
        var section: ConfigurationSection? = config.getConfigurationSection("players") ?: return
        section!!.getKeys(false).forEach { PlayerData.load(it, section.getConfigurationSection(it)) }

        warpManager.load()
    }

    override fun onDisable() {

        var section = config.createSection("players")
        PlayerData.players.forEach { section.createSection(it.key.toString(), it.value.serialize()) }
        saveConfig()

        warpManager.save()

        logger.info("BukkitKotlin has been disabled! Goodbye ^w^")

    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        e.joinMessage = "Hello, ${e.player.name}"
        e.player.allowFlight = true
    }

    @EventHandler
    fun onGamemodeChange(e: PlayerGameModeChangeEvent) {
        e.player.allowFlight = true
    }

    @EventHandler
    fun onStartFlying(e: PlayerToggleFlightEvent) {
        if (e.player.gameMode == GameMode.CREATIVE || !PlayerData[e.player].doubleJump) return
        e.isCancelled = true
        e.player.isFlying = false
        e.player.allowFlight = false
        e.player.velocity = e.player.eyeLocation.direction.multiply(1.5)
    }

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        if (e.from == e.to || !PlayerData[e.player].doubleJump || e.player.allowFlight) return
        if (e.from.isSolidBelow()) {
            e.player.allowFlight = true
        }
    }

    @EventHandler
    fun onDamage(e: EntityDamageByEntityEvent) {
        if (e.entityType != EntityType.PLAYER || e.cause != EntityDamageEvent.DamageCause.FALL) return
        e.isCancelled = true
        e.damage = 0.0
    }

}
