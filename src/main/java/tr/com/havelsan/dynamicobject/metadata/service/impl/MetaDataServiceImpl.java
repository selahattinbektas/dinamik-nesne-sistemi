package tr.com.havelsan.dynamicobject.metadata.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tr.com.havelsan.dynamicobject.metadata.api.dto.MetaDataClassificationDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.MetaDataDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.OptionDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.PropertyItemDTO;
import tr.com.havelsan.dynamicobject.common.sequence.SequenceGeneratorService;
import tr.com.havelsan.dynamicobject.metadata.domain.MetaData;
import tr.com.havelsan.dynamicobject.metadata.domain.MetaDataClassification;
import tr.com.havelsan.dynamicobject.metadata.domain.Option;
import tr.com.havelsan.dynamicobject.metadata.domain.PropertyItem;
import tr.com.havelsan.dynamicobject.metadata.repository.MetaDataClassificationRepository;
import tr.com.havelsan.dynamicobject.metadata.repository.MetaDataRepository;
import tr.com.havelsan.dynamicobject.metadata.repository.OptionRepository;
import tr.com.havelsan.dynamicobject.metadata.service.MetaDataService;

@Service
public class MetaDataServiceImpl implements MetaDataService {
    private static final String PROPERTY_ITEM_SEQUENCE = "property_items";
    private static final String CLASSIFICATION_SEQUENCE = "metadata_classifications";
    private final MetaDataRepository metaDataRepository;
    private final MetaDataClassificationRepository classificationRepository;
    private final OptionRepository optionRepository;
    private final SequenceGeneratorService sequenceGenerator;

    public MetaDataServiceImpl(
            MetaDataRepository metaDataRepository,
            MetaDataClassificationRepository classificationRepository,
            OptionRepository optionRepository,
            SequenceGeneratorService sequenceGenerator
    ) {
        this.metaDataRepository = metaDataRepository;
        this.classificationRepository = classificationRepository;
        this.optionRepository = optionRepository;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public MetaData createMetaData(MetaDataDTO dto) {
        if (metaDataRepository.existsById(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "MetaData already exists");
        }
        MetaData metaData = mapMetaData(dto);
        return metaDataRepository.save(metaData);
    }

    @Override
    public MetaData getMetaDataByName(String name) {
        return metaDataRepository.findById(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MetaData not found"));
    }

    @Override
    public MetaData updateMetaData(String name, MetaDataDTO dto) {
        MetaData existing = getMetaDataByName(name);
        String newName = dto.getName() == null ? existing.getName() : dto.getName();
        if (newName != null && !name.equals(newName) && metaDataRepository.existsById(newName)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "MetaData already exists");
        }
        existing.setName(newName);
        existing.setEntityType(dto.getEntityType() == null ? existing.getEntityType() : dto.getEntityType());
        if (!name.equals(existing.getName())) {
            metaDataRepository.deleteById(name);
        }
        return metaDataRepository.save(existing);
    }

    @Override
    public void deleteMetaData(String name) {
        MetaData metaData = getMetaDataByName(name);
        metaDataRepository.delete(metaData);
    }

    @Override
    public List<MetaData> getAllMetaDatas() {
        return metaDataRepository.findAll();
    }

    @Override
    public List<MetaData> searchMetaDataByName(String name) {
        if (name == null || name.isBlank()) {
            return List.of();
        }
        return metaDataRepository.findByNameContainingIgnoreCase(name);
    }

    private MetaData mapMetaData(MetaDataDTO dto) {
        MetaData metaData = new MetaData();
        metaData.setName(dto.getName());
        metaData.setEntityType(dto.getEntityType());
        metaData.setPropertyItemList(mapPropertyItems(dto.getPropertyItemList()));
        return metaData;
    }

    private List<PropertyItem> mapPropertyItems(List<PropertyItemDTO> items) {
        List<PropertyItem> mappedItems = new ArrayList<>();
        if (items == null) {
            return mappedItems;
        }
        for (PropertyItemDTO dto : items) {
            PropertyItem item = new PropertyItem();
            item.setId(sequenceGenerator.getNextSequence(PROPERTY_ITEM_SEQUENCE));
            item.setType(dto.getType());
            item.setItemName(dto.getItemName());
            item.setTitle(dto.getTitle());
            item.setUnit(dto.getUnit());
            item.setMin(dto.getMin());
            item.setMax(dto.getMax());
            item.setDefaultValue(dto.getDefaultValue());
            item.setGroupProperty(dto.isGroupProperty());
            item.setReadOnly(dto.isReadOnly());
            item.setToValidatorClass(dto.getToValidatorClass());
            item.setFromValidatorClass(dto.getFromValidatorClass());
            item.setShowContextMenu(dto.isShowContextMenu());
            item.setPropertyItemType(dto.getPropertyItemType());
            item.setSelectedVisible(dto.isSelectedVisible());
            List<Option> options = new ArrayList<>();
            if (dto.getOptions() != null) {
                dto.getOptions().forEach(optionDTO -> options.add(new Option(optionDTO.getValue(), optionDTO.getLabel())));
            }
            item.setOptions(options);
            mappedItems.add(item);
        }
        return mappedItems;
    }

    //classification
    @Override
    public MetaDataClassification createClassification(MetaDataClassificationDTO dto) {
        MetaDataClassification classification = mapClassification(dto);
        classification.setId(String.valueOf(sequenceGenerator.getNextSequence(CLASSIFICATION_SEQUENCE)));
        return classificationRepository.save(classification);
    }

    @Override
    public MetaDataClassification getClassificationById(String id) {
        return classificationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classification not found"));
    }

    @Override
    public MetaDataClassification updateClassification(String id, MetaDataClassificationDTO dto) {
        MetaDataClassification existing = getClassificationById(id);
        existing.setDrawClassification(dto.getDrawClassification());
        existing.setClassification(dto.getClassification());
        existing.setMetaDataName(dto.getMetaDataName());
        existing.setMetaDataCode(dto.getMetaDataCode());
        existing.setSymbolCode(dto.getSymbolCode());
        existing.setUnitType(dto.getUnitType());
        existing.setUndeletable(dto.isUndeletable());
        return classificationRepository.save(existing);
    }

    @Override
    public List<MetaDataClassification> getAllClassifications() {
        return classificationRepository.findAll();
    }

    @Override
    public List<MetaDataClassification> filterByClassification(String classification) {
        return classificationRepository.findByClassification(classification);
    }

    @Override
    public void deleteClassification(String id) {
        MetaDataClassification existing = getClassificationById(id);
        if (existing.isUndeletable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Classification is undeletable");
        }
        classificationRepository.deleteById(id);
    }

    private MetaDataClassification mapClassification(MetaDataClassificationDTO dto) {
        MetaDataClassification classification = new MetaDataClassification();
        classification.setId(dto.getId());
        classification.setDrawClassification(dto.getDrawClassification());
        classification.setClassification(dto.getClassification());
        classification.setMetaDataName(dto.getMetaDataName());
        classification.setMetaDataCode(dto.getMetaDataCode());
        classification.setSymbolCode(dto.getSymbolCode());
        classification.setUnitType(dto.getUnitType());
        classification.setUndeletable(dto.isUndeletable());
        return classification;
    }

    //property item service
    @Override
    public PropertyItem createPropertyItem(String metaDataName, PropertyItemDTO dto) {
        MetaData metaData = getMetaDataByName(metaDataName);
        PropertyItem item = mapPropertyItem(dto);
        item.setId(sequenceGenerator.getNextSequence(PROPERTY_ITEM_SEQUENCE));
        metaData.getPropertyItemList().add(item);
        metaDataRepository.save(metaData);
        return item;
    }

    @Override
    public PropertyItem getPropertyItemById(Integer id) {
        return findPropertyItem(id);
    }

    @Override
    public PropertyItem updatePropertyItem(Integer id, PropertyItemDTO dto) {
        PropertyItem item = findPropertyItem(id);
        item.setType(dto.getType());
        item.setItemName(dto.getItemName());
        item.setTitle(dto.getTitle());
        item.setUnit(dto.getUnit());
        item.setMin(dto.getMin());
        item.setMax(dto.getMax());
        item.setDefaultValue(dto.getDefaultValue());
        item.setGroupProperty(dto.isGroupProperty());
        item.setReadOnly(dto.isReadOnly());
        item.setToValidatorClass(dto.getToValidatorClass());
        item.setFromValidatorClass(dto.getFromValidatorClass());
        item.setShowContextMenu(dto.isShowContextMenu());
        item.setPropertyItemType(dto.getPropertyItemType());
        item.setSelectedVisible(dto.isSelectedVisible());
        List<Option> options = new ArrayList<>();
        if (dto.getOptions() != null) {
            dto.getOptions().forEach(optionDTO -> options.add(new Option(optionDTO.getValue(), optionDTO.getLabel())));
        }
        item.setOptions(options);
        saveMetaDataContainingProperty(item);
        return item;
    }

    @Override
    public void deletePropertyItem(Integer id) {
        boolean removed = false;
        for (MetaData metaData : getAllMetaDatas()) {
            boolean changed = metaData.getPropertyItemList().removeIf(item -> id.equals(item.getId()));
            if (changed) {
                removed = true;
                metaDataRepository.save(metaData);
            }
        }
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Property item not found");
        }
    }

    @Override
    public List<PropertyItem> getAllPropertyItemsByMetaData(String metaDataName) {
        MetaData metaData = getMetaDataByName(metaDataName);
        return new ArrayList<>(metaData.getPropertyItemList());
    }

    private PropertyItem findPropertyItem(Integer id) {
        for (MetaData metaData : getAllMetaDatas()) {
            for (PropertyItem item : metaData.getPropertyItemList()) {
                if (id.equals(item.getId())) {
                    return item;
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Property item not found");
    }

    private PropertyItem mapPropertyItem(PropertyItemDTO dto) {
        PropertyItem item = new PropertyItem();
        item.setType(dto.getType());
        item.setItemName(dto.getItemName());
        item.setTitle(dto.getTitle());
        item.setUnit(dto.getUnit());
        item.setMin(dto.getMin());
        item.setMax(dto.getMax());
        item.setDefaultValue(dto.getDefaultValue());
        item.setGroupProperty(dto.isGroupProperty());
        item.setReadOnly(dto.isReadOnly());
        item.setToValidatorClass(dto.getToValidatorClass());
        item.setFromValidatorClass(dto.getFromValidatorClass());
        item.setShowContextMenu(dto.isShowContextMenu());
        item.setPropertyItemType(dto.getPropertyItemType());
        item.setSelectedVisible(dto.isSelectedVisible());
        List<Option> options = new ArrayList<>();
        if (dto.getOptions() != null) {
            dto.getOptions().forEach(optionDTO -> options.add(new Option(optionDTO.getValue(), optionDTO.getLabel())));
        }
        item.setOptions(options);
        return item;
    }

    // meta data property service
    @Override
    public PropertyItem addPropertyToMetaData(String metaDataName, PropertyItemDTO dto) {
        return createPropertyItem(metaDataName, dto);
    }

    @Override
    public void removePropertyFromMetaData(String metaDataName, Integer propertyId) {
        deletePropertyItem(propertyId);
    }

    @Override
    public List<PropertyItem> getPropertiesOfMetaData(String metaDataName) {
        return getAllPropertyItemsByMetaData(metaDataName);
    }

    //options
    @Override
    public Option createOption(OptionDTO dto) {
        if (optionRepository.existsById(dto.getValue())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Option already exists");
        }
        Option option = new Option(dto.getValue(), dto.getLabel());
        return optionRepository.save(option);
    }

    @Override
    public Option getOptionById(String value) {
        return optionRepository.findById(value)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Option not found"));
    }

    @Override
    public Option updateOption(String value, OptionDTO dto) {
        Option option = getOptionById(value);
        option.setValue(dto.getValue());
        option.setLabel(dto.getLabel());
        if (!value.equals(option.getValue())) {
            optionRepository.deleteById(value);
        }
        return optionRepository.save(option);
    }

    @Override
    public void deleteOption(String value) {
        Option option = getOptionById(value);
        optionRepository.delete(option);
    }

    @Override
    public List<Option> getAllOptions() {
        return optionRepository.findAll();
    }

    private void saveMetaDataContainingProperty(PropertyItem updatedItem) {
        for (MetaData metaData : getAllMetaDatas()) {
            List<PropertyItem> items = metaData.getPropertyItemList();
            for (int i = 0; i < items.size(); i++) {
                if (updatedItem.getId().equals(items.get(i).getId())) {
                    items.set(i, updatedItem);
                    metaDataRepository.save(metaData);
                    return;
                }
            }
        }
    }
}
