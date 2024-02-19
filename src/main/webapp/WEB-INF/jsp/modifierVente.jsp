<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="fr.eni.encheres.messages.LecteurMessage"%>
<%@page import="java.util.List"%>

<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Votre Formulaire</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>

<body>

	<div class="flex justify-between items-center p-4 bg-sky-600 mb-10">
		<div class="text-2xl text-white">
			<a href="./listeEncheres">ENI-Enchères</a>
		</div>
	</div>

	<div class="flex justify-center items-start mt-10">

		<div class="ml-[-300px]">

				<%
				List<Integer> listeCodesErreur = (List<Integer>)request.getAttribute("listeCodesErreur");
				if(listeCodesErreur!=null)
				{
				%>
						<p class="text-red-600">Erreur, l'article n'a pas pu être ajouté :</p>
				<%
						for(int codeErreur:listeCodesErreur)
						{
				%>
							<p><%=LecteurMessage.getMessageErreur(codeErreur)%></p>
				<%	
						}
				}
				%>
				
			<div class="flex items-center">

				<div class="mr-20 mt-[-200px]">
					<img
						src="img/visuel-indisponible.jpg"
						alt="visuel indisponible"
						class="w-[290px] h-[290px] sm:w-[300px] sm:h-[300px] object-cover">
				</div>


				<form method="POST" action="./modifierVente?noArticle=${article.noArticle}"
					class="bg-white p-8 shadow-md rounded max-w-500 mx-auto border border-2 border-gray-300">





					<div class="mb-4">
						<label for="article"
							class="block text-sm font-medium text-gray-600">Article</label> <input
							type="text" id="article" name="article"
							class="mt-1 p-2 border w-full h-8" value="${article.nomArticle}">

					</div>

					<div class="mb-4">
						<label for="description"
							class="block text-sm font-medium text-gray-600">Description</label>
						<textarea id="description" name="description" rows="3"
							class="mt-1 p-2 border w-full">${article.description}</textarea>
					</div>

					<div class="mb-4">
						<label for="categorie"
							class="block text-sm font-medium text-gray-600">Catégorie</label>
						<select id="categorie" name="categorie"
							class="mt-1 p-2 border w-full h-10">

							<option value="Informatique" ${article.categorie.libelle == 'Informatique' ? 'selected' : ''}>Informatique</option>
							<option value="Ameublement" ${article.categorie.libelle == 'Ameublement' ? 'selected' : ''}>Ameublement</option>
							<option value="Vêtement" ${article.categorie.libelle == 'Vêtement' ? 'selected' : ''}>Vêtement</option>
							<option value="Sport&Loisirs" ${article.categorie.libelle == 'Sport&Loisirs' ? 'selected' : ''}>Sport&Loisirs</option>
						</select>
					</div>


					<div class="mb-4 flex items-center">
						<label for="photo"
							class="block text-sm font-medium text-gray-600 mr-4">Photo
							de l'article :</label>
						<div>
							<input class="text-white px-4 py-2 rounded-md" type="file"
								name="photo" id="photo" accept=".jpg, .jpeg, .png">
						</div>
					</div>




					<div class="mb-4">
						<label for="miseAPrix"
							class="block text-sm font-medium text-gray-600">Mise à
							Prix</label> <input class="mt-1 p-2 border w-full h-8" type="number"
							name="miseAPrix" id="miseAPrix"
							value="${article.prixInitial}">

					</div>

					<div class="mb-4">
						<label for="debutEnchere"
							class="block text-sm font-medium text-gray-600">Début de
							l'enchère</label> <input type="date" id="debutEnchere"
							name="debutEnchere" class="mt-1 p-2 border w-full h-8"
							value="${article.dateDebutEncheres}">
					</div>

					<div class="mb-4">
						<label for="finEnchere"
							class="block text-sm font-medium text-gray-600">Fin de
							l'enchère</label> <input type="date" id="finEnchere" name="finEnchere"
							class="mt-1 p-2 border w-full h-8"
							value="${article.dateFinEncheres}">
					</div>


					<fieldset class="mb-4">
						<legend class="text-lx font-medium text-gray-600">Retrait</legend>

						<div class="mb-2">
							<label for="rue" class="block text-sm font-medium text-gray-600">Rue</label>
							<input type="text" id="rue" name="rue"
								class="mt-1 p-2 border w-full h-8" value="${retrait.rue}">
						</div>

						<div class="mb-2">
							<label for="codePostal"
								class="block text-sm font-medium text-gray-600">Code
								Postal</label> <input type="text" id="codePostal" name="codePostal"
								class="mt-1 p-2 border w-full h-8" value="${retrait.codePostal}">
						</div>

						<div class="mb-2">
							<label for="ville"
								class="block text-sm font-medium text-gray-600">Ville</label> <input
								type="text" id="ville" name="ville"
								class="mt-1 p-2 border w-full h-8" value="${retrait.ville}">
						</div>
					</fieldset>


					<div class="flex justify-between">
						<button type="submit"
							class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mr-10">Enregistrer</button>
						<a href="./listeEncheres">
							<button type="button"
								class="bg-blue-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded mr-10">Annuler</button>
						</a> <a href="./annulerVente?noArticle=${article.noArticle}">
							<button type="button"
								class="bg-blue-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">Annuler
								la vente</button>
						</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>

</html>

