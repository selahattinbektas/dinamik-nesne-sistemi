package tr.com.havelsan.dynamicobject.mainobject.service;

import java.util.List;
import tr.com.havelsan.dynamicobject.mainobject.api.dto.EntityDataDTO;
import tr.com.havelsan.dynamicobject.mainobject.domain.EntityData;

public interface EntityService {
    EntityData createEntity(String metaDataName, EntityDataDTO dto);

    EntityData getEntityById(String id);

    EntityData updateEntity(String id, EntityDataDTO dto);

    void deleteEntity(String id);

    List<EntityData> getAllEntities();

    List<EntityData> getAllEntitiesByMetaData(String metaDataName);
}
