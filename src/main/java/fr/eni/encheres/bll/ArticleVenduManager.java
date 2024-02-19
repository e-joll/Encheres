package fr.eni.encheres.bll;

import java.time.LocalDate;
import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.dal.ArticleVenduDAO;
import fr.eni.encheres.dal.DAOFactory;

public class ArticleVenduManager {
	private ArticleVenduDAO articleVenduDAO;

	public ArticleVenduManager() {
		this.articleVenduDAO = DAOFactory.getArticleVenduDAO();
	}

	public void insert(ArticleVendu articleVendu) throws BusinessException {
		// Création d'une nouvelle instance de BusinessException pour collecter
		// d'éventuelles erreurs
		BusinessException businessException = new BusinessException();
		// Validation de l'article en appelant la méthode validerArticle avec l'instance
		// de BusinessException
		this.validerArticle(articleVendu, businessException);
		// Vérification s'il y a des erreurs après la validation
		if (!businessException.hasErreurs()) {
			// Si aucune erreur n'est détectée, procéder à l'insertion de l'article dans la
			// base de données
			articleVenduDAO.insert(articleVendu);
		} else {
			// Si des erreurs sont présentes, lancer une BusinessException pour signaler les
			// erreurs
			throw businessException;
		}
	}

	public ArticleVendu selectById(Integer noArticle) throws BusinessException {
		return articleVenduDAO.selectById(noArticle);
	}

	public List<ArticleVendu> selectAll() throws BusinessException {
		return articleVenduDAO.selectAll();
	}

	public List<ArticleVendu> selectByMotCle(String motCle) throws BusinessException {
		return articleVenduDAO.selectByMotCle(motCle);
	}

	public List<ArticleVendu> selectByMotCleEtCategorie(String motCle, String libelle) throws BusinessException {
		return articleVenduDAO.selectByMotCleEtCategorie(motCle, libelle);
	}

	public List<ArticleVendu> selectVentesTermineesParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException {
		return articleVenduDAO.selectVentesTermineesParUtilisateurEtFiltres(noUtilisateur, motCle, libelle);
	}

	public List<ArticleVendu> selectVentesNonDebuteesParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException {
		return articleVenduDAO.selectVentesNonDebuteesParUtilisateurEtFiltres(noUtilisateur, motCle, libelle);
	}

	public List<ArticleVendu> selectVentesEnCoursParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException {
		return articleVenduDAO.selectVentesEnCoursParUtilisateurEtFiltres(noUtilisateur, motCle, libelle);
	}

	public List<ArticleVendu> selectEncheresOuvertesParFiltres(String motCle, String libelle) throws BusinessException {
		return articleVenduDAO.selectEncheresOuvertesParFiltres(motCle, libelle);
	}

	public List<ArticleVendu> selectEncheresOuvertesParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException {
		return articleVenduDAO.selectEncheresOuvertesParUtilisateurEtFiltres(noUtilisateur, motCle, libelle);
	}

	public List<ArticleVendu> selectEncheresRemporteesParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException {
		return articleVenduDAO.selectEncheresRemporteesParUtilisateurEtFiltres(noUtilisateur, motCle, libelle);
	}

	public void updateArticle(ArticleVendu articleVendu) throws BusinessException {
		BusinessException businessException = new BusinessException();
		validerArticle(articleVendu, businessException);

		if (!businessException.hasErreurs()) {
			articleVenduDAO.updateArticle(articleVendu);
		} else {
			throw businessException;
		}
	}

	public void deleteArticle(Integer noArticle) throws BusinessException {
		articleVenduDAO.deleteArticle(noArticle);
	}

	private void validerArticle(ArticleVendu art, BusinessException businessException) {
		// Vérification du nom d'article
		if (art.getNomArticle() == null || art.getNomArticle().trim().equals("") || art.getNomArticle().length() > 30) {
			businessException.ajouterErreur(CodesResultatBLL.ARTICLE_NOM_ERREUR);
		}
		// Vérification de la description
		if (art.getDescription() == null || art.getDescription().trim().equals("")
				|| art.getDescription().length() > 300) {
			businessException.ajouterErreur(CodesResultatBLL.DESCRIPTION_ERREUR);
		}
		// Vérification des dates d'enchères
		if (art.getDateDebutEncheres() == null || art.getDateFinEncheres() == null) {
			businessException.ajouterErreur(CodesResultatBLL.DATE_NULL_ERREUR);
		} else {
			if ((art.getDateDebutEncheres()).isAfter(art.getDateFinEncheres())
					|| (art.getDateDebutEncheres()).isEqual(art.getDateFinEncheres())) {
				businessException.ajouterErreur(CodesResultatBLL.DATE_ERREUR);
			}
			if (art.getDateDebutEncheres().isBefore(LocalDate.now())) {
				businessException.ajouterErreur(CodesResultatBLL.DATE_ANTERIEURE_ERREUR);
			}
		}

		// Vérification du prix initial
		if (art.getPrixInitial() <= 0) {
			businessException.ajouterErreur(CodesResultatBLL.PRIX_ERREUR);
		}
	}
}
