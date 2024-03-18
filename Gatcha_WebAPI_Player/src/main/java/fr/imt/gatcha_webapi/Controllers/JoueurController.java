package fr.imt.gatcha_webapi.Controllers;

import fr.imt.gatcha_webapi.Beans.Joueur;
import fr.imt.gatcha_webapi.Beans.Monstre;
import fr.imt.gatcha_webapi.Encryption.AES256;
import fr.imt.gatcha_webapi.Environment.APIRequests;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.pow;

@RestController
@RequestMapping("/players")
public class JoueurController {

    private MongoTemplate mongoTemplate;
    public APIRequests authAPIClient = new APIRequests(mongoTemplate);

    public JoueurController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Returns true if token is valid
     * @param token
     * @return Boolean
     */
    @RequestMapping("/testtoken")
    public String testToken(@RequestHeader("Authorization") String token){
        return authAPIClient.requestAuthTokenValidity(token);
    }

    @GetMapping("/list")
    public List<Joueur> getPlayerList(){
        return mongoTemplate.findAll(Joueur.class,"Players");
    }

    @RequestMapping("/register")
    public String addPlayer(@RequestHeader("Authorization") String token){
        String username = testToken(token);
        if (!mongoTemplate.exists(Query.query(Criteria.where("identifiant").is(username)),Joueur.class,"Players")){
            mongoTemplate.save(new Joueur(username), "Players");
            return "200 Player has been created";
        }
        else{
            return "401 Invalid token or player already exists";
        }
    }


    @RequestMapping("/deleteMyPlayer")
    public String removePlayer(@RequestHeader("Authorization") String token){
        String username = testToken(token);
        if (mongoTemplate.exists(Query.query(Criteria.where("identifiant").is(username)),Joueur.class,"Players")) {
            mongoTemplate.remove(getInformationsProfil(token), "Players");
            return "200 Your player has been deleted, please go to /players/register to create a new one.";
        }
        return "401 Error deleting your player, invalid token or player doesn't exist";
    }

    @GetMapping("/info")
    public Joueur getInformationsProfil(@RequestHeader("Authorization") String token) {
        String username = testToken(token);
        return mongoTemplate.findById(username, Joueur.class, "Players");
    }

    @GetMapping("/monsters")
    public List<Monstre> getListeMonstres(@RequestHeader("Authorization") String token) {
        String username = testToken(token);
        return mongoTemplate.findById(username, Joueur.class, "Players").getMonsters();
    }

    @PostMapping("/level")
    public int getNiveau(@RequestHeader("Authorization") String token) {
        String username = testToken(token);
        return mongoTemplate.findById(username, Joueur.class, "Players").getLevel();
    }

    @PostMapping("/getXp/{quantity}")
    public void gainExperience(@PathVariable double quantity, @RequestHeader("Authorization") String token) {
        String username = testToken(token);
        double playerXp = mongoTemplate.findById(username,Joueur.class,"Players").getExperience();
        double newXp = playerXp+quantity;
        int playerLevel = mongoTemplate.findById(username,Joueur.class,"Players").getLevel();
        int newLevel = playerLevel;
        while(newXp>50*pow(1.1,playerLevel)){
            //Ajoute la quantité entrée en paramètre et baisse le niveau
            newXp = newXp - 50*pow(1.1,newLevel);
            if(newLevel<50) {
                newLevel++;
            }
        }
        mongoTemplate.findAndModify(Query.query(Criteria.where("username").is(username)), Update.update("experience", newXp), Joueur.class, "Players");
        mongoTemplate.findAndModify(Query.query(Criteria.where("username").is(username)), Update.update("level", newLevel), Joueur.class, "Players");
    }

    @PostMapping("/levelup")
    public void gainNiveau(@RequestHeader("Authorization") String token) {
        String username = testToken(token);
        int playerLevel = mongoTemplate.findById(username,Joueur.class,"Players").getLevel();
        mongoTemplate.findAndModify(Query.query(Criteria.where("username").is(username)), Update.update("level", playerLevel+1), Joueur.class, "Players");
    }

    @PostMapping("/monsters/add/{id}")
    public void acquisitionMonstre(@RequestHeader("Authorization") String token,@RequestBody int monsterId) {
        String username = testToken(token);
        List<Monstre> playerMonsters = mongoTemplate.findById(username,Joueur.class,"Players").getMonsters();
        //playerMonsters.add(monster);
        mongoTemplate.findAndModify(Query.query(Criteria.where("username").is(username)), Update.update("monsters", playerMonsters), Joueur.class, "Players");
    }

    @PostMapping("/monsters/remove/{id}")
    public void suppressionMonstre(@RequestHeader("Authorization") String token, @PathVariable int monstreId) {
        String username = testToken(token);
        List<Monstre> playerMonsters = mongoTemplate.findById(username,Joueur.class,"Players").getMonsters();
        //playerMonsters.add(monster);
        mongoTemplate.findAndModify(Query.query(Criteria.where("username").is(username)), Update.update("monsters", playerMonsters), Joueur.class, "Players");
    }

}