package fr.imt.gatcha_webapi.Beans;

import fr.imt.gatcha_webapi.Beans.Attacks.Ratio;
import org.springframework.data.annotation.Id;

public class Ability {
    @Id
    int num;
    int dmg;
    Ratio ratio;
    int cooldown;
    int lvl;
    int lvlMax;
}
