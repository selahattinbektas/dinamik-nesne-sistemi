package tr.com.havelsan.dynamicobject.metadata.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tr.com.havelsan.dynamicobject.metadata.domain.MetaData;

import java.util.List;

public interface MetaDataRepository extends MongoRepository<MetaData, String> {
    List<MetaData> findByNameContainingIgnoreCase(String name);
}
