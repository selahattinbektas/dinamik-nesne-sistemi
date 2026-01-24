package tr.com.havelsan.dynamicobject.metadata.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tr.com.havelsan.dynamicobject.common.enums.EEntityType;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "metadata")
public class MetaData {
    @Id
    private String name;
    private EEntityType entityType;
    private List<PropertyItem> propertyItemList = new ArrayList<>();

    public MetaData() {
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

    public List<PropertyItem> getPropertyItemList() {
        return propertyItemList;
    }

    public void setPropertyItemList(List<PropertyItem> propertyItemList) {
        this.propertyItemList = propertyItemList;
    }
}
