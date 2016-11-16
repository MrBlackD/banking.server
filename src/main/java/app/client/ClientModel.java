package app.client;

import app.util.Security;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Admin on 09.11.2016.
 */
@Entity
@Table(name="CLIENT")
public class ClientModel {

        @Id
        @GeneratedValue(strategy= GenerationType.AUTO)
        private long client_id;
        @Column(unique=true)
        private String login;
        private String password;
        private String firstName;
        private String lastName;
        private long regDate;
        private String role;

        protected ClientModel() {}

        public ClientModel(String login,String firstName, String lastName,String password,String role) {
            this.login=login;
            this.firstName = firstName;
            this.lastName = lastName;
            this.regDate=new Date().getTime();
            this.password= Security.MD5(password);
            this.role=role;

        }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getRegDate() {
        return regDate;
    }

    public void setRegDate(long regDate) {
        this.regDate = regDate;
    }

    @Override
        public String toString() {
            return String.format(
                    "Client[id=%d, firstName='%s', lastName='%s',password='%s']",
                    client_id, firstName, lastName,password);
        }

    public long getId() {
        return client_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
