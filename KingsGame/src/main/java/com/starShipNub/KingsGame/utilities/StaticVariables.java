package com.starShipNub.KingsGame.utilities;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StaticVariables {
	public static final int MAX_GAMES = 3;

	//Shared Stuff
	public static final String ID = "id";
	public static final String NAME = "name";
	
	// Player Entity
	public static final String PASSWORD = "password";
	public static final String EMAIL = "email";
	public static final String IS_TURN = "isTurn";
	public static final String ACTIVE_GAMES = "activeGames";

	// Champion Entity
	public static final String MAX_MOVES = "maxMoves";
	public static final String CURR_MOVES = "currMoves";
	public static final String MAX_HP = "maxHP";
	public static final String CURR_HP = "currHP";
	public static final String MAX_MANA = "maxMana";
	public static final String CURR_MANA = "currMana";
	public static final String POSITION = "position";
	public static final String IS_DEAD = "isDead";
	public static final String IS_KING = "isKing";
	public static final String ATTACKS = "attacks";

	// Board Entity
	public static final String CHAMPION_OWNER_ID = "playerId";
	
	//Attack Entity
	public static final String COOLDOWN = "cooldown";
	public static final String COOLDOWN_REMAINING = "cooldownRemaining";
	public static final String RANGE = "range";
	public static final String DAMAGE = "damage";
	
	public synchronized static String getStackTrace(final Throwable throwable) {
	     final StringWriter sw = new StringWriter();
	     final PrintWriter pw = new PrintWriter(sw, true);
	     throwable.printStackTrace(pw);
	     return sw.getBuffer().toString();
	}

}
