package fr.eni.encheres.bo;

import java.time.LocalDate;

public class Enchere {

	private LocalDate dateEnchere;
	private Integer montantEnchere;
	private Utilisateur utilisateur;
	private ArticleVendu article;

	// Constructeurs

	public Enchere() {
	}

	public Enchere(LocalDate dateEnchere, Integer montantEnchere, Utilisateur utilisateur, ArticleVendu article) {
		this.dateEnchere = dateEnchere;
		this.montantEnchere = montantEnchere;
		this.utilisateur = utilisateur;
		this.article = article;
	}

	// Getters et Setters

	public LocalDate getDateEnchere() {
		return dateEnchere;
	}

	public void setDateEnchere(LocalDate dateEnchere) {
		this.dateEnchere = dateEnchere;
	}

	public Integer getMontantEnchere() {
		return montantEnchere;
	}

	public void setMontantEnchere(Integer montantEnchere) {
		this.montantEnchere = montantEnchere;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public ArticleVendu getArticle() {
		return article;
	}

	public void setArticle(ArticleVendu article) {
		this.article = article;
	}

	@Override
	public String toString() {
		return "Enchere [dateEnchere=" + dateEnchere + ", montantEnchere=" + montantEnchere + ", utilisateur="
				+ utilisateur + ", article=" + article + "]";
	}

}
