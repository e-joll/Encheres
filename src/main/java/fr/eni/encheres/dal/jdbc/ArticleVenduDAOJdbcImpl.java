package fr.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.ArticleVenduDAO;
import fr.eni.encheres.dal.CategorieDAO;
import fr.eni.encheres.dal.CodesResultatDAL;
import fr.eni.encheres.dal.ConnectionProvider;
import fr.eni.encheres.dal.UtilisateurDAO;

public class ArticleVenduDAOJdbcImpl implements ArticleVenduDAO {

	public static final String INSERT_ARTICLES = "INSERT INTO ARTICLES_VENDUS (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String SELECT_BY_ID = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie FROM ARTICLES_VENDUS where no_article = ?";
	public static final String SELECT_ALL = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie FROM ARTICLES_VENDUS";
	public static final String SELECT_BY_MOT_CLE = "SELECT * FROM ARTICLES_VENDUS WHERE nom_article LIKE ?";
	public static final String SELECT_BY_MOTCLE_AND_CATEGORIE = "SELECT * FROM ARTICLES_VENDUS A JOIN CATEGORIES C ON C.no_categorie = A.no_categorie WHERE LOWER(A.nom_article) LIKE ? AND C.libelle LIKE ?";
	public static final String SELECT_FINISHED_SALES_BY_USER_AND_FILTERS = "SELECT * FROM ARTICLES_VENDUS A JOIN CATEGORIES C ON C.no_categorie = A.no_categorie WHERE A.no_utilisateur = ? AND GETDATE() > A.date_fin_encheres AND LOWER(A.nom_article) LIKE ? AND C.libelle LIKE ?";
	public static final String SELECT_UNSTARTED_SALES_BY_USER_AND_FILTERS = "SELECT * FROM ARTICLES_VENDUS A JOIN CATEGORIES C ON C.no_categorie = A.no_categorie WHERE A.no_utilisateur = ? AND A.date_debut_encheres > GETDATE() AND LOWER(A.nom_article) LIKE ? AND C.libelle LIKE ?";
	public static final String SELECT_ONGOING_SALES_BY_USER_AND_FILTERS = "SELECT * FROM ARTICLES_VENDUS A JOIN CATEGORIES C ON C.no_categorie = A.no_categorie WHERE A.no_utilisateur = ? AND A.date_debut_encheres < GETDATE() AND A.date_fin_encheres > GETDATE() AND LOWER(A.nom_article) LIKE ? AND C.libelle LIKE ?";
	public static final String SELECT_OPEN_AUCTIONS_BY_FILTERS = "SELECT * FROM ARTICLES_VENDUS A JOIN CATEGORIES C ON C.no_categorie = A.no_categorie WHERE A.date_debut_encheres < GETDATE() AND A.date_fin_encheres > GETDATE() AND LOWER(A.nom_article) LIKE ? AND C.libelle LIKE ?";
	public static final String SELECT_OPEN_AUCTIONS_BY_USER_AND_FILTERS = "SELECT DISTINCT A.no_article, A.nom_article, A.date_debut_encheres, A.date_fin_encheres, A.description, A.no_categorie, A.no_utilisateur, A.prix_initial, A.prix_vente FROM ARTICLES_VENDUS A JOIN ENCHERES E ON A.no_article = E.no_article JOIN CATEGORIES C ON C.no_categorie = A.no_categorie WHERE E.no_utilisateur = ? AND A.date_debut_encheres < GETDATE() AND A.date_fin_encheres > GETDATE() AND LOWER(A.nom_article) LIKE ? AND C.libelle LIKE ?";
	public static final String SELECT_WON_AUCTIONS_BY_USER_AND_FILTERS = "SELECT * FROM ENCHERES E1 JOIN UTILISATEURS U ON E1.no_utilisateur = U.no_utilisateur JOIN ARTICLES_VENDUS A ON A.no_article = E1.no_article JOIN CATEGORIES C ON C.no_categorie = A.no_categorie WHERE E1.montant_enchere = (SELECT MAX(E2.montant_enchere) FROM ENCHERES E2 WHERE E1.no_article = E2.no_article) AND U.no_utilisateur = ? AND LOWER(A.nom_article) LIKE ? AND C.libelle LIKE ? AND A.date_fin_encheres < GETDATE();";
	public static final String UPDATE_ARTICLE = "UPDATE ARTICLES_VENDUS SET nom_article=?, description=?, date_debut_encheres=?, date_fin_encheres=?, prix_initial=?, prix_vente=?, no_utilisateur=?, no_categorie=? where no_article = ?";
	public static final String DELETE_ARTICLE = "DELETE From ARTICLES_VENDUS where no_article=?";

	// Initialisation d'une instance de UtilisateurDAOJdbcImpl pour gérer l'accès
	// aux données des utilisateurs depuis la base de données.
	UtilisateurDAO utilisateurDAO = new UtilisateurDAOJdbcImpl();
	// Initialisation d'une instance de CategorieDAOJdbcImpl pour gérer l'accès aux
	// données des catégories depuis la base de données.
	CategorieDAO categorieDAO = new CategorieDAOJdbcImpl();

	@Override
	public void insert(ArticleVendu articleVendu) throws BusinessException {
		// Verification que l'article à insérer n'est pas null
		if (articleVendu == null) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_OBJET_NULL);
			throw businessException;
		}
		try (Connection cnx = ConnectionProvider.getConnection()) {

			cnx.setAutoCommit(false); // Désactivation du mode de validation automatique pour la gestion manuelle des
										// transactions

			try (PreparedStatement pstmt = cnx.prepareStatement(INSERT_ARTICLES,
					PreparedStatement.RETURN_GENERATED_KEYS)) {

				pstmt.setString(1, articleVendu.getNomArticle());
				pstmt.setString(2, articleVendu.getDescription());
				pstmt.setDate(3, java.sql.Date.valueOf(articleVendu.getDateDebutEncheres()));
				pstmt.setDate(4, java.sql.Date.valueOf(articleVendu.getDateFinEncheres()));
				pstmt.setInt(5, articleVendu.getPrixInitial());
				pstmt.setInt(6, articleVendu.getPrixVente());
				pstmt.setInt(7, articleVendu.getUtilisateur().getNoUtilisateur());
				pstmt.setInt(8, articleVendu.getCategorie().getNoCategorie());

				// Exécution de la requête et récupération du nombre de lignes affectées
				int rowsAffected = pstmt.executeUpdate();

				// Vérification du succès de l'insertion
				if (rowsAffected == 1) {
					try (ResultSet rs = pstmt.getGeneratedKeys()) {
						if (rs.next())
							articleVendu.setNoArticle(rs.getInt(1));
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
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_OBJET_ECHEC);
			throw businessException;
		}
	}

	@Override
	public ArticleVendu selectById(Integer noArticle) throws BusinessException {
		ArticleVendu articleVendu = null;
		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_BY_ID)) {
			pstmt.setInt(1, noArticle);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					articleVendu = getArticleFromRs(rs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}
		return articleVendu;
	}

	@Override
	public List<ArticleVendu> selectAll() throws BusinessException {
		List<ArticleVendu> listArticle = new ArrayList<ArticleVendu>();
		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_ALL);
				ResultSet rs = pstmt.executeQuery()) {
			// Itération sur les résultats du ResultSet
			while (rs.next()) {
				// Ajout de chaque article à la liste
				listArticle.add(getArticleFromRs(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}
		return listArticle;
	}

	@Override
	public List<ArticleVendu> selectByMotCle(String motCle) throws BusinessException {
		List<ArticleVendu> listArticle = new ArrayList<>();

		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_BY_MOT_CLE)) {

			pstmt.setString(1, "%" + motCle + "%");

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					listArticle.add(getArticleFromRs(rs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}
		return listArticle;
	}

	@Override
	public List<ArticleVendu> selectByMotCleEtCategorie(String motCle, String libelle) throws BusinessException {
		List<ArticleVendu> listArticle = new ArrayList<>();

		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_BY_MOTCLE_AND_CATEGORIE)) {

			pstmt.setString(1, "%" + motCle.toLowerCase() + "%");
			pstmt.setString(2, "%" + libelle + "%");

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					listArticle.add(getArticleFromRs(rs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}

		return listArticle;
	}

	@Override
	public List<ArticleVendu> selectVentesTermineesParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException {
		List<ArticleVendu> listArticle = new ArrayList<>();

		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_FINISHED_SALES_BY_USER_AND_FILTERS)) {

			pstmt.setInt(1, noUtilisateur);
			pstmt.setString(2, "%" + motCle.toLowerCase() + "%");
			pstmt.setString(3, "%" + libelle + "%");

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					listArticle.add(getArticleFromRs(rs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}

		return listArticle;
	}

	@Override
	public List<ArticleVendu> selectVentesNonDebuteesParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException {
		List<ArticleVendu> listArticle = new ArrayList<>();

		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_UNSTARTED_SALES_BY_USER_AND_FILTERS)) {

			pstmt.setInt(1, noUtilisateur);
			pstmt.setString(2, "%" + motCle.toLowerCase() + "%");
			pstmt.setString(3, "%" + libelle + "%");

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					listArticle.add(getArticleFromRs(rs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}

		return listArticle;
	}

	@Override
	public List<ArticleVendu> selectVentesEnCoursParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException {
		List<ArticleVendu> listArticle = new ArrayList<>();

		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_ONGOING_SALES_BY_USER_AND_FILTERS)) {

			pstmt.setInt(1, noUtilisateur);
			pstmt.setString(2, "%" + motCle.toLowerCase() + "%");
			pstmt.setString(3, "%" + libelle + "%");

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					listArticle.add(getArticleFromRs(rs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}

		return listArticle;
	}

	@Override
	public List<ArticleVendu> selectEncheresOuvertesParFiltres(String motCle, String libelle) throws BusinessException {
		List<ArticleVendu> listArticle = new ArrayList<>();

		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_OPEN_AUCTIONS_BY_FILTERS)) {

			pstmt.setString(1, "%" + motCle.toLowerCase() + "%");
			pstmt.setString(2, "%" + libelle + "%");

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					listArticle.add(getArticleFromRs(rs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}

		return listArticle;
	}

	@Override
	public List<ArticleVendu> selectEncheresOuvertesParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException {
		List<ArticleVendu> listArticle = new ArrayList<>();

		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_OPEN_AUCTIONS_BY_USER_AND_FILTERS)) {

			pstmt.setInt(1, noUtilisateur);
			pstmt.setString(2, "%" + motCle.toLowerCase() + "%");
			pstmt.setString(3, "%" + libelle + "%");

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					listArticle.add(getArticleFromRs(rs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}

		return listArticle;
	}

	@Override
	public List<ArticleVendu> selectEncheresRemporteesParUtilisateurEtFiltres(Integer noUtilisateur, String motCle,
			String libelle) throws BusinessException {
		List<ArticleVendu> listArticle = new ArrayList<>();

		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_WON_AUCTIONS_BY_USER_AND_FILTERS)) {

			pstmt.setInt(1, noUtilisateur);
			pstmt.setString(2, "%" + motCle.toLowerCase() + "%");
			pstmt.setString(3, "%" + libelle + "%");

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					listArticle.add(getArticleFromRs(rs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}

		return listArticle;
	}

	@Override
	public void updateArticle(ArticleVendu articleVendu) throws BusinessException {
		// Vérification que l'article à mettre à jour n'est pas null
		if (articleVendu == null) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_OBJET_NULL);
			throw businessException;
		}
		try (Connection cnx = ConnectionProvider.getConnection()) {
			// Désactivation du mode de validation automatique pour la gestion manuelle des
			// transactions
			cnx.setAutoCommit(false);

			try (PreparedStatement pstmt = cnx.prepareStatement(UPDATE_ARTICLE)) {

				pstmt.setString(1, articleVendu.getNomArticle());
				pstmt.setString(2, articleVendu.getDescription());
				pstmt.setDate(3, Date.valueOf(articleVendu.getDateDebutEncheres()));
				pstmt.setDate(4, Date.valueOf(articleVendu.getDateFinEncheres()));
				pstmt.setInt(5, articleVendu.getPrixInitial());
				pstmt.setInt(6, articleVendu.getPrixVente());
				pstmt.setInt(7, articleVendu.getUtilisateur().getNoUtilisateur());
				pstmt.setInt(8, articleVendu.getCategorie().getNoCategorie());
				pstmt.setInt(9, articleVendu.getNoArticle());

				int rowsUpdated = pstmt.executeUpdate();
				// Vérification du succès de la mise à jour
				if (rowsUpdated > 0) {
					// Validation de la transaction
					cnx.commit();
				}

			} catch (Exception e) {
				e.printStackTrace();
				cnx.rollback();
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_OBJET_ECHEC);
			throw businessException;

		}
	}

	@Override
	public void deleteArticle(Integer noArticle) throws BusinessException {

		if (noArticle == null || noArticle <= 0) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.DELETE_OBJET_NULL);
			throw businessException;
		}

		try (Connection cnx = ConnectionProvider.getConnection()) {
			// Désactivation du mode de validation automatique pour la gestion manuelle des
			// transactions
			cnx.setAutoCommit(false);

			// Vérification si l'article existe avant de le supprimer
			if (articleExists(cnx, noArticle)) {
				try (PreparedStatement pstmt = cnx.prepareStatement(DELETE_ARTICLE)) {
					pstmt.setInt(1, noArticle);

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
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.DELETE_OBJET_ECHEC);
			throw businessException;
		}
	}

	private boolean articleExists(Connection cnx, Integer noArticle) throws BusinessException, SQLException {
		try (PreparedStatement pstmt = cnx.prepareStatement("SELECT 1 FROM ARTICLES_VENDUS WHERE no_article = ?")) {
			pstmt.setInt(1, noArticle);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				return resultSet.next(); // Retourne true si l'article existe, false sinon
			}
		}
	}

	private ArticleVendu getArticleFromRs(ResultSet rs) throws Exception {
		// Récupération des données à partir du ResultSet
		Integer noArticle = rs.getInt("no_article");
		String nomArticle = rs.getString("nom_article");
		String description = rs.getString("description");
		LocalDate dateDebutEncheres = rs.getDate("date_debut_encheres").toLocalDate();
		LocalDate dateFinEncheres = rs.getDate("date_fin_encheres").toLocalDate();
		Integer prixInitial = rs.getInt("prix_initial");
		Integer prixVente = rs.getInt("prix_vente");
		Integer noUtilisateur = rs.getInt("no_utilisateur");
		Integer noCategorie = rs.getInt("no_categorie");

		// Appel des DAO pour récupérer l'utilisateur et la catégorie associés
		Utilisateur utilisateur = utilisateurDAO.selectById(noUtilisateur);
		Categorie categorie = categorieDAO.selectById(noCategorie);

		// Création de l'objet ArticleVendu en utilisant le constructeur avec paramètres
		ArticleVendu articleVendu = new ArticleVendu(noArticle, nomArticle, description, dateDebutEncheres,
				dateFinEncheres, prixInitial, prixVente, utilisateur, categorie);

		return articleVendu;
	}
}
