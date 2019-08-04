/**
 * 
 * @author StevenLPHD
 * 
 */
package org.playuniverse.collider.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerCollideBlockEvent extends PlayerEvent {
	
	private Block collided;
	private double distance;
	private boolean velocity;
	
	public PlayerCollideBlockEvent(Player player, Block collided, double distance, boolean velocity) {
		super(player);
		this.collided = collided;
		this.distance = distance;
		this.velocity = velocity;
	}

	private final static HandlerList HANDLERS = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
	/*
	 * 
	 * 
	 * 
	 */
	
	public Block getBlock() {
		return collided;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public boolean fromVelocity() {
		return velocity;
	}
	
}
