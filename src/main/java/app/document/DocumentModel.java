package app.document;


import app.account.AccountModel;
import app.account.AccountRepository;
import app.client.ClientModel;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 13.11.2016.
 */
@Entity
public class DocumentModel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CLIENT_ID")
    private ClientModel client;
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FROM_ACC")
    private AccountModel from;
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TO_ACC")
    private AccountModel to;
    private float amount;
    private long date;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    protected DocumentModel() {}

    public DocumentModel(ClientModel client,AccountModel from, AccountModel to, float amount) {
        this.client=client;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.date = new Date().getTime();
    }

    public AccountModel getFrom() {
        return from;
    }

    public ClientModel getClient() {
        return client;
    }

    public void setClient(ClientModel client) {
        this.client = client;
    }

    public void setFrom(AccountModel from) {
        this.from = from;
    }

    public AccountModel getTo() {
        return to;
    }

    public void setTo(AccountModel to) {
        this.to = to;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }


    public void execute(AccountRepository accountRepository) throws Exception {
        AccountModel from=this.from;
        AccountModel to=this.to;
        if(from==null||to==null)
            throw new Exception("Wrong from/to account");
        List<AccountModel> list=new ArrayList<>();
        list.add(from);
        list.add(to);
        from.setBalance(from.getBalance()-this.amount);
        to.setBalance(to.getBalance()+this.amount);
        accountRepository.save(list);
    }


    public void revert(AccountRepository accountRepository) throws Exception {
        AccountModel from=this.from;
        AccountModel to=this.to;
        if(from==null||to==null)
            throw new Exception("Wrong from/to account");
        List<AccountModel> list=new ArrayList<>();
        list.add(from);
        list.add(to);
        to.setBalance(to.getBalance()-this.amount);
        from.setBalance(from.getBalance()+this.amount);
        accountRepository.save(list);

    }
}
