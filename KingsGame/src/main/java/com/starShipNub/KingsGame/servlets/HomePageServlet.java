package com.starShipNub.KingsGame.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import com.starShipNub.KingsGame.models.Game;
import com.starShipNub.KingsGame.models.Player;
import com.starShipNub.KingsGame.utilities.BaseDAO;
import com.starShipNub.KingsGame.utilities.Session;
import com.starShipNub.KingsGame.utilities.StaticVariables;

/**
 * Servlet implementation class HomePageServlet
 */
@WebServlet("/homePage")
public class HomePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BaseDAO myDAO;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HomePageServlet() {
		super();
		myDAO = new BaseDAO();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = request.getHeader("SESSIONID");
		if (sessionId == null) {
			response.setHeader("result", "Error: Not Logged In");
		} else {
			Player player1 = (Player) Session.get(sessionId).get("player");
			response.setHeader("username", player1.getPlayerName());
			if (Session.get(sessionId).get("games") != null) {
				List<Game> activeGames = player1.getActiveGames();
				Session.put(sessionId, "games", activeGames);
				response.setHeader("games", getGameString(activeGames));
			}
			response.setHeader("result", "Success!");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionId = request.getParameter("sessionId");
		if (sessionId == null) {
			response.setHeader("result", "Error: Not Logged In");
		} else {
			try {
				Player player1 = (Player) Session.get(sessionId).get("player");
				if (myDAO.countPlayersActiveGames(player1.getPlayerId()) < StaticVariables.MAX_GAMES) {
					Game newGame = new Game();
					newGame.newGame(player1, sessionId);
					response.setHeader("result", "Success!");
				} else {
					response.setHeader("result", "Error: Max number of active games reached!");
				}
			} catch (Exception e) {
				response.setHeader("result", "Error:" + StaticVariables.getStackTrace(e));
			}
		}
	}

	private String getGameString(List<Game> activeGames) {
		StringBuilder gameString = new StringBuilder();
		if (activeGames.size() == 0) {
			gameString.append("{ games : { count : { 0 } } }");
		} else {
			gameString.append("{ games : { count : " + Integer.toString(activeGames.size()) + " , game : [ ");
			for (Game g : activeGames) {
				gameString.append("{ gameId : " + g.getGameId() + ", ");
				gameString.append("player1 : " + g.getPlayers().get(0).getPlayerName() + ", ");
				gameString.append("player2 : " + g.getPlayers().get(1).getPlayerName() + ", ");
				gameString.append("activePlayer : " + g.getActivePlayerName() + " }, ");
			}
			gameString.deleteCharAt(gameString.length() - 2);
			gameString.append("] } }");
		}
		return gameString.toString();
	}
}
