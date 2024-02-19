package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Utilisateur;

public interface EnchereDAO {

	public void insert(Enchere enchere) throws BusinessException;

	public List<Enchere> selectAll() throws BusinessException;

	public List<Enchere> selectAllByArticle(Integer noArticle) throws BusinessException;

	public List<Enchere> selectAllByUser(Integer noUtilisateur) throws BusinessException;

	public void updateEnchere(Enchere enchere) throws BusinessException;

	public void deleteEncheresByArticle(Integer noArticle) throws BusinessException;

	public void deleteEncheresByUser(Integer noUtilisateur) throws BusinessException;

	public int getMontantActuelByArticle(Integer noArticle) throws BusinessException;

	public void enchereAJour(ArticleVendu article, Utilisateur utilisateur, Integer proposition) throws BusinessException;

	public List<Enchere> selectAllByArticleAndDecreasingAmount(Integer noArticle) throws BusinessException;

}
