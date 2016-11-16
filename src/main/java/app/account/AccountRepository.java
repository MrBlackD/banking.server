package app.account;


import app.client.ClientModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Admin on 10.11.2016.
 */
public interface AccountRepository extends CrudRepository<AccountModel, Long> {
    List<AccountModel> findByClient(ClientModel client);
    AccountModel findByAccountIdInAndClientIn(long accountId,ClientModel client);
}
