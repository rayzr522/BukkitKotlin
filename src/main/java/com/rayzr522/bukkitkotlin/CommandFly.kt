package com.rayzr522.bukkitkotlin

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandFly : CommandExecutor {

    val usage = "${ChatColor.RED}Usage: /speed <fly|walk> <0-100>"

    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        if (sender !is Player) {
            sender?.sendMessage("${ChatColor.RED}Only players can use this command!")
            return true
        }

        var player: Player = sender
        if (args?.size!! < 2) {
            player.sendMessage(usage)
            return true
        }

        var amount: Int

        try {
            println("Args: " + args!![0] + ", " + args!![1])
            amount = Integer.parseInt(args!![1])
        } catch (ex: Exception) {
            amount = -1
        }

        if (amount < 0 || amount > 100) {
            player.sendMessage(usage)
            return true
        }

        if (args!![0] == "fly") {
            player.flySpeed = amount / 100f
            player.sendMessage("${ChatColor.GREEN}Fly speed is now ${ChatColor.AQUA}$amount")
        } else if (args!![0] == "walk") {
            player.walkSpeed = amount / 100f
            player.sendMessage("${ChatColor.GREEN}Walk speed is now ${ChatColor.AQUA}$amount")
        } else {
            player.sendMessage(usage)
        }

        return true
    }

}