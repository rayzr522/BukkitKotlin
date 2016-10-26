package com.rayzr522.bukkitkotlin

import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created by Rayzr522 on 10/25/16.
 */

class CommandDoubleJump : CommandExecutor {

    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender?.sendMessage("${ChatColor.RED}Only players can use this command!")
            return true
        }

        var dblJmp = !PlayerData[sender].doubleJump
        PlayerData[sender].doubleJump = dblJmp

        if (sender?.gameMode != GameMode.CREATIVE) {
            sender?.allowFlight = dblJmp
        }

        sender?.sendMessage("${ChatColor.GREEN}Double jumping is now ${ChatColor.AQUA}${if (dblJmp) "enabled" else "disabled"}")

        return true
    }

}