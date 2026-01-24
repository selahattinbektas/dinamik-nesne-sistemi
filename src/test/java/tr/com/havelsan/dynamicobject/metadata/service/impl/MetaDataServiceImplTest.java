package tr.com.havelsan.dynamicobject.metadata.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import tr.com.havelsan.dynamicobject.common.sequence.SequenceGeneratorService;
import tr.com.havelsan.dynamicobject.metadata.api.dto.MetaDataClassificationDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.MetaDataDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.OptionDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.PropertyItemDTO;
import tr.com.havelsan.dynamicobject.metadata.domain.MetaData;
import tr.com.havelsan.dynamicobject.metadata.domain.MetaDataClassification;
import tr.com.havelsan.dynamicobject.metadata.domain.Option;
import tr.com.havelsan.dynamicobject.metadata.domain.PropertyItem;
import tr.com.havelsan.dynamicobject.metadata.repository.MetaDataClassificationRepository;
import tr.com.havelsan.dynamicobject.metadata.repository.MetaDataRepository;
import tr.com.havelsan.dynamicobject.metadata.repository.OptionRepository;

@ExtendWith(MockitoExtension.class)
class MetaDataServiceImplTest {
    @Mock
    private MetaDataRepository metaDataRepository;

    @Mock
    private MetaDataClassificationRepository classificationRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private SequenceGeneratorService sequenceGenerator;

    @InjectMocks
    private MetaDataServiceImpl service;

    private MetaData metaData;

    @BeforeEach
    void setUp() {
        metaData = new MetaData();
        metaData.setName("IHA");
        metaData.setPropertyItemList(new ArrayList<>());
    }

    @Test
    void createMetaDataThrowsWhenExists() {
        MetaDataDTO dto = new MetaDataDTO();
        dto.setName("IHA");
        when(metaDataRepository.existsById("IHA")).thenReturn(true);

        assertThatThrownBy(() -> service.createMetaData(dto))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode()).isEqualTo(HttpStatus.CONFLICT));
    }

    @Test
    void createMetaDataSavesWhenNew() {
        MetaDataDTO dto = new MetaDataDTO();
        dto.setName("IHA");
        when(metaDataRepository.existsById("IHA")).thenReturn(false);
        when(metaDataRepository.save(any(MetaData.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MetaData created = service.createMetaData(dto);

        assertThat(created.getName()).isEqualTo("IHA");
        verify(metaDataRepository).save(any(MetaData.class));
    }

    @Test
    void getMetaDataByNameThrowsWhenMissing() {
        when(metaDataRepository.findById("IHA")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getMetaDataByName("IHA"))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void updateMetaDataRenamesAndDeletesOldRecord() {
        MetaDataDTO dto = new MetaDataDTO();
        dto.setName("SIHA");
        when(metaDataRepository.findById("IHA")).thenReturn(Optional.of(metaData));
        when(metaDataRepository.existsById("SIHA")).thenReturn(false);
        when(metaDataRepository.save(metaData)).thenReturn(metaData);

        MetaData updated = service.updateMetaData("IHA", dto);

        assertThat(updated.getName()).isEqualTo("SIHA");
        verify(metaDataRepository).deleteById("IHA");
        verify(metaDataRepository).save(metaData);
    }

    @Test
    void updateMetaDataThrowsWhenTargetNameExists() {
        MetaDataDTO dto = new MetaDataDTO();
        dto.setName("SIHA");
        when(metaDataRepository.findById("IHA")).thenReturn(Optional.of(metaData));
        when(metaDataRepository.existsById("SIHA")).thenReturn(true);

        assertThatThrownBy(() -> service.updateMetaData("IHA", dto))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode()).isEqualTo(HttpStatus.CONFLICT));
        verify(metaDataRepository, never()).save(any(MetaData.class));
    }

    @Test
    void deleteMetaDataDeletesResolvedEntity() {
        when(metaDataRepository.findById("IHA")).thenReturn(Optional.of(metaData));

        service.deleteMetaData("IHA");

        verify(metaDataRepository).delete(metaData);
    }

    @Test
    void getAllMetaDatasDelegatesToRepository() {
        when(metaDataRepository.findAll()).thenReturn(List.of(metaData));

        List<MetaData> result = service.getAllMetaDatas();

        assertThat(result).containsExactly(metaData);
    }

    @Test
    void createClassificationAssignsSequenceId() {
        MetaDataClassificationDTO dto = new MetaDataClassificationDTO();
        dto.setClassification("UAV");
        when(sequenceGenerator.getNextSequence("metadata_classifications")).thenReturn(42);
        when(classificationRepository.save(any(MetaDataClassification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MetaDataClassification created = service.createClassification(dto);

        assertThat(created.getId()).isEqualTo("42");
        verify(classificationRepository).save(any(MetaDataClassification.class));
    }

    @Test
    void updateClassificationUpdatesAllFieldsExceptId() {
        MetaDataClassification existing = new MetaDataClassification();
        existing.setId("1");
        when(classificationRepository.findById("1")).thenReturn(Optional.of(existing));
        when(classificationRepository.save(existing)).thenReturn(existing);

        MetaDataClassificationDTO dto = new MetaDataClassificationDTO();
        dto.setDrawClassification("TECH");
        dto.setClassification("UAV");
        dto.setMetaDataName("IHA");
        dto.setMetaDataCode("IHA-CODE");
        dto.setSymbolCode("SYM");
        dto.setUnitType("metric");
        dto.setUndeletable(true);

        MetaDataClassification updated = service.updateClassification("1", dto);

        assertThat(updated.getId()).isEqualTo("1");
        assertThat(updated.getClassification()).isEqualTo("UAV");
        assertThat(updated.getMetaDataCode()).isEqualTo("IHA-CODE");
        assertThat(updated.isUndeletable()).isTrue();
    }

    @Test
    void getAllClassificationsDelegatesToRepository() {
        MetaDataClassification classification = new MetaDataClassification();
        when(classificationRepository.findAll()).thenReturn(List.of(classification));

        List<MetaDataClassification> result = service.getAllClassifications();

        assertThat(result).containsExactly(classification);
    }

    @Test
    void filterByClassificationDelegatesToRepository() {
        MetaDataClassification classification = new MetaDataClassification();
        when(classificationRepository.findByClassification("UAV")).thenReturn(List.of(classification));

        List<MetaDataClassification> result = service.filterByClassification("UAV");

        assertThat(result).containsExactly(classification);
    }

    @Test
    void deleteClassificationThrowsWhenUndeletable() {
        MetaDataClassification classification = new MetaDataClassification();
        classification.setId("1");
        classification.setUndeletable(true);
        when(classificationRepository.findById("1")).thenReturn(Optional.of(classification));

        assertThatThrownBy(() -> service.deleteClassification("1"))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST));
        verify(classificationRepository, never()).deleteById("1");
    }

    @Test
    void createPropertyItemAddsItemWithSequenceAndSavesMetaData() {
        PropertyItemDTO dto = new PropertyItemDTO();
        dto.setItemName("speed");
        when(metaDataRepository.findById("IHA")).thenReturn(Optional.of(metaData));
        when(sequenceGenerator.getNextSequence("property_items")).thenReturn(10);
        when(metaDataRepository.save(metaData)).thenReturn(metaData);

        PropertyItem created = service.createPropertyItem("IHA", dto);

        assertThat(created.getId()).isEqualTo(10);
        assertThat(metaData.getPropertyItemList()).extracting(PropertyItem::getId).contains(10);
        verify(metaDataRepository).save(metaData);
    }

    @Test
    void createOptionSavesWhenValueIsNew() {
        OptionDTO dto = new OptionDTO();
        dto.setValue("TEAM_ALPHA");
        dto.setLabel("Team Alpha");
        when(optionRepository.existsById("TEAM_ALPHA")).thenReturn(false);
        when(optionRepository.save(any(Option.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Option created = service.createOption(dto);

        assertThat(created.getValue()).isEqualTo("TEAM_ALPHA");
        verify(optionRepository).save(any(Option.class));
    }
}
