package org.playuniverse.collider.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.playuniverse.collider.config.ColliderConfig;

import net.md_5.bungee.api.ChatColor;

public class BlockColliderCommand implements CommandExecutor {

	private final ColliderConfig config;

	public BlockColliderCommand(ColliderConfig config) {
		this.config = config;
	}

	@Override
	public boolean onCommand(CommandSender send, Command cmd, String arg, String[] args) {
		send.sendMessage(color("&8[&aCollider&8] &7Reloading config..."));
		config.reload();
		send.sendMessage(color("&8[&aCollider&8] &7Reloaded config &asuccessfully&7!"));
		return false;
	}

	private String color(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}

}
