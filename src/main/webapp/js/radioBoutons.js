// Récupération des éléments HTML
const radioBoutonAchats = document.getElementById('achats');
const radioBoutonMesVentes = document.getElementById('ventes');
const checkboxesAchats = document.querySelectorAll('[name="checkboxGroupAchats"]');
const checkboxesMesVentes = document.querySelectorAll('[name="checkboxGroupVentes"]');

// Ajout d'un écouteur d'événements pour le bouton radio "Achats"
radioBoutonAchats.addEventListener('change', () => {
	// Désactivation des cases à cocher du groupe "Achats" si le bouton radio "Achats" est désélectionné
	checkboxesAchats.forEach((checkbox) => {
		checkbox.disabled = !radioBoutonAchats.checked;
		// Si le bouton radio "Achats" est déselectionné, les case à cocher du groupe "Achats" sont décochées
		if (!radioBoutonAchats.checked) {
			checkbox.checked = false;
		}
	});
	// Désactivation des cases à cocher du groupe "Mes Ventes" si le bouton radio "Achats" est sélectionné
	checkboxesMesVentes.forEach((checkbox) => {
		checkbox.disabled = radioBoutonAchats.checked;
		// Si le bouton radio "Achats" est selectionné, les case à cocher du groupe "Mes Ventes" sont décochées
		if (radioBoutonAchats.checked) {
			checkbox.checked = false;
		}
	});
});

// Ajout d'un écouteur d'événements pour le bouton radio "Mes Ventes"
radioBoutonMesVentes.addEventListener('change', () => {
	// Désactivation des cases à cocher du groupe "Mes Ventes" si le bouton radio "Mes Ventes" est désélectionné
	checkboxesMesVentes.forEach((checkbox) => {
		checkbox.disabled = !radioBoutonMesVentes.checked;
		// Si le bouton radio "Mes Ventes" est déselectionné, les case à cocher du groupe "Mes Ventes" sont décochées
		if (!radioBoutonMesVentes.checked) {
			checkbox.checked = false;
		}
	});
	// Désactivation des cases à cocher du groupe "Achats" si le bouton radio "Mes Ventes" est sélectionné
	checkboxesAchats.forEach((checkbox) => {
		checkbox.disabled = radioBoutonMesVentes.checked;
		// Si le bouton radio "Mes Ventes" est selectionné, les case à cocher du groupe "Achats" sont décochées
		if (radioBoutonMesVentes.checked) {
			checkbox.checked = false;
		}
	});
});
