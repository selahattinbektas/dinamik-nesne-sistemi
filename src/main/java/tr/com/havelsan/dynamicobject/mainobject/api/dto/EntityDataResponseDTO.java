package tr.com.havelsan.dynamicobject.mainobject.api.dto;

import java.util.Map;

public class EntityDataResponseDTO {
    private String id;
    private String metaDataName;
    private Map<String, Object> properties;

    public EntityDataResponseDTO() {
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
}
