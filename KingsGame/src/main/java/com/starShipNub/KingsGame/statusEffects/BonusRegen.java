package com.starShipNub.KingsGame.statusEffects;

import com.starShipNub.KingsGame.models.Champion;
import com.starShipNub.KingsGame.models.StatusEffect;

public class BonusRegen extends StatusEffect {
	
	public BonusRegen(int damage){
		this.setName("bonusRegen");
		this.setDamage(damage);
	}
	
	@Override
	public boolean execute(Champion c) {
		return c.takeDamage(damage);
	}
}
