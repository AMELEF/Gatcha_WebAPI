package fr.imt.gatcha_webapi.Controllers;

import fr.imt.gatcha_webapi.Beans.Monster;
import fr.imt.gatcha_webapi.Environment.APIRequests;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.Math.pow;

@RestController
@RequestMapping("/monsters")
public class MonsterController {
    private MongoTemplate mongoTemplate;

    public APIRequests apiClient = new APIRequests(mongoTemplate);

    public String playerMonsterCollection(String token){
        String username = apiClient.requestAuthTokenValidity(token);
        return username+"Monsters";
    }

    public MonsterController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @GetMapping("/list")
    public Object listPlayerMonsters(@RequestHeader("Authorization") String token) {
        return mongoTemplate.findAll(Monster.class,playerMonsterCollection(token));
    }

    @GetMapping("/globalList")
    public Object listAllMonsters() {
        return mongoTemplate.findAll(Monster.class,"GlobalMonsterList");
    }

    @RequestMapping("/addBulkGlobal")
    public void bulkAddMonsterGlobal(@RequestBody List<Monster> monsterList){
        for (Monster monster:monsterList) {
            if(!mongoTemplate.exists(Query.query(Criteria.where("id").is(monster.getId())),Monster.class,"GlobalMonsterList")) {
                //On vérifie que l'id n'est pas déjà pris et on ajoute le monstre
                mongoTemplate.save(monster,"GlobalMonsterList");
            }
        }
    }

    @RequestMapping("/addBulkPlayer")
    public void bulkAddMonsterPlayer(@RequestBody List<Monster> monsterList,@RequestHeader("Authorization") String token){
        String username = apiClient.requestAuthTokenValidity(token);
        for (Monster monster:monsterList) {
            if(!mongoTemplate.exists(Query.query(Criteria.where("id").is(monster.getId())),Monster.class,playerMonsterCollection(token))) {
                //On vérifie que l'id n'est pas déjà pris et on ajoute le monstre
                mongoTemplate.save(monster,playerMonsterCollection(token));
            }
        }
    }

    @RequestMapping("/addGlobal")
    public void addMonster(@RequestBody Monster monster){
        if(!mongoTemplate.exists(Query.query(Criteria.where("id").is(monster.getId())),Monster.class,"Monsters")) {
            //On vérifie que l'id n'est pas déjà pris et on ajoute le monstre
            mongoTemplate.save(monster,"GlobalMonsterList");
        }
    }

    @RequestMapping("/resetPlayerMonsters")
    public void resetPlayerMonsters(@RequestHeader("Authorization") String token){
        mongoTemplate.findAllAndRemove(new Query(),Monster.class,playerMonsterCollection(token));
    }

    @RequestMapping("/resetGlobalMonsters")
    public void resetGlobalMonsters(){
        mongoTemplate.findAllAndRemove(new Query(),Monster.class,"GlobalMonsterList");
    }

    @RequestMapping("/getXp/{monsterId}/{quantity}")
    public void monsterGainXp(@PathVariable String monsterId, @PathVariable double quantity, @RequestHeader("Authorization") String token){
        Monster monster = mongoTemplate.findById(monsterId,Monster.class,playerMonsterCollection(token));
        assert monster != null;
        double xp = monster.getExperience() + quantity;
        int level = monster.getLevel();
        int skillPoints = monster.getSkillPoints();
        //Calcul du reste d'xp, des levels et des points de compétence à ajouter
        while(xp>=50*pow(1.1,level)){
            xp = xp - 50*pow(1.1,level);
            level++;
            skillPoints++;
        }
        //Mise à jour du level, xp et points de compétence
        mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(monsterId)), Update.update("level",level),Monster.class,playerMonsterCollection(token));
        mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(monsterId)), Update.update("skillPoints",skillPoints),Monster.class,playerMonsterCollection(token));
        mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(monsterId)), Update.update("experience",xp),Monster.class,playerMonsterCollection(token));
    }



}
