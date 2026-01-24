package tr.com.havelsan.dynamicobject.metadata.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tr.com.havelsan.dynamicobject.metadata.domain.Option;

public interface OptionRepository extends MongoRepository<Option, String> {
}
