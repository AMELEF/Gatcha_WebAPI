package fr.imt.gatcha_webapi.Environment;

public enum APIURLs {
    MONGODB("http://host.docker.internal:27017"),
    AUTHAPI("http://host.docker.internal:27018"),
    PLAYERAPI("http://host.docker.internal:27019"),
    MONSTERAPI("http://host.docker.internal:27021"),
    INVOCAPI("http://host.docker.internal:27022");

    public final String link;

    APIURLs(String link) {
        this.link = link;
    }
}
