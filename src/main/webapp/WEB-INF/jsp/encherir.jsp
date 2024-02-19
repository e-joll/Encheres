<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="fr.eni.encheres.messages.LecteurMessage"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Détail vente</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>

<body>
	<div class="flex justify-between items-center p-4 bg-sky-600 mb-10">
		<div class="text-2xl text-white">
			<a href="./listeEncheres">ENI-Enchères</a>
		</div>
	</div>

	<div class="mx-5 mb-5 sm:mx-10">
		<h1 class="my-4 text-center text-2xl">Détail vente</h1>
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
						<div class="w-[180px]">Catégorie :</div>
						<div class="w-full">${article.categorie.libelle}</div>
					</div>
					<div class="flex">
						<div class="w-[180px]">Meilleure offre :</div>
						<div class="w-full">${article.prixVente}
							points par <a href="./profil?pseudo=${gagnant.pseudo}"
								class="underline">${gagnant.pseudo}</a>
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
					<div class="flex items-center">
						<div class="w-[180px]">Ma proposition :</div>
						<div class="flex gap-1 sm:gap-10 items-center w-full">
							<form method="POST" action="./encherir?noArticle=${article.noArticle}" class="flex gap-1">
								<input type="number" id="montant_enchere" name="montant_enchere"
									min="0"
									class="block w-20 rounded-md border-0 px-3.5 py-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-sky-600 sm:text-sm sm:leading-6">
								<button type="submit"
									class="rounded-md bg-sky-600 px-6 py-2 text-sm font-semibold text-white shadow-sm hover:bg-sky-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-sky-600">Enchérir</button>
							</form>
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
				</div>
			</div>
		</div>
	</div>



</body>

</html>