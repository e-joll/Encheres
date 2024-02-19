package fr.eni.encheres.dal;

import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurDAO {

	public void insert(Utilisateur u) throws BusinessException;

	public Utilisateur connect(String pseudo, String motDePasse) throws BusinessException;

	public void update(Utilisateur u) throws BusinessException;

	Utilisateur selectById(Integer no_Utilisateur) throws BusinessException;

	Utilisateur selectByPseudo(String pseudo) throws BusinessException;

	public List<Utilisateur> selectAll() throws BusinessException;

	public void delete(Integer noUtilisateur) throws BusinessException;

	public boolean pseudoUtilise(String pseudo);

	public boolean mailUtilise(String email);

	public Utilisateur gagnant(Integer no_article) throws BusinessException;

	public void updateCredit(Utilisateur u) throws BusinessException;
	
}
