package fr.imt.gatcha_webapi.Controllers;

import fr.imt.gatcha_webapi.Beans.Monster;
import fr.imt.gatcha_webapi.Environment.APIRequests;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/invoc")
public class InvocationController {

    private MongoTemplate mongoTemplate;

    public APIRequests apiClient = new APIRequests(mongoTemplate);

    @RequestMapping("/draw")
    public Monster drawMonster(@RequestHeader("Authorization") String token) {
        apiClient.requestAuthTokenValidity(token);
        Random rand = new Random();
        double randValue = rand.nextDouble(); // Génère un nombre aléatoire entre 0.0 et 1.0
        List<Monster> globalMonsterList = apiClient.getGlobalMonsterList();
        double totalLootRate = 0.0;
        for (Monster monster : globalMonsterList) {
            totalLootRate += monster.getLootRate();
            if (randValue < totalLootRate) {
                String monsterid = apiClient.generateDrawnMonster(monster.getId(),token);
                if (monsterid.equals("error")){Monster errormonster = new Monster(); errormonster.setId("ErrorMonster:("); return errormonster;}
                apiClient.sendDrawnMonsterIdToPlayerAPI(token, monsterid);
                return monster;
            }
        }
        return drawMonster(token); // Si aucun objet n'est tiré, on recommence

    }

    public Monster drawTestMonster() {
        Random rand = new Random();
        double randValue = rand.nextDouble(); // Génère un nombre aléatoire entre 0.0 et 1.0
        List<Monster> globalMonsterList = apiClient.getGlobalMonsterList();
        double totalLootRate = 0.0;
        for (Monster monster : globalMonsterList) {
            totalLootRate += monster.getLootRate();
            if (randValue < totalLootRate) {
                return monster;
            }
        }
        return drawTestMonster(); // Si aucun objet n'est tiré, on recommence

    }

    @RequestMapping("/testDrawFunction/{drawNumber}")
    public void testDrawFunction(@PathVariable int drawNumber){
        System.out.println("------------- Draw probability test ----------------");
        List<Monster> globalMonsterList = apiClient.getGlobalMonsterList();
        List<String> drawnMonsterList = new ArrayList<>();
        for (int i=0;i<drawNumber;i++){
            drawnMonsterList.add(drawTestMonster().getId());
        }
        for (Monster globalMonster:globalMonsterList) {
            String globalMonsterName = globalMonster.getId();
            int drawnCounter = 0;
            for (String drawnMonster:drawnMonsterList) {
                if(drawnMonster.equals(globalMonsterName)){
                    drawnCounter++;
                }
            }
            System.out.println(globalMonsterName+" has been drawn "+drawnCounter+" times");
            System.out.println("Test said monster "+globalMonsterName+" has a dropRate of "+(float)drawnCounter/(float)drawNumber*100+"% instead of "+globalMonster.getLootRate()*100+"%");
        }
        System.out.println("-------------------------------------------");
    }

}
