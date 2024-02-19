package fr.eni.encheres.servlet;

import java.io.IOException;
import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.ArticleVenduManager;
import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.dal.ArticleVenduDAO;
import fr.eni.encheres.dal.jdbc.ArticleVenduDAOJdbcImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui renvoie la page d'accueil et permet de rechercher des articles
 * par mot clé et par catégorie
 */
@WebServlet("")
public class PageAccueilServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			ArticleVenduManager articleManager = new ArticleVenduManager();
			
			List<ArticleVendu> listeArticles = articleManager.selectAll();
			
			request.setAttribute("listeArticles", listeArticles);
		} catch (BusinessException e) {
			e.printStackTrace();
			request.setAttribute("listeCodesErreur",e.getListeCodesErreur());
		}
		
		request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String motCle = request.getParameter("search");
		String categorie = request.getParameter("categorie");
		
		if (categorie.equals("toutes")) {
			categorie = "";
		}
		
		ArticleVenduManager articleManager;
		try {
			articleManager = new ArticleVenduManager();
			
			List<ArticleVendu> listeArticles = articleManager.selectByMotCleEtCategorie(motCle, categorie);
			
			request.setAttribute("listeArticles", listeArticles);
		} catch (BusinessException e) {
			e.printStackTrace();
			request.setAttribute("listeCodesErreur",e.getListeCodesErreur());
		}

		request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
	}

}
