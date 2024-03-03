package fr.imt.gatcha_webapi.Controllers;

import fr.imt.gatcha_webapi.Beans.AuthToken;
import fr.imt.gatcha_webapi.Beans.User;
import fr.imt.gatcha_webapi.Encryption.AES256;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
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

        User searchResult = mongoTemplate.findOne(Query.query(Criteria.where("username").is(user)),User.class);
        if(searchResult.getPassword().equals(pass)) {
            AuthToken authToken = new AuthToken();
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(new Date());
            System.out.println(timeStamp);
            String plainToken = user + "-" + timeStamp;
            System.out.println(plainToken);
            String cryptedToken = AES256.encrypt(plainToken, "QSDFGHJKLM", "OKOKOK");
            System.out.println(cryptedToken);
            authToken.setToken(cryptedToken);
            mongoTemplate.save(authToken,"Tokens");
            return authToken;
        }
        return new AuthToken();
    }

    @RequestMapping("/check")
    public String checkToken(@RequestBody AuthToken token) throws ParseException {
        String plaintoken = AES256.decrypt(token.getToken(),"QSDFGHJKLM","OKOKOK");
        System.out.println(plaintoken);
        SimpleDateFormat timestamp = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        Date tokenDate = timestamp.parse(plaintoken.substring(plaintoken.indexOf('-')));
        Date now = new Date();
        if (now.toInstant().compareTo(tokenDate.toInstant().plus(1, ChronoUnit.HOURS)) > 0){
            return plaintoken.substring(0,plaintoken.indexOf("-"));
        }
        if (mongoTemplate.exists(Query.query(Criteria.where("token").is(token.getToken())),AuthToken.class)) {
            mongoTemplate.findAndRemove(Query.query(Criteria.where("token").is(token.getToken())), AuthToken.class);
        }
        return "Erreur 401, veuillez vous connecter";
    }



}

