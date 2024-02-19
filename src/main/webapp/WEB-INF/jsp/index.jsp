<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Liste des enchères</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>

<body>
	<div class="flex justify-between items-center p-4 bg-sky-600 mb-10">
		<div class="text-2xl text-white">ENI-Enchères</div>
		<div>
			<a href="./connexion" class="text-white">S'inscrire - Se connecter</a>
		</div>
	</div>

	<div class="mx-10">
		<h1 class="my-4 text-center text-2xl">Liste des enchères</h1>

		<form method="POST" action="">
			<div class="flex flex-col md:flex-row md:items-end">
				<fieldset class="w-11/12 sm:w-[470px]">
					<legend class="text-lg">Filtres :</legend>
					<div class="relative py-1">
						<input type="search" id="search" name="search"
							placeholder="Le nom de l'article contient"
							class="block w-full rounded-md border-0 pl-10 pr-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6">
						<div
							class="absolute inset-y-0  flex items-center pointer-events-none">
							<!-- Loupe au format SVG -->
							<svg fill="#000000" height="50px" width="50px" version="1.1"
								xmlns="http://www.w3.org/2000/svg"
								xmlns:xlink="http://www.w3.org/1999/xlink"
								viewBox="-512 -512 1536.00 1536.00" xml:space="preserve"
								stroke="#000000" stroke-width="26.112000000000002">
                                <g>
                                    <path
									d="M508.255,490.146l-128-128c-0.06-0.06-0.137-0.077-0.196-0.128c34.193-38.434,55.142-88.917,55.142-144.418
                            c0-120.175-97.425-217.6-217.6-217.6S0.001,97.425,0.001,217.6s97.425,217.6,217.6,217.6c55.501,0,105.975-20.949,144.418-55.151
                            c0.06,0.06,0.077,0.137,0.128,0.196l128,128c2.5,2.509,5.777,3.755,9.054,3.755s6.554-1.246,9.054-3.746
                            C513.247,503.253,513.247,495.147,508.255,490.146z M217.601,409.6c-105.865,0-192-86.135-192-192s86.135-192,192-192
                            s192,86.135,192,192S323.466,409.6,217.601,409.6z" />
                                </g>
                            </svg>
						</div>
					</div>
					<div class="flex w-full items-center py-1">
						<label for="categorie" class="pr-2 w-2/5 sm:w-1/5">Catégorie
							:</label> <select id="categorie" name="categorie"
							class="block w-3/5 sm:w-4/5 rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6">
							<option value="toutes">Toutes</option>
							<option value="Informatique">Informatique</option>
							<option value="Ameublement">Ameublement</option>
							<option value="Vêtement">Vêtement</option>
							<option value="Sport&Loisirs">Sport&Loisirs</option>
						</select>
					</div>
				</fieldset>
				<div class="md:px-5">
					<button type="submit"
						class="rounded-md bg-sky-600 px-6 py-2 text-sm font-semibold text-white shadow-sm hover:bg-sky-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-sky-600">Rechercher</button>
				</div>

			</div>
		</form>
	</div>

	
	<div class="flex flex-wrap gap-10 m-10">
	<c:forEach var="article" items="${listeArticles}">
		<div
			class="flex flex-col sm:flex-row bg-slate-50 rounded-xl sm:h-[200px] sm:w-[470px] overflow-hidden border border-dashed">
			<img
				src="img/visuel-indisponible.jpg"
				alt="visuel indisponible"
				class="sm:w-[190px] h-full object-cover transform hover:scale-110 focus:scale-110 transition-transform">
			<div class="flex flex-col justify-between p-5 ml-2">
				<div class="flex flex-col">
					<h3 class="font-bold">${article.nomArticle}</h3>
					<div>Prix : ${article.prixInitial} points</div>
					<div>Fin de l'enchère : ${article.dateFinEncheres}
					</div>
				</div>
				<span class="mt-2">Vendeur : ${article.utilisateur.pseudo}</span>
			</div>
		</div>
		</c:forEach>
	</div>

</body>

</html>