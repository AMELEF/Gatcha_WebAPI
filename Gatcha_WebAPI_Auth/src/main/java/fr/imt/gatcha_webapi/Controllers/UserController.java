package fr.imt.gatcha_webapi.Controllers;

import fr.imt.gatcha_webapi.Beans.AuthToken;
import fr.imt.gatcha_webapi.Beans.User;
import fr.imt.gatcha_webapi.Environment.APIRequests;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import fr.imt.gatcha_webapi.Controllers.AuthTokenController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final MongoTemplate mongoTemplate;
    public APIRequests apiRequests;

    public UserController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @RequestMapping("/register")
    public void registerUser(@RequestBody User userAccount) {
        mongoTemplate.insert(userAccount,"Users");
    }


    @RequestMapping("/delete")
    public void deleteUser(@RequestHeader("Authorization") String token) {
        APIRequests APIClient = new APIRequests(mongoTemplate);
        String username = APIClient.getTokenUsername(token);
        if(mongoTemplate.exists((Query.query(Criteria.where("username").is(username))),User.class,"Users")) {
            mongoTemplate.findAndRemove(Query.query(Criteria.where("username").is(username)), User.class, "Users"); //Supprime le compte
            APIClient.deletePlayer(token); //Supprime le joueur du compte
            mongoTemplate.findAndRemove(Query.query(Criteria.where("token").is(token)), AuthToken.class,"Tokens"); //Supprime le token
        }
    }

    @GetMapping("/list")
    public List<User> getUsers(){
        return mongoTemplate.findAll(User.class,"Users");
    }


}
