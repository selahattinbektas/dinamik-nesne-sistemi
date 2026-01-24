package tr.com.havelsan.dynamicobject.metadata.api.dto;

public class OptionResponseDTO {
    private String value;
    private String label;

    public OptionResponseDTO() {
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
