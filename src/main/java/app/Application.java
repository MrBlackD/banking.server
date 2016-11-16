package app;

import app.account.Account;
import app.account.AccountModel;
import app.account.AccountRepository;
import app.client.ClientModel;
import app.client.ClientRepository;
import app.document.DocumentModel;
import app.document.DocumentRepository;
import org.springframework.boot.CommandLineRunner;


import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Admin on 09.11.2016.
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(ClientRepository clientrepo, AccountRepository accrepo,DocumentRepository docrepo) {
        return (args) -> {

            // save a couple of customers

            clientrepo.save(new ClientModel("JackB","Jack", "Bauer","123","USER"));
            clientrepo.save(new ClientModel("Ch123","Chloe", "O'Brian","1234","USER"));
            clientrepo.save(new ClientModel("KimChenIr","Kim", "Bauer","12345","USER"));
            clientrepo.save(new ClientModel("GoliafSlayer","David", "Palmer","123456","USER"));
            clientrepo.save(new ClientModel("Miky","Michelle", "Dessler","password","USER"));
            clientrepo.save(new ClientModel("admin","admin", "admin","123","ADMIN"));

            accrepo.save(new AccountModel(clientrepo.findOne(1L),100));
            accrepo.save(new AccountModel(clientrepo.findOne(1L),150));
            accrepo.save(new AccountModel(clientrepo.findOne(2L),1000));

            docrepo.save(new DocumentModel(clientrepo.findOne(1L),accrepo.findOne(1L),accrepo.findOne(2L),50));
            docrepo.save(new DocumentModel(clientrepo.findOne(2L),accrepo.findOne(3L),accrepo.findOne(1L),50));
        };
    }


}