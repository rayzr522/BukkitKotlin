package com.rayzr522.bukkitkotlin

import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

/**
 * Created by Rayzr522 on 10/25/16.
 */


class WarpManager(val plugin: BukkitKotlin) {

    private var warps: HashMap<String, Warp> = HashMap()

    private fun getFile(path: String): File {
        return File(plugin.dataFolder, path.replace("/", File.pathSeparator))
    }

    fun enable() {
        var config = getFile("warps.yml")
        if (!config.exists()) {
            config.createNewFile()
        }
    }

    fun load() {
        warps.clear()

        var config = getConfig()
        var section = config.getConfigurationSection("warps") ?: return
        section.getKeys(false).forEach {
            var name = it
            var pos = section.get("pos") ?: throw IllegalArgumentException("`pos` must exist in warp!")
            warps.put(name, Warp(name, pos as Location))
        }
    }

    fun save() {
    }

    fun getConfig(): YamlConfiguration {
        return YamlConfiguration.loadConfiguration(getFile("warps.yml"))
    }

}

data class Warp(val name: String, val pos: Location)