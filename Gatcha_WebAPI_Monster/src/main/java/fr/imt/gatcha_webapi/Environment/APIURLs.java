package fr.imt.gatcha_webapi.Environment;

public enum APIURLs {
    MONGODB("localhost:27017"),
    AUTHAPI("localhost:27018"),
    PLAYERAPI("localhost:27019"),
    MONSTERAPI("localhost:27021"),
    INVOCAPI("localhost:27022");

    public final String link;

    private APIURLs(String link) {
        this.link = link;
    }
}
