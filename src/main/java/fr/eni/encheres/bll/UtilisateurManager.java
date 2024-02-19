package fr.eni.encheres.bll;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.DAOFactory;
import fr.eni.encheres.dal.UtilisateurDAO;

public class UtilisateurManager {

	private UtilisateurDAO utilisateurDAO;

	public UtilisateurManager() {
		this.utilisateurDAO = DAOFactory.getUtilisateurDAO();
	}

	public void insert(Utilisateur u) throws BusinessException {
		BusinessException businessException = new BusinessException();
		this.validerUtilisateur(u, businessException);
		u.setMotDePasse(hashSHA256(u.getMotDePasse()));
		
		if (!businessException.hasErreurs()) {
			utilisateurDAO.insert(u);
		} else {
			throw businessException;
		}
	}

	private void validerUtilisateur(Utilisateur u, BusinessException businessException) {
		// vérification que les champs ne sont pas vides
		if (u.getPseudo().isEmpty() || u.getNom().isEmpty() || u.getPrenom().isEmpty() || u.getEmail().isEmpty()
				|| u.getTelephone().isEmpty() || u.getRue().isEmpty() || u.getCodePostal().isEmpty()
				|| u.getVille().isEmpty() || u.getMotDePasse().isEmpty()) {
			businessException.ajouterErreur(CodesResultatBLL.INSERT_UTILISATEURS_CHAMPS_VIDE);
		}
		// Vérification que le code postal est numérique
		if (!u.getCodePostal().matches("\\d+")) {
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEURS_CODE_POSTAL_NON_NUMERIQUE);
		}
		// Vérification que le numéro de téléphone a un maximum de 10 chiffres
		if (u.getTelephone().length() > 10) {
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEURS_TELEPHONE_TROP_LONG);
		}
		// Vérification que l'adresse e-mail est de type test@test.com ou fr ou autres
		if (!u.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$")) {
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEURS_EMAIL_INVALIDE);
		}
	}

	public Utilisateur connect(String pseudo, String motDePasse) throws BusinessException {
		Utilisateur u = utilisateurDAO.connect(pseudo, hashSHA256(motDePasse));
		if (u == null) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatBLL.IDENTIFIANT_MDP_ERREUR);
			throw businessException;
		}
		return u;
	}

	public Utilisateur selectById(int noUtilisateur) throws Exception {
		return utilisateurDAO.selectById(noUtilisateur);
	}

	public Utilisateur selectByPseudo(String pseudo) {
		try {
			return utilisateurDAO.selectByPseudo(pseudo);
		} catch (Exception e) {
			System.out.println("Erreur lors de la sélection de l'utilisateur par pseudo : " + e.getMessage());
			return null;
		}
	}

	public void update(Utilisateur u) throws BusinessException {
		BusinessException businessException = new BusinessException();
		this.validerUtilisateur(u, businessException);
		u.setMotDePasse(hashSHA256(u.getMotDePasse()));
		
		if (!businessException.hasErreurs()) {
			utilisateurDAO.update(u);
		} else {
			throw businessException;
		}
	}

	public boolean pseudoUtilise(String pseudo) {
		try {
			return utilisateurDAO.pseudoUtilise(pseudo);
		} catch (Exception e) {
			System.out.println("Erreur lors de la vérification de l'utilisation du pseudo : " + e.getMessage());
			return false;
		}
	}

	public boolean mailUtilise(String email) {
		try {
			return utilisateurDAO.mailUtilise(email);
		} catch (Exception e) {
			System.out.println("Erreur lors de la vérification de l'utilisation de l'email : " + e.getMessage());
			return false;
		}
	}

	public Utilisateur gagnant(int noArticle) throws BusinessException {
		try {
			return utilisateurDAO.gagnant(noArticle);
		} catch (NullPointerException e) {
			throw new BusinessException(
					"Erreur lors de la sélection du gagnant : l'article avec l'ID " + noArticle + " n'existe pas.", e);
		} catch (Exception e) {
			throw new BusinessException("Erreur inattendue lors de la sélection du gagnant.", e);
		}
	}

	public void delete(Integer noUtilisateur) throws BusinessException {
		BusinessException businessException = new BusinessException();
		Utilisateur utilisateurEnBase = utilisateurDAO.selectById(noUtilisateur);
		// Vérification que l'utilisateur existe dans la base de données
		if (utilisateurEnBase == null) {
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEURS_UTILISATEUR_INEXISTANT);
		}
		// Vérification s'il y a des erreurs après la validation
		if (!businessException.hasErreurs()) {
			// Si aucune erreur n'est détectée, procéder à la suppression de l'utilisateur
			// dans la base de données
			utilisateurDAO.delete(noUtilisateur);
		} else {
			// Si des erreurs sont présentes, lancer une BusinessException pour signaler les
			// erreurs
			throw businessException;
		}
	}

	public String hashSHA256(String mdp) {
        MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
	        md.update(mdp.getBytes());
	        byte byteData[] = md.digest();

	        // Convertir le tableau de bits en format hexadécimal
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < byteData.length; i++) {
	            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;

	}

}
