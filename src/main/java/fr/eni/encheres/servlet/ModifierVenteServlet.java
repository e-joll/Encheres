package fr.eni.encheres.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bll.ArticleVenduManager;
import fr.eni.encheres.bll.CategorieManager;
import fr.eni.encheres.bll.RetraitManager;
import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.bo.Utilisateur;

/**
 * Servlet qui permet de modifier une vente non commencée
 */
@WebServlet("/modifierVente")
public class ModifierVenteServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int noArticle = Integer.parseInt(request.getParameter("noArticle"));
		
		ArticleVenduManager articleManager = new ArticleVenduManager();
		RetraitManager retraitManager = new RetraitManager();
		
		try {
			ArticleVendu article = articleManager.selectById(noArticle);
			Retrait retrait = retraitManager.selectByArticleId(noArticle);
			
			request.setAttribute("article", article);
			request.setAttribute("retrait", retrait);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("/WEB-INF/jsp/modifierVente.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nom = request.getParameter("article");
        String description = request.getParameter("description");
        String libelle = request.getParameter("categorie");
        Integer miseAPrix;
        if (request.getParameter("miseAPrix").equals("")) {
        	miseAPrix = 0;
        }
        else {
        	miseAPrix = Integer.parseInt(request.getParameter("miseAPrix"));
        }
        LocalDate dateDebutEncheres = LocalDate.parse(request.getParameter("debutEnchere"));
        LocalDate dateFinEncheres = LocalDate.parse(request.getParameter("finEnchere"));
        String rue = request.getParameter("rue");
        String codePostal = request.getParameter("codePostal");
        String ville = request.getParameter("ville");
        
        Utilisateur u = (Utilisateur) request.getSession(false).getAttribute("utilisateur");
        
        ArticleVendu articleModifie = new ArticleVendu();
        articleModifie.setNoArticle(Integer.parseInt(request.getParameter("noArticle")));
        articleModifie.setNomArticle(nom);
        articleModifie.setDescription(description);
        articleModifie.setPrixInitial(miseAPrix);
        articleModifie.setPrixVente(0);
        articleModifie.setDateDebutEncheres(dateDebutEncheres);
        articleModifie.setDateFinEncheres(dateFinEncheres);
        articleModifie.setUtilisateur(u);
        
        Retrait retrait = null;
        
        try {
			CategorieManager categorieManager = new CategorieManager();
			ArticleVenduManager articleManager = new ArticleVenduManager();
			RetraitManager retraitManager = new RetraitManager();
			
			Categorie categorie = categorieManager.selectByLibelle(libelle);
			articleModifie.setCategorie(categorie);
			
			retrait = new Retrait(rue, codePostal, ville, articleModifie);
			
			articleManager.updateArticle(articleModifie);
			retraitManager.updateRetrait(retrait);
		} catch (BusinessException e) {
			e.printStackTrace();
			request.setAttribute("listeCodesErreur",e.getListeCodesErreur());
			request.setAttribute("article", articleModifie);
			request.setAttribute("retrait", retrait);
		}
       
        if (request.getAttribute("listeCodesErreur") != null) {
        	request.getRequestDispatcher("/WEB-INF/jsp/modifierVente.jsp").forward(request, response);
        }
        else {
        	request.getRequestDispatcher("/WEB-INF/jsp/listeEncheres.jsp").forward(request, response);
        }
        
	}

}
