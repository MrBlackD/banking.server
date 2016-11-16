package app.authLogger;

import app.client.ClientModel;

import javax.persistence.*;

/**
 * Created by Admin on 12.11.2016.
 */
@Entity
public class AuthLoggerModel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CLIENT_ID")
    private ClientModel client;
    private String accessToken;
    private long loginTime;
    private long expires;

    protected AuthLoggerModel() {}

    public AuthLoggerModel(ClientModel client, String accessToken, long loginTime, long expires) {
        this.client = client;
        this.accessToken = accessToken;
        this.loginTime = loginTime;
        this.expires = expires;
    }

    public ClientModel getClient() {
        return client;
    }

    public void setClient(ClientModel client) {
        this.client = client;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }
}
