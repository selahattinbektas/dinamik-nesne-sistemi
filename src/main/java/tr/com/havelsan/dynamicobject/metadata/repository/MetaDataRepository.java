package tr.com.havelsan.dynamicobject.metadata.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tr.com.havelsan.dynamicobject.metadata.domain.MetaData;

public interface MetaDataRepository extends MongoRepository<MetaData, String> {
}
