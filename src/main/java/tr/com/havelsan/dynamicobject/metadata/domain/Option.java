package tr.com.havelsan.dynamicobject.metadata.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tr.com.havelsan.dynamicobject.common.enums.EOptionsPropertyItemType;

@Document(collection = "options")
public class Option {
    @Id
    private String value;
    private String label;
    private EOptionsPropertyItemType optionsPropertyItemType;

    public Option() {
    }

    public Option(String value, String label, EOptionsPropertyItemType optionsPropertyItemType) {
        this.value = value;
        this.label = label;
        this.optionsPropertyItemType = optionsPropertyItemType;
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
