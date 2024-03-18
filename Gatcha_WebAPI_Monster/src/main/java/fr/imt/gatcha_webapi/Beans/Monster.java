package fr.imt.gatcha_webapi.Beans;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class Monster {
    @Getter
    @Id
    String id;
    String element;
    int level; //niveau
    double experience; //xp
    int skillPoints; // Points d'amélioration à attribuer
    int hp; //vie
    int atk; //attaque
    int def; //défense
    int spd; //vitesse
    List<Ability> skills; //compétences
    float lootRate; //Taux de drop
}
