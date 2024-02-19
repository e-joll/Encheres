package fr.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.CodesResultatDAL;
import fr.eni.encheres.dal.ConnectionProvider;
import fr.eni.encheres.dal.UtilisateurDAO;

public class UtilisateurDAOJdbcImpl implements UtilisateurDAO {

	private static final String INSERT_UTILISATEUR = "INSERT INTO UTILISATEURS(pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String SELECT_BY_ID = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur FROM UTILISATEURS WHERE no_utilisateur = ?;";
	private static final String UPDATE = "UPDATE UTILISATEURS SET pseudo = ?, nom = ?, prenom = ?, email = ?, telephone = ?, rue = ?, code_postal = ?, ville = ?, mot_de_passe = ?, credit = ?, administrateur = ? WHERE no_utilisateur = ?;";
	private static final String DELETE_UTILISATEUR = "DELETE FROM UTILISATEURS WHERE no_utilisateur = ?;";
	private static final String SELECT_ALL = "SELECT * FROM UTILISATEURS;";
	private static final String SELECT_BY_IDENTIFIANT = "SELECT * FROM UTILISATEURS WHERE (pseudo = ? OR email = ?) AND mot_de_passe = ?";
	private static final String PSEUDOEXISTE = "SELECT pseudo FROM UTILISATEURS WHERE pseudo = ?";
	private static final String EMAILEXISTE = "SELECT email FROM UTILISATEURS WHERE email = ?";
	private static final String SELECTBYPSEUDO = "SELECT * FROM UTILISATEURS WHERE pseudo = ?";
	private static final String GAGNANT = "SELECT * , E1.montant_enchere AS montant_gagnant " + "FROM ENCHERES E1 "
			+ "JOIN UTILISATEURS U ON E1.no_utilisateur = U.no_utilisateur "
			+ "WHERE E1.no_article = ? AND E1.montant_enchere = ( " + "    SELECT MAX(E2.montant_enchere) "
			+ "    FROM ENCHERES E2 " + " WHERE E1.no_article = E2.no_article " + ")";
	private static final String UPDATE_CREDIT = "UPDATE UTILISATEURS SET credit = ? WHERE no_utilisateur = ?";
	private static final String CHECKUSEREXISTE = "SELECT COUNT(*) FROM UTILISATEURS WHERE no_utilisateur = ?";

	// Inscription de l'utilisateur
	@Override
	public void insert(Utilisateur u) throws BusinessException {
		if (u == null) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_OBJET_NULL);
			throw businessException;
		}
		try (Connection con = ConnectionProvider.getConnection();
				PreparedStatement pstmt = con.prepareStatement(INSERT_UTILISATEUR, Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, u.getPseudo());
			pstmt.setString(2, u.getNom());
			pstmt.setString(3, u.getPrenom());
			pstmt.setString(4, u.getEmail());
			pstmt.setString(5, u.getTelephone());
			pstmt.setString(6, u.getRue());
			pstmt.setString(7, u.getCodePostal());
			pstmt.setString(8, u.getVille());
			pstmt.setString(9, u.getMotDePasse());
			pstmt.setLong(10, u.getCredit());
			pstmt.setBoolean(11, u.isAdministrateur());

			pstmt.executeUpdate();
			try (ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next()) {
					u.setNoUtilisateur(rs.getInt(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_OBJET_ECHEC);
			throw businessException;
		}
	}

	// Connection de l'utilisateur
	@Override
	public Utilisateur connect(String pseudo, String motDePasse) throws BusinessException {
		Utilisateur u = null;
		try {
			Connection con = ConnectionProvider.getConnection();
			PreparedStatement pstmt = con.prepareStatement(SELECT_BY_IDENTIFIANT);
			pstmt.setString(1, pseudo);
			pstmt.setString(2, pseudo);
			pstmt.setString(3, motDePasse);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				u = getUtilisateursFromResultSet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}
		return u;
	}

	// Mise a jour de l'utilisateur
	@Override
	public void update(Utilisateur u) throws BusinessException {
		// Connexion à la base de données
		try {
			Connection con = ConnectionProvider.getConnection();
			PreparedStatement pstmt = con.prepareStatement(UPDATE);
			pstmt.setString(1, u.getPseudo());
			pstmt.setString(2, u.getNom());
			pstmt.setString(3, u.getPrenom());
			pstmt.setString(4, u.getEmail());
			pstmt.setString(5, u.getTelephone());
			pstmt.setString(6, u.getRue());
			pstmt.setString(7, u.getCodePostal());
			pstmt.setString(8, u.getVille());
			pstmt.setString(9, u.getMotDePasse());
			pstmt.setLong(10, u.getCredit());
			pstmt.setBoolean(11, u.isAdministrateur());
			pstmt.setInt(12, u.getNoUtilisateur());
			// Exécution de la requête SQL Update
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_OBJET_ECHEC);
			throw businessException;
		}

	}

	// Afficher un utilisateur par son id
	@Override
	public Utilisateur selectById(Integer noUtilisateur) throws BusinessException {
		Utilisateur u = null;
		try (Connection con = ConnectionProvider.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SELECT_BY_ID)) {
			pstmt.setInt(1, noUtilisateur);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
					u = getUtilisateursFromResultSet(rs);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				BusinessException businessException = new BusinessException();
				businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
				throw businessException;
			}
			return u;
	}

	// Afficher tout les utilisateurs
	@Override
	public List<Utilisateur> selectAll() throws BusinessException {
		List<Utilisateur> utilisateurs = new ArrayList<>();
		try (Connection con = ConnectionProvider.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
			while (rs.next()) {
				utilisateurs.add(getUtilisateursFromResultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}
		return utilisateurs;
	}

	private Utilisateur getUtilisateursFromResultSet(ResultSet rs) throws SQLException {
		int noUtilisateur = rs.getInt("no_utilisateur");
		String pseudo = rs.getString("pseudo");
		String nom = rs.getString("nom");
		String prenom = rs.getString("prenom");
		String email = rs.getString("email");
		String telephone = rs.getString("telephone");
		String rue = rs.getString("rue");
		String code_postal = rs.getString("code_postal");
		String ville = rs.getString("ville");
		String mot_de_passe = rs.getString("mot_de_passe");
		int credit = rs.getInt("credit");
		boolean administrateur = rs.getBoolean("administrateur");
		Utilisateur u = new Utilisateur(noUtilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville,
				mot_de_passe, credit, administrateur);
		return u;
	}

	// Supprimer un utilisateur
	@Override
	public void delete(Integer noUtilisateur) throws BusinessException {
		try (Connection con = ConnectionProvider.getConnection();
				PreparedStatement pstmt = con.prepareStatement(DELETE_UTILISATEUR)) {
			pstmt.setInt(1, noUtilisateur);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.DELETE_OBJET_ECHEC);
			throw businessException;
		}
	}

	// Verifie si le pseudo est déjà utilisé dans la bdd
	@Override
	public boolean pseudoUtilise(String pseudo) {
		try (Connection con = ConnectionProvider.getConnection();
				PreparedStatement pstmt = con.prepareStatement(PSEUDOEXISTE)) {
			pstmt.setString(1, pseudo);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return true; // Le pseudo existe déjà
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false; // Le pseudo n'existe pas
	}

	// Verifie si l'email est déjà utilisée dans la bdd
	@Override
	public boolean mailUtilise(String email) {
		try (Connection con = ConnectionProvider.getConnection();
				PreparedStatement pstmt = con.prepareStatement(EMAILEXISTE)) {
			pstmt.setString(1, email);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return true; // L'email existe déjà
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false; // L'email n'existe pas
	}

	// Afficher un utlilisateur par son pseudo
	@Override
	public Utilisateur selectByPseudo(String pseudo) throws BusinessException {
		Utilisateur u = null;
		try (Connection con = ConnectionProvider.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SELECTBYPSEUDO)) {
			pstmt.setString(1, pseudo);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					u = getUtilisateursFromResultSet(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}
		return u;
	}

	@Override
	public Utilisateur gagnant(Integer no_article) throws BusinessException {
		Utilisateur u = null;
		try (Connection con = ConnectionProvider.getConnection();
				PreparedStatement pstmt = con.prepareStatement(GAGNANT)) {
			pstmt.setInt(1, no_article);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					u = getUtilisateursFromResultSet(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}
		return u;
	}

	@Override
	public void updateCredit(Utilisateur u) throws BusinessException {
		try (Connection con = ConnectionProvider.getConnection();
				PreparedStatement pstmt = con.prepareStatement(UPDATE_CREDIT)) {
			pstmt.setLong(1, u.getCredit());
			pstmt.setInt(2, u.getNoUtilisateur());
			pstmt.executeUpdate();
			}
		 catch (SQLException e) {
				BusinessException businessException = new BusinessException();
				businessException.ajouterErreur(CodesResultatDAL.UPDATE_OBJET_ECHEC);
				throw businessException;
		}
	}
}
