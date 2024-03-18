# Gatcha_WebAPI_Authentication
## Groupe:
André-Mathys FLINOIS<br>
Yuzhe ZHU<br>
Zhengkun YANG<br>
Serigne Saliou CISSE<br>
Montaha BEN SALEM<br>

## Comment lancer
- Cloner le projet entier dans un dossier<br>
- Exécuter start.bat (ou "docker-compose up -d" dans un terminal dans le dossier du projet)<br>


## Documentation

### AuthAPI :
> URL : http://localhost:27018

## Objets :
- **Token** : String token, java.time.Instant expirationDate, boolean isExpired (un token expire 1 heure après la dernière action)
- **User** : String username, String password
# /user/register : <br>
- Prend un Utilisateur dans le RequestBody : {"username":"nom","password":"mdp"}<br>
- L'utilisateur est enregistré dans la base Mongo.<br>
# /user/list : <br>
- Affiche la liste des Utilisateurs dans la base Mongo.<br>
# /user/delete : <br>
- Prend un String (token) en header ("Authorization")
- Vérifie les identifiants entrés et supprime l'Utilisateur, le Joueur associé de la base, et le token utilisé
<br>
# /token/login : <br>
- Prend un Utilisateur dans le RequestBody : {"username":"nom","password":"mdp"}<br>
- Si les identifiants sont valides, renvoie un **Token** et le sauvegarde dans la base.
# /token/check : <br>
- Prend le **header Authorization** en entrée (champ token d'un Token)
- Vérifie qu'il existe dans la base et qu'il n'est pas expiré (si expiré -> supprimé de la base)
- Renvoie le nom de l'utilisateur associé au token s'il est valide
# /token/list : <br>
- Affiche la liste des **Tokens** enregistrés dans la base Mongo.


### PlayerAPI :
TBD<br>

### MonsterAPI :
TBD<br>

### InvocAPI :
TBD<br>


### Mapping des ports
MongoDB : 27017<br>
AuthAPI : 27018<br>
PlayerAPI : 27019<br>
MonsterAPI : 27021<br>
InvocAPI : 27022<br>





