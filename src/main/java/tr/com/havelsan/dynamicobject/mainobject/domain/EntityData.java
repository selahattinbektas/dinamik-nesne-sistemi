package tr.com.havelsan.dynamicobject.mainobject.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tr.com.havelsan.dynamicobject.metadata.domain.PropertyItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "entities")
public class EntityData {
    @Id
    private String id;
    private String metaDataName;
    private Map<String, Object> properties = new HashMap<>();
    private List<PropertyItem> propertyItemList = new ArrayList<>();

    public EntityData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMetaDataName() {
        return metaDataName;
    }

    public void setMetaDataName(String metaDataName) {
        this.metaDataName = metaDataName;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public List<PropertyItem> getPropertyItemList() {
        return propertyItemList;
    }

    public void setPropertyItemList(List<PropertyItem> propertyItemList) {
        this.propertyItemList = propertyItemList;
    }
}
