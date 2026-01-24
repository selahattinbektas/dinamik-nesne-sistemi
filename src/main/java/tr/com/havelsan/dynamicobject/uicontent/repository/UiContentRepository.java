package tr.com.havelsan.dynamicobject.uicontent.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tr.com.havelsan.dynamicobject.uicontent.domain.UiContent;

public interface UiContentRepository extends MongoRepository<UiContent, String> {
}
