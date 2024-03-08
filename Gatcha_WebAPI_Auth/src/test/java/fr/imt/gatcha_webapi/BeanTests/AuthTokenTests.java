package fr.imt.gatcha_webapi.BeanTests;

import fr.imt.gatcha_webapi.Beans.AuthToken;
import fr.imt.gatcha_webapi.Beans.User;
import fr.imt.gatcha_webapi.Controllers.AuthTokenController;
import fr.imt.gatcha_webapi.Controllers.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@SpringBootTest
public class AuthTokenTests {

    /*private static MongoTemplate mongoTemplate;
    @Test
    public void registerUserInDB(){
        UserController userController = new UserController(mongoTemplate);
        User newUser = new User();
        newUser.setUsername("PhilippeVerdier");
        newUser.setPassword("didier04");
        userController.registerUser(newUser);
        User getUserInDb= mongoTemplate.findById("PhilippeVerdier",User.class, "Users");
        assert(getUserInDb.equals(newUser));
        mongoTemplate.findAndRemove(Query.query(Criteria.where("username").is("PhilippeVerdier")),User.class,"Users");
    }*/
}
