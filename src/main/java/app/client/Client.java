package app.client;

/**
 * Created by Admin on 09.11.2016.
 */
public class Client {
    private final long id;
    private final String login;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final long regDate;
    private final String role;

    public Client(Long id,String login, String firstName, String lastName,long regDate,String password,String role) {
        this.id = id;
        this.login=login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.regDate=regDate;
        this.password=password;
        this.role=role;
    }

    public Client(ClientModel model){
        this.id = model.getId();
        this.login=model.getLogin();
        this.firstName = model.getFirstName();
        this.lastName = model.getLastName();
        this.regDate=model.getRegDate();
        this.password=model.getPassword();
        this.role=model.getRole();
    }

    public long getId() {
        return id;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getRegDate() {
        return regDate;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public String getRole() {

        return role;
    }
}
