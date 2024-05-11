# Gatcha_WebAPI

Dans le cadre du module **WebAPI et Data**, voici le compte-rendu du projet réalisé.

## Groupe:
Yuzhe ZHU<br>
Zhengkun YANG<br>
André-Mathys FLINOIS<br>
Serigne Saliou CISSE<br>
Montaha BEN SALEM<br>
Lucas KADERI<br>
Nohaman L'MOSTAQIM<br>

## Comment lancer
- Cloner le projet entier dans un dossier<br>
- Exécuter start.bat (ou "docker-compose up -d" dans un terminal dans le dossier du projet)<br>
> Ce problème peut arriver lorsque l'on lance le docker-compose pour la première fois de 0, il faut simplement le lancer une 2ème fois :
> <img src="https://i.ibb.co/LP1LWCn/issue-pomxml.png">

## Etapes pour tester
- Créer un compte avec l'URL : http://localhost:27018/user/register, en postant un JSON {"username":"foo","password":"bar"}.<br><br>
- Générer un token avec l'URL : http://localhost:27018/token/login, en postant le même JSON (identifiants).<br><br>
- Les tokens sont visualisables depuis l'URL http://localhost:27018/token/list
- Récupérer le token, puis le mettre en header "Authorization" (Custom HTTP authentication).<br><br>
- Créer un compte Joueur avec l'URL : http://localhost:27019/players/register, simplement en envoyant la requête avec le token en header.<br><br>
- Vérifier la création du compte avec l'URL : http://localhost:27019/players/list, cette URL est accessible directement depuis un navigateur car ne nécessite pas d'authentification <br><br>
- Ajouter les monstres au jeu, en copiant le contenu du fichier globalMonsters.json dans la racine du projet, et en l'envoyant à l'URL : http://localhost:27021/monsters/addBulkGlobal. <br><br>
- Invoquer un monstre en envoyant une requête avec le token en header à l'URL : http://localhost:27022/invoc/draw. <br><br>
- Toutes les URL se trouvent dans la documentation ci-dessous

## Documentation

<details open>
<summary> AuthAPI : </summary>

> URL : http://localhost:27018

### Objets :
- **Token** : String token, java.time.Instant expirationDate, boolean isExpired (un token expire 1 heure après la dernière action)
- **User** : String username, String password

### Endpoints :
> **/user/register** : <br>
> - Prend un Utilisateur dans le RequestBody : {"username":"nom","password":"mdp"}<br>
> - L'utilisateur est enregistré dans la base Mongo.<br>

> **/user/list** :
> - Affiche la liste des Utilisateurs dans la base Mongo.<br>

> **/user/delete** :
> - Prend un String (token) en header ("Authorization")
> - Vérifie les identifiants entrés et supprime l'Utilisateur, le Joueur associé de la base, et le token utilisé
<br>

> **/token/login** : <br>
> - Prend un Utilisateur dans le RequestBody : {"username":"nom","password":"mdp"}<br>
> - Si les identifiants sont valides, renvoie un **Token** et le sauvegarde dans la base.

> **/token/check** : <br>
> - Prend le **header Authorization** en entrée (champ token d'un Token)
> - Vérifie qu'il existe dans la base et qu'il n'est pas expiré (si expiré -> supprimé de la base)
> - Renvoie le nom de l'utilisateur associé au token s'il est valide

> /token/list : <br>
> - Affiche la liste des **Tokens** enregistrés dans la base Mongo.

</details>
<br>

<details open>
<summary> PlayerAPI : </summary>

> Url : http://localhost:27019/players

### Objets :
- **Player** : String username, int level, double experience, List<Monstre> monsters;
- **Monstre** : String id

### Endpoints :
**Sans token** :
> **/list** :
> - Renvoie la liste de tous les joueurs inscrits

**Avec token (header "Authorization")** :
> **/register** :
> - Crée un Player pour le User associé au Token

> **/deleteMyPlayer** :
> - Supprime le Player du compte User associé au Token

> **/info** :
> - Renvoie toutes les informations du compte associé au Token

> **/monsters** :
> - Renvoie la liste des monstres possédés par le Player

> **/level** :
> - Renvoie le niveau du joueur

> **/getXp/{quantity}** :
> - Simule un gain d'XP pour le joueur (augmente xp, lvl up...)

> **/levelup** :
> - Simule un gain de niveau pour le joueur (reset exp, +1 lvl...)

> **/monsters/add/{id}** : 
> - Ajoute un monstre à la liste des monstres du joueur

> **/monsters/remove/{monsterId}** :
> - Retire un monstre du joueur à partir de son id (ex: "pikachu")



</details>
<br>


<details open>
<summary> MonsterAPI : </summary>

> Url : http://localhost:27021/monsters


### Objets :
- **Ability** : int num, int dmg, Ratio ratio, int cooldown, int lvl, int lvlMax;
- **Monstre** : String id, String element, int level, double experience, int skillPoints, int hp, int atk, int def, int spd, List skills, float lootRate;

### Endpoints :

**Avec token (header "Authorization")** :
> **/list** :
> - Renvoie la liste de tous les monstres du player

> **/globalList** :
> - Renvoie la liste de tous les monstres

> **/addBulkGlobal** :
> - Créer plusieurs monstres dans la liste des monstres

> **/addBulkPlayer** :
> - Créer plusieurs monstres dans la liste des monstres possédés par le Player

> **/addPlayer** :
> - Créer un monstre dans la liste des monstres pour un player

> **/addGlobal** :
> - Créer un monstre dans la liste des monstres

> **/getXp/{monsterId}/{quantity}** :
> - Simule un gain d'XP pour le monstre (augmente xp, lvl up, points de compétence...)

### Tests
> **/resetPlayerMonsters** :
> - Fonction de test (Supprimer tous les monstres du player)

> **/resetGlobalMonsters** :
> - Fonction de test (Supprimer tous les monstres)


</details>
<br>

<details open>
<summary> InvocAPI : </summary>

> Url : http://localhost:27022/invoc

/ ! \ La fonctionnalité de replay en cas de serveur down n'est pas implémentée

### Objets
- **Monster** : identique à l'API Monstres

### Endpoints
> **/draw** :
> - Tire un monstre au hasard, et l'enregistre dans la liste du Joueur associé au token. Le monstre est enregistré dans les API Monstre et Joueur. 



### Tests
Un endpoint est disponible pour tester l'algorithme d'invocation, en remplaçant *{quantity}* par le nombre de tirages souhaités (pas de token nécessaire pour ce test):
> http://localhost:27022/invoc/testDrawFunction/{quantity}

Les résultats des tirages sont écrits dans les logs du conteneur "invoc-api"

</details>
<br>
<details open>
<summary> CombatAPI : (non implémentée)</summary>
</details>
<br>
<details open>
<summary> MongoDB : </summary>

> Url : http://localhost:27017

### Gestion des collections
Tous les objets sont stockés dans une db propre à leur API, puis chacun dans une collection qui leur est propre<br><br>
**AuthAPI (authdb)** : <br>
Les tokens sont stockés dans la collection Tokens<br>
Les utilisateurs sont stockés dans la collection Users<br>
<br>
**PlayerAPI (playerdb)** : <br>
Les joueurs sont stockés dans la collection Players<br>
<br>
**MonsterAPI (monsterdb)** : <br>
Tous les monstres présents dans le jeu sont stockés dans la collection GlobalMonsterList<br>
Les monstres d'un joueur sont stockés dans une nouvelle collection : NomDuJoueur+"Monsters"<br>
<br>


</details>

### Mapping des ports
MongoDB : 27017<br>
AuthAPI : 27018<br>
PlayerAPI : 27019<br>
MonsterAPI : 27021<br>
InvocAPI : 27022<br>
CombatAPI (non implémentée) : 27023<br>





