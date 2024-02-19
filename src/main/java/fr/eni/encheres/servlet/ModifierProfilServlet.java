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
 * Servlet qui permet de modifier son profil
 */
@WebServlet("/modifierProfil")
public class ModifierProfilServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
//		UtilisateurManager utilisateurManager = new UtilisateurManager();
		Utilisateur u = (Utilisateur) request.getSession(false).getAttribute("utilisateur");
		System.out.println(u);
		try {
//			u = utilisateurManager.selectById(6);
			request.setAttribute("user", u);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		request.getRequestDispatcher("/WEB-INF/jsp/modifierProfil.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pseudo = request.getParameter("pseudo");
		String nom = request.getParameter("nom");
		String prenom = request.getParameter("prenom");
		String email = request.getParameter("email");
		String telephone = request.getParameter("telephone");
		String rue = request.getParameter("rue");
		String codePostal = request.getParameter("codePostal");
		String ville = request.getParameter("ville");
		String mdpActuel = request.getParameter("mdpActuel");
		String mdpNouveau = request.getParameter("mdpNouveau");
		String mdpConfirmation = request.getParameter("mdpConfirmation");
		
		List<Integer> listeCodesErreur = new ArrayList<>();
		
		UtilisateurManager utilisateurManager = new UtilisateurManager();
		
		Utilisateur u = (Utilisateur) request.getSession(false).getAttribute("utilisateur");
		
		try {
			if (mdpNouveau.equals(mdpConfirmation)) {
				u.setPseudo(pseudo);
				u.setNom(nom);
				u.setPrenom(prenom);
				u.setEmail(email);
				u.setTelephone(telephone);
				u.setRue(rue);
				u.setCodePostal(codePostal);
				u.setVille(ville);
				u.setMotDePasse(mdpNouveau);
				utilisateurManager.update(u);
			}
			else {
				listeCodesErreur.add(CodesResultatServlets.MDP_DIFFERENTS_ERREUR);
				request.setAttribute("listeCodesErreur",listeCodesErreur);
			}
			request.setAttribute("user", u);
		} catch (BusinessException e) {
			e.printStackTrace();
			request.setAttribute("listeCodesErreur",e.getListeCodesErreur());
		}
		
		request.getRequestDispatcher("/WEB-INF/jsp/modifierProfil.jsp").forward(request, response);

	}

}
