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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static fr.imt.gatcha_webapi.Encryption.AES256.encrypt;

@RestController
@RequestMapping("/token")
public class AuthTokenController {

    private final MongoTemplate mongoTemplate;

    public AuthTokenController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping
    public String welcome() {
        return "OK OK Get Page of /token";
    }

    @RequestMapping("/login")
    public AuthToken authenticate(@RequestBody User userCredentials) throws NoSuchAlgorithmException {
        String user = userCredentials.getUsername();
        String pass = userCredentials.getPassword();

        User searchResult = mongoTemplate.findById(user,User.class);
        if(searchResult.getPassword().equals(pass)) {
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(new Date());
            System.out.println(timeStamp);
            String plainToken = user + "-" + timeStamp;
            String cryptedToken = AES256.encrypt(plainToken, "QSDFGHJKLM", "");
            System.out.println(cryptedToken);
            AuthToken authToken = new AuthToken();
            authToken.setToken(cryptedToken);
            return authToken;
        }
        return new AuthToken();
    }



}

