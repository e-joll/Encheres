package fr.eni.encheres.dal;

/**
 * Les codes disponibles sont entre 10000 et 19999
 */
public abstract class CodesResultatDAL {

	/**
	 * Echec général quand tentative d'ajouter un objet null
	 */
	public static final int INSERT_OBJET_NULL = 10000;
	

	/**
	 * Echec général quand erreur non gérée à l'insertion
	 */
	public static final int INSERT_OBJET_ECHEC = 10001;

	/**
	 * Echec de la lecture des articles
	 */
	public static final int LECTURE_OBJET_ECHEC = 10002;

	public static final int UPDATE_OBJET_ECHEC = 10003;

	public static final int DELETE_OBJET_ECHEC = 10004;

	public static final int UPDATE_OBJET_NULL = 10005;

	public static final int DELETE_OBJET_NULL = 10006;
}