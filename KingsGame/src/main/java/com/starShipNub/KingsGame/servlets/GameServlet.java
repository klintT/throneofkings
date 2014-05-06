package com.starShipNub.KingsGame.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.starShipNub.KingsGame.models.Attack;
import com.starShipNub.KingsGame.models.Game;
import com.starShipNub.KingsGame.models.Player;
import com.starShipNub.KingsGame.utilities.Session;

/**
 * Servlet implementation class GameServlet
 */
@WebServlet("/game")
public class GameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GameServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = request.getHeader("SESSIONID");
		String gameId = request.getHeader("GAMEID");
		if (sessionId == null) {
			response.setHeader("result", "Error: Not Logged In");
		} else if (gameId == null) {
			response.setHeader("result", "Error: Game Not Found");
		} else {
			Player player = (Player) Session.get(sessionId).get("player");
			List<Game> activeGames = (List<Game>) Session.get(sessionId).get("games");
			Game g = getGameFromActiveGames(gameId, activeGames);

			boolean gameFound = false;
			if (g == null) {
				g = retryFromDBGames(player, sessionId, gameId);
				if (g == null) {
					response.setHeader("result", "Error: Game Does Not Exist");
				} else {
					gameFound = true;
				}
			} else {
				gameFound = true;
			}

			if (gameFound) {
				if (g.getActivePlayerId().compareTo(player.getPlayerId()) != 0) {
					// Inactive player is viewing game
					g.inactiveView();
				}
				String gameString = getGameString(g);
				response.setHeader("gameInfo", gameString);
				response.setHeader("result", "Success!");
			}
		}
	}

	private String getGameString(Game g) {
		StringBuilder gameString = new StringBuilder();
		gameString.append("{ game : { gameId : " + g.getGameId().toString() + " , playerNumberTurn : " + g.getPlayerNumberTurn() + " , players : [ ");
		for (int i = 0; i < 2; i++) {
			gameString.append("{ playerId : " + g.getPlayers().get(i).getPlayerId() + ", ");
			gameString.append("playerName : " + g.getPlayers().get(i).getPlayerName() + ", ");
			gameString.append("champions : [ ");
			// TODO: Split champions into two different lists
			if (i == 0) {
				for (int p = 0; p < 4; p++) {
					gameString.append("{ name: " + g.getBoard().getChamps().get(p).getName() + ", ");
					gameString.append("id: " + g.getBoard().getChamps().get(p).getId() + ", ");
					gameString.append("maxHP: " + g.getBoard().getChamps().get(p).getMaxHp() + ", ");
					gameString.append("currHP: " + g.getBoard().getChamps().get(p).getCurrentHp() + ", ");
					gameString.append("maxMana: " + g.getBoard().getChamps().get(p).getMaxMana() + ", ");
					gameString.append("currMana: " + g.getBoard().getChamps().get(p).getCurrentMana() + ", ");
					gameString.append("maxMoves: " + g.getBoard().getChamps().get(p).getMoveDistance() + ", ");
					gameString.append("currMoves: " + g.getBoard().getChamps().get(p).getMovesRemaining() + ", ");
					gameString.append("isKing: " + String.valueOf(g.getBoard().getChamps().get(p).isKing()) + ", ");
					gameString.append("position: " + g.getBoard().getChamps().get(p).getPosition() + ", ");
					gameString.append("attackCount: " + g.getBoard().getChamps().get(p).getAttacks().size() + ", ");

					gameString.append("attacks : [ ");
					for (Attack a : g.getBoard().getChamps().get(p).getAttacks()) {
						gameString.append("{ name: " + a.getName() + ", ");
						gameString.append("id: " + a.getId() + ", ");
						gameString.append("damage: " + a.getDamage() + ", ");
						gameString.append("range: " + a.getDistance() + ", ");
						gameString.append("cooldown: " + a.getCooldown() + ", ");
						gameString.append("cooldownRemaining: " + a.getCooldownRemaining() + " }, ");
					}
					// Close Attack Array
					gameString.deleteCharAt(gameString.length() - 2);
					gameString.append("] ");
					gameString.append(" }, ");
				}
			} else {
				for (int p = 4; p < 8; p++) {
					gameString.append("{ name: " + g.getBoard().getChamps().get(p).getName() + ", ");
					gameString.append("id: " + g.getBoard().getChamps().get(p).getId() + ", ");
					gameString.append("maxHP: " + g.getBoard().getChamps().get(p).getMaxHp() + ", ");
					gameString.append("currHP: " + g.getBoard().getChamps().get(p).getCurrentHp() + ", ");
					gameString.append("maxMana: " + g.getBoard().getChamps().get(p).getMaxMana() + ", ");
					gameString.append("currMana: " + g.getBoard().getChamps().get(p).getCurrentMana() + ", ");
					gameString.append("maxMoves: " + g.getBoard().getChamps().get(p).getMoveDistance() + ", ");
					gameString.append("currMoves: " + g.getBoard().getChamps().get(p).getMovesRemaining() + ", ");
					gameString.append("isKing: " + String.valueOf(g.getBoard().getChamps().get(p).isKing()) + ", ");
					gameString.append("position: " + g.getBoard().getChamps().get(p).getPosition() + " , ");
					gameString.append("attackCount: " + g.getBoard().getChamps().get(p).getAttacks().size() + ", ");

					gameString.append("attacks : [ ");
					for (Attack a : g.getBoard().getChamps().get(p).getAttacks()) {
						gameString.append("{ name: " + a.getName() + ", ");
						gameString.append("id: " + a.getId() + ", ");
						gameString.append("damage: " + a.getDamage() + ", ");
						gameString.append("range: " + a.getDistance() + ", ");
						gameString.append("cooldown: " + a.getCooldown() + ", ");
						gameString.append("cooldownRemaining: " + a.getCooldownRemaining() + " }, ");
					}
					// Close Attack Array
					gameString.deleteCharAt(gameString.length() - 2);
					gameString.append("] ");
					gameString.append(" }, ");
				}
			}
			// Close Champ Array
			gameString.deleteCharAt(gameString.length() - 2);
			gameString.append("] ");
			gameString.append(" }, ");
		}
		// Close Player Array
		gameString.deleteCharAt(gameString.length() - 2);
		gameString.append("], ");
		gameString.append("activePlayer : " + g.getActivePlayerName() + " } }");
		return gameString.toString();
	}

	private Game getGameFromActiveGames(String gameId, List<Game> activeGames) {
		for (Game g : activeGames) {
			if (g.getGameId().toString().equalsIgnoreCase(gameId)) {
				return g;
			}
		}
		return null;
	}

	private Game retryFromDBGames(Player player, String sessionId, String gameId) {
		List<Game> activeGames = player.getActiveGames();
		Session.put(sessionId, "games", activeGames);
		Game g = getGameFromActiveGames(gameId, activeGames);
		if (g != null) {
			return g;
		} else {
			return null;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = request.getParameter("SESSIONID");
		String gameId = request.getParameter("GAMEID");
		String turnActions = request.getParameter("TURNACTIONS");
		if (sessionId == null) {
			response.setHeader("result", "Error: Not Logged In");
		} else {
			Player player = (Player) Session.get(sessionId).get("player");
			List<Game> activeGames = (List<Game>) Session.get(sessionId).get("games");
			Game g = getGameFromActiveGames(gameId, activeGames);
			boolean gameFound = false;
			if (g == null) {
				g = retryFromDBGames(player, sessionId, gameId);
				if (g == null) {
					response.setHeader("result", "Error: Game Does Not Exist");
				} else {
					gameFound = true;
				}
			} else {
				gameFound = true;
			}

			if (gameFound) {
				if (g.getActivePlayerId().compareTo(player.getPlayerId()) == 0) {
					if (g.performTurn(turnActions)) {
						if (g.isGameOver()) {
							activeGames = player.getActiveGames();
							Session.put(sessionId, "games", activeGames);
							//Session.removeByGameId(g.getGameId());
							response.setHeader("result", "recentVictory");
						} else {
							response.setHeader("result", "Success!");
						}
					} else {
						response.setHeader("result", "Error: Move was invalid.");
					}
				} else {
					// Inactive player is trying to make a move.
					response.setHeader("result", "Error: It's not your turn!");
				}
			}
		}
	}

}
