package tr.com.havelsan.dynamicobject.metadata.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tr.com.havelsan.dynamicobject.common.enums.EOptionsPropertyItemType;
import tr.com.havelsan.dynamicobject.metadata.domain.Option;

public interface OptionRepository extends MongoRepository<Option, String> {
    boolean existsByValueAndLabelAndOptionsPropertyItemType(String value, String label,
                                                            EOptionsPropertyItemType optionsPropertyItemType);
}
