package com.starShipNub.KingsGame.statusEffects;

import com.starShipNub.KingsGame.models.Champion;
import com.starShipNub.KingsGame.models.StatusEffect;

public class Poison extends StatusEffect {

	public Poison(int damage) {
		this.setName("poison");
		this.setDamage(damage);
	}

	@Override
	public boolean execute(Champion c) {
		return c.takeDamage(damage);
	}
}
