package tr.com.havelsan.dynamicobject.mainobject.service.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tr.com.havelsan.dynamicobject.mainobject.api.dto.EntityDataDTO;
import tr.com.havelsan.dynamicobject.mainobject.domain.EntityData;
import tr.com.havelsan.dynamicobject.mainobject.repository.EntityDataRepository;
import tr.com.havelsan.dynamicobject.mainobject.service.EntityService;
import tr.com.havelsan.dynamicobject.metadata.service.MetaDataService;

@Service
public class EntityServiceImpl implements EntityService {
    private final MetaDataService metaDataService;
    private final EntityDataRepository entityDataRepository;

    public EntityServiceImpl(MetaDataService metaDataService, EntityDataRepository entityDataRepository) {
        this.metaDataService = metaDataService;
        this.entityDataRepository = entityDataRepository;
    }

    @Override
    public EntityData createEntity(String metaDataName, EntityDataDTO dto) {
        metaDataService.getMetaDataByName(metaDataName);
        EntityData entity = new EntityData();
        entity.setId(dto.getId() == null ? UUID.randomUUID().toString() : dto.getId());
        if (entityDataRepository.existsById(entity.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Entity already exists");
        }
        entity.setMetaDataName(metaDataName);
        entity.setProperties(dto.getProperties());
        return entityDataRepository.save(entity);
    }

    @Override
    public EntityData getEntityById(String id) {
        return entityDataRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));
    }

    @Override
    public EntityData updateEntity(String id, EntityDataDTO dto) {
        EntityData existing = getEntityById(id);
        existing.setMetaDataName(dto.getMetaDataName() == null ? existing.getMetaDataName() : dto.getMetaDataName());
        existing.setProperties(dto.getProperties() == null ? existing.getProperties() : dto.getProperties());
        return entityDataRepository.save(existing);
    }

    @Override
    public void deleteEntity(String id) {
        EntityData entity = getEntityById(id);
        entityDataRepository.delete(entity);
    }

    @Override
    public List<EntityData> getAllEntities() {
        return entityDataRepository.findAll();
    }

    @Override
    public List<EntityData> getAllEntitiesByMetaData(String metaDataName) {
        return entityDataRepository.findAllByMetaDataName(metaDataName);
    }
}
