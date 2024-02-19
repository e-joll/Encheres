<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="fr.eni.encheres.messages.LecteurMessage"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Inscritption</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>

<body>
	<div class="flex justify-between items-center p-4 bg-sky-600 mb-10">
		<div class="text-2xl text-white">ENI-Enchères</div>
	</div>
	<h1 class="my-4 text-center text-2xl">Inscription</h1>
	<div class="w-1/2 mx-auto p-2">
		<%
		List<Integer> listeCodesErreur = (List<Integer>)request.getAttribute("listeCodesErreur");
		if(listeCodesErreur!=null)
		{
		%>
				<p class="text-red-600">Erreur, votre profil n'a pas pu être créé :</p>
		<%
				for(int codeErreur:listeCodesErreur)
				{
		%>
					<p><%=LecteurMessage.getMessageErreur(codeErreur)%></p>
		<%	
				}
		}
		%>
	</div>
	<form method="POST" action="./creationCompte">
		<div
			class="grid sm:grid-cols-2 w-4/5 mx-auto gap-x-20 xl:gap-x-30 gap-y-2 ">
			<div class="flex justify-between items-center">
				<label for="pseudo">Pseudo :</label> <input type="text"
					name="pseudo" id="pseudo" value="${user.pseudo}"
					class="block w-3/5 xl:w-[70%] rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6"
					required>
			</div>
			<div class="flex justify-between items-center">
				<label for="nom">Nom :</label> <input type="text" name="nom"
					id="nom" value="${user.nom}"
					class="block w-3/5 xl:w-[70%] rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6"
					required>
			</div>
			<div class="flex justify-between items-center">
				<label for="prenom">Prénom :</label> <input type="text"
					name="prenom" id="prenom" value="${user.prenom}"
					class="block w-3/5 xl:w-[70%] rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6"
					required>
			</div>
			<div class="flex justify-between items-center">
				<label for="email">Email :</label> <input type="email" name="email"
					id="email" value="${user.email}"
					class="block w-3/5 xl:w-[70%] rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6"
					required>
			</div>
			<div class="flex justify-between items-center">
				<label for="telephone">Téléphone :</label> <input type="text"
					name="telephone" id="telephone" value="${user.telephone}"
					class="block w-3/5 xl:w-[70%] rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6">
			</div>
			<div class="flex justify-between items-center">
				<label for="Rue">Rue :</label> <input type="text" name="rue"
					id="rue" value="${user.rue}"
					class="block w-3/5 xl:w-[70%] rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6"
					required>
			</div>
			<div class="flex justify-between items-center">
				<label for="codePostal">Code postal :</label> <input type="text"
					name="codePostal" id="codePostal" value="${user.codePostal}"
					class="block w-3/5 xl:w-[70%] rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6"
					required>
			</div>
			<div class="flex justify-between items-center">
				<label for="ville">Ville :</label> <input type="text" name="ville"
					id="ville" value="${user.ville}"
					class="block w-3/5 xl:w-[70%] rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6"
					required>
			</div>
			<div class="flex justify-between items-center">
				<label for="mdp">Mot de passe :</label> <input type="password"
					name="mdp" id="mdps"
					class="block w-3/5 xl:w-[70%] rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6"
					required>
			</div>
			<div class="flex justify-between items-center">
				<label for="mdpConfirmation">Confirmation :</label> <input
					type="password" name="mdpConfirmation" id="mdpConfirmation"
					class="block w-3/5 xl:w-[70%] rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6"
					required>
			</div>
		</div>
		<div class="w-3/4 xl:w-2/3 mx-auto flex justify-evenly mt-4">

			<button type="submit"
				class="rounded-md bg-green-600 px-6 py-2 text-sm font-semibold text-white shadow-sm hover:bg-green-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:bg-green-600">Créer</button>
			<a href="./">
				<button type="button"
					class="rounded-md bg-red-600 px-6 py-2 text-sm font-semibold text-white shadow-sm hover:bg-red-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-red-600">Annuler</button>
			</a>
		</div>
	</form>
</body>
</html>