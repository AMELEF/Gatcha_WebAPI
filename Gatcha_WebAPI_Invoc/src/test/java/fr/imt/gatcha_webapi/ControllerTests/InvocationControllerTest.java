package fr.imt.gatcha_webapi.ControllerTests;

import fr.imt.gatcha_webapi.Beans.Monster;
import fr.imt.gatcha_webapi.Controllers.InvocationController;
import fr.imt.gatcha_webapi.Environment.APIRequests;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class InvocationControllerTest {
    private MongoTemplate mongoTemplate;
    private APIRequests apiClient = new APIRequests(mongoTemplate);

    public void testDrawProbabilities(int drawNumber){
        System.out.println("-----------------------------");
        List<Monster> globalMonsterList = apiClient.getGlobalMonsterList();
        InvocationController invocationController = new InvocationController();
        List<String> drawnMonsterList = null;
        for (int i=0;i<drawNumber;i++){
            drawnMonsterList.add(invocationController.drawMonster("8uRVFUYIOt7WwJgevMgF+QJc5UFdg8zYjN4XfnlX8SONfOJIVDztm+sdGL4lqCfr").getId());
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
            System.out.println("Test said monster "+globalMonsterName+" has a dropRate of "+(float)drawnCounter/(float)drawNumber+"% instead of "+globalMonster.getLootRate()+"%");
        }
        System.out.println("-----------------------------");
    }
}
