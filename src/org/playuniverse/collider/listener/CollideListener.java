package org.playuniverse.collider.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.playuniverse.collider.Collider;
import org.playuniverse.collider.config.ColliderConfig;
import org.playuniverse.collider.event.PlayerCollideBlockEvent;

public class CollideListener extends Collider {

	public CollideListener(ColliderConfig config) {
		super(config);
	}

	private final ArrayList<UUID> blacklisted = new ArrayList<>();

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if(!isEnabled()) {
			return;
		}
		Player player = event.getPlayer();
		if (blacklisted.contains(player.getUniqueId())) {
			return;
		}
		Vector direction = player.getEyeLocation().getDirection();
		Vector velocity = player.getVelocity();
		Location location = player.getLocation().add(0, 0.5, 0);
		Location[] blockLocations = { location.clone().add(direction.getX(), 0, direction.getZ()),
				location.clone().add(velocity.getX(), 0, velocity.getZ()) };
		int current = 0;
		for (Location blockLocation : blockLocations) {
			if(current == 0 && !this.direction) {
				continue;
			} else if(current == 1 && !this.velocity) {
				continue;
			}
			Block block = blockLocation.getBlock();
			if (!block.getType().name().endsWith("AIR")) {
				double distance = location.distance(block.getLocation().add(0.5, 0.5, 0.5));
				if (distance <= collide && distance != 0.0) {
					Bukkit.getPluginManager().callEvent(new PlayerCollideBlockEvent(player, block, distance, current == 1));
				}
			}
			current++;
		}
	}

	@Override
	public void onDisable() {
		blacklisted.clear();
	}

	/*
	 *
	 *
	 *
	 */

	@Override
	public List<Player> getCheckedPlayers() {
		ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
		players.stream().filter(player -> blacklisted.contains(player.getUniqueId()))
				.forEach(player -> players.remove(player));
		return players;
	}

	@Override
	public List<UUID> getCheckedUniqueIds() {
		ArrayList<UUID> uniqueIds = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!blacklisted.contains(player.getUniqueId())) {
				uniqueIds.add(player.getUniqueId());
			}
		}
		return uniqueIds;
	}

	@Override
	public boolean check(Player player) {
		if (!blacklisted.contains(player.getUniqueId())) {
			return false;
		}
		blacklisted.remove(player.getUniqueId());
		return true;
	}

	@Override
	public boolean check(UUID uuid) {
		if (!blacklisted.contains(uuid)) {
			return false;
		}
		blacklisted.remove(uuid);
		return true;
	}

	@Override
	public boolean isChecked(Player player) {
		return !blacklisted.contains(player.getUniqueId());
	}

	@Override
	public boolean isChecked(UUID uuid) {
		return !blacklisted.contains(uuid);
	}

	@Override
	public boolean stopCheck(Player player) {
		if (blacklisted.contains(player.getUniqueId())) {
			return false;
		}
		blacklisted.add(player.getUniqueId());
		return true;
	}

	@Override
	public boolean stopCheck(UUID uuid) {
		if (blacklisted.contains(uuid)) {
			return false;
		}
		blacklisted.add(uuid);
		return true;
	}

}
