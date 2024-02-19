package fr.eni.encheres.bll;

import java.time.LocalDate;
import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.ArticleVenduDAO;
import fr.eni.encheres.dal.DAOFactory;
import fr.eni.encheres.dal.EnchereDAO;
import fr.eni.encheres.dal.UtilisateurDAO;

public class EnchereManager {
	private EnchereDAO enchereDAO;
	private UtilisateurDAO utilisateurDAO;
	private ArticleVenduDAO articleDAO;

	public EnchereManager() {
		super();
		this.enchereDAO = DAOFactory.getEnchereDAO();
		this.utilisateurDAO = DAOFactory.getUtilisateurDAO();
		this.articleDAO = DAOFactory.getArticleVenduDAO();
	}

	public void insert(Enchere enchere) throws BusinessException {
		BusinessException businessException = new BusinessException();
		validerEnchere(enchere, businessException);
		if (!businessException.hasErreurs()) {
			enchereDAO.insert(enchere);
		} else {
			throw businessException;
		}
	}

	public List<Enchere> selectAll() throws BusinessException {
		return enchereDAO.selectAll();
	}

	public List<Enchere> selectAllByArticle(Integer noArticle) throws BusinessException {
		return enchereDAO.selectAllByArticle(noArticle);
	}

	public List<Enchere> selectAllByArticleAndDecreasingAmount(Integer noArticle) throws BusinessException {
		return enchereDAO.selectAllByArticleAndDecreasingAmount(noArticle);
	}
	
	public List<Enchere> selectAllByUser(Integer noUtilisateur) throws BusinessException {
		return enchereDAO.selectAllByUser(noUtilisateur);
	}

	public void updateEnchere(Enchere enchere) throws BusinessException {
		BusinessException businessException = new BusinessException();
		validerEnchere(enchere, businessException);
		if (!businessException.hasErreurs()) {
			enchereDAO.updateEnchere(enchere);
		} else {
			throw businessException;
		}
	}

	public void deleteEncheresByArticle(Integer noArticle) throws BusinessException {
		enchereDAO.deleteEncheresByArticle(noArticle);
	}

	public void deleteEncheresByUser(Integer noUtilisateur) throws BusinessException {
		enchereDAO.deleteEncheresByUser(noUtilisateur);
	}

	public void validerEnchere(Enchere enchere, BusinessException businessException) {

		if (enchere.getUtilisateur().getNoUtilisateur() == null || enchere.getUtilisateur().getNoUtilisateur() <= 0) {
			businessException.ajouterErreur(CodesResultatBLL.ENCHERE_UTILISATEUR_ERREUR);
		}

		if (enchere.getArticle().getNoArticle() == null || enchere.getArticle().getNoArticle() <= 0) {
			businessException.ajouterErreur(CodesResultatBLL.ENCHERE_ARTICLE_ERREUR);
		}

		if (enchere.getDateEnchere() == null || enchere.getDateEnchere().isBefore(LocalDate.now())
				|| enchere.getDateEnchere().isAfter(enchere.getArticle().getDateFinEncheres())) {
			businessException.ajouterErreur(CodesResultatBLL.ENCHERE_DATE_ERREUR);
		}

		if (enchere.getMontantEnchere() == null
				|| enchere.getMontantEnchere() <= enchere.getArticle().getPrixInitial()) {
			businessException.ajouterErreur(CodesResultatBLL.ENCHERE_MONTANT_ERREUR);
		}
	}

	public void encherir(Enchere enchere) throws BusinessException {

		// le montant actuel de l'enchère pour cet article
		int montantActuel = enchere.getArticle().getPrixVente();
		// Vérification si la proposition est supérieure au crédit de l'utilisateur
		if (enchere.getMontantEnchere() > enchere.getUtilisateur().getCredit()) {
			// Si la proposition est supérieure au crédit, on lance une exception
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatBLL.CREDIT_INSUFFISANT);
			throw businessException;
		}
		// Vérification si la proposition est supérieure au montant actuel et initial de
		// l'enchère
		if (enchere.getMontantEnchere() <= montantActuel) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatBLL.PROPOSITION_INFERIEURE);
			throw businessException;
		} else {
			if (enchere.getMontantEnchere() < enchere.getArticle().getPrixInitial()) {
				BusinessException businessException = new BusinessException();
				businessException.ajouterErreur(CodesResultatBLL.ENCHERE_MONTANT_ERREUR);
				throw businessException;
			} else {
				int nouveauCreditUtilisateur = enchere.getUtilisateur().getCredit() - enchere.getMontantEnchere();
				// Mettre à jour le crédit de l'utilisateur dans la base de données
				enchere.getUtilisateur().setCredit(nouveauCreditUtilisateur);
				utilisateurDAO.updateCredit(enchere.getUtilisateur());

				// On récupère l'ancien utilisateur gagnant de l'enchère
				Utilisateur ancienUtilisateur = utilisateurDAO.gagnant(enchere.getArticle().getNoArticle());

				// Vérification s'il y a un ancien utilisateur
				if (ancienUtilisateur != null) {
					// Recréditer l'ancien utilisateur
					int nouveauCreditAncienUtilisateur = ancienUtilisateur.getCredit() + montantActuel;
					ancienUtilisateur.setCredit(nouveauCreditAncienUtilisateur);
					utilisateurDAO.update(ancienUtilisateur);
				}

				// Insert de l'enchère
				enchereDAO.insert(enchere);

				// Mise a jour prix vente de l'article
				enchere.getArticle().setPrixVente(enchere.getMontantEnchere());
				articleDAO.updateArticle(enchere.getArticle());
			}
		}
	}

	public int getMontantActuelByArticle(int noArticle) throws BusinessException {
		return enchereDAO.getMontantActuelByArticle(noArticle);
	}
}
