package tr.com.havelsan.dynamicobject.uicontent.service;

import java.util.List;
import tr.com.havelsan.dynamicobject.uicontent.api.dto.UiContentDTO;
import tr.com.havelsan.dynamicobject.uicontent.domain.UiContent;

public interface UiContentService {
    UiContent createUiContent(UiContentDTO dto);

    UiContent getUiContentById(String name);

    UiContent updateUiContent(String name, UiContentDTO dto);

    void deleteUiContent(String name);

    List<UiContent> getAllUiContents();
}
