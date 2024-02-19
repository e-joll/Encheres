package fr.eni.encheres.servlet;

import java.io.IOException;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.UtilisateurManager;
import fr.eni.encheres.bo.Utilisateur;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet qui permet de supprimer son propre compte
 */
@WebServlet("/supressionCompte")
public class SuppressionCompteServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Supression
		Utilisateur u = (Utilisateur) request.getSession(false).getAttribute("utilisateur");
		
		UtilisateurManager utilisateurManager = new UtilisateurManager();
		
		try {
			utilisateurManager.delete(u.getNoUtilisateur());
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		
		// Déconnexion
		HttpSession session = request.getSession(false);
		session.invalidate();
		
		request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
	}

}
