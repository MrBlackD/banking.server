package app.authLogger;


import app.client.ClientModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Admin on 12.11.2016.
 */
public interface AuthLoggerRepository extends CrudRepository<AuthLoggerModel, Long> {
    AuthLoggerModel findByAccessToken(String accessToken);
    List<AuthLoggerModel> findByClient(ClientModel client);
}
