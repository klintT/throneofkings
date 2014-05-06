package com.starShipNub.KingsGame.models;

import java.util.List;

import com.starShipNub.KingsGame.utilities.BaseDAO;
import com.starShipNub.KingsGame.utilities.StaticVariables;

public class Player {
	private Integer playerId;
	private String playerName;
	private BaseDAO myDAO;
	private String password;
	private String email;

	public Player() {
		myDAO = new BaseDAO();
	}

	public Player(Integer playerId) {
		myDAO = new BaseDAO();
		Player p = myDAO.selectPlayer(playerId);
		this.playerId = p.getPlayerId();
		this.playerName = p.getPlayerName();
	}

	public Player(String username, String password) {
		myDAO = new BaseDAO();
		Player p = myDAO.selectPlayer(username, password);
		this.playerId = p.getPlayerId();
		this.playerName = p.getPlayerName();
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public final boolean canMakeNewGame() {
		if (myDAO.countPlayersActiveGames(playerId) <= StaticVariables.MAX_GAMES) {
			return true;
		}
		return false;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void createNewUser(final String username, final String password) {
		createNewUser(username, password, "");
	}

	public void createNewUser(final String username, final String password, final String email) {
		this.playerId = myDAO.createNewId("players");
		this.playerName = username;
		// TODO: Hash/Salt this
		this.password = password;
		this.email = email;
		myDAO.createNewUser(this);
	}

	public List<Game> getActiveGames() {
		return this.myDAO.getActiveGames(this.playerId);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
