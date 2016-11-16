package app.account;

/**
 * Created by Admin on 10.11.2016.
 */
public class Account {
    private final long account_id;
    private final long client_id;
    private final float balance;

    public long getAccount_id() {
        return account_id;
    }

    public long getClient_id() {
        return client_id;
    }

    public float getBalance() {
        return balance;
    }



    public Account(long account_id, long client_id, float balance) {
        this.account_id = account_id;
        this.client_id = client_id;
        this.balance = balance;
    }

    public Account(AccountModel model){
        this.account_id=model.getAccount_id();
        this.client_id=model.getClient().getId();
        this.balance=model.getBalance();
    }
}
