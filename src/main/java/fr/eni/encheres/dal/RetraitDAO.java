package fr.eni.encheres.dal;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Retrait;

public interface RetraitDAO {

	public void insert(Retrait retrait) throws BusinessException;

	public Retrait selectByArticleId(Integer noArticle) throws BusinessException;

	public void updateRetrait(Retrait retrait) throws BusinessException;

	public void deleteRetraitByArticle(Integer noArticle) throws BusinessException;

}
