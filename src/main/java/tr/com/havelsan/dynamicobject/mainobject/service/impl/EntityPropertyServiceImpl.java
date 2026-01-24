package tr.com.havelsan.dynamicobject.mainobject.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tr.com.havelsan.dynamicobject.mainobject.api.dto.EntityPropertyValueDTO;
import tr.com.havelsan.dynamicobject.mainobject.domain.EntityData;
import tr.com.havelsan.dynamicobject.mainobject.repository.EntityDataRepository;
import tr.com.havelsan.dynamicobject.metadata.domain.PropertyItem;
import tr.com.havelsan.dynamicobject.mainobject.service.EntityPropertyService;
import tr.com.havelsan.dynamicobject.mainobject.service.EntityService;
import tr.com.havelsan.dynamicobject.metadata.service.MetaDataService;

@Service
public class EntityPropertyServiceImpl implements EntityPropertyService {
    private final EntityService entityService;
    private final EntityDataRepository entityDataRepository;
    private final MetaDataService metaDataService;

    public EntityPropertyServiceImpl(
            EntityService entityService,
            EntityDataRepository entityDataRepository,
            MetaDataService metaDataService
    ) {
        this.entityService = entityService;
        this.entityDataRepository = entityDataRepository;
        this.metaDataService = metaDataService;
    }

    @Override
    public EntityData addPropertyValueToEntity(String entityId, EntityPropertyValueDTO dto) {
        EntityData entity = entityService.getEntityById(entityId);
        String metaDataName = entity.getMetaDataName();
        if (metaDataName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity metadata name is missing");
        }
        PropertyItem propertyItem = metaDataService.getPropertiesOfMetaData(metaDataName).stream()
                .filter(item -> dto.getPropertyId() != null && dto.getPropertyId().equals(item.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property item not found"));
        String key = propertyItem.getItemName() == null ? String.valueOf(propertyItem.getId()) : propertyItem.getItemName();
        entity.getProperties().put(key, dto.getValue());
        entityDataRepository.save(entity);
        return entity;
    }

    @Override
    public EntityData addPropertyValuesToEntity(String entityId, List<EntityPropertyValueDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Property list is empty");
        }
        EntityData entity = entityService.getEntityById(entityId);
        String metaDataName = entity.getMetaDataName();
        if (metaDataName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity metadata name is missing");
        }
        Map<Integer, PropertyItem> propertyItemMap = new HashMap<>();
        for (PropertyItem item : metaDataService.getPropertiesOfMetaData(metaDataName)) {
            if (item.getId() != null) {
                propertyItemMap.put(item.getId(), item);
            }
        }
        for (EntityPropertyValueDTO dto : dtoList) {
            PropertyItem propertyItem = propertyItemMap.get(dto.getPropertyId());
            if (propertyItem == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Property item not found");
            }
            String key = propertyItem.getItemName() == null ? String.valueOf(propertyItem.getId()) : propertyItem.getItemName();
            entity.getProperties().put(key, dto.getValue());
        }
        entityDataRepository.save(entity);
        return entity;
    }

    @Override
    public void removePropertyFromEntity(String entityId, Integer propertyId) {
        EntityData entity = entityService.getEntityById(entityId);
        String metaDataName = entity.getMetaDataName();
        if (metaDataName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity metadata name is missing");
        }
        PropertyItem propertyItem = metaDataService.getPropertiesOfMetaData(metaDataName).stream()
                .filter(item -> propertyId != null && propertyId.equals(item.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property item not found"));
        String key = propertyItem.getItemName() == null ? String.valueOf(propertyItem.getId()) : propertyItem.getItemName();
        if (!entity.getProperties().containsKey(key)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity property not found");
        }
        entity.getProperties().remove(key);
        entityDataRepository.save(entity);
    }

    @Override
    public Map<String, Object> getPropertiesOfEntity(String entityId) {
        EntityData entity = entityService.getEntityById(entityId);
        return entity.getProperties();
    }

}
