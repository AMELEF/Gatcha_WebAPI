package fr.imt.gatcha_webapi.Controllers;

import fr.imt.gatcha_webapi.Beans.Monster;
import fr.imt.gatcha_webapi.Environment.APIRequests;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monster")
public class MonsterController {
    private final MongoTemplate mongoTemplate;

    public MonsterController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @GetMapping
    public Object listMonsters(@RequestHeader("Authorization") String token) {
        String userName = new APIRequests(mongoTemplate).requestAuthTokenValidity(token);
        if (mongoTemplate.exists(Query.query(Criteria.where("username").is(userName)),"Users"))  return mongoTemplate.findAll(Monster.class,"Monsters");
        else return HttpStatus.NOT_FOUND;
    }



}
