package tr.com.havelsan.dynamicobject.mainobject.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import tr.com.havelsan.dynamicobject.mainobject.domain.EntityData;

public interface EntityDataRepository extends MongoRepository<EntityData, String> {
    List<EntityData> findAllByMetaDataName(String metaDataName);
}
