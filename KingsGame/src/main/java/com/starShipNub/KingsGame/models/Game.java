package com.starShipNub.KingsGame.models;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.starShipNub.KingsGame.utilities.BaseDAO;
import com.starShipNub.KingsGame.utilities.Session;

public class Game {
	private Integer gameId;
	private Board board;
	private List<Player> players;
	private BaseDAO myDAO;
	private Integer activePlayerId;
	private Integer playerNumberTurn;
	private String errorString;
	private boolean isGameOver;
	private String lastGameOutcome;

	public Game(Player player1, Player player2) {

	}

	public Game(Integer existingId) {

	}

	public Game() {
		players = new ArrayList<Player>();
		board = new Board();
		myDAO = new BaseDAO();
	}

	public void newGame(final Player player1, final String sessionId) {
		gameId = myDAO.createNewId("games");
		// TODO: Add random turn determining logic
		players.add(player1);

		Player player2 = myDAO.matchMake(player1);

		players.add(player2);
		board.defaultBoard(player1.getPlayerId().toString(), player2.getPlayerId().toString());
		myDAO.createNewGame(gameId, players, board);

		updateGameSession(sessionId, player1);
	}

	private void updateGameSession(final String sessionId, final Player player) {
		List<Game> activeGames = player.getActiveGames();
		Session.put(sessionId, "games", activeGames);
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(final Integer gameId) {
		this.gameId = gameId;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(final Board board) {
		this.board = board;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(final List<Player> players) {
		this.players = players;
	}

	public BaseDAO getMyDAO() {
		return myDAO;
	}

	public void setMyDAO(final BaseDAO myDAO) {
		this.myDAO = myDAO;
	}

	public Integer getActivePlayerId() {
		return activePlayerId;
	}

	public String getActivePlayerName() {
		for (Player p : players) {
			if (p.getPlayerId().compareTo(getActivePlayerId()) == 0) {
				return p.getPlayerName();
			}
		}
		return "Error: Player Name Not Found";
	}

	public Player getPlayerById(int id) {
		for (Player p : players) {
			if (p.getPlayerId().compareTo(id) == 0) {
				return p;
			}
		}
		return null;
	}

	public void setActivePlayerId(final Integer activePlayerId) {
		this.activePlayerId = activePlayerId;
	}

	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(final String errorString) {
		this.errorString = errorString;
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public String getLastGameOutcome() {
		return lastGameOutcome;
	}

	public void setLastGameOutcome(String lastGameOutcome) {
		this.lastGameOutcome = lastGameOutcome;
	}

	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}

	public Integer getPlayerNumberTurn() {
		return playerNumberTurn;
	}

	public void setPlayerNumberTurn(Integer playerNumberTurn) {
		this.playerNumberTurn = playerNumberTurn;
	}

	// ---------------------- Inactive Player -------------------------
	public boolean inactiveView() {
		boolean success = true;

		if (!board.inactiveView()) {
			// I think this is overkill since this only can return true.
			success = false;
		}

		return success;
	}

	// ---------------------- Turn Functions -------------------------

	public boolean performTurn(final String actionString) {
		boolean success = true;
		try {
			JSONParser parser = new JSONParser();
			JSONArray ob = (JSONArray) parser.parse(actionString);
			JSONObject object = (JSONObject) ob.get(0);

			JSONArray actions = (JSONArray) object.get("actions");
			for (int i = 0; i < actions.size(); i++) {
				JSONObject action = (JSONObject) actions.get(i);
				if (!doAction(action)) {
					success = false;
					// return error string
				}
				if ("endTurn".equalsIgnoreCase((String) action.get("action"))) {
					break;
				}
			}
		} catch (Exception e) {
			// Add logging
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	private boolean doAction(final JSONObject action) {
		String actionTaken = (String) action.get("action");
		if ("move".equalsIgnoreCase(actionTaken)) {
			return doMove(action);
		} else if ("attack".equalsIgnoreCase(actionTaken)) {
			return doAttack(action);
		} else if ("endTurn".equalsIgnoreCase(actionTaken)) {
			return endTurn();
		} else {
			errorString = "Action not found!";
			return false;
		}
	}

	// ------------------------- ACTIONS -------------------------
	private boolean doMove(JSONObject action) {
		Champion c = board.getChampionById((String) action.get("actor1"));
		if (c != null) {
			if (c.move(Integer.parseInt((String) action.get("position")))) {
				return true;
			} else {
				errorString = "Not a valid move";
				return false;
			}
		} else {
			errorString = "Champion Not Found!";
			return false;
		}
	}

	private boolean doAttack(JSONObject action) {
		Champion c1 = board.getChampionById((String) action.get("actor1"));
		Champion c2 = board.getChampionById((String) action.get("actor2"));
		if (c1 != null) {
			if (c1.doAttack((String) action.get("attack"))) {
				Attack a = c1.getAttackById((String) action.get("attack"));
				if (a == null) {
					errorString = "Attack Doesn't exist!";
					return false;
				}
				int damage = a.getDamage();
				if (c2 != null && c2.takeDamage(damage)) {
					return true;
				} else {
					errorString = "Champion 2 Not Found!";
					return false;
				}
			} else {
				errorString = "Not a valid move";
				return false;
			}
		} else {
			errorString = "Champion Not Found!";
			return false;
		}
	}

	private boolean endTurn() {
		boolean endSuccess = true;

		// Update Board
		if (!board.endTurn(getActivePlayerId())) {
			errorString = "Board failed to finish turn";
			endSuccess = false;
		}

		// Check win conidtion
		if (!board.checkWinCondition()) {

			// Update active player
			for (Player p : players) {
				if (!p.getPlayerName().equalsIgnoreCase(getActivePlayerName())) {
					this.setActivePlayerId(p.getPlayerId());
					break;
				}
			}

			if (playerNumberTurn == 1) {
				playerNumberTurn = 2;
			} else {
				playerNumberTurn = 1;
			}
			
		} else {
			isGameOver=true;
		}

		// Update DB
		if (!myDAO.endTurn(this)) {
			errorString = "DB Failed to Update!";
			endSuccess = false;
		}

		return endSuccess;
	}

}
