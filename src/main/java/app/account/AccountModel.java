package app.account;

import app.client.ClientModel;

import javax.persistence.*;

/**
 * Created by Admin on 10.11.2016.
 */
@Entity
@Table(name="ACCOUNT")
public class AccountModel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long accountId;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CLIENT_ID")
    private ClientModel client;

    private float balance;

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public long getAccount_id() {
        return accountId;
    }

    public void setAccount_id(long account_id) {
        this.accountId = account_id;
    }

    public ClientModel getClient() {
        return client;
    }

    public void setClient(ClientModel client) {
        this.client = client;
    }

    protected AccountModel() {}
    public AccountModel(ClientModel client,float balance) {
        this.balance=balance;
        this.client = client;
    }

    @Override
    public String toString() {
        return String.format(
                "Client[account_id=%d, client_id='%s', balance='%s']",
                accountId, client.getId(), balance);
    }
}
