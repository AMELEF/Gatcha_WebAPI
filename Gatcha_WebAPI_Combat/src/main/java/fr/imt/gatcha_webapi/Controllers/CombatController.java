package fr.imt.gatcha_webapi.Controllers;

import fr.imt.gatcha_webapi.Beans.Monster;
import fr.imt.gatcha_webapi.Environment.APIRequests;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.Math.pow;

@RestController
@RequestMapping("/combat")
public class CombatController {
    private MongoTemplate mongoTemplate;

    public APIRequests apiClient = new APIRequests(mongoTemplate);


    public CombatController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


}
