# projet-ihm-bateaux


##TODO
- ReportDetailFragments + lien on tap Overlay items/trigger fragment
- interfaces (dans fragments .xml) à compléter pour chaque incident
- intégrer les champs des fragments aux Entities/modifier constructeurs


- le reste des axes
- \orientation --> layout-land/ (already created), besoin de le remplir + autorisation de l'app sûrement
- notifs à intégrer (création + visualisation image) en local pour le moment
- flux réseaux sociaux
- affichage tablette
- back-end server/remote bd

## Equipe et Axes Techniques
###Dans l'idéal:

* Gérer l'orientation paysage/portait de l'appareil dans les différentes vues, principalement lorsqu’elle apporte un plus à l’application.
	justification: permet la continuité avec les autres applications avec carte/GPS --> google maps, Waze... pour conserver les habitudes des utilisateurs
* Intégrer un flux réseaux sociaux (ex. Twitter)
	justification: partage des incidents sur Facebook/Twitter, permet de toucher/attirer un plus grand nb d'utilisateurs
AlmostOK (à intégrer) * Créer une notification utile avec temps d'affichage
	justification: pour que l'application donne du feedback
OK * Exchanger des objets complexes (non natifs) dans une communication entre les activités
	justification: pour remplir un Incident, le créer/faire passer de l'activité ReportActivity à l'activité IncidentActivity OU l'inverse

** Faire une application compatible avec une affichage différent pour tablette et smartphone
	justification: pour les personnes qui prennent leur tablette en mer, avantage écran plus grand, plus de visibilité
** Visualiser une notification avec des images ou vidéo en entête.
	justification: voir une image (vector asset)/l'icone spécifique du type de l'incident notifié
OK ** Implémenter une idée qui utilise les capteurs du dispositif gyroscope, GPS, wifi ou Bluetooth.
	justification: pour récupérer les coordonnées GPS de l'utilisateur lors d'un signalement et afficher le signalement au bon endroit sur la carte
OK ** Intégrer OpenStreetMap dans l'application, pour indiquer le lieu des incidents par exemple
	justification: besoin d'une carte pour afficher les incidents et se situer
** Notifier et organiser les notifications d’une période donnée de façon utile
	justification: pour avoir les (5? dernières) infos des dernières 2h (par exemple)

*** Afficher les incidents dynamiquement à partir d'un Web Service en ligne
	justification: pour récupérer les incidents signalés par les autres utilisateurs et ne pas se contenter d'une application 100% en local...



###Pas primordial/justifiable (mais si qqun veut en faire en bonus... libre à lui):
//* Appel à un service via Intent
	bonus, en fait sert à communiquer entre applications --> agenda/contacts du tel etc..
//* Implémenter une idée qui utilise les capteurs du dispositif du type photo ou vidéo.
	pas utile

//** Implémenter deux versions d'interfaces avec des layouts differents. Comparez-les.
	mouai
//** Lier les informations de contact aux applications du téléphone (tel, sms, ...)
	bonus
//** Lier les informations des incidents à l'agenda du téléphone.
	bonus

//*** Trouver et implémenter une interaction à l’aide de smartwatch (Android Wear)
	bonus



### Hamza Ayoub
-  \* Créer une notification utile avec temps d'affichage
	justification: pour que l'application donne du feedback
	
-  ** Visualiser une notification avec des images ou vidéo en entête.
	justification: voir une image (vector asset)/l'icone spécifique du type de l'incident notifié

### Angèle Badia
-  \*** Afficher les incidents dynamiquement à partir d'un Web Service en ligne
	justification: pour récupérer les incidents signalés par les autres utilisateurs et ne pas se contenter d'une application 100% en local...

- ** Notifier et organiser les notifications d’une période donnée de façon utile
	justification: pour avoir les (5? dernières) infos des dernières 2h (par exemple)

### Alexandre Col
-  \* Gérer l'orientation paysage/portait de l'appareil dans les différentes vues, principalement lorsqu’elle apporte un plus à l’application.
	justification: permet la continuité avec les autres applications avec carte/GPS --> google maps, Waze... pour conserver les habitudes des utilisateurs

- ** Faire une application compatible avec une affichage différent pour tablette et smartphone
	justification: pour les personnes qui prennent leur tablette en mer, avantage écran plus grand, plus de visibilité

### Armand Fargeon
-  \* Intégrer un flux réseaux sociaux (ex. Twitter)
	justification: partage des incidents sur Facebook/Twitter, permet de toucher/attirer un plus grand nb d'utilisateurs

-  ** Intégrer ~~OpenStreetMap~~ OpenSeeMap dans l'application, pour indiquer le lieu des incidents par exemple
	justification: besoin d'une carte pour afficher les incidents et se situer

### David Lebrisse
-  \* Exchanger des objets complexes (non natifs) dans une communication entre les activités
	justification: pour remplir un Incident, le créer/faire passer de l'activité ReportActivity à l'activité IncidentActivity OU l'inverse

-  ** Implémenter une idée qui utilise les capteurs du dispositif gyroscope, GPS, wifi ou Bluetooth.
	justification: pour récupérer les coordonnées GPS de l'utilisateur lors d'un signalement et afficher le signalement au bon endroit sur la carte