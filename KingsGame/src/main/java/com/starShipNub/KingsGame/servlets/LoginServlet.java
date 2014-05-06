package com.starShipNub.KingsGame.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.starShipNub.KingsGame.models.Game;
import com.starShipNub.KingsGame.models.Player;
import com.starShipNub.KingsGame.utilities.ConnectionFactory;
import com.starShipNub.KingsGame.utilities.Session;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();

	    out.println("<html>");
	    out.println("<head>");
	    out.println("<title>Server Status</title>");
	    out.println("</head>");
	    out.println("<body bgcolor=\"white\">");
	    out.println("<center><p>Status is Up!</p></center>");
	    if(ConnectionFactory.checkConnection()){
	    	out.println("<center><p>Connection to DB Successful!</p></center>");
	    } else {
	    	out.println("<center><p>Connection to DB Failed!</p></center>");
	    }
	    out.println("</body>");
	    out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("user") != null && request.getParameter("pass") != null) {

			String username = request.getParameter("user");
			String password = request.getParameter("pass");
			try {
				Player player1 = new Player(username, password);
				List<Game> activeGames = player1.getActiveGames();
				// TODO: It's possible for one user to have multiple sessions
				// going like this. Fix that
				String newSessionId = Session.getUniqueId(player1.getPlayerId());
				Session.put(newSessionId, "games", activeGames);
				Session.put(newSessionId, "player", player1);
				response.setHeader("sessionId", newSessionId);
				response.setHeader("result", "Success!");
			} catch (Exception e) {
				response.setHeader("result", "Error: Username not found.");
			}
		} else {
			response.setHeader("result", "Error: Username not found.");
		}
	}

}
