package fr.eni.encheres.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.ArticleVenduManager;
import fr.eni.encheres.bll.EnchereManager;
import fr.eni.encheres.bll.RetraitManager;
import fr.eni.encheres.bll.UtilisateurManager;
import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.bo.Utilisateur;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui redirige vers la bonne page entre (detail de l'enchère,
 * modifier/annuler l'enchère, enchérir, vous avez gagné, gagnant)
 */
@WebServlet("/detail")
public class RedirectionDetailEnchereServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Integer noArticle = Integer.parseInt(request.getParameter("noArticle"));

		UtilisateurManager utilisateurManager = new UtilisateurManager();
		ArticleVenduManager articleManager = new ArticleVenduManager();
		RetraitManager retraitManager = new RetraitManager();
		EnchereManager enchereManager = new EnchereManager();

		Utilisateur u = (Utilisateur) request.getSession(false).getAttribute("utilisateur");

		try {
			ArticleVendu article = articleManager.selectById(noArticle);
			Retrait retrait = retraitManager.selectByArticleId(noArticle);
			Utilisateur gagnant = utilisateurManager.gagnant(noArticle);

			request.setAttribute("article", article);
			request.setAttribute("retrait", retrait);
			request.setAttribute("montant_enchere", article.getPrixVente());

			LocalDate dateDebutEnchere = article.getDateDebutEncheres();
			LocalDate dateFinEnchere = article.getDateFinEncheres();

			// L'utilisateur est le vendeur de l'article
			if (u.getNoUtilisateur() == article.getUtilisateur().getNoUtilisateur()) {
				// Si la date de fin de l'enchère est passée, alors on affiche le gagnant
				if (dateFinEnchere.isBefore(LocalDate.now())) {
					request.setAttribute("gagnant", gagnant);
					request.getRequestDispatcher("/WEB-INF/jsp/gagnantEnchere.jsp").forward(request, response);
				} else {
					// Si l'enchère n'est pas commencée, on peut la modifier ou l'annuler
					if (dateDebutEnchere.isAfter(LocalDate.now())) {
						request.getRequestDispatcher("/WEB-INF/jsp/modifierVente.jsp").forward(request, response);
					}
					// Si l'enchère est en cours, on peut consulter le détail
					else {
						request.setAttribute("gagnant", gagnant);
						List<Enchere> listeEncheres = enchereManager.selectAllByArticleAndDecreasingAmount(noArticle);
						request.setAttribute("listeEncheres", listeEncheres);
						request.getRequestDispatcher("/WEB-INF/jsp/detailVente.jsp").forward(request, response);
					}
				}
			}
			// L'utilisateur n'est pas le vendeur de l'article
			// Soit il peut enchérir, soit il a remporté l'enchère
			else {
				// Si la date de fin de l'enchère est passée, l'utilisateur a remporté l'enchère
				if (dateFinEnchere.isBefore(LocalDate.now())) {
					request.getRequestDispatcher("/WEB-INF/jsp/enchereRemportee.jsp").forward(request, response);
				}
				// Sinon, l'utilisateur peut enchérir
				else {
					request.setAttribute("gagnant", gagnant);
					request.getRequestDispatcher("/WEB-INF/jsp/encherir.jsp").forward(request, response);
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			request.setAttribute("listeCodesErreur", e.getListeCodesErreur());
		}
	}

}
