package tr.com.havelsan.dynamicobject.metadata.api.dto;

import java.util.ArrayList;
import java.util.List;
import tr.com.havelsan.dynamicobject.common.enums.EEntityType;

public class MetaDataDTO {
    private String name;
    private EEntityType entityType;
    private List<PropertyItemDTO> propertyItemList = new ArrayList<>();

    public MetaDataDTO() {
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

    public List<PropertyItemDTO> getPropertyItemList() {
        return propertyItemList;
    }

    public void setPropertyItemList(List<PropertyItemDTO> propertyItemList) {
        this.propertyItemList = propertyItemList;
    }
}
