package tr.com.havelsan.dynamicobject.mainobject.service;

import tr.com.havelsan.dynamicobject.mainobject.api.dto.EntityPropertyValueDTO;
import tr.com.havelsan.dynamicobject.mainobject.domain.EntityData;
import java.util.Map;
import java.util.List;

public interface EntityPropertyService {
    EntityData addPropertyValueToEntity(String entityId, EntityPropertyValueDTO dto);

    EntityData addPropertyValuesToEntity(String entityId, List<EntityPropertyValueDTO> dtoList);

    void removePropertyFromEntity(String entityId, Integer propertyId);

    Map<String, Object> getPropertiesOfEntity(String entityId);
}
