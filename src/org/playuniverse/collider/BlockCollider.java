package org.playuniverse.collider;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.playuniverse.collider.commands.BlockColliderCommand;
import org.playuniverse.collider.config.ColliderConfig;
import org.playuniverse.collider.listener.CollideListener;
import org.playuniverse.collider.listener.CollidingHandler;

public class BlockCollider extends JavaPlugin {

	private static Collider[] colliders = new Collider[2];
	private ColliderConfig config;

	@Override
	public void onEnable() {
		config = new ColliderConfig(useTimer -> {
			colliders[0].reload();
			colliders[1].reload();
			if (!colliders[1].isEnabled() && useTimer) {
				colliders[0].disable();
				colliders[1].enable();
			} else if (!colliders[0].isEnabled() && !useTimer) {
				colliders[0].enable();
				colliders[1].disable();
			}
		});
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(colliders[0] = new CollideListener(config), this);
		pm.registerEvents(colliders[1] = new CollidingHandler(config, this), this);
		config.reload();
		getCommand("blockcollider").setExecutor(new BlockColliderCommand(config));
	}

	@Override
	public void onDisable() {
		config.save();
	}

	public static Collider[] getColliders() {
		return colliders;
	}

	public static Collider getEnabledCollider() {
		if (colliders[0].isEnabled()) {
			return colliders[0];
		}
		return colliders[1];
	}

}
