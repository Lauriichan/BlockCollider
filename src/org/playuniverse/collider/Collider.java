package org.playuniverse.collider;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.playuniverse.collider.config.ColliderConfig;

public abstract class Collider implements Listener {

	private boolean enabled = false;
	protected final ColliderConfig config;
	protected double collide = 0.95;
	protected boolean velocity = true;
	protected boolean direction = true;

	public Collider(ColliderConfig config) {
		this.config = config;
	}

	public void disable() {
		if (isEnabled()) {
			enabled = false;
			onDisable();
		}
	}

	public void enable() {
		if (!isEnabled()) {
			enabled = true;
			onEnable();
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	/*
	 *
	 */

	public void reload() {
		collide = config.getCollide();
		velocity = config.velocityEnabled();
		direction = config.directionEnabled();
		onReload();
	}

	public void onReload() {
	}

	public void onDisable() {
	}

	public void onEnable() {
	}

	public abstract List<Player> getCheckedPlayers();

	public abstract List<UUID> getCheckedUniqueIds();

	public abstract boolean check(Player player);

	public abstract boolean check(UUID uuid);

	public abstract boolean isChecked(Player player);

	public abstract boolean isChecked(UUID uuid);

	public abstract boolean stopCheck(Player player);

	public abstract boolean stopCheck(UUID uuid);

}
