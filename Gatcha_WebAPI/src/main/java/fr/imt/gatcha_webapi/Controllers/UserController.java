package fr.imt.gatcha_webapi.Controllers;

import fr.imt.gatcha_webapi.Beans.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final MongoTemplate mongoTemplate;

    public UserController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @RequestMapping("/register")
    public void registerUser(@RequestBody User userAccount) {
        mongoTemplate.insert(userAccount,"Users");
    }

    @RequestMapping("/delete")
    public void deleteUser(@RequestBody User userAccount) {
        mongoTemplate.findAndRemove(Query.query(Criteria.where("username").is(userAccount.getUsername())),User.class);
    }

    @GetMapping
    public List<User> getUsers(){
        return mongoTemplate.findAll(User.class,"Users");
    }


}
