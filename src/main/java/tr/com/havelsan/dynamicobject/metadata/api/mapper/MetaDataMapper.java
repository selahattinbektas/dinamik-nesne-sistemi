package tr.com.havelsan.dynamicobject.metadata.api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import tr.com.havelsan.dynamicobject.metadata.api.dto.MetaDataClassificationResponseDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.MetaDataResponseDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.OptionResponseDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.PropertyItemResponseDTO;
import tr.com.havelsan.dynamicobject.metadata.domain.MetaData;
import tr.com.havelsan.dynamicobject.metadata.domain.MetaDataClassification;
import tr.com.havelsan.dynamicobject.metadata.domain.Option;
import tr.com.havelsan.dynamicobject.metadata.domain.PropertyItem;

@Mapper(componentModel = "spring")
public interface MetaDataMapper {
    MetaDataResponseDTO toMetaDataResponse(MetaData metaData);

    List<MetaDataResponseDTO> toMetaDataResponseList(List<MetaData> metaDataList);

    PropertyItemResponseDTO toPropertyItemResponse(PropertyItem item);

    List<PropertyItemResponseDTO> toPropertyItemResponseList(List<PropertyItem> items);

    OptionResponseDTO toOptionResponse(Option option);

    List<OptionResponseDTO> toOptionResponseList(List<Option> options);

    MetaDataClassificationResponseDTO toClassificationResponse(MetaDataClassification classification);

    List<MetaDataClassificationResponseDTO> toClassificationResponseList(List<MetaDataClassification> classifications);
}
