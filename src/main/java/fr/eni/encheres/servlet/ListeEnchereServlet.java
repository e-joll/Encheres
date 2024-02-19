package fr.eni.encheres.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.ArticleVenduManager;
import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.ArticleVenduDAO;
import fr.eni.encheres.dal.jdbc.ArticleVenduDAOJdbcImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui permet d'afficher les enchères avec recherche par mot clé, par
 * catégorie, par achats/ventes
 */
@WebServlet("/listeEncheres")
public class ListeEnchereServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			ArticleVenduManager articleManager = new ArticleVenduManager();

			List<ArticleVendu> listeArticles = articleManager.selectAll();

			request.setAttribute("listeArticles", listeArticles);
		} catch (BusinessException e) {
			e.printStackTrace();
			request.setAttribute("listeCodesErreur", e.getListeCodesErreur());
		}

		request.getRequestDispatcher("/WEB-INF/jsp/listeEncheres.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String motCle = request.getParameter("search");
		String categorie = request.getParameter("categorie");

		// Si on recherche dans toutes les catégories, on met catégorie = "" pour que la
		// requête se fasse sur LIKE '% + "" + %'
		if (categorie.equals("toutes")) {
			categorie = "";
		}

		Utilisateur u = (Utilisateur) request.getSession(false).getAttribute("utilisateur");
		Integer noUtilisateur = u.getNoUtilisateur();

		List<ArticleVendu> listeArticles = new ArrayList<ArticleVendu>();

		try {
			ArticleVenduManager articleManager = new ArticleVenduManager();
			// Si le radio bouton "achats" est sélectionné, on crée le tableau de String
			// checkboxAchatsValues contenant les values des cases cochées
			// ("encheresOuvertes" OU "encheresEnCours" OU "ventesTerminees")
			String[] checkboxAchatsValuesTab = request.getParameterValues("checkboxGroupAchats");
			if (request.getParameter("achatsOuVentes").equals("achats") && checkboxAchatsValuesTab != null) {
				ArrayList<String> checkboxAchatsValues = new ArrayList<String>(Arrays.asList(checkboxAchatsValuesTab));
				if (checkboxAchatsValues != null) {
					if (checkboxAchatsValues.contains("encheresOuvertes")) {
						listeArticles.addAll(articleManager.selectEncheresOuvertesParFiltres(motCle, categorie));
					}
					if (checkboxAchatsValues.contains("encheresEnCours")) {
						// Si "encheresOuvertes" est sélectionné, on ne remet pas les "encheresEnCours"
						// pour qu'on ne les retrouve pas en 2 exemplaires
						if (!checkboxAchatsValues.contains("encheresOuvertes")) {
							listeArticles.addAll(articleManager
									.selectEncheresOuvertesParUtilisateurEtFiltres(noUtilisateur, motCle, categorie));
						}
					}
					if (checkboxAchatsValues.contains("encheresRemportees")) {
						listeArticles.addAll(articleManager
								.selectEncheresRemporteesParUtilisateurEtFiltres(noUtilisateur, motCle, categorie));
					}
				}

				// Si le radio bouton "ventes" est sélectionné, on crée le tableau de String
				// checkboxVentesValues contenant les values des cases cochées ("ventesEnCours"
				// OU "ventesNonDebutees" OU "ventesTerminees")
			} else {
				String[] checkboxVentesValuesTab = request.getParameterValues("checkboxGroupVentes");
				if (checkboxVentesValuesTab != null) {
					ArrayList<String> checkboxVentesValues = new ArrayList<String>(
							Arrays.asList(checkboxVentesValuesTab));
					if (checkboxVentesValues != null) {
						if (checkboxVentesValues.contains("ventesEnCours")) {
							listeArticles.addAll(articleManager
									.selectVentesEnCoursParUtilisateurEtFiltres(noUtilisateur, motCle, categorie));
						}
						if (checkboxVentesValues.contains("ventesNonDebutees")) {
							listeArticles.addAll(articleManager
									.selectVentesNonDebuteesParUtilisateurEtFiltres(noUtilisateur, motCle, categorie));
						}
						if (checkboxVentesValues.contains("ventesTerminees")) {
							listeArticles.addAll(articleManager
									.selectVentesTermineesParUtilisateurEtFiltres(noUtilisateur, motCle, categorie));
						}
					}
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			request.setAttribute("listeCodesErreur", e.getListeCodesErreur());
		}

		request.setAttribute("listeArticles", listeArticles);

		request.getRequestDispatcher("/WEB-INF/jsp/listeEncheres.jsp").forward(request, response);
	}

}
