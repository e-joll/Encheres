<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Gagnant de l'enchère</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>

<body>
	<div class="flex justify-between items-center p-4 bg-sky-600 mb-10">
		<div class="text-2xl text-white"><a href="./listeEncheres">ENI-Enchères</a></div>
	</div>

	<div class="mx-5 mb-5 sm:mx-10">
		<h1 class="my-4 text-center text-2xl">${gagnant.pseudo} a remporté l'enchère</h1>
		<div class="flex flex-col lg:flex-row gap-10">
			<div class="w-[290px] sm:w-[400px] mx-auto lg:mx-0">
				<img
					src="img/visuel-indisponible.jpg"
					alt="visuel indisponible"
					class="w-[290px] h-[290px] sm:w-[400px] sm:h-[400px] object-cover">
			</div>
			<div class="w-full lg:w-1/2">
				<h3 class="font-bold">${article.nomArticle}</h3>
				<div class="flex flex-col gap-5 ">
					<div class="flex">
						<div class="w-[180px]">Description :</div>
						<div class="w-full">${article.description}</div>
					</div>
					<div class="flex">
						<div class="w-[180px]">Meilleure offre :</div>
						<div class="w-full">
							${montant_enchere} points par <a href="./profil?pseudo=${gagnant.pseudo}">${gagnant.pseudo}</a>
						</div>
					</div>
					<div class="flex">
						<div class="w-[180px]">Mise à prix :</div>
						<div class="w-full">${article.prixInitial} points</div>
					</div>
					<div class="flex">
						<div class="w-[180px]">Fin de l'enchère :</div>
						<div class="w-full">${article.dateFinEncheres}</div>
					</div>
					<div class="flex">
						<div class="w-[180px]">Retrait :</div>
						<div class="w-full">
							<div>${retrait.rue}</div>
							<div>${retrait.codePostal} ${retrait.ville}</div>
						</div>
					</div>
					<div class="flex">
						<div class="w-[180px]">Vendeur :</div>
						<div class="w-full">
							<a href="./profil?pseudo=${article.utilisateur.pseudo}"
								class="underline">${article.utilisateur.pseudo}</a>
						</div>
					</div>
				</div>
				<a href="./listeEncheres">
					<button type="button"
						class="rounded-md bg-sky-600 my-5 px-6 py-2 text-sm font-semibold text-white shadow-sm hover:bg-sky-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-sky-600">Retrait effectué</button>
				</a>
			</div>
		</div>
	</div>



</body>

</html>