package fr.eni.encheres.servlet;

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
import fr.eni.encheres.dal.ArticleVenduDAO;
import fr.eni.encheres.dal.jdbc.ArticleVenduDAOJdbcImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet qui permet de vendre un article
 */
@WebServlet("/nouvelleVente")
public class NouvelleVenteServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/nouvelleVente.jsp").forward(request, response);
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
        
        ArticleVendu nouvelArticle = new ArticleVendu();
        nouvelArticle.setNomArticle(nom);
        nouvelArticle.setDescription(description);
        nouvelArticle.setPrixInitial(miseAPrix);
        nouvelArticle.setPrixVente(0);
        nouvelArticle.setDateDebutEncheres(dateDebutEncheres);
        nouvelArticle.setDateFinEncheres(dateFinEncheres);
        nouvelArticle.setUtilisateur(u);
        
        try {
			CategorieManager categorieManager = new CategorieManager();
			ArticleVenduManager articleManager = new ArticleVenduManager();
			RetraitManager retraitManager = new RetraitManager();
			
			Categorie categorie = categorieManager.selectByLibelle(libelle);
			nouvelArticle.setCategorie(categorie);
			
			articleManager.insert(nouvelArticle);
			
			Retrait retrait = new Retrait(rue, codePostal, ville, nouvelArticle);
			retraitManager.insert(retrait);
		} catch (BusinessException e) {
			e.printStackTrace();
			request.setAttribute("listeCodesErreur",e.getListeCodesErreur());
			request.setAttribute("article", nouvelArticle);
		}
        
        if (request.getAttribute("listeCodesErreur") != null) {
        	request.getRequestDispatcher("/WEB-INF/jsp/nouvelleVente.jsp").forward(request, response);
        }
        else {
        	request.getRequestDispatcher("/WEB-INF/jsp/listeEncheres.jsp").forward(request, response);
        }
        
	}
}
