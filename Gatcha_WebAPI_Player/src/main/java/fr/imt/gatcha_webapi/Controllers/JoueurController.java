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
import org.springframework.http.HttpStatusCode;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/players")
public class JoueurController {

    private MongoTemplate mongoTemplate;
    public APIRequests authAPIClient = new APIRequests(mongoTemplate);

    public JoueurController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @RequestMapping("/testtoken")
    public String testToken(@RequestHeader("Authorization") String token){
        return authAPIClient.requestAuthTokenValidity(token).toString();
    }

    @GetMapping("/list")
    public List<Joueur> getPlayerList(){
        return mongoTemplate.findAll(Joueur.class,"Players");
    }

    @RequestMapping("/add")
    public void addPlayer(@RequestBody String username, @RequestHeader("Authorization") String token){
        if(authAPIClient.requestAuthTokenValidity(token)== HttpStatusCode.valueOf(200)){
            if (!mongoTemplate.exists(Query.query(Criteria.where("identifiant").is(username)),Joueur.class,"Players")){
                mongoTemplate.save(new Joueur(username), "Players");
            }
        }
    }

    @GetMapping("/{id}")
    public Joueur getInformationsProfil(@PathVariable String id, @RequestHeader("Authorization") String token) {
        if(authAPIClient.requestAuthTokenValidity(token)== HttpStatusCode.valueOf(200)){
            return mongoTemplate.findById(id, Joueur.class, "Players");
        }
        return null;
    }

    @GetMapping("/{id}/monsters")
    public List<Monstre> getListeMonstres(@PathVariable String id, @RequestHeader("Authorization") String token) {
        if(authAPIClient.requestAuthTokenValidity(token)== HttpStatusCode.valueOf(200)){
            return getInformationsProfil(id,token).getMonstres();
        }
        return null;
    }

    @PostMapping("/{id}/level")
    public int getNiveau(@PathVariable String id, @RequestHeader("Authorization") String token) {
        if(authAPIClient.requestAuthTokenValidity(token)== HttpStatusCode.valueOf(200)){
            return getInformationsProfil(id, token).getLevel();
        }
        return -1;
    }

    @PostMapping("/{id}/experience")
    public void gainExperience(@PathVariable String id, @RequestBody double quantite, @RequestHeader("Authorization") String token) {
        Joueur joueur = getInformationsProfil(id, token);
        double newExperience = joueur.getExperience();
    }

    @PostMapping("/{id}/levelup")
    public void gainNiveau(@PathVariable String id, @RequestHeader("Authorization") String token) {
        Joueur joueur = getInformationsProfil(id, token);
        joueur.setExperience(0);
        if (joueur.getLevel()<50) {
            joueur.setLevel(joueur.getLevel() + 1);
            //Augmenter la taille de la liste des monstres de 1
        }
    }

    @PostMapping("/monsters/add")
    public void acquisitionMonstre(@RequestBody Joueur joueur) {
        joueur.acquisitionMonstre();
    }

    @PostMapping("/monsters/remove")
    public void suppressionMonstre(@RequestBody Joueur joueur, @PathVariable int monstreId) {
        joueur.suppressionMonstre(monstreId);
    }

}