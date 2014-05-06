package com.starShipNub.KingsGame.models;

public class Attack {
	private Integer id;
	private String name;
	private int damage;
	private int distance;
	private int cooldown;
	private int cooldownRemaining;

	public final Integer getId() {
		return id;
	}

	public final void setId(final int id) {
		this.id = id;
	}

	public final String getName() {
		return name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final int getDamage() {
		return damage;
	}

	public final void setDamage(final int damage) {
		this.damage = damage;
	}

	public final int getCooldown() {
		return cooldown;
	}

	public final void setCooldown(final int cooldown) {
		this.cooldown = cooldown;
	}

	public final int getCooldownRemaining() {
		return cooldownRemaining;
	}

	public final void setCooldownRemaining(final int cooldownRemaining) {
		this.cooldownRemaining = cooldownRemaining;
	}

	public final int getDistance() {
		return distance;
	}

	public final void setDistance(final int distance) {
		this.distance = distance;
	}
}
