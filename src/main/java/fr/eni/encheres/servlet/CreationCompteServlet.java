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

/**
 * Servlet qui permet la création d'un compte
 */
@WebServlet("/creationCompte")
public class CreationCompteServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/creationCompte.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pseudo = request.getParameter("pseudo");
		String nom = request.getParameter("nom");
		String prenom = request.getParameter("prenom");
		String email = request.getParameter("email");
		String telephone = request.getParameter("telephone");
		String rue = request.getParameter("rue");
		String codePostal = request.getParameter("codePostal");
		String ville = request.getParameter("ville");
		String mdp = request.getParameter("mdp");
		String mdpConfirmation = request.getParameter("mdpConfirmation");
		
		List<Integer> listeCodesErreur = new ArrayList<>();
		
		UtilisateurManager utilisateurManager = new UtilisateurManager();
		
		Utilisateur u = new Utilisateur(pseudo, nom, prenom, email, telephone, rue, codePostal, ville, mdp, 0, false);
		
		if(mdp.equals(mdpConfirmation)) {
			try {
				utilisateurManager.insert(u);
				request.getSession().setAttribute("utilisateur", u);
			} catch (BusinessException e) {
				e.printStackTrace();
				request.setAttribute("listeCodesErreur",e.getListeCodesErreur());
			}
		}
		else {
			listeCodesErreur.add(CodesResultatServlets.MDP_DIFFERENTS_ERREUR);
			request.setAttribute("listeCodesErreur",listeCodesErreur);
		}
		
        if (request.getAttribute("listeCodesErreur") != null) {
        	request.setAttribute("user", u);
        	request.getRequestDispatcher("/WEB-INF/jsp/creationCompte.jsp").forward(request, response);
        }
        else {
        	response.sendRedirect(request.getContextPath() + "/listeEncheres");
        }
		
		// TODO : Rajouter la connexion
		
	}
}
