/**
 * 
 * @author StevenLPHD
 * 
 */
package org.playuniverse.collider.config;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class ColliderConfig {
	
	private final File file = new File("plugins//BlockCollider//config.yml");
	private final Consumer<Boolean> consume;
	private YamlConfiguration config;
	
	
	public ColliderConfig(Consumer<Boolean> consume) {
		this.consume = consume;
		config = new YamlConfiguration();
	}

	private void checkFile() {
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void load() {
		check("collider.trigger.distance", 0.95d);
		check("collider.trigger.onVelocity", true);
		check("collider.trigger.onDirection", true);
		check("collider.timer.enabled", false);
		check("collider.timer.time", 1.0f);
		consume.accept(useTimedTask());
	}
	
	public void save() {
		checkFile();
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reload() {
		checkFile();
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		load();
		save();
	}
	
	private void check(String path, Object object) {
		if(!config.contains(path)) {
			config.set(path, object);
		}
	}

	public double getCollide() {
		return config.getDouble("collider.trigger.distance");
	}
	
	public boolean velocityEnabled() {
		return config.getBoolean("collider.trigger.onVelocity");
	}
	
	public boolean directionEnabled() {
		return config.getBoolean("collider.trigger.onDirection");
	}
	
	public float getTime() {
		return Double.valueOf(config.getDouble("collider.timer.time")).floatValue();
	}
	
	public boolean useTimedTask() {
		return config.getBoolean("collider.timer.enabled");
	}

}
