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
    private String identifiant;
    private int level;
    private double experience;
    private List<Monstre> monstres;

    @Override
    public String toString() {
        return
                "Identifiant :" + identifiant + "\n" +
                "Level :" + level + "\n"+
                "Expérience :" + experience + "\n" +
                "Monstres :" + monstres;
    }

    public Joueur(String identifiant) {
        this.identifiant = identifiant;
        this.level = 1;
        this.experience = 50;
        this.monstres = new ArrayList<Monstre>(10);
    }

    // Récupération du niveau du joueur
    public int getNiveau() {
        return level;
    }

    // Gain d'expérience
    public void gainExperience(double quantite) {
        experience += quantite;
        while (experience >= 50 * Math.pow(1.1, level - 1)) {
            level++;
            // Augmenter la taille max de la liste de monstres
            monstres.add(new Monstre(monstres.size() + 1));
        }
    }

    // Gain de niveau
    public void gainNiveau() {
        level++;
        experience = 50;
        // Augmenter le seuil de level up
        // Augmenter la taille max de la liste de monstres
        monstres.add(new Monstre(monstres.size() + 1));
    }

    // Acquisition d'un nouveau monstre
    public void acquisitionMonstre() {
        monstres.add(new Monstre(monstres.size() + 1));
    }

    // Suppression d'un monstre
    public void suppressionMonstre(int monstreId) {
        monstres.removeIf(monstre -> monstre.getId() == monstreId);
    }
}
