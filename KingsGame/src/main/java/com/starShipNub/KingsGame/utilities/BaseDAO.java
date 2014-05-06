package com.starShipNub.KingsGame.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.starShipNub.KingsGame.models.Attack;
import com.starShipNub.KingsGame.models.Board;
import com.starShipNub.KingsGame.models.Champion;
import com.starShipNub.KingsGame.models.Game;
import com.starShipNub.KingsGame.models.Player;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class BaseDAO {
	private DB db;

	public BaseDAO() {
		db = ConnectionFactory.getConnection();
	}

	public boolean createNewGame(Integer id, List<Player> players, Board board) {
		DBCollection table = db.getCollection("games");
		BasicDBObject insert = new BasicDBObject();
		insert.put("id", id);
		List<BasicDBObject> playerList = new ArrayList<BasicDBObject>();
		for (int i = 0; i < players.size(); i++) {
			playerList.add(new BasicDBObject("playerId", players.get(i).getPlayerId()));
		}
		insert.put("players", playerList);
		// TODO: Add random order logic
		insert.put("activePlayer", players.get(0).getPlayerId());
		insert.put("playerNumberTurn", 1);
		insert = formBoardInsert(board, insert, players.get(0).getPlayerId(), players.get(1).getPlayerId());
		table.insert(insert);
		return true;
	}

	private Game parseGameObject(DBObject ob) {
		Game g = new Game();
		g.setGameId(Integer.valueOf(ob.get("id").toString()));
		g.setActivePlayerId(Integer.valueOf(ob.get("activePlayer").toString()));
		g.setPlayerNumberTurn(Integer.valueOf(ob.get("playerNumberTurn").toString()));
		List<Player> players = new ArrayList<Player>();
		BasicDBList playas = (BasicDBList) ob.get("players");
		for (BasicDBObject plaOb : playas.toArray(new BasicDBObject[0])) {
			Player p = new Player(plaOb.getInt("playerId"));
			players.add(p);
		}
		g.setPlayers(players);

		// Board
		Board board = new Board();
		BasicDBList champs = (BasicDBList) ob.get("champions");
		for (BasicDBObject champOb : champs.toArray(new BasicDBObject[0])) {
			Champion c = new Champion();
			c.setId(champOb.getInt(StaticVariables.ID));
			c.setName(champOb.getString(StaticVariables.NAME));
			c.setMaxHp(champOb.getInt(StaticVariables.MAX_HP));
			c.setMaxMana(champOb.getInt(StaticVariables.MAX_MANA));
			c.setMoveDistance(champOb.getInt(StaticVariables.MAX_MOVES));
			c.setCurrentHp(champOb.getInt(StaticVariables.CURR_HP));
			c.setCurrentMana(champOb.getInt(StaticVariables.CURR_MANA));
			c.setMovesRemaining(champOb.getInt(StaticVariables.CURR_MOVES));
			c.setDead(champOb.getBoolean(StaticVariables.IS_DEAD));
			c.setKing(champOb.getBoolean(StaticVariables.IS_KING));
			if (c.getCurrentHp() <= 0) {
				c.setPosition(-1);
			} else {
				c.setPosition(champOb.getInt(StaticVariables.POSITION));
			}
			c.setPlayerId(champOb.getString(StaticVariables.CHAMPION_OWNER_ID));

			BasicDBList attacks = (BasicDBList) champOb.get("attacks");
			List<Attack> attackList = new ArrayList<Attack>();
			for (BasicDBObject attacksOb : attacks.toArray(new BasicDBObject[0])) {
				Attack a = new Attack();
				a.setId(attacksOb.getInt(StaticVariables.ID));
				a.setName(attacksOb.getString(StaticVariables.NAME));
				a.setDamage(attacksOb.getInt(StaticVariables.DAMAGE));
				a.setCooldown(attacksOb.getInt(StaticVariables.COOLDOWN));
				a.setCooldownRemaining(attacksOb.getInt(StaticVariables.COOLDOWN_REMAINING));
				a.setDistance(attacksOb.getInt(StaticVariables.RANGE));
				attackList.add(a);
			}
			c.setAttacks(attackList);

			board.getChamps().add(c);
		}

		g.setBoard(board);
		return g;
	}

	@SuppressWarnings("unused")
	private BasicDBObject formBoardInsert(BasicDBObject ob, Board board) {
		// ob.put(StaticVariables.ID, board.get);
		return ob;
	}

	public Integer createNewId(String tableName) {
		Random random = new Random();
		DBCollection table = db.getCollection(tableName);
		while (true) {
			Integer id = random.nextInt();
			DBCursor cursor = table.find(new BasicDBObject("id", id));
			if (cursor.size() == 0) {
				return id;
			}
		}
	}

	public boolean endTurn(Game game) {
		BasicDBObject insert = new BasicDBObject();
		DBCollection table = null;

		try {
			table = db.getCollection("games");
			insert.put("id", game.getGameId());
			// TODO: get this table row here to make sure we don't lose the game
			table.remove(insert);

			if (game.isGameOver()) {
				table = db.getCollection("gameArchive");
			}

		} catch (Exception e) {
			return false;
		}

		try {
			List<BasicDBObject> playerList = new ArrayList<BasicDBObject>();
			for (int i = 0; i < game.getPlayers().size(); i++) {
				playerList.add(new BasicDBObject("playerId", game.getPlayers().get(i).getPlayerId()));
			}
			insert.put("players", playerList);
			insert.put("activePlayer", game.getActivePlayerId());
			insert.put("playerNumberTurn", game.getPlayerNumberTurn());
			insert = formBoardInsert(game.getBoard(), insert, game.getPlayers().get(0).getPlayerId(), game.getPlayers().get(1).getPlayerId());
			table.insert(insert);
		} catch (Exception e) {
			// retry or revert game back to what it was
			return false;
		}

		return true;
	}

	/**
	 * 
	 * PLAYERS SECTION
	 * 
	 */

	public Player selectPlayer(Integer playerId) {
		DBCollection table = db.getCollection("players");
		DBCursor cursor = table.find(new BasicDBObject("id", playerId));
		while (cursor.hasNext()) {
			DBObject ob = cursor.next();
			Player p = new Player();
			p.setPlayerId(playerId);
			p.setPlayerName(ob.get("name").toString());
			return p;
		}
		// NOT FOUND ERROR
		return null;
	}

	public Player selectPlayer(String username, String password) {
		DBCollection table = db.getCollection("players");
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("name", username));
		obj.add(new BasicDBObject("password", password));
		andQuery.put("$and", obj);
		DBCursor cursor = table.find(andQuery);
		while (cursor.hasNext()) {
			DBObject ob = cursor.next();
			Player p = new Player();
			p.setPlayerId(Integer.valueOf(ob.get("id").toString()));
			p.setPlayerName(username);
			return p;
		}
		// NOT FOUND ERROR
		return null;
	}

	public boolean isUsernameTaken(String username) {
		DBCollection table = db.getCollection("players");
		DBCursor cursor = table.find(new BasicDBObject("name", username));
		if (cursor.size() > 0) {
			return true;
		}
		return false;
	}

	public boolean createNewUser(Player player) {
		if (!isUsernameTaken(player.getPlayerName())) {
			DBCollection table = db.getCollection("players");
			BasicDBObject insert = formPlayerInsert(player);
			table.insert(insert);
			return true;
		} else {
			// USERNAME TAKEN ERROR
			return false;
		}
	}

	public Integer countPlayersActiveGames(Integer playerId) {
		DBCollection table = db.getCollection("games");
		return (int) table.count(new BasicDBObject("players", new BasicDBObject("playerId", playerId)));
	}

	@SuppressWarnings("unused")
	private void updatePlayers(List<Player> players) {
		DBCollection table = db.getCollection("players");
		for (Player p : players) {
			BasicDBObject old = new BasicDBObject();
			old.put("id", p.getPlayerId());

			BasicDBObject update = formPlayerInsert(p);

			BasicDBObject updateOb = new BasicDBObject();
			updateOb.put("$set", update);

			table.update(old, updateOb);
		}
	}

	private BasicDBObject formPlayerInsert(Player player) {
		BasicDBObject ob = new BasicDBObject();
		ob.put(StaticVariables.ID, player.getPlayerId());
		ob.put(StaticVariables.NAME, player.getPlayerName());
		ob.put(StaticVariables.PASSWORD, player.getPassword());
		ob.put(StaticVariables.EMAIL, player.getEmail());
		return ob;
	}

	public List<Game> getActiveGames(Integer playerId) {
		DBCollection table = db.getCollection("games");
		List<Game> activeGames = new ArrayList<Game>();

		BasicDBObject search = new BasicDBObject("players", new BasicDBObject("playerId", playerId));
		DBCursor cursor = table.find(search);
		while (cursor.hasNext()) {
			DBObject ob = cursor.next();
			activeGames.add(parseGameObject(ob));
		}
		return activeGames;
	}

	public Player matchMake(Player player1) {
		DBCollection table = db.getCollection("players");
		DBCursor cursor = table.find();
		List<Player> pList = new ArrayList<Player>();
		while (cursor.hasNext()) {
			DBObject ob = cursor.next();
			Player p = new Player();
			p.setPlayerId(Integer.parseInt(ob.get("id").toString()));
			p.setPlayerName(ob.get("name").toString());
			pList.add(p);
		}

		if (pList.size() == 0) {
			return null;
		}

		while (true) {
			for (Player p : pList) {
				Random rand = new Random();
				if (rand.nextInt() % 2 == 0) {
					if (player1.getPlayerId().compareTo(p.getPlayerId()) != 0) {
						if (countPlayersActiveGames(p.getPlayerId()) < StaticVariables.MAX_GAMES) {
							return p;
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * END PLAYERS SECTION
	 * 
	 */

	/**
	 * 
	 * Board Section
	 * 
	 */

	public BasicDBObject formBoardInsert(Board board, BasicDBObject insert, Integer player1Id, Integer player2Id) {
		List<BasicDBObject> champList = new ArrayList<BasicDBObject>();
		for (int i = 0; i < board.getChamps().size(); i++) {
			Champion c = board.getChamps().get(i);
			if (i < 4) {
				c.setPlayerId(player1Id.toString());
			} else {
				c.setPlayerId(player2Id.toString());
			}
			champList.add(formChampionInsert(c));
		}
		insert.put("champions", champList);
		return insert;
	}

	/**
	 * 
	 * Champion Section
	 * 
	 */

	public BasicDBObject formChampionInsert(Champion champ) {
		BasicDBObject insert = new BasicDBObject();
		insert.put(StaticVariables.ID, champ.getId());
		insert.put(StaticVariables.NAME, champ.getName());
		insert.put(StaticVariables.MAX_HP, champ.getMaxHp());
		insert.put(StaticVariables.CURR_HP, champ.getCurrentHp());
		insert.put(StaticVariables.MAX_MANA, champ.getMaxMana());
		insert.put(StaticVariables.CURR_MANA, champ.getCurrentMana());
		insert.put(StaticVariables.MAX_MOVES, champ.getMoveDistance());
		insert.put(StaticVariables.CURR_MOVES, champ.getMovesRemaining());
		insert.put(StaticVariables.POSITION, champ.getPosition());
		insert.put(StaticVariables.IS_DEAD, champ.isDead());
		insert.put(StaticVariables.IS_KING, champ.isKing());
		insert.put(StaticVariables.CHAMPION_OWNER_ID, champ.getPlayerId());

		insert.put("attacks", formAttackInsert(champ));

		return insert;
	}

	public Champion getDefaultChampByName(String name) {
		DBCollection table = db.getCollection("champions");
		Champion champ = new Champion();

		BasicDBObject search = new BasicDBObject(StaticVariables.NAME, name);
		DBCursor cursor = table.find(search);
		while (cursor.hasNext()) {
			DBObject ob = cursor.next();
			champ.setId(this.createNewId("champions"));
			champ.setName(name);
			try {
				champ.setMaxHp(Integer.parseInt(ob.get(StaticVariables.MAX_HP).toString()));
			} catch (NumberFormatException e) {
				double hp = Double.parseDouble(ob.get(StaticVariables.MAX_HP).toString());
				champ.setMaxHp((int) hp);
			}
			champ.setCurrentHp(champ.getMaxHp());
			try {
				champ.setMaxMana(Integer.parseInt(ob.get(StaticVariables.MAX_MANA).toString()));
			} catch (NumberFormatException e) {
				double mana = Double.parseDouble(ob.get(StaticVariables.MAX_MANA).toString());
				champ.setMaxMana((int) mana);
			}
			champ.setCurrentMana(champ.getMaxMana());
			try {
				champ.setMoveDistance(Integer.parseInt(ob.get(StaticVariables.MAX_MOVES).toString()));
			} catch (NumberFormatException e) {
				double move = Double.parseDouble(ob.get(StaticVariables.MAX_MOVES).toString());
				champ.setMoveDistance((int) move);
			}
			champ.setMovesRemaining(champ.getMoveDistance());
			// TODO: Update this later
			if (name.equalsIgnoreCase("king"))
				champ.setKing(true);
			else
				champ.setKing(false);
			champ.setDead(false);

			BasicDBList attacks = (BasicDBList) ob.get("attacks");
			List<Attack> attackList = new ArrayList<Attack>();
			for (BasicDBObject attacksOb : attacks.toArray(new BasicDBObject[0])) {
				Attack a = new Attack();
				a.setId(attacksOb.getInt(StaticVariables.ID));
				a.setName(attacksOb.getString(StaticVariables.NAME));
				a.setDamage(attacksOb.getInt(StaticVariables.DAMAGE));
				a.setCooldown(attacksOb.getInt(StaticVariables.COOLDOWN));
				a.setCooldownRemaining(attacksOb.getInt(StaticVariables.COOLDOWN_REMAINING));
				a.setDistance(attacksOb.getInt(StaticVariables.RANGE));
				attackList.add(a);
			}
			champ.setAttacks(attackList);

			return champ;
		}
		return null;
	}

	/**
	 * 
	 * Attacks Section
	 * 
	 */

	public List<BasicDBObject> formAttackInsert(Champion champ) {
		List<BasicDBObject> attacks = new ArrayList<BasicDBObject>();
		for (Attack a : champ.getAttacks()) {
			BasicDBObject attack = new BasicDBObject();
			attack.put(StaticVariables.ID, a.getId());
			attack.put(StaticVariables.NAME, a.getName());
			attack.put(StaticVariables.DAMAGE, a.getDamage());
			attack.put(StaticVariables.COOLDOWN, a.getCooldown());
			attack.put(StaticVariables.COOLDOWN_REMAINING, a.getCooldownRemaining());
			attack.put(StaticVariables.RANGE, a.getDistance());
			attacks.add(attack);
		}
		return attacks;
	}

	/**
	 * 
	 * Defaults SECTION
	 * 
	 */

	public void addDefaultChampsToChampionsTable(List<Champion> champs) {
		DBCollection table = db.getCollection("champions");
		for (Champion c : champs) {
			BasicDBObject insert = new BasicDBObject();
			insert.put(StaticVariables.ID, c.getId());
			insert.put(StaticVariables.NAME, c.getName());
			insert.put(StaticVariables.MAX_HP, c.getMaxHp());
			insert.put(StaticVariables.MAX_MANA, c.getMaxMana());
			insert.put(StaticVariables.MAX_MOVES, c.getMoveDistance());

			insert.put("attacks", formAttackInsert(c));

			System.out.println(insert.toString());

			// table.insert(insert);
		}
	}
}
