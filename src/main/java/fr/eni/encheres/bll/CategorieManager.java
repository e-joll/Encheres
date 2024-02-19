package fr.eni.encheres.bll;

import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.dal.CategorieDAO;
import fr.eni.encheres.dal.DAOFactory;

public class CategorieManager {
	private CategorieDAO categorieDAO;

	public CategorieManager() {
		this.categorieDAO = DAOFactory.getCategorieDAO();
	}

	public void insert(Categorie categorie) throws BusinessException {
		BusinessException businessException = new BusinessException();
		validerLibelle(categorie, businessException);
		if (!businessException.hasErreurs()) {
			categorieDAO.insert(categorie);
		} else {
			throw businessException;
		}
	}

	public Categorie selectById(Integer noCategorie) throws BusinessException {
		return categorieDAO.selectById(noCategorie);
	}

	public Categorie selectByLibelle(String libelle) throws BusinessException {
		return categorieDAO.selectByLibelle(libelle);
	}
	
	public List<Categorie> selectAll() throws BusinessException {
		return categorieDAO.selectAll();
	}

	public void updateCategorie(Categorie categorie) throws BusinessException {
		BusinessException businessException = new BusinessException();
		validerLibelle(categorie, businessException);
		if (!businessException.hasErreurs()) {
			categorieDAO.updateCategorie(categorie);
		} else {
			throw businessException;
		}
	}

	public void deleteCategorie(Integer noCategorie) throws BusinessException {
		categorieDAO.deleteCategorie(noCategorie);
	}

	private void validerLibelle(Categorie categorie, BusinessException businessException) {
		if (categorie.getLibelle() == null || categorie.getLibelle().trim().equals("")
				|| categorie.getLibelle().length() > 30) {
			businessException.ajouterErreur(CodesResultatBLL.LIBELLE_ERREUR);
		}
	}
}
