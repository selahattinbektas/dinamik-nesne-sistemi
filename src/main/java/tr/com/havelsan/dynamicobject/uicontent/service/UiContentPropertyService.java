package tr.com.havelsan.dynamicobject.uicontent.service;

import java.util.List;

public interface UiContentPropertyService {
    List<Integer> addPropertyToUiContent(String uiContentName, Integer propertyId);

    void removePropertyFromUiContent(String uiContentName, Integer propertyId);

    List<Integer> getPropertiesOfUiContent(String uiContentName);
}
