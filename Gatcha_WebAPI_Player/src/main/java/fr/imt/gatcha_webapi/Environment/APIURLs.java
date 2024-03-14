package fr.imt.gatcha_webapi.Environment;

public enum APIURLs {
    MONGODB("http://localhost:27017"),
    AUTHAPI("http://localhost:27018"),
    PLAYERAPI("http://localhost:27019"),
    MONSTERAPI("http://localhost:27021"),
    INVOCAPI("http://localhost:27022");

    public final String link;

    private APIURLs(String link) {
        this.link = link;
    }
}
