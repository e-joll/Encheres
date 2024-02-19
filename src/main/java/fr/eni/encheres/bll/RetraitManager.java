package fr.eni.encheres.bll;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.dal.DAOFactory;
import fr.eni.encheres.dal.RetraitDAO;

public class RetraitManager {
	private RetraitDAO retraitDAO;

	public RetraitManager() {
		this.retraitDAO = DAOFactory.getRetraitDAO();
	}

	public void insert(Retrait retrait) throws BusinessException {
		BusinessException businessException = new BusinessException();
		validerRetrait(retrait, businessException);
		if (!businessException.hasErreurs()) {
			retraitDAO.insert(retrait);
		} else {
			throw businessException;
		}
	}

	public Retrait selectByArticleId(Integer noArticle) throws BusinessException {
		return retraitDAO.selectByArticleId(noArticle);
	}

	public void updateRetrait(Retrait retrait) throws BusinessException {
		BusinessException businessException = new BusinessException();
		validerRetrait(retrait, businessException);
		if (!businessException.hasErreurs()) {
			retraitDAO.updateRetrait(retrait);
		} else {
			throw businessException;
		}
	}

	public void deleteRetraitByArticle(Integer noArticle) throws BusinessException {
		retraitDAO.deleteRetraitByArticle(noArticle);
	}

	private void validerRetrait(Retrait retrait, BusinessException businessException) {

		if (retrait.getRue() == null || retrait.getRue().trim().equals("") || retrait.getRue().length() > 30) {
			businessException.ajouterErreur(CodesResultatBLL.NOM_RUE_ERREUR);
		}

		if (retrait.getCodePostal() == null || retrait.getCodePostal().trim().equals("")
				|| retrait.getCodePostal().length() > 15) {
			businessException.ajouterErreur(CodesResultatBLL.CODE_POSTAL_ERREUR);
		}
		if (retrait.getVille() == null || retrait.getVille().trim().equals("") || retrait.getVille().length() > 30) {
			businessException.ajouterErreur(CodesResultatBLL.NOM_VILLE_ERREUR);
		}

	}
}
