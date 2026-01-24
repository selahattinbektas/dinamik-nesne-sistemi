package tr.com.havelsan.dynamicobject.uicontent.api.dto;

import java.util.List;
import tr.com.havelsan.dynamicobject.common.enums.EUiContentType;

public class UiContentResponseDTO {
    private String name;
    private String cssClassName;
    private EUiContentType type;
    private List<Integer> itemIdList;

    public UiContentResponseDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCssClassName() {
        return cssClassName;
    }

    public void setCssClassName(String cssClassName) {
        this.cssClassName = cssClassName;
    }

    public EUiContentType getType() {
        return type;
    }

    public void setType(EUiContentType type) {
        this.type = type;
    }

    public List<Integer> getItemIdList() {
        return itemIdList;
    }

    public void setItemIdList(List<Integer> itemIdList) {
        this.itemIdList = itemIdList;
    }
}
