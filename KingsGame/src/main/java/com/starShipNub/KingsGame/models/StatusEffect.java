package com.starShipNub.KingsGame.models;

public class StatusEffect {
	
	protected String name;
	protected int damage;
	protected int duration;
	protected int durationRemaining;
	
	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(final int damage) {
		this.damage = damage;
	}
	
	public int getDuration() {
		return duration;
	}
	public void setDuration(final int duration) {
		this.duration = duration;
	}
	public int getDurationRemaining() {
		return durationRemaining;
	}
	public void setDurationRemaining(final int durationRemaining) {
		this.durationRemaining = durationRemaining;
	}
	public boolean execute(){
		return false;
	}
	public boolean execute(Champion c){
		return false;
	}

}
