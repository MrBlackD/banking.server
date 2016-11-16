package app.controllers;

import app.authLogger.AuthLoggerModel;
import app.authLogger.AuthLoggerRepository;
import app.client.ClientModel;
import app.client.ClientRepository;
import app.util.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by Admin on 12.11.2016.
 */
@CrossOrigin
@RestController
public class AuthController {

    private final ClientRepository clientRepository;
    private final AuthLoggerRepository authLoggerRepository;

    @Autowired
    public AuthController(ClientRepository repository,AuthLoggerRepository authLoggerRepository) {
        this.clientRepository = repository;
        this.authLoggerRepository=authLoggerRepository;
    }

    @RequestMapping("/login")
    public String login(@RequestParam(name="login") String login,@RequestParam(name="password")String password){
        ClientModel client=clientRepository.findByLogin(login);
        System.out.println(client+" "+Security.MD5(password));
        if(client!=null&&client.getPassword().equals(Security.MD5(password))) {
            String accessToken=Security.generateAccessToken(login);
            authLoggerRepository.save(new AuthLoggerModel(client,accessToken,new Date().getTime(),new Date().getTime()+1000*60));
            return "{\"response\":\""+accessToken+"\",\"role\":\""+client.getRole()+"\"}";
        }else {
            return "{\"response\":\"null\"}";
        }
    }

}
