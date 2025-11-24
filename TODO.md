# TODO's

1 - Quand on saisit un statut prochePPE ou PPE, et que la vigilance renforcée est à false, l'erreur est une 500, devrait être une 400 au format JSON
(ProblemDetails), avec un code pour que le front puisse l'internationaliser.

2 - Mapping de la création d'une connaissance client sur le verbe http POST, mapping de la correction d'une connaissance client sur le verbe http PUT
On va plutot mapper toutes les créations sur un PUT sur la resource personne, avec un body contenant le type de modification. (boolean CORRECTION facultatif,
pas présent en création
initiale ou en modification)
Dans le cas de la création initiale, on va retourner un code 201.
Dans le cas de l'ajout d'une entrée ou de correction, on va retourner un code 200.
S'assurer que dans le front, si on sauve la première fois, on ne peut pas cocher la case disant que c'est une correction.

3 - renommer `historique_modification_connaissance_client` en `connaissance_client_historique`

4 - Gérer les CORS et allow origins dans le .env

5 - Gestion des migrations: utiliser uniquement Flyway et pas SchemaUtils de Exposed

6 - Renommer *Ser en *Dto

7 - Gérer le multi tenant

9 - Faire des tests d'intégration des routes




