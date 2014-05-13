package com.starShipNub.KingsGame.statusEffects;

import com.starShipNub.KingsGame.models.Champion;
import com.starShipNub.KingsGame.models.StatusEffect;

public class Stun extends StatusEffect {
	
	public Stun(){
		this.setName("stun");
	}
	
	@Override
	public boolean execute(Champion c) {
		c.setMovesRemaining(0);
		return true;
	}
}
