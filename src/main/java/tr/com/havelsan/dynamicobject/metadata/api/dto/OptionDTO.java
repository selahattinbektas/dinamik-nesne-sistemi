package tr.com.havelsan.dynamicobject.metadata.api.dto;

public class OptionDTO {
    private String value;
    private String label;

    public OptionDTO() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
