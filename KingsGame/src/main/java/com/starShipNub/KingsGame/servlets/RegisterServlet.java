package com.starShipNub.KingsGame.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import com.starShipNub.KingsGame.models.Game;
import com.starShipNub.KingsGame.models.Player;
import com.starShipNub.KingsGame.utilities.Session;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("user") != null && request.getParameter("pass") != null) {
			Player player = new Player();
			String username = request.getParameter("user");
			String password = request.getParameter("pass");

			if (request.getParameter("email") != null) {
				String email = request.getParameter("email");
				player.createNewUser(username, password, email);
			} else {
				player.createNewUser(username, password);
			}
			List<Game> activeGames = player.getActiveGames();
			// TODO: It's possible for one user to have multiple sessions going
			// like this. Fix that
			String newSessionId = Session.getUniqueId(player.getPlayerId());
			Session.put(newSessionId, "games", activeGames);
			Session.put(newSessionId, "player", player);
			response.setHeader("sessionId", newSessionId);
			response.setHeader("result", "Success!");
		}
	}

}
