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
    public List<Monster> listAllMonsters() {
        return mongoTemplate.findAll(Monster.class,"GlobalMonsterList");
    }

    @RequestMapping("/addBulkGlobal")
    public void bulkAddMonsterGlobal(@RequestBody List<Monster> monsterList){
        for (Monster monster:monsterList) {
            addMonster(monster);
        }
    }

    @RequestMapping("/addBulkPlayer")
    public void bulkAddMonsterPlayer(@RequestBody List<Monster> monsterList,@RequestHeader("Authorization") String token){
        for (Monster monster : monsterList) {
            addMonsterPlayer(monster,token);
        }
    }

    @RequestMapping("/addPlayer")
    public String addMonsterPlayer(@RequestBody Monster monster, @RequestHeader("Authorization") String token){
        if (monster!=null) {
            mongoTemplate.save(monster, playerMonsterCollection(token));
            return monster.getId();
        }
        return "error";
    }

    @RequestMapping("/addGlobal")
    public void addMonster(@RequestBody Monster monster){
        List<Monster> globalMonsterList = mongoTemplate.findAll(Monster.class,"GlobalMonsterList");
        //On vérifie que l'id n'est pas déjà pris
        if(!mongoTemplate.exists(Query.query(Criteria.where("id").is(monster.getId())),Monster.class,"GlobalMonsterList")) {
            // On vérifie que la somme des lootRates fait 1
            float lootRateSum = 0;
            for(Monster m:globalMonsterList){
                lootRateSum+=m.getLootRate();
            }
            if(lootRateSum+monster.getLootRate()<=1){
                mongoTemplate.save(monster,"GlobalMonsterList");
            }
            else{
                //On recalibre les taux de drops des monstres déjà présents
                for(Monster m:globalMonsterList){
                    float newLootRate = (1-monster.getLootRate())*m.getLootRate();
                    mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(m.getId())),Update.update("lootRate",newLootRate), Monster.class,"GlobalMonsterList");
                }
                mongoTemplate.save(monster,"GlobalMonsterList");
            }
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
        //Mise à jour des stats
        mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(monsterId)), Update.update("hp",monster.getHp()*(pow(1.1,level))),Monster.class,playerMonsterCollection(token));
        mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(monsterId)), Update.update("atk",monster.getAtk()*(pow(1.1,level))),Monster.class,playerMonsterCollection(token));
        mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(monsterId)), Update.update("def",monster.getDef()*(pow(1.1,level))),Monster.class,playerMonsterCollection(token));
        mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(monsterId)), Update.update("spd",monster.getSpd()*(pow(1.1,level))),Monster.class,playerMonsterCollection(token));
    }



}
