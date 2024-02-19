package fr.eni.encheres.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui permet de se déconnecter et redirige vers la page d'accueil
 */
@WebServlet("/deconnexion")
public class DeconnexionServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getSession(false).invalidate();
		
		response.sendRedirect(request.getContextPath());
	}

}
