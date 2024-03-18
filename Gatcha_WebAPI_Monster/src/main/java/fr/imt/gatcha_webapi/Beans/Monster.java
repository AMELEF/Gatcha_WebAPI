package fr.imt.gatcha_webapi.Beans;

import fr.imt.gatcha_webapi.Environment.ElementalType;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Monster {
    @Id
    String id;
    ElementalType type;
    int hp; //vie
    int atk; //attaque
    int def; //défense
    int spd; //vitesse
    List<Ability> abilities; //compétences
    float lootRate; //Taux de drop

}
