package com.starShipNub.KingsGame.models;

public class Destroyable extends BaseObject {
	private int currentHp;
	private int maxHp;
	private boolean isDead;
	private int magicDefense;
	private int physicalDefense;

	public final int getCurrentHp() {
		return currentHp;
	}

	public final void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}

	public final int getMaxHp() {
		return maxHp;
	}

	public final void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public final boolean isDead() {
		return isDead;
	}

	public final void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public final int getMagicDefense() {
		return magicDefense;
	}

	public final void setMagicDefense(int magicDefense) {
		this.magicDefense = magicDefense;
	}

	public final int getPhysicalDefense() {
		return physicalDefense;
	}

	public final void setPhysicalDefense(int physicalDefense) {
		this.physicalDefense = physicalDefense;
	}
}
