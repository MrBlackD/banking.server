package app.controllers;


import app.account.Account;
import app.account.AccountModel;
import app.account.AccountRepository;
import app.account.AccountWrapper;
import app.authLogger.AuthLoggerRepository;
import app.client.ClientModel;
import app.client.ClientRepository;
import app.exceptions.PermissionException;
import app.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Admin on 10.11.2016.
 */
@CrossOrigin
@RestController
public class AccountController {
    private final AccountRepository repository;
    private final ClientRepository client_repository;
    private final AuthLoggerRepository authLoggerRepository;

    @Autowired
    public AccountController(AccountRepository repository, ClientRepository client_repository,AuthLoggerRepository authLoggerRepository) {
        this.repository = repository;
        this.client_repository = client_repository;
        this.authLoggerRepository=authLoggerRepository;
    }




    @RequestMapping("/accounts")
    public AccountWrapper readAll(@RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.adminCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        List<Account> response=new ArrayList<>();
        for (AccountModel account : this.repository.findAll()) {
            response.add(new Account(account));
        }

        return new AccountWrapper(response);
    }
    @RequestMapping("/accounts/{account_id}")
    public Account readOne(@PathVariable String account_id,@RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.adminCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        return new Account(repository.findOne(Long.parseLong(account_id)));
    }

    @RequestMapping(path="/client/{client_id}/accounts/new",method = RequestMethod.POST)
    public String createForClient(@PathVariable String client_id,
                                  @RequestParam(name="value",required = false,defaultValue = "0")String value,
                                  @RequestParam(name = "accesstoken")String accesstoken) throws Exception {
        if(!Auth.adminCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        ClientModel client=client_repository.findOne(Long.parseLong(client_id));
        System.out.println(client);
        if(client==null)
            throw new Exception("no such id");
        repository.save(new AccountModel(client, Float.parseFloat(value)));
        return "Added";
    }

    @RequestMapping(path="/client/accounts/new",method = RequestMethod.POST)
    public String create(@RequestParam(name="value",required = false,defaultValue = "0")String value,@RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.authCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        ClientModel authorizedClient=Auth.authorizedClient(accesstoken,authLoggerRepository);
        repository.save(new AccountModel(authorizedClient, Float.parseFloat(value)));
        return "Added";
    }
    @RequestMapping("/client/accounts")
    public AccountWrapper readAllForClient(@RequestParam(name="accesstoken")String accesstoken, HttpSession session) throws PermissionException {
        if(!Auth.authCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        ClientModel authorizedClient=Auth.authorizedClient(accesstoken,authLoggerRepository);
        List<Account> response= this.repository.findByClient(authorizedClient).stream().map(Account::new).collect(Collectors.toList());
        return new AccountWrapper(response);
    }

    @RequestMapping("/client/accounts/{account_id}")
    public Account readOneForClient(@PathVariable String account_id,@RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.authCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        ClientModel authorizedClient=Auth.authorizedClient(accesstoken,authLoggerRepository);

        return new Account(this.repository.findByAccountIdInAndClientIn(Long.parseLong(account_id),authorizedClient));
    }

    @RequestMapping("/client/{client_id}/accounts")
    public AccountWrapper readAllById(@PathVariable String client_id,@RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.adminCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        List<Account> response= this.repository.findByClient(client_repository.findOne(Long.parseLong(client_id))).stream().map(Account::new).collect(Collectors.toList());
        return new AccountWrapper(response);
    }
    @RequestMapping("/client/{client_id}/accounts/{account_id}")
    public Account readOneById(@PathVariable String client_id,
                               @PathVariable String account_id,
                               @RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.adminCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
       return new Account(this.repository.findByAccountIdInAndClientIn(Long.parseLong(account_id),client_repository.findOne(Long.parseLong(client_id))));
    }

    @RequestMapping(path="/accounts/{account_id}/del",method = RequestMethod.POST)
    public String delete(@PathVariable String account_id,@RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.adminCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        repository.delete(repository.findOne(Long.parseLong(account_id)));
        return "Deleted";
    }

    @RequestMapping(path="/client/accounts/{account_id}/del",method = RequestMethod.POST)
    public String deleteForClient(@PathVariable String account_id,
                                  @RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.authCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        ClientModel authorizedClient=Auth.authorizedClient(accesstoken,authLoggerRepository);
        repository.delete(this.repository.findByAccountIdInAndClientIn(Long.parseLong(account_id),authorizedClient));
        return "Deleted";
    }

    @RequestMapping(path="/accounts/{account_id}/update",method = RequestMethod.POST)
    public Account update(@PathVariable String account_id,
                          @RequestParam(name="client_id",defaultValue = "") String client_id,
                          @RequestParam(name="balance",defaultValue = "0") String balance,
                          @RequestParam(name = "accesstoken")String accesstoken) throws Exception {
        if(!Auth.adminCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        AccountModel account=repository.findOne(Long.parseLong(account_id));
        if(client_id==""||client_id.isEmpty())
            client_id=""+account.getClient().getId();
        if(balance==""||balance.isEmpty())
            balance=""+account.getBalance();
        ClientModel clientModel=client_repository.findOne(Long.parseLong(client_id));
        if(clientModel==null)
            throw new Exception("no such client id");
        account.setClient(clientModel);
        account.setBalance(Float.parseFloat(balance));
        repository.save(account);
        return new Account(account);
    }

}
