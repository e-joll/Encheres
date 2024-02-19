package fr.eni.encheres.dal;

import fr.eni.encheres.dal.jdbc.ArticleVenduDAOJdbcImpl;
import fr.eni.encheres.dal.jdbc.CategorieDAOJdbcImpl;
import fr.eni.encheres.dal.jdbc.EnchereDAOJdbcImpl;
import fr.eni.encheres.dal.jdbc.RetraitDAOJdbcImpl;
import fr.eni.encheres.dal.jdbc.UtilisateurDAOJdbcImpl;

public class DAOFactory {

	public static ArticleVenduDAO getArticleVenduDAO() {
		return new ArticleVenduDAOJdbcImpl();
	}

	public static CategorieDAO getCategorieDAO() {
		return new CategorieDAOJdbcImpl();
	}

	public static EnchereDAO getEnchereDAO() {
		return new EnchereDAOJdbcImpl();
	}

	public static RetraitDAO getRetraitDAO() {
		return new RetraitDAOJdbcImpl();
	}

	public static UtilisateurDAO getUtilisateurDAO() {
		return new UtilisateurDAOJdbcImpl();
	}

}
