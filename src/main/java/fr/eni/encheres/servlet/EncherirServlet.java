package fr.eni.encheres.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.ArticleVenduManager;
import fr.eni.encheres.bll.EnchereManager;
import fr.eni.encheres.bll.RetraitManager;
import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.ArticleVenduDAO;
import fr.eni.encheres.dal.RetraitDAO;
import fr.eni.encheres.dal.jdbc.ArticleVenduDAOJdbcImpl;
import fr.eni.encheres.dal.jdbc.RetraitDAOJdbcImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui permet d'enchérir et qui renvoie vers la page des enchères
 */
@WebServlet("/encherir")
public class EncherirServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArticleVenduManager articleManager = new ArticleVenduManager();
		RetraitManager retraitManager = new RetraitManager();
		
		int noArticle = Integer.parseInt(request.getParameter("noArticle"));
		try {
			ArticleVendu article = articleManager.selectById(noArticle);
			Retrait retrait = retraitManager.selectByArticleId(noArticle);
			request.setAttribute("article", article);
			request.setAttribute("retrait", retrait);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		request.setAttribute("noArticle", noArticle);
		
		request.getRequestDispatcher("/WEB-INF/jsp/encherir.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int noArticle = Integer.parseInt(request.getParameter("noArticle"));
		
		String montant_enchere = request.getParameter("montant_enchere");
		
		ArticleVenduManager articleManager = new ArticleVenduManager();
		EnchereManager enchereManager = new EnchereManager();
		RetraitManager retraitManager = new RetraitManager();
		
		if (!montant_enchere.equals("")) {
			Integer prop = Integer.parseInt(montant_enchere);
			
			Utilisateur u = (Utilisateur) request.getSession(false).getAttribute("utilisateur");
			
			try {
				ArticleVendu article = articleManager.selectById(noArticle);
				Enchere enchere = new Enchere(LocalDate.now(), prop, u ,article);
				enchereManager.encherir(enchere);
			} catch (BusinessException e) {
				e.printStackTrace();
				request.setAttribute("listeCodesErreur",e.getListeCodesErreur());
			}
		}
		
		if (request.getAttribute("listeCodesErreur") != null) {
			try {
				ArticleVendu article = articleManager.selectById(noArticle);
				Retrait retrait = retraitManager.selectByArticleId(noArticle);
				request.setAttribute("article", article);
				request.setAttribute("retrait",retrait);
			} catch (BusinessException e) {
				e.printStackTrace();
				request.setAttribute("listeCodesErreur",e.getListeCodesErreur());
			}
			
			request.getRequestDispatcher("/WEB-INF/jsp/encherir.jsp").forward(request, response);
		}
		else {
			response.sendRedirect(request.getContextPath() + "/listeEncheres");
		}
		
	}

}
