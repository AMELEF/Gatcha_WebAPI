package fr.imt.gatcha_webapi.Environment;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

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
}
