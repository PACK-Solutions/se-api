# TODO's

1 (OK) - Quand on saisit un statut prochePPE ou PPE, et que la vigilance renforcée est à false, l'erreur est une 500, devrait être une 400 au format JSON
(ProblemDetails), avec un code pour que le front puisse l'internationaliser.

2 - Mapping de la création d'une connaissance client sur le verbe http POST, mapping de la correction d'une connaissance client sur le verbe http PUT
On va plutot mapper toutes les créations sur un PUT sur la resource personne, avec un body contenant le type de modification. (boolean CORRECTION facultatif,
pas présent en création
initiale ou en modification)
Dans le cas de la création initiale, on va retourner un code 201.
Dans le cas de l'ajout d'une entrée ou de correction, on va retourner un code 200.
S'assurer que dans le front, si on sauve la première fois, on ne peut pas cocher la case disant que c'est une correction.

3 (OK) - renommer `historique_modification_connaissance_client` en `connaissance_client_historique`

4 - Gérer les CORS et allow origins dans le .env

5 (OK) - Gestion des migrations: utiliser uniquement Flyway et pas SchemaUtils de Exposed

6 (OK) - Renommer *Ser en *Dto

7 (OK) - Gérer le multi tenant

9 (OK) - Faire des tests d'intégration des routes

10 - Pattern command pour log et passage de contexte (user, etc.)

11 - Log structurée (MDC ?) ou https://ktor.io/docs/server-call-id.html#put-call-id-mdc

12 (OK) - Déplacer le projet dans un repo gitlab ou github

13 - Vérifier les logins et tenantID (possibilité d'avoir une table d'association entre le tenantId et un UUID que l'on stockera)

14 (OK) - Rajouter un cas dans les TI pour un login ou tenantID qui n'existe pas

15 (OK) - La route get connaissance client retourne un 404, mais la route get historique retourne un 200 avec un tableau vide.

16 - gérer la modification initiale de l'historique

17 (OK) - Sur le saveModification, si pas de modifs, retourné un 304 et le gérer côté front

18 - Mode sandbox

