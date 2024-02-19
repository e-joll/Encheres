package fr.eni.encheres.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.ArticleVenduManager;
import fr.eni.encheres.bll.RetraitManager;
import fr.eni.encheres.bo.Retrait;

/**
 * Servlet qui permet d'annuler une vente
 */
@WebServlet("/annulerVente")
public class AnnulerVenteServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer noArticle = Integer.parseInt(request.getParameter("noArticle"));
		
		try {
			ArticleVenduManager articleManager = new ArticleVenduManager();
			RetraitManager retraitManager = new RetraitManager();
			
			retraitManager.deleteRetraitByArticle(noArticle);
			articleManager.deleteArticle(noArticle);
		} catch (BusinessException e) {
			e.printStackTrace();
			request.setAttribute("listeCodesErreur", e.getListeCodesErreur());
		}
		
		request.getRequestDispatcher("/WEB-INF/jsp/listeEncheres.jsp").forward(request, response);
	}

}
