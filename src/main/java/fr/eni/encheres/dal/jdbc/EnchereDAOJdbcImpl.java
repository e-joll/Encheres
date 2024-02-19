package fr.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.ArticleVenduDAO;
import fr.eni.encheres.dal.CodesResultatDAL;
import fr.eni.encheres.dal.ConnectionProvider;
import fr.eni.encheres.dal.EnchereDAO;
import fr.eni.encheres.dal.UtilisateurDAO;

public class EnchereDAOJdbcImpl implements EnchereDAO {

	private static final String INSERT_ENCHERES = "INSERT INTO ENCHERES (date_enchere, montant_enchere, no_article, no_utilisateur) VALUES(?,?,?,?)";
	private static final String SELECT_ALL = "SELECT * FROM ENCHERES";
	private static final String SELECT_ALL_BY_ARTICLE = "SELECT * FROM ENCHERES WHERE no_article=?";
	private static final String SELECT_ALL_BY_USER = "SELECT * FROM ENCHERES WHERE no_utilisateur = ?";
	private static final String UPDATE_ENCHERES = "UPDATE ENCHERES SET no_utilisateur = ?, date_enchere = ?, montant_enchere = ? WHERE no_article = ?";
	private static final String DELETE_ENCHERES_BY_ARTICLE = "DELETE FROM ENCHERES WHERE no_article = ?";
	private static final String DELETE_ENCHERES_BY_USER = "DELETE FROM ENCHERES WHERE no_utilisateur = ?";
	private static final String MAX_MONTANT_ENCHERES_BY_ARTICLE = "SELECT MAX(montant_enchere) FROM ENCHERES WHERE no_article = ?";
	private static final String SELECT_ALL_BY_ARTICLE_AND_DECREASING_AMOUNT = "SELECT * FROM ENCHERES WHERE no_article = ? ORDER BY montant_enchere DESC";
	
	ArticleVenduDAO articleVenduDAO = new ArticleVenduDAOJdbcImpl();
	UtilisateurDAO utilisateurDAO = new UtilisateurDAOJdbcImpl();

	@Override
	public void insert(Enchere enchere) throws BusinessException {

		if (enchere == null) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_OBJET_NULL);
			throw businessException;
		}
		try (Connection cnx = ConnectionProvider.getConnection()) {
			cnx.setAutoCommit(false);

			try (PreparedStatement pstmt = cnx.prepareStatement(INSERT_ENCHERES)) {

				pstmt.setDate(1, Date.valueOf(enchere.getDateEnchere()));
				pstmt.setInt(2, enchere.getMontantEnchere());
				pstmt.setInt(3, enchere.getArticle().getNoArticle());
				pstmt.setInt(4, enchere.getUtilisateur().getNoUtilisateur());

				int rowsAffected = pstmt.executeUpdate();

				if (rowsAffected == 1) {
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
	public List<Enchere> selectAll() throws BusinessException {
		List<Enchere> encheres = new ArrayList<>();
		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_ALL);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				encheres.add(getEnchereFromResultSet(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}
		return encheres;
	}

	@Override
	public List<Enchere> selectAllByArticle(Integer noArticle) throws BusinessException {
		List<Enchere> encheres = new ArrayList<>();

		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_ALL_BY_ARTICLE)) {

			pstmt.setInt(1, noArticle);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					encheres.add(getEnchereFromResultSet(rs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}

		return encheres;
	}
	
	@Override
	public List<Enchere> selectAllByArticleAndDecreasingAmount(Integer noArticle) throws BusinessException {
		List<Enchere> encheres = new ArrayList<>();

		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_ALL_BY_ARTICLE_AND_DECREASING_AMOUNT)) {

			pstmt.setInt(1, noArticle);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					encheres.add(getEnchereFromResultSet(rs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}

		return encheres;
	}

	@Override
	public List<Enchere> selectAllByUser(Integer noUtilisateur) throws BusinessException {
		List<Enchere> encheres = new ArrayList<>();

		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_ALL_BY_USER)) {

			pstmt.setInt(1, noUtilisateur);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					encheres.add(getEnchereFromResultSet(rs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}

		return encheres;
	}

	@Override
	public void updateEnchere(Enchere enchere) throws BusinessException {
		if (enchere == null) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_OBJET_NULL);
			throw businessException;
		}
		try (Connection cnx = ConnectionProvider.getConnection()) {
			cnx.setAutoCommit(false);

			try (PreparedStatement pstmt = cnx.prepareStatement(UPDATE_ENCHERES)) {

				pstmt.setInt(1, enchere.getUtilisateur().getNoUtilisateur());
				pstmt.setDate(2, Date.valueOf(enchere.getDateEnchere()));
				pstmt.setInt(3, enchere.getMontantEnchere());
				pstmt.setInt(4, enchere.getArticle().getNoArticle());

				int rowsUpdated = pstmt.executeUpdate();

				if (rowsUpdated > 0) {
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
	public void deleteEncheresByArticle(Integer noArticle) throws BusinessException {
		if (noArticle == null || noArticle <= 0) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.DELETE_OBJET_NULL);
			throw businessException;
		}
		try (Connection cnx = ConnectionProvider.getConnection()) {
			cnx.setAutoCommit(false);
			try (PreparedStatement pstmt = cnx.prepareStatement(DELETE_ENCHERES_BY_ARTICLE)) {

				pstmt.setInt(1, noArticle);
				int rowsDeleted = pstmt.executeUpdate();
				if (rowsDeleted > 0) {
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
			businessException.ajouterErreur(CodesResultatDAL.DELETE_OBJET_ECHEC);
			throw businessException;
		}
	}

	@Override
	public void deleteEncheresByUser(Integer noUtilisateur) throws BusinessException {
		if (noUtilisateur == null || noUtilisateur <= 0) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.DELETE_OBJET_NULL);
			throw businessException;
		}
		try (Connection cnx = ConnectionProvider.getConnection()) {
			cnx.setAutoCommit(false);

			try (PreparedStatement pstmt = cnx.prepareStatement(DELETE_ENCHERES_BY_USER)) {

				pstmt.setInt(1, noUtilisateur);
				int rowsDeleted = pstmt.executeUpdate();
				if (rowsDeleted > 0) {
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
			businessException.ajouterErreur(CodesResultatDAL.DELETE_OBJET_ECHEC);
			throw businessException;
		}
	}

	@Override
	public int getMontantActuelByArticle(Integer noArticle) throws BusinessException {
		try (Connection connection = ConnectionProvider.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(MAX_MONTANT_ENCHERES_BY_ARTICLE)) {
			pstmt.setInt(1, noArticle);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}
		return 0;
	}

	@Override
	public void enchereAJour(ArticleVendu article, Utilisateur utilisateur, Integer proposition) throws BusinessException {
		LocalDateTime dateEnchere = LocalDateTime.now();
		try (Connection cnx = ConnectionProvider.getConnection()) {
			cnx.setAutoCommit(false);

			try (PreparedStatement pstmt = cnx.prepareStatement(UPDATE_ENCHERES)) {

				pstmt.setInt(1, utilisateur.getNoUtilisateur());
				pstmt.setInt(2, article.getNoArticle());
				pstmt.setDate(3, Date.valueOf(dateEnchere.toLocalDate()));
				pstmt.setInt(4, proposition);

				int rowsUpdated = pstmt.executeUpdate();

				if (rowsUpdated > 0) {
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

	private Enchere getEnchereFromResultSet(ResultSet rs) throws Exception {
		LocalDateTime dateEnchere = rs.getTimestamp("date_enchere").toLocalDateTime();
		Integer montantEnchere = rs.getInt("montant_enchere");
		Integer noUtilisateur = rs.getInt("no_utilisateur");
		Integer noArticle = rs.getInt("no_article");

		// Utilisation du constructeur de Enchere avec conversions de types
		Enchere enchere = new Enchere(dateEnchere.toLocalDate(), montantEnchere,
				utilisateurDAO.selectById(noUtilisateur), articleVenduDAO.selectById(noArticle));

		return enchere;
	}
}
