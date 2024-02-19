package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Categorie;

public interface CategorieDAO {
	
	public void insert(Categorie categorie) throws BusinessException;

	public Categorie selectById(Integer noCategorie) throws BusinessException;
	
	public Categorie selectByLibelle(String libelle) throws BusinessException;

	public List<Categorie> selectAll() throws BusinessException;

	public void updateCategorie(Categorie categorie) throws BusinessException;

	public void deleteCategorie(Integer noCategorie) throws BusinessException;

}
