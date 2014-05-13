package com.starShipNub.KingsGame.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.starShipNub.KingsGame.utilities.StaticVariables;

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
	private ConcurrentHashMap<String, StatusEffect> statusEffects = new ConcurrentHashMap<String, StatusEffect>();

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

	public void newStatusEffect(String statusEffectName) {
		for (StatusEffect e : StaticVariables.statusEffectCache) {
			if (e.getName().equalsIgnoreCase(statusEffectName)) {
				e.setDurationRemaining(e.getDuration());
				this.statusEffects.put(e.getName(), e);
			}
		}
	}

	public boolean endTurn(Integer activePlayerId) {

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

		// Status effects
		Iterator<String> i = statusEffects.keySet().iterator();
		while (i.hasNext()) {
			String name = i.next();
			StatusEffect se = statusEffects.get(name);
			if (se.durationRemaining > 0) {
				if (!se.execute(this)) {
					// LOG FAILURE
					// It would be cool if it also logged an ID of the failure
					// and included it in the client error logs
					// so they could reference like a bug status or something
				}
			}

			se.durationRemaining--;
			if (se.durationRemaining <= 0) {
				statusEffects.remove(name);
			}
		}

		// To live or Die?
		if (this.getCurrentHp() <= 0) {
			this.setDead(true);
		}

		return true;
	}

}
