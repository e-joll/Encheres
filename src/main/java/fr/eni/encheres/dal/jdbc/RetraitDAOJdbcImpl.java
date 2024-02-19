package fr.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.dal.ArticleVenduDAO;
import fr.eni.encheres.dal.CodesResultatDAL;
import fr.eni.encheres.dal.ConnectionProvider;
import fr.eni.encheres.dal.RetraitDAO;

public class RetraitDAOJdbcImpl implements RetraitDAO {

	private static final String INSERT_RETRAIT = "INSERT INTO RETRAITS values (?, ?, ?, ?)";
	private static final String SELECT_BY_ARTICLE_ID = "SELECT no_article, rue, code_postal, ville FROM RETRAITS WHERE no_article = ?";
	private static final String UPDATE_RETRAIT = "UPDATE RETRAITS SET rue = ?, code_postal = ?, ville = ? WHERE no_article=?";
	private static final String DELETE_RETRAIT_BY_ARTICLE = "DELETE RETRAITS WHERE no_article = ?";

	ArticleVenduDAO articleVenduDAO = new ArticleVenduDAOJdbcImpl();

	@Override
	public void insert(Retrait retrait) throws BusinessException {

		try (Connection cnx = ConnectionProvider.getConnection()) {
			cnx.setAutoCommit(false);
			try (PreparedStatement pstmt = cnx.prepareStatement(INSERT_RETRAIT)) {

				pstmt.setInt(1, retrait.getArticle().getNoArticle());
				pstmt.setString(2, retrait.getRue());
				pstmt.setString(3, retrait.getCodePostal());
				pstmt.setString(4, retrait.getVille());

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
	public Retrait selectByArticleId(Integer noArticle) throws BusinessException {

		Retrait retrait = null;

		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_BY_ARTICLE_ID)) {

			pstmt.setInt(1, noArticle);

			try (ResultSet rs = pstmt.executeQuery()) {

				if (rs.next()) {
					retrait = new Retrait();
					String rue = rs.getString("rue");
					String codePostal = rs.getString("code_postal");
					String ville = rs.getString("ville");
					Integer articleId = rs.getInt("no_article");
					retrait = new Retrait(rue, codePostal, ville, articleVenduDAO.selectById(articleId));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.LECTURE_OBJET_ECHEC);
			throw businessException;
		}

		return retrait;
	}

	@Override
	public void updateRetrait(Retrait retrait) throws BusinessException {
		if (retrait == null) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_OBJET_NULL);
			throw businessException;
		}

		try (Connection cnx = ConnectionProvider.getConnection()) {
			cnx.setAutoCommit(false);

			try (PreparedStatement pstmt = cnx.prepareStatement(UPDATE_RETRAIT)) {

				pstmt.setString(1, retrait.getRue());
				pstmt.setString(2, retrait.getCodePostal());
				pstmt.setString(3, retrait.getVille());
				pstmt.setInt(4, retrait.getArticle().getNoArticle());

				int rowsUpdated = pstmt.executeUpdate();

				if (rowsUpdated > 0) {
					cnx.commit();
				}
			} catch (Exception e) {
				e.printStackTrace();
				cnx.rollback();
				throw e;
			}
		} catch (SQLException e) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_OBJET_ECHEC);
			throw businessException;
		}
	}

	@Override
	public void deleteRetraitByArticle(Integer noArticle) throws BusinessException {
		if (noArticle == null || noArticle <= 0) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.DELETE_OBJET_NULL);
			throw businessException;
		}
		try (Connection cnx = ConnectionProvider.getConnection();
				PreparedStatement pstmt = cnx.prepareStatement(DELETE_RETRAIT_BY_ARTICLE)) {
			cnx.setAutoCommit(false);
			pstmt.setInt(1, noArticle);

			int rowsDeleted = pstmt.executeUpdate();
			if (rowsDeleted > 0) {
				cnx.commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.DELETE_OBJET_ECHEC);
			throw businessException;

		}
	}
}