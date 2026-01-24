package tr.com.havelsan.dynamicobject.metadata.domain;

import tr.com.havelsan.dynamicobject.common.enums.EComponentType;
import tr.com.havelsan.dynamicobject.common.enums.EPropertyItemType;

import java.util.ArrayList;
import java.util.List;

public class PropertyItem {
    private EComponentType type;
    private Integer id;
    private String itemName;
    private String title;
    private String unit;
    private List<Option> options = new ArrayList<>();
    private Integer min;
    private Integer max;
    private String defaultValue;
    private boolean groupProperty;
    private boolean readOnly;
    private String toValidatorClass;
    private String fromValidatorClass;
    private boolean showContextMenu;
    private EPropertyItemType propertyItemType;
    private boolean selectedVisible;

    public PropertyItem() {
    }

    public EComponentType getType() {
        return type;
    }

    public void setType(EComponentType type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isGroupProperty() {
        return groupProperty;
    }

    public void setGroupProperty(boolean groupProperty) {
        this.groupProperty = groupProperty;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getToValidatorClass() {
        return toValidatorClass;
    }

    public void setToValidatorClass(String toValidatorClass) {
        this.toValidatorClass = toValidatorClass;
    }

    public String getFromValidatorClass() {
        return fromValidatorClass;
    }

    public void setFromValidatorClass(String fromValidatorClass) {
        this.fromValidatorClass = fromValidatorClass;
    }

    public boolean isShowContextMenu() {
        return showContextMenu;
    }

    public void setShowContextMenu(boolean showContextMenu) {
        this.showContextMenu = showContextMenu;
    }

    public EPropertyItemType getPropertyItemType() {
        return propertyItemType;
    }

    public void setPropertyItemType(EPropertyItemType propertyItemType) {
        this.propertyItemType = propertyItemType;
    }

    public boolean isSelectedVisible() {
        return selectedVisible;
    }

    public void setSelectedVisible(boolean selectedVisible) {
        this.selectedVisible = selectedVisible;
    }
}
