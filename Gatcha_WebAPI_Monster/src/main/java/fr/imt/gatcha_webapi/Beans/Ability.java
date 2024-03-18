package fr.imt.gatcha_webapi.Beans;

import fr.imt.gatcha_webapi.Beans.Attacks.Ratio;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Ability {
    @Id
    int num;
    int dmg;
    Ratio ratio;
    int cooldown;
    int lvl;
    int lvlMax;
}
