package tr.com.havelsan.dynamicobject.metadata.service;

import java.util.List;

import tr.com.havelsan.dynamicobject.metadata.api.dto.MetaDataClassificationDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.MetaDataDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.OptionDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.PropertyItemDTO;
import tr.com.havelsan.dynamicobject.metadata.domain.MetaData;
import tr.com.havelsan.dynamicobject.metadata.domain.MetaDataClassification;
import tr.com.havelsan.dynamicobject.metadata.domain.Option;
import tr.com.havelsan.dynamicobject.metadata.domain.PropertyItem;

public interface MetaDataService {
    MetaData createMetaData(MetaDataDTO dto);

    MetaData getMetaDataByName(String name);

    MetaData updateMetaData(String name, MetaDataDTO dto);

    void deleteMetaData(String name);

    List<MetaData> getAllMetaDatas();

    List<MetaData> searchMetaDataByName(String name);

    //property item
    PropertyItem createPropertyItem(String metaDataName, PropertyItemDTO dto);

    PropertyItem getPropertyItemById(Integer id);

    PropertyItem updatePropertyItem(Integer id, PropertyItemDTO dto);

    void deletePropertyItem(Integer id);

    List<PropertyItem> getAllPropertyItemsByMetaData(String metaDataName);


    //options
    Option createOption(OptionDTO dto);

    Option getOptionById(String value);

    Option updateOption(String value, OptionDTO dto);

    void deleteOption(String value);

    List<Option> getAllOptions();

    //meta data property service
    PropertyItem addPropertyToMetaData(String metaDataName, PropertyItemDTO dto);

    void removePropertyFromMetaData(String metaDataName, Integer propertyId);

    List<PropertyItem> getPropertiesOfMetaData(String metaDataName);

    //classification
    MetaDataClassification createClassification(MetaDataClassificationDTO dto);

    MetaDataClassification getClassificationById(String id);

    MetaDataClassification updateClassification(String id, MetaDataClassificationDTO dto);

    List<MetaDataClassification> getAllClassifications();

    List<MetaDataClassification> filterByClassification(String classification);

    void deleteClassification(String id);
}
