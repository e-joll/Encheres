<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="fr.eni.encheres.messages.LecteurMessage"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="fr">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Connexion - ENI Encheres</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>

<body>

	<div class="flex justify-between items-center p-4 bg-sky-600 mb-10">
		<div class="text-2xl text-white"><a href="./">ENI-Enchères</a></div>
	</div>

	<div
		class="bg-gray-100 p-8 rounded shadow-md w-full max-w-md mx-auto mt-16">
		<form action="./connexion" method="post">

			<div class="mb-4 text-4xl sm:text-7xl font-bold text-center">Connexion</div>

			<div class="md:max-w-xl">
				<div
					class="p-8 space-y-6 bg-white rounded-xl border border-neutral-100">

					<div>
						<label for="text">Identifiant</label>
						<div class="mt-1">
							<input type="text" id="pseudo" name="pseudo"
								placeholder="Entrez votre identifiant" required
								class="mt-2 w-full border py-1.5 rounded-md px-2 shadow-sm">
						</div>
					</div>

					<div>
						<label for="password">Password</label>
						<div class="mt-2">
							<input type="password" id="motDePasse" name="motDePasse"
								placeholder="Entrez votre mot de passe" required
								class="mt-2 w-full border py-1.5 rounded-md px-2 shadow-sm">
						</div>
					</div>

					<%
					List<Integer> listeCodesErreur = (List<Integer>)request.getAttribute("listeCodesErreur");
					if(listeCodesErreur!=null)
					{
							for(int codeErreur:listeCodesErreur)
							{
					%>
								<p class="text-red-600"><%=LecteurMessage.getMessageErreur(codeErreur)%></p>
					<%	
							}
					}
					%>

					<div class="flex flex-col sm:flex-row items-center justify-between">
						<div class="flex items-center space-x-2">
							<button type="submit"
								class="py-2 px-8 border border-neutral rounded-full bg-blue-500 text-white">Connexion</button>

						</div>
						<div class="flex flex-col">
							<!-- Checkbox -->
							<div class="flex items-center mb-2">
								<input type="checkbox" id="rememberMe" name="rememberMe"
									class="mr-2"> <label for="rememberMe">Se
									souvenir de moi</label>
							</div>

							<!-- Mot de passe oublié -->
							<div>
								<a href="#" class="underline">Mot de passe oublié ?</a>
							</div>
						</div>
					</div>

					<div>
						<div class="text-center mt-4">
							<a href="./creationCompte"
								class="py-2 px-8 border border-neutral rounded-full bg-blue-500 text-white w-full">Créer
								un compte</a>
						</div>
					</div>

				</div>
			</div>
		</form>
	</div>

</body>

</html>