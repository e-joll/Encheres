package fr.eni.encheres.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * Servlet qui permet la connexion d'un utilisateur
 */
@WebServlet("/connexion")
public class ConnexionServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pseudo = request.getParameter("pseudo");
		String motDePasse = request.getParameter("motDePasse");
		
		List<Integer> listeCodesErreur = new ArrayList<>();

		UtilisateurManager uM = new UtilisateurManager();
		try {
			Utilisateur u = uM.connect(pseudo, motDePasse);

			// Création d'une session de 5 minutes
			HttpSession session = request.getSession();
			session.setMaxInactiveInterval(5 * 60); // 5 minutes en secondes
			session.setAttribute("utilisateur", u); // Stocker l'utilisateur dans la session
		} catch (BusinessException e) {
			e.printStackTrace();
			request.setAttribute("listeCodesErreur", e.getListeCodesErreur());
		}
		
        if (request.getAttribute("listeCodesErreur") != null) {
        	request.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
        }
        else {
        	// Redirection vers la liste des encheres
			response.sendRedirect(request.getContextPath() + "/listeEncheres");
		}
		
	}
}
