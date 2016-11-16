package app.document;


import app.client.ClientModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Admin on 13.11.2016.
 */
public interface DocumentRepository extends CrudRepository<DocumentModel, Long> {
    List<DocumentModel> findByClient(ClientModel client);
    DocumentModel findByIdInAndClientIn(long id,ClientModel client);
}
