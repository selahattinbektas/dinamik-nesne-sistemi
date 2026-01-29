package tr.com.havelsan.dynamicobject.metadata.api.dto;

import tr.com.havelsan.dynamicobject.common.enums.EOptionsPropertyItemType;

public class OptionResponseDTO {
    private String value;
    private String label;
    private EOptionsPropertyItemType optionsPropertyItemType;

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

    public EOptionsPropertyItemType getOptionsPropertyItemType() {
        return optionsPropertyItemType;
    }

    public void setOptionsPropertyItemType(EOptionsPropertyItemType optionsPropertyItemType) {
        this.optionsPropertyItemType = optionsPropertyItemType;
    }
}
