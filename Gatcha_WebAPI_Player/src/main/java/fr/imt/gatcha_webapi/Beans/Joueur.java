package fr.imt.gatcha_webapi.Beans;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
@Data
@Document
public class Joueur {
    @Id
    private String username;
    private int level;
    private double experience;
    private List<Monstre> monsters;

    @Override
    public String toString() {
        return
                "Identifiant :" + username + "\n" +
                "Level :" + level + "\n"+
                "Expérience :" + experience + "\n" +
                "Monstres :" + monsters;
    }

    public Joueur(String username) {
        this.username = username;
        this.level = 0;
        this.experience = 0;
        this.monsters = new ArrayList<Monstre>(10);
    }
}
