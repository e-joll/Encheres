package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.ArticleVendu;

public interface ArticleVenduDAO {

	public void insert(ArticleVendu articleVendu) throws BusinessException;

	public ArticleVendu selectById(Integer noArticle) throws BusinessException;

	public List<ArticleVendu> selectAll() throws BusinessException;

	public List<ArticleVendu> selectByMotCle(String motCle) throws BusinessException;

	public List<ArticleVendu> selectByMotCleEtCategorie(String motCle, String libelle) throws BusinessException;

	public List<ArticleVendu> selectVentesTermineesParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException;

	public List<ArticleVendu> selectVentesNonDebuteesParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException;

	public List<ArticleVendu> selectVentesEnCoursParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException;

	public List<ArticleVendu> selectEncheresOuvertesParFiltres(String motCle, String libelle) throws BusinessException;

	public List<ArticleVendu> selectEncheresOuvertesParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException;

	public List<ArticleVendu> selectEncheresRemporteesParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException;

	public void updateArticle(ArticleVendu articleVendu) throws BusinessException;

	public void deleteArticle(Integer noArticle) throws BusinessException;


	
	

}
