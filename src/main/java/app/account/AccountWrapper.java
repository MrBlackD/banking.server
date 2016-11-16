package app.account;



import java.util.List;

/**
 * Created by Admin on 09.11.2016.
 */
public class AccountWrapper {
    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public AccountWrapper(List<Account> accounts) {
        this.accounts = accounts;
    }

    public AccountWrapper() {

    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
