package com.starShipNub.KingsGame.models;

import java.util.ArrayList;
import java.util.List;

public class Champion extends Destroyable {
	private String playerId;
	private String name;
	private int physicalDamage;
	private int magicDamage;
	private int moveDistance;
	private int movesRemaining;
	private int currentMana;
	private int maxMana;
	private List<Attack> attacks = new ArrayList<Attack>();

	public final String getName() {
		return name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final int getPhysicalDamage() {
		return physicalDamage;
	}

	public final void setPhysicalDamage(final int physicalDamage) {
		this.physicalDamage = physicalDamage;
	}

	public final int getMagicDamage() {
		return magicDamage;
	}

	public final void setMagicDamage(final int magicDamage) {
		this.magicDamage = magicDamage;
	}

	public final int getMoveDistance() {
		return moveDistance;
	}

	public final void setMoveDistance(final int moveDistance) {
		this.moveDistance = moveDistance;
	}

	public final int getCurrentMana() {
		return currentMana;
	}

	public final void setCurrentMana(final int currentMana) {
		this.currentMana = currentMana;
	}

	public final int getMaxMana() {
		return maxMana;
	}

	public final void setMaxMana(final int maxMana) {
		this.maxMana = maxMana;
	}

	public final List<Attack> getAttacks() {
		return attacks;
	}

	public final void setAttacks(final List<Attack> attacks) {
		this.attacks = attacks;
	}

	public int getMovesRemaining() {
		return movesRemaining;
	}

	public void setMovesRemaining(int movesRemaining) {
		this.movesRemaining = movesRemaining;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public boolean move(int position) {
		if (!isDead()) {
			// TODO:Validate move
			setPosition(position);
			return true;
		} else {
			return false;
		}
	}

	public boolean doAttack(String attackName) {
		if (!isDead()) {
			Attack a = getAttackById(attackName);
			if (a != null) {
				a.setCooldownRemaining(a.getCooldown());
			}
			return true;
		} else {
			return false;
		}
	}

	public Attack getAttackById(String attackId) {
		for (Attack a : this.getAttacks()) {
			if (a.getId().toString().equalsIgnoreCase(attackId)) {
				return a;
			}
		}
		return null;
	}

	public Attack getAttackByName(String attackName) {
		for (Attack a : this.getAttacks()) {
			if (a.getName().equalsIgnoreCase(attackName)) {
				return a;
			}
		}
		return null;
	}

	public boolean takeDamage(int damage) {
		if (!isDead()) {
			this.setCurrentHp(this.getCurrentHp() - damage);
			return true;
		} else {
			// Dead people can't take damage
			return false;
		}
	}

	public boolean endTurn(Integer activePlayerId) {

		// TODO: Status effects

		// Cooldowns
		for (Attack a : this.getAttacks()) {
			if (a.getCooldownRemaining() < 0) {
				a.setCooldownRemaining(0);
			} else if (a.getCooldownRemaining() > 0) {
				a.setCooldownRemaining(a.getCooldownRemaining() - 1);
			}
		}

		// TODO: Regen

		// Update Moves
		if (activePlayerId.toString().equalsIgnoreCase(playerId)) {
			this.setMovesRemaining(0);
		} else {
			this.setMovesRemaining(this.getMoveDistance());
		}

		// To live or Die?
		if (this.getCurrentHp() <= 0) {
			this.setDead(true);
		}

		return true;
	}

}
