package app.util;

import app.authLogger.AuthLoggerRepository;
import app.client.ClientModel;

import javax.servlet.http.HttpSession;

/**
 * Created by Admin on 12.11.2016.
 */
public class Auth {

    public static ClientModel authorizedClient(String accesstoken, AuthLoggerRepository authLoggerRepository){
        return authLoggerRepository.findByAccessToken(accesstoken).getClient();
    }
    public static boolean authCheck(String accesstoken,AuthLoggerRepository authLoggerRepository){
        System.out.println(accesstoken);
        System.out.println(authLoggerRepository.findByAccessToken(accesstoken));
        if(authLoggerRepository.findByAccessToken(accesstoken)==null)
            return false;
        return true;
    }
    public static boolean adminCheck(String accesstoken,AuthLoggerRepository authLoggerRepositor) {
        if(!authCheck(accesstoken,authLoggerRepositor)||!authorizedClient(accesstoken,authLoggerRepositor).getRole().equals("ADMIN"))
            return false;
        return true;

    }

}
