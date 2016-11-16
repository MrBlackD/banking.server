package app.controllers;

import app.account.AccountModel;
import app.account.AccountRepository;
import app.authLogger.AuthLoggerModel;
import app.authLogger.AuthLoggerRepository;
import app.client.*;
import app.document.DocumentModel;
import app.document.DocumentRepository;
import app.exceptions.PermissionException;
import app.util.Auth;
import app.util.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 09.11.2016.
 */
@CrossOrigin
@RestController
public class ClientController {

    private final ClientRepository repository;
    private final AuthLoggerRepository authLoggerRepository;
    private final AccountRepository accountRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public ClientController(ClientRepository repository,
                            AuthLoggerRepository authLoggerRepository,
                            AccountRepository accountRepository,
                            DocumentRepository documentRepository) {
        this.repository = repository;
        this.authLoggerRepository=authLoggerRepository;
        this.accountRepository=accountRepository;
        this.documentRepository=documentRepository;
    }

    @RequestMapping("/clients")
    public ClientWrapper readAll(@RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
       if(!Auth.adminCheck(accesstoken,authLoggerRepository))
           throw new PermissionException();
        List<Client> response=new ArrayList<>();
        for (ClientModel client : this.repository.findAll()) {
            response.add(new Client(client));
        }

        return new ClientWrapper(response);
    }
    @RequestMapping(path="/client/new",method = RequestMethod.POST)
    public Client create(
            @RequestParam(value="login") String login,
            @RequestParam(value="password") String password,
            @RequestParam(value="firstname") String firstName,
            @RequestParam(value="lastname") String lastName
    ) throws Exception {
        if(login.isEmpty()||password.isEmpty()||firstName.isEmpty()||lastName.isEmpty())
            throw new Exception("Empty field");
        login=login.trim();
        lastName=lastName.trim();
        firstName=firstName.trim();
        password=password.trim();
        if(login.equals("")||password.equals("")||firstName.equals("")||lastName.equals(""))
            throw new Exception("Empty field");
        ClientModel client=new ClientModel(login,firstName, lastName,password,"USER");
        repository.save(client);

        return new Client(client);
    }
    @RequestMapping("/client")
    public Client read(@RequestParam(name = "accesstoken")String accesstoken) throws PermissionException{
        if(!Auth.authCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        ClientModel authedClient=Auth.authorizedClient(accesstoken,authLoggerRepository);

        return new Client(authedClient);
    }

    @RequestMapping("/client/{id}")
    public Client readOne(@PathVariable String id, @RequestParam(name = "accesstoken")String accesstoken) throws PermissionException{
        if(!Auth.adminCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();

        return new Client(this.repository.findOne(Long.parseLong(id)));
    }



    @RequestMapping(path="/client/update",method = RequestMethod.POST)
    public String update(@RequestParam(name="login",defaultValue = "") String login,
                         @RequestParam(name="password",defaultValue = "") String password,
                         @RequestParam(name="firstname",defaultValue = "") String firstName,
                         @RequestParam(name="lastname",defaultValue = "") String lastName,
                         @RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {

        if(!Auth.authCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        ClientModel authedClient=Auth.authorizedClient(accesstoken,authLoggerRepository);

        if(login==""||login.isEmpty())
            login=""+authedClient.getLogin();
        if(password==""||password.isEmpty())
            password=""+authedClient.getPassword();
        else
            password=Security.MD5(password);
        if(firstName==""||firstName.isEmpty())
            firstName=""+authedClient.getFirstName();
        if(lastName==""||lastName.isEmpty())
            lastName=""+authedClient.getLastName();
        authedClient.setLogin(login);
        authedClient.setPassword(password);
        authedClient.setFirstName(firstName);
        authedClient.setLastName(lastName);
        this.repository.save(authedClient);
        return "updated";
    }

    @RequestMapping(path="/client/{id}/update",method = RequestMethod.POST)
    public String updateById(@PathVariable String id,
                             @RequestParam(value="login",defaultValue = "") String login,
                             @RequestParam(value="password",defaultValue = "") String password,
                             @RequestParam(value="firstname",defaultValue = "") String firstName,
                             @RequestParam(value="lastname",defaultValue = "") String lastName,
                             @RequestParam(value="role",defaultValue = "USER") String role,
                             @RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {


        if(!Auth.adminCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();

        ClientModel client=this.repository.findOne(Long.parseLong(id));

        if(login==""||login.isEmpty())
            login=""+client.getLogin();
        if(password==""||password.isEmpty())
            password=""+client.getPassword();
        else
            password=Security.MD5(password);
        if(firstName==""||firstName.isEmpty())
            firstName=""+client.getFirstName();
        if(lastName==""||lastName.isEmpty())
            lastName=""+client.getLastName();

        client.setLogin(login);
        client.setPassword(password);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setRole(role);
        this.repository.save(client);
        return "updated";
    }

    @RequestMapping(path="/client/del",method = RequestMethod.POST)
    public String delete(@RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.authCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        ClientModel authedClient=Auth.authorizedClient(accesstoken,authLoggerRepository);

        List<AccountModel> accounts =this.accountRepository.findByClient(authedClient);
        for (AccountModel account:accounts) {
            this.accountRepository.delete(account);
        }
        this.repository.delete(authedClient);

        return "Deleted";
    }

    @RequestMapping(path="/client/{id}/del",method = RequestMethod.POST)
    public String deleteById(@PathVariable String id,@RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.adminCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        ClientModel authedClient=Auth.authorizedClient(accesstoken,authLoggerRepository);

        ClientModel client=this.repository.findOne(Long.parseLong(id));
        List<AuthLoggerModel> authLoggerModels=authLoggerRepository.findByClient(client);
        for(AuthLoggerModel model:authLoggerModels){
            authLoggerRepository.delete(model);
        }
        List<DocumentModel> documentModels=documentRepository.findByClient(client);
        for(DocumentModel model:documentModels){
            documentRepository.delete(model);
        }
        List<AccountModel> accounts =this.accountRepository.findByClient(client);

        for (AccountModel account:accounts) {
            this.accountRepository.delete(account);
        }
        this.repository.delete(client);

        return "Deleted";
    }


}
