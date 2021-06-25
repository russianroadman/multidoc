package tanto.multidoc.repos;

import org.springframework.data.repository.CrudRepository;
import tanto.multidoc.model.Document;

public interface DocumentRepository extends CrudRepository<Document, String> {

}
