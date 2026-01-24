package tr.com.havelsan.dynamicobject.metadata.api.dto;

import java.util.List;
import tr.com.havelsan.dynamicobject.common.enums.EEntityType;

public class MetaDataResponseDTO {
    private String name;
    private EEntityType entityType;
    private List<PropertyItemResponseDTO> propertyItemList;

    public MetaDataResponseDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EEntityType entityType) {
        this.entityType = entityType;
    }

    public List<PropertyItemResponseDTO> getPropertyItemList() {
        return propertyItemList;
    }

    public void setPropertyItemList(List<PropertyItemResponseDTO> propertyItemList) {
        this.propertyItemList = propertyItemList;
    }
}
