package tr.com.havelsan.dynamicobject.metadata.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import tr.com.havelsan.dynamicobject.metadata.domain.MetaDataClassification;

public interface MetaDataClassificationRepository extends MongoRepository<MetaDataClassification, String> {
    List<MetaDataClassification> findByClassification(String classification);
}
