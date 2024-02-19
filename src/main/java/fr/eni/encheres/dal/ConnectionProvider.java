package fr.eni.encheres.dal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ConnexionBDServlet")
public class ConnectionProvider extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static DataSource dataSource;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection cnx = null;
		try {
			if (dataSource == null) {
				Context context = new InitialContext();
				dataSource = (DataSource)context.lookup("java:comp/env/jdbc/pool_cnx");
			}
			cnx = dataSource.getConnection();
			response.getWriter().println("Connexion réussie à la base de données : " + cnx.getCatalog());
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().println("erreur à la récupération de la connexion : " + e.getMessage());
		} finally {
			if (cnx != null) {
				try {
					cnx.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Connection getConnection() throws SQLException {
		if (dataSource == null) {
			try {
				Context context = new InitialContext();
				dataSource = (DataSource)context.lookup("java:comp/env/jdbc/pool_cnx");
			} catch (NamingException e) {
				e.printStackTrace();
				throw new SQLException("Impossible de trouver la source de données", e);
			}
		}
		return dataSource.getConnection();
	}
}

