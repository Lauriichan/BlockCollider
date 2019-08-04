/**
 * 
 * @author StevenLPHD
 * 
 */
package org.playuniverse.collider.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.playuniverse.collider.Collider;
import org.playuniverse.collider.config.ColliderConfig;
import org.playuniverse.collider.event.PlayerCollideBlockEvent;

public class CollidingHandler extends Collider {

	private final Plugin plugin;
	private int[] times;

	@Override
	public void onReload() {
		setTime(config.getTime());
	}

	@Override
	public void onDisable() {
		if (!detectors.isEmpty()) {
			for (BukkitTask task : detectors.values()) {
				task.cancel();
			}
			detectors.clear();
		}
	}
	
	@Override
	public void onEnable() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			UUID uuid = player.getUniqueId();
			if (detectors.containsKey(uuid)) {
				return;
			}
			detectors.put(uuid, getDetector(player));
		}
	}

	public CollidingHandler(ColliderConfig config, Plugin plugin) {
		super(config);
		this.plugin = plugin;
		setTime(config.getTime());
	}

	private void setTime(float time) {
		times = new int[] { Math.round(4 * time), Math.round(20 * time) };
	}

	public final HashMap<UUID, BukkitTask> detectors = new HashMap<>();

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if(!isEnabled()) {
			return;
		}
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		if (detectors.containsKey(uuid)) {
			return;
		}
		detectors.put(uuid, getDetector(player));
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if(!isEnabled()) {
			return;
		}
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		if (!detectors.containsKey(uuid)) {
			return;
		}
		detectors.remove(uuid).cancel();
	}

	private BukkitTask getDetector(Player player) {
		return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
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
				int cur = current;
				Block block = blockLocation.getBlock();
				if (!block.getType().name().endsWith("AIR")) {
					double distance = location.distance(block.getLocation().add(0.5, 0.5, 0.5));
					if (distance <= collide && distance != 0.0) {
						Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager()
								.callEvent(new PlayerCollideBlockEvent(player, block, distance, cur == 1)));
					}
				}
				current++;
			}
		}, times[0], times[1]);
	}

	/*
	 * 
	 * 
	 * 
	 */

	@Override
	public List<Player> getCheckedPlayers() {
		ArrayList<Player> players = new ArrayList<>();
		for (UUID uuid : detectors.keySet()) {
			players.add(Bukkit.getPlayer(uuid));
		}
		return players;
	}

	@Override
	public List<UUID> getCheckedUniqueIds() {
		return new ArrayList<>(detectors.keySet());
	}

	@Override
	public boolean check(Player player) {
		if (detectors.containsKey(player.getUniqueId())) {
			return false;
		}
		detectors.put(player.getUniqueId(), getDetector(player));
		return true;
	}

	@Override
	public boolean check(UUID uuid) {
		if (detectors.containsKey(uuid)) {
			return false;
		}
		Player player = Bukkit.getPlayer(uuid);
		if (player == null) {
			return false;
		}
		detectors.put(uuid, getDetector(player));
		return true;
	}

	@Override
	public boolean isChecked(Player player) {
		return detectors.containsKey(player.getUniqueId());
	}

	@Override
	public boolean isChecked(UUID uuid) {
		return detectors.containsKey(uuid);
	}

	@Override
	public boolean stopCheck(Player player) {
		if (!detectors.containsKey(player.getUniqueId())) {
			return false;
		}
		detectors.remove(player.getUniqueId()).cancel();
		return true;
	}

	@Override
	public boolean stopCheck(UUID uuid) {
		if (!detectors.containsKey(uuid)) {
			return false;
		}
		detectors.remove(uuid).cancel();
		return true;
	}

}
