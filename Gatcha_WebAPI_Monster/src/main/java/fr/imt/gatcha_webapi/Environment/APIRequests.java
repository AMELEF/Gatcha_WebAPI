package fr.imt.gatcha_webapi.Environment;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

public class APIRequests {

    private final MongoTemplate mongoTemplate;

    public APIRequests(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public String requestAuthTokenValidity(String token){
        RestClient authAPIClient = RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .baseUrl(APIURLs.AUTHAPI.link)
                .defaultHeader("Authorization",token)
                .build();
        String result = authAPIClient.get().uri(APIURLs.AUTHAPI.link).retrieve().body(String.class);
        return result;
    }
}
