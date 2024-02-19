package fr.eni.encheres.servlet;

import java.io.IOException;

import fr.eni.encheres.bll.UtilisateurManager;
import fr.eni.encheres.bo.Utilisateur;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui permet d'afficher un profil
 */
@WebServlet("/profil")
public class AfficherProfilServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pseudo = request.getParameter("pseudo");
		
		UtilisateurManager utilisateurManager = new UtilisateurManager();
		Utilisateur u = utilisateurManager.selectByPseudo(pseudo);
		
		request.setAttribute("user", u);
		
		request.getRequestDispatcher("/WEB-INF/jsp/profil.jsp").forward(request, response);
	}

}
