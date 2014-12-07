package de.lucawimmer.cloudinventory.objects;

public class ServerInfo {
    private String password;
    private String server;
    private String player;
    private String playerip;
    private String playerdata;
    private String header;
    private String ownpassword;
    private String destination;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPlayerip() {
        return playerip;
    }

    public void setPlayerip(String playerip) {
        this.playerip = playerip;
    }

    public String getPlayerdata() {
        return playerdata;
    }

    public void setPlayerdata(String playerdata) {
        this.playerdata = playerdata;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getOwnpassword() {
        return ownpassword;
    }

    public void setOwnpassword(String ownpassword) {
        this.ownpassword = ownpassword;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
