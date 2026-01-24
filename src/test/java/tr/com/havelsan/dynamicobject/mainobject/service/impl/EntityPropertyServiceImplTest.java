package tr.com.havelsan.dynamicobject.mainobject.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import tr.com.havelsan.dynamicobject.mainobject.api.dto.EntityPropertyValueDTO;
import tr.com.havelsan.dynamicobject.mainobject.domain.EntityData;
import tr.com.havelsan.dynamicobject.mainobject.repository.EntityDataRepository;
import tr.com.havelsan.dynamicobject.mainobject.service.EntityService;
import tr.com.havelsan.dynamicobject.metadata.domain.PropertyItem;
import tr.com.havelsan.dynamicobject.metadata.service.MetaDataService;

@ExtendWith(MockitoExtension.class)
class EntityPropertyServiceImplTest {
    @Mock
    private EntityService entityService;

    @Mock
    private EntityDataRepository entityDataRepository;

    @Mock
    private MetaDataService metaDataService;

    @InjectMocks
    private EntityPropertyServiceImpl service;

    private EntityData entity;
    private PropertyItem propertyItem;

    @BeforeEach
    void setUp() {
        entity = new EntityData();
        entity.setId("entity-1");
        entity.setMetaDataName("IHA");
        entity.setProperties(new java.util.HashMap<>());

        propertyItem = new PropertyItem();
        propertyItem.setId(10);
        propertyItem.setItemName("speed");
    }

    @Test
    void addPropertyValueToEntityAddsResolvedProperty() {
        EntityPropertyValueDTO dto = new EntityPropertyValueDTO();
        dto.setPropertyId(10);
        dto.setValue(120);
        when(entityService.getEntityById("entity-1")).thenReturn(entity);
        when(metaDataService.getPropertiesOfMetaData("IHA")).thenReturn(List.of(propertyItem));
        when(entityDataRepository.save(entity)).thenReturn(entity);

        EntityData result = service.addPropertyValueToEntity("entity-1", dto);

        assertThat(result.getProperties()).containsEntry("speed", 120);
        verify(entityDataRepository).save(entity);
    }

    @Test
    void addPropertyValuesToEntityAddsAllPropertiesInBatch() {
        EntityPropertyValueDTO dto1 = new EntityPropertyValueDTO();
        dto1.setPropertyId(10);
        dto1.setValue(120);
        PropertyItem altitudeItem = new PropertyItem();
        altitudeItem.setId(11);
        altitudeItem.setItemName("altitude");
        EntityPropertyValueDTO dto2 = new EntityPropertyValueDTO();
        dto2.setPropertyId(11);
        dto2.setValue(1000);
        when(entityService.getEntityById("entity-1")).thenReturn(entity);
        when(metaDataService.getPropertiesOfMetaData("IHA")).thenReturn(List.of(propertyItem, altitudeItem));
        when(entityDataRepository.save(entity)).thenReturn(entity);

        EntityData result = service.addPropertyValuesToEntity("entity-1", List.of(dto1, dto2));

        assertThat(result.getProperties()).containsEntry("speed", 120).containsEntry("altitude", 1000);
        verify(entityDataRepository).save(entity);
    }

    @Test
    void addPropertyValuesToEntityThrowsWhenListEmpty() {
        assertThatThrownBy(() -> service.addPropertyValuesToEntity("entity-1", List.of()))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST));
        verify(entityService, never()).getEntityById(any());
    }

    @Test
    void addPropertyValuesToEntityThrowsWhenPropertyMissing() {
        EntityPropertyValueDTO dto = new EntityPropertyValueDTO();
        dto.setPropertyId(999);
        when(entityService.getEntityById("entity-1")).thenReturn(entity);
        when(metaDataService.getPropertiesOfMetaData("IHA")).thenReturn(List.of(propertyItem));

        assertThatThrownBy(() -> service.addPropertyValuesToEntity("entity-1", List.of(dto)))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void removePropertyFromEntityRemovesResolvedKey() {
        entity.setProperties(new java.util.HashMap<>(Map.of("speed", 120)));
        when(entityService.getEntityById("entity-1")).thenReturn(entity);
        when(metaDataService.getPropertiesOfMetaData("IHA")).thenReturn(List.of(propertyItem));

        service.removePropertyFromEntity("entity-1", 10);

        assertThat(entity.getProperties()).doesNotContainKey("speed");
        verify(entityDataRepository).save(entity);
    }

    @Test
    void removePropertyFromEntityThrowsWhenKeyMissing() {
        entity.setProperties(new java.util.HashMap<>());
        when(entityService.getEntityById("entity-1")).thenReturn(entity);
        when(metaDataService.getPropertiesOfMetaData("IHA")).thenReturn(List.of(propertyItem));

        assertThatThrownBy(() -> service.removePropertyFromEntity("entity-1", 10))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
        verify(entityDataRepository, never()).save(entity);
    }

    @Test
    void getPropertiesOfEntityReturnsPropertiesMap() {
        entity.setProperties(new java.util.HashMap<>(Map.of("speed", 120)));
        when(entityService.getEntityById("entity-1")).thenReturn(entity);

        Map<String, Object> result = service.getPropertiesOfEntity("entity-1");

        assertThat(result).containsEntry("speed", 120);
    }
}
