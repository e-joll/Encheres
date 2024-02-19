<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Profil</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>

<body>
	<div class="flex justify-between items-center p-4 bg-sky-600 mb-10">
		<div class="text-2xl text-white"><a href="./listeEncheres">ENI-Enchères</a></div>
	</div>

	<div class="flex flex-col w-[60%] md:w-1/2 lg:w-[40%] mx-auto gap-y-2 ">
		<div class="flex items-center mb-3">
			<p class="w-1/2">Pseudo :</p>
			<span class="w-1/2 break-words text-center">${user.pseudo}</span>
		</div>
		<div class="flex items-center">
			<p class="w-1/2">Nom :</p>
			<span class="w-1/2 break-words text-center">${user.nom}</span>
		</div>
		<div class="flex items-center">
			<p class="w-1/2">Prénom :</p>
			<span class="w-1/2 break-words text-center">${user.prenom}</span>
		</div>
		<div class="flex items-center">
			<p class="w-1/2">Email :</p>
			<span class="w-1/2 break-words text-center">${user.email}</span>
		</div>
		<div class="flex items-center">
			<p class="w-1/2">Téléphone :</p>
			<span class="w-1/2 break-words text-center">${user.telephone}</span>
		</div>
		<div class="flex items-center">
			<p class="w-1/2">Rue :</p>
			<span class="w-1/2 break-words text-center">${user.rue}</span>
		</div>
		<div class="flex items-center">
			<p class="w-1/2">Code postal :</p>
			<span class="w-1/2 break-words text-center">${user.codePostal}</span>
		</div>
		<div class="flex items-center">
			<p class="w-1/2">Ville :</p>
			<span class="w-1/2 break-words text-center">${user.ville}</span>
		</div>
	</div>
	<div class="flex w-1/3 justify-center mx-auto mt-4">
		<a href="./modifierProfil">
			<button type="button"
				class="rounded-md bg-sky-600 px-6 py-2 text-sm font-semibold text-white shadow-sm hover:bg-sky-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-sky-600">Modifier</button>
		</a>
	</div>
</body>

</html>