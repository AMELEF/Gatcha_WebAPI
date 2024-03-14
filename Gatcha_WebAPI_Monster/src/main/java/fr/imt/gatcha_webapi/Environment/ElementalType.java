package fr.imt.gatcha_webapi.Environment;

public enum ElementalType {
    FIRE("Feu"),
    WIND("Vent"),
    WATER("Eau");

    public final String label;

    private ElementalType(String label){
        this.label = label;
    }


}
