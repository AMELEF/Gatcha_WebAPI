package fr.imt.gatcha_webapi.Environment;

import fr.imt.gatcha_webapi.Beans.Monster;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class APIRequests {

    private final MongoTemplate mongoTemplate;

    public APIRequests(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public String requestAuthTokenValidity(String token){
        String url = APIURLs.AUTHAPI.link+"/token/check";
        RestClient restClient = RestClient.create();
        return restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",token)
                .retrieve()
                .body(String.class);
    }

    public List<Monster> getGlobalMonsterList(){
        String url = APIURLs.MONSTERAPI.link+"/monsters/globalList";
        RestTemplate restTemplate = new RestTemplate();
        return Arrays.stream(restTemplate.getForEntity(url,Monster[].class).getBody()).toList();
    }

    public String generateDrawnMonster(Monster monster, String token){
        String url = APIURLs.MONSTERAPI.link+"/monsters/addPlayer";
        RestClient restClient = RestClient.create();
        return restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(monster)
                .header("Authorization",token)
                .retrieve()
                .body(String.class);
    }

    public void sendDrawnMonsterIdToPlayerAPI(String token, String monsterId){
        String url = APIURLs.PLAYERAPI.link+"/players/monsters/add/"+monsterId;
        RestClient restClient = RestClient.create();
        restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",token)
                .retrieve();
    }
}
