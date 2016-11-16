package app.controllers;

import app.account.AccountModel;
import app.account.AccountRepository;
import app.authLogger.AuthLoggerRepository;
import app.client.ClientModel;
import app.client.ClientRepository;
import app.document.Document;
import app.document.DocumentModel;
import app.document.DocumentRepository;
import app.document.DocumentWrapper;
import app.exceptions.PermissionException;
import app.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Admin on 13.11.2016.
 */
@CrossOrigin
@RestController
public class DocumentController {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AuthLoggerRepository authLoggerRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentController(AccountRepository accountRepository,
                              ClientRepository clientRepository,
                              AuthLoggerRepository authLoggerRepository,
                              DocumentRepository documentRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.authLoggerRepository=authLoggerRepository;
        this.documentRepository=documentRepository;
    }
    @RequestMapping("/client/documents")
    public DocumentWrapper readAllForClient(@RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.authCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();

        ClientModel authorizedClient=Auth.authorizedClient(accesstoken,authLoggerRepository);

        List<Document> response= this.documentRepository.findByClient(authorizedClient).stream().map(Document::new).collect(Collectors.toList());
        return new DocumentWrapper(response);
    }

    @RequestMapping("/client/documents/{id}")
    public Document readOneForClient(@PathVariable String id, @RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.authCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        ClientModel authorizedClient=Auth.authorizedClient(accesstoken,authLoggerRepository);

        return new Document(this.documentRepository.findByIdInAndClientIn(Long.parseLong(id),authorizedClient));
    }

    @RequestMapping(path="/client/documents/new",method = RequestMethod.POST)
    public Document readOneForClient(@RequestParam(name="fromacc")String fromAcc,
                                     @RequestParam(name="toacc")String toAccId,
                                     @RequestParam(name="ammount")String ammount,
                                     @RequestParam(name = "accesstoken")String accesstoken) throws Exception {
        if(!Auth.authCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        ClientModel authorizedClient=Auth.authorizedClient(accesstoken,authLoggerRepository);
        AccountModel account=accountRepository.findByAccountIdInAndClientIn(Long.parseLong(fromAcc),authorizedClient);
        AccountModel toAccount=accountRepository.findOne(Long.parseLong(toAccId));
        if(account==null||toAccount==null)
            throw new PermissionException();
        if(Long.parseLong(ammount)<=0)
            throw new Exception("Ammount value should be positive");
        if(account.getBalance()-Long.parseLong(ammount)<0)
            throw new Exception("Insufficient funds");

        DocumentModel documentModel=new DocumentModel(authorizedClient,account,toAccount,Float.parseFloat(ammount));
        documentModel.execute(accountRepository);
        documentRepository.save(documentModel);
        return new Document(documentModel);
    }

    @RequestMapping("/client/{id}/documents")
    public DocumentWrapper readAllById(@PathVariable String id, @RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.adminCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();

        List<Document> response=new ArrayList<>();
        for (DocumentModel document : this.documentRepository.findByClient(clientRepository.findOne(Long.parseLong(id)))) {
            response.add(new Document(document));
        }
        return new DocumentWrapper(response);
    }

    @RequestMapping("/documents")
    public DocumentWrapper readAll(@RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.adminCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();

        List<Document> response=new ArrayList<>();
        for (DocumentModel document : this.documentRepository.findAll()) {
            response.add(new Document(document));
        }
        return new DocumentWrapper(response);
    }

    @RequestMapping("/documents/{id}")
    public Document readOne(@PathVariable String id, @RequestParam(name = "accesstoken")String accesstoken) throws PermissionException {
        if(!Auth.adminCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();

        return new Document(documentRepository.findOne(Long.parseLong(id)));
    }

    @RequestMapping(path="/documents/{id}/del",method = RequestMethod.POST)
    public String delete(@PathVariable String id, @RequestParam(name = "accesstoken")String accesstoken) throws Exception {
        if(!Auth.adminCheck(accesstoken,authLoggerRepository))
            throw new PermissionException();
        DocumentModel documentModel=documentRepository.findOne(Long.parseLong(id));
        if(documentModel.getTo().getBalance()-documentModel.getAmount()<0)
            throw new Exception("Insufficient funds");
        documentModel.revert(accountRepository);
        documentRepository.delete(documentModel);
        return "Operation reverted";
    }

}
