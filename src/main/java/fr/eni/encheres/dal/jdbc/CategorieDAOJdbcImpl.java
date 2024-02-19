package fr.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.dal.CategorieDAO;
import fr.eni.encheres.dal.CodesResultatDAL;
import fr.eni.encheres.dal.ConnectionProvider;

public class CategorieDAOJdbcImpl implements CategorieDAO {

	private static final String INSERT_CATEGORIE = "INSERT INTO CATEGORIES (libelle) values (?)";
	private static final String SELECT_BY_ID = "SELECT no_categorie, libelle FROM CATEGORIES WHERE no_categorie=?";
	private static final String SELECT_BY_LIBELLE = "SELECT no_categorie, libelle FROM CATEGORIES WHERE libelle = ?;";
	private static final String SELECT_ALL = "SELECT no_categorie, libelle FROM CATEGORIES";
	private static final String UPDATE_CATEGORIE = "UPDATE CATEGORIES SET libelle = ? WHERE no_categorie=?";
	private static final String DELETE_CATEGORIE = "DELETE CATEGORIES where no_categorie=?";

	@Override
	public void insert(Categorie categorie) throws BusinessException {
		// Vérification que la catégorie à insérer n'est pas null
		if (categorie == null) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_OBJET_NULL);
			throw businessException;
		}

		try (Connection cnx = ConnectionProvider.getConnection()) {
			cnx.setAutoCommit(false); // Désactivation du mode de validation automatique pour la gestion manuelle des
										// transactions

			try (PreparedStatement pstmt = cnx.prepareStatement(INSERT_CATEGORIE,
					PreparedStatement.RETURN_GENERATED_KEYS)) {

				pstmt.setString(1, categorie.getLibelle());

				// Exécution de la requête et récupération du nombre de lignes affectées
				int rowsAffected = pstmt.executeUpdate();

				// Vérification du succès de l'insertion
				if (rowsAffected == 1) {
					try (ResultSet rs = pstmt.getGeneratedKeys()) {
						if (rs.next())
							categorie.setNoCategorie(rs.getInt(1));
					}
					// Validation de la transaction
					cnx.commit();
				}
			} catch (Exception e) {
				e.printStackTrace();
				cnx.rollback();
				throw e;
			}
		} catch (Exception e) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_OBJET_ECHEC);
			throw businessException;
		}
	}

	@Override
	public Categorie selectById(Integer noCategorie) throws BusinessException {

		// Initialise une variable pour stocker la catégorie récupérée depuis la base de
		// données.
		Categorie categorie = null;

		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_BY_ID)) {

			// Paramètre la requête avec l'identifiant de la catégorie à récupérer.
			pstmt.setInt(1, noCategorie);

			try (ResultSet rs = pstmt.executeQuery()) {

				if (rs.next()) {
					categorie = new Categorie(rs.getInt("no_categorie"), rs.getString("libelle"));
				}
			}

		} catch (Exception e) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}

		return categorie;
	}
	
	@Override
	public Categorie selectByLibelle(String libelle) throws BusinessException {

		// Initialise une variable pour stocker la catégorie récupérée depuis la base de
		// données.
		Categorie categorie = null;

		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_BY_LIBELLE)) {

			// Paramètre la requête avec l'identifiant de la catégorie à récupérer.
			pstmt.setString(1, libelle);

			try (ResultSet rs = pstmt.executeQuery()) {

				if (rs.next()) {
					categorie = new Categorie(rs.getInt("no_categorie"), rs.getString("libelle"));
				}
			}

		} catch (Exception e) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}

		return categorie;
	}

	@Override
	public List<Categorie> selectAll() throws BusinessException {
		List<Categorie> categories = new ArrayList<Categorie>();
		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_ALL);
				ResultSet rs = pstmt.executeQuery()) {
			// Itération sur les résultats du ResultSet
			while (rs.next()) {
				Integer noCategorie = rs.getInt("no_categorie");
				String libelle = rs.getString("libelle");

				Categorie categorie = new Categorie(noCategorie, libelle);

				categories.add(categorie);
			}

		} catch (Exception e) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}
		return categories;
	}

	@Override
	public void updateCategorie(Categorie categorie) throws BusinessException {
		// Vérification que a categorie à mettre à jour n'est pas null
		if (categorie == null) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_OBJET_NULL);
			throw businessException;
		}
		try (Connection cnx = ConnectionProvider.getConnection()) {
			// Désactivation du mode de validation automatique pour la gestion manuelle des
			// transactions
			cnx.setAutoCommit(false);

			try (PreparedStatement pstmt = cnx.prepareStatement(UPDATE_CATEGORIE)) {

				pstmt.setString(1, categorie.getLibelle());
				pstmt.setInt(2, categorie.getNoCategorie());

				int rowsUpdated = pstmt.executeUpdate();
				// Vérification du succès de la mise à jour
				if (rowsUpdated > 0) {
					cnx.commit();
				}

			} catch (Exception e) {
				e.printStackTrace();
				cnx.rollback();
				throw e;
			}
		} catch (Exception e) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_OBJET_ECHEC);
			throw businessException;
		}
	}

	@Override
	public void deleteCategorie(Integer noCategorie) throws BusinessException {
		if (noCategorie == null || noCategorie <= 0) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.DELETE_OBJET_NULL);
			throw businessException;
		}

		try (Connection cnx = ConnectionProvider.getConnection()) {
			// Désactivation du mode de validation automatique pour la gestion manuelle des
			// transactions
			cnx.setAutoCommit(false);

			// Vérification si la catégorie existe avant de la supprimer
			if (categorieExists(cnx, noCategorie)) {
				try (PreparedStatement pstmt = cnx.prepareStatement(DELETE_CATEGORIE)) {
					pstmt.setInt(1, noCategorie);

					int rowsDeleted = pstmt.executeUpdate();

					// Vérification du succès de la suppression
					if (rowsDeleted > 0) {
						// Validation de la transaction
						cnx.commit();
					}
				} catch (Exception e) {
					e.printStackTrace();
					// En cas d'erreur, rollback la transaction
					cnx.rollback();
					throw e;
				}
			}
		} catch (Exception e) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.DELETE_OBJET_ECHEC);
			throw businessException;
		}
	}

	private boolean categorieExists(Connection cnx, int noCategorie) throws BusinessException, SQLException {
		try (PreparedStatement pstmt = cnx.prepareStatement("SELECT 1 FROM CATEGORIES WHERE no_categorie = ?")) {
			pstmt.setInt(1, noCategorie);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				return resultSet.next(); // Retourne true si la catégorie existe, false sinon
			}
		}
	}

}