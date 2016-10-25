package com.rayzr522.bukkitkotlin

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

/**
 * Created by Rayzr522 on 10/25/16.
 */

class PlayerData(val id: UUID, val name: String) {

    companion object : Listener {
        val players = HashMap<UUID, PlayerData>()

        //allows us to do KotlinPlayerData[id]
        operator fun get(id: UUID?) = players[id]

        operator fun get(p: Player) = PlayerData[p.uniqueId]!!

        @EventHandler
        fun onJoin(e: PlayerJoinEvent) = get(e.player.uniqueId) ?: PlayerData(e.player.uniqueId, e.player.name)

        @EventHandler
        fun onQuit(e: PlayerQuitEvent) = players.remove(e.player.uniqueId)

        fun load(id: String?, section: ConfigurationSection?) {
            var data = PlayerData(UUID.fromString(id), section?.getString("name") ?: return)
            data.doubleJump = section?.getBoolean("doubleJump") ?: return
        }
    }

    init {
        players[id] = this
    }

    fun serialize(): MutableMap<String, Any> {
        var data: MutableMap<String, Any> = HashMap()
        data.put("name", name)
        data.put("doubleJump", doubleJump)
        return data
    }

    var doubleJump = false

}
