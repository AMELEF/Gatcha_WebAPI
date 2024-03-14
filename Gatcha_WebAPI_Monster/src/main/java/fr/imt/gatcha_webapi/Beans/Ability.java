package fr.imt.gatcha_webapi.Beans;

import org.springframework.data.annotation.Id;

public class Ability {
    @Id
    int id;
    int baseDamage;
    float bonusDmgRatio;
    int cooldown;
    int level;
    int maxLevel;
}
