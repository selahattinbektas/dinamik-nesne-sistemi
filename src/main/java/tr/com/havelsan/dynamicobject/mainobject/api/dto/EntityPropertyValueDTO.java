package tr.com.havelsan.dynamicobject.mainobject.api.dto;

public class EntityPropertyValueDTO {
    private Integer propertyId;
    private Object value;

    public EntityPropertyValueDTO() {
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
