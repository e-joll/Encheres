package fr.eni.encheres.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtre qui empêche d'accéder a certaines pages si l'utilisateur n'est pas
 * connecté
 */
@WebFilter(urlPatterns = { "/profil", "/annulerVente", "/encherir", "/listeEncheres", "/modifierProfil",
		"/nouvelleVente", "/detail", "/supressionCompte"})
public class ConnecteFilter extends HttpFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (httpRequest.getSession().getAttribute("utilisateur") != null) {
			chain.doFilter(request, response);
		} else {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/connexion");
		}
	}

}
