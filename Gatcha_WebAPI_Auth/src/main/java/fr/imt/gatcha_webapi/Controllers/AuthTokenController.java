package fr.imt.gatcha_webapi.Controllers;

import fr.imt.gatcha_webapi.Beans.AuthToken;
import fr.imt.gatcha_webapi.Beans.User;
import fr.imt.gatcha_webapi.Encryption.AES256;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/token")
public class AuthTokenController {

    private final MongoTemplate mongoTemplate;

    public AuthTokenController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping
    public String welcome() {
        return "Sub-url :\n /login \n /check";
    }

    @RequestMapping("/login")
    public AuthToken authenticate(@RequestBody User userCredentials) throws NoSuchAlgorithmException {
        String user = userCredentials.getUsername();
        String pass = userCredentials.getPassword();

        User searchResult = mongoTemplate.findOne(Query.query(Criteria.where("username").is(user)),User.class,"Users");
        if (searchResult == null) return new AuthToken();
        if(searchResult.getPassword().equals(pass)) {
            AuthToken authToken = new AuthToken();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
            String timeStamp = dateFormat.format(new Date());
            System.out.println(timeStamp);
            String plainToken = user + "-" + timeStamp;
            System.out.println(plainToken);
            String cryptedToken = AES256.encrypt(plainToken, "QSDFGHJKLM", "OKOKOK");
            System.out.println(cryptedToken);
            authToken.setToken(cryptedToken);
            authToken.setExpirationDate(Instant.now().plus(1, ChronoUnit.HOURS));
            System.out.println(authToken.getExpirationDate());
            mongoTemplate.save(authToken,"Tokens");
            return authToken;
        }
        return new AuthToken();
    }

    private void renew(AuthToken token){
        mongoTemplate.findAndModify(Query.query(Criteria.where("token").is(token.getToken())), Update.update("expirationDate", Instant.now().plus(1,ChronoUnit.HOURS)),AuthToken.class, "Tokens");
    }

    /**
     * Vérifie un token, retourne le nom d'utilisateur si le token est valide, et met à jour sa date d'expiration, Erreur 401 sinon.
     * @param token
     * @return String
     * @throws ParseException
     */
    @RequestMapping("/check")
    public String checkToken(@RequestHeader("Authorization") String token) throws ParseException {
        ResponseStatusException error401 = new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Token Invalide, reconnexion nécessaire");
        if (token == null) throw error401;
        String plaintoken = AES256.decrypt(token,"QSDFGHJKLM","OKOKOK");
        AuthToken dbToken = mongoTemplate.findOne(Query.query(Criteria.where("token").is(token)), AuthToken.class, "Tokens");
        if (dbToken == null) {throw error401;}
        Instant tokenExpiration = dbToken.getExpirationDate();
        if (plaintoken == null) throw error401;
        if (Instant.now().compareTo(tokenExpiration) < 0){
            renew(dbToken);
            return plaintoken.substring(0,plaintoken.indexOf("-"));
        }
        if (mongoTemplate.exists(Query.query(Criteria.where("token").is(token)),AuthToken.class, "Tokens")) {
            mongoTemplate.findAndRemove(Query.query(Criteria.where("token").is(token)), AuthToken.class, "Tokens");
        }
        throw error401;
    }

    @RequestMapping("/list")
    public List<AuthToken> listTokens(){
        return mongoTemplate.findAll(AuthToken.class,"Tokens");
    }


}

