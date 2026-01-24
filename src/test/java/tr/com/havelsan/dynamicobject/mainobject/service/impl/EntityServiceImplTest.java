package tr.com.havelsan.dynamicobject.mainobject.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import tr.com.havelsan.dynamicobject.mainobject.api.dto.EntityDataDTO;
import tr.com.havelsan.dynamicobject.mainobject.domain.EntityData;
import tr.com.havelsan.dynamicobject.mainobject.repository.EntityDataRepository;
import tr.com.havelsan.dynamicobject.metadata.service.MetaDataService;

@ExtendWith(MockitoExtension.class)
class EntityServiceImplTest {
    @Mock
    private MetaDataService metaDataService;

    @Mock
    private EntityDataRepository entityDataRepository;

    @InjectMocks
    private EntityServiceImpl service;

    private EntityDataDTO dto;

    @BeforeEach
    void setUp() {
        dto = new EntityDataDTO();
        dto.setId("entity-1");
        dto.setMetaDataName("IHA");
        dto.setProperties(java.util.Map.of("speed", 120));
    }

    @Test
    void createEntityThrowsWhenIdExists() {
        when(entityDataRepository.existsById("entity-1")).thenReturn(true);

        assertThatThrownBy(() -> service.createEntity("IHA", dto))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode()).isEqualTo(HttpStatus.CONFLICT));
    }

    @Test
    void createEntitySavesWhenNew() {
        when(entityDataRepository.existsById("entity-1")).thenReturn(false);
        when(entityDataRepository.save(any(EntityData.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EntityData created = service.createEntity("IHA", dto);

        assertThat(created.getId()).isEqualTo("entity-1");
        assertThat(created.getMetaDataName()).isEqualTo("IHA");
        assertThat(created.getProperties()).containsEntry("speed", 120);
        verify(metaDataService).getMetaDataByName("IHA");
        verify(entityDataRepository).save(any(EntityData.class));
    }

    @Test
    void getEntityByIdThrowsWhenMissing() {
        when(entityDataRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getEntityById("missing"))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void updateEntityUpdatesProvidedFields() {
        EntityData existing = new EntityData();
        existing.setId("entity-1");
        existing.setMetaDataName("IHA");
        existing.setProperties(new java.util.HashMap<>(java.util.Map.of("speed", 100)));
        when(entityDataRepository.findById("entity-1")).thenReturn(Optional.of(existing));
        when(entityDataRepository.save(existing)).thenReturn(existing);

        EntityDataDTO updateDto = new EntityDataDTO();
        updateDto.setMetaDataName("SIHA");
        updateDto.setProperties(java.util.Map.of("speed", 150));

        EntityData updated = service.updateEntity("entity-1", updateDto);

        assertThat(updated.getMetaDataName()).isEqualTo("SIHA");
        assertThat(updated.getProperties()).containsEntry("speed", 150);
        verify(entityDataRepository).save(existing);
    }

    @Test
    void deleteEntityRemovesResolvedEntity() {
        EntityData existing = new EntityData();
        existing.setId("entity-1");
        when(entityDataRepository.findById("entity-1")).thenReturn(Optional.of(existing));

        service.deleteEntity("entity-1");

        verify(entityDataRepository).delete(existing);
    }

    @Test
    void getAllEntitiesDelegatesToRepository() {
        EntityData entity = new EntityData();
        when(entityDataRepository.findAll()).thenReturn(List.of(entity));

        List<EntityData> result = service.getAllEntities();

        assertThat(result).containsExactly(entity);
    }

    @Test
    void getAllEntitiesByMetaDataDelegatesToRepository() {
        EntityData entity = new EntityData();
        when(entityDataRepository.findAllByMetaDataName("IHA")).thenReturn(List.of(entity));

        List<EntityData> result = service.getAllEntitiesByMetaData("IHA");

        assertThat(result).containsExactly(entity);
    }
}
