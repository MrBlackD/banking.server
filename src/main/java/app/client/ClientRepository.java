package app.client;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Admin on 09.11.2016.
 */
public interface ClientRepository extends CrudRepository<ClientModel, Long> {

    ClientModel findByLogin(String login);
}