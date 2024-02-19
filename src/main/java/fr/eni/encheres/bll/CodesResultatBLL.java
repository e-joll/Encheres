package fr.eni.encheres.bll;

/**
 * Les codes disponibles sont entre 20000 et 29999
 */
public abstract class CodesResultatBLL {

	public static final int ARTICLE_NOM_ERREUR = 20000;

	public static final int DESCRIPTION_ERREUR = 20001;

	public static final int DATE_NULL_ERREUR = 20002;

	public static final int DATE_ERREUR = 20003;

	public static final int PRIX_ERREUR = 20004;

	public static final int NOM_RUE_ERREUR = 20005;

	public static final int CODE_POSTAL_ERREUR = 20006;

	public static final int NOM_VILLE_ERREUR = 20007;

	public static final int INSERT_UTILISATEURS_CHAMPS_VIDE = 20008;

	public static final int REGLE_UTILISATEURS_MDP_DIFFERENT = 20009;

	public static final int REGLE_UTILISATEURS_CODE_POSTAL_NON_NUMERIQUE = 20010;

	public static final int REGLE_UTILISATEURS_TELEPHONE_TROP_LONG = 20011;

	public static final int REGLE_UTILISATEURS_EMAIL_INVALIDE = 20012;

	public static final int REGLE_UTILISATEURS_UTILISATEUR_INEXISTANT = 20013;

	public static final int REGLE_UTILISATEURS_ID_DIFFERENT = 20014;

	public static final int CREDIT_INSUFFISANT = 20015;

	public static final int PROPOSITION_INFERIEURE = 20016;

	public static final int ENCHERE_MONTANT_ERREUR = 20017;

	public static final int LIBELLE_ERREUR = 20018;
	
	public static final int ENCHERE_UTILISATEUR_ERREUR = 20019;
	
	public static final int ENCHERE_ARTICLE_ERREUR = 20020;
	
	public static final int ENCHERE_DATE_ERREUR = 20021;
	
	public static final int DATE_ANTERIEURE_ERREUR = 20022;
	
	/**
	 * L'identifiant et le mot de passe ne correspondent à aucun utilisateur
	 */
	public static final int IDENTIFIANT_MDP_ERREUR = 20023;
}
