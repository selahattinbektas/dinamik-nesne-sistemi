package tr.com.havelsan.dynamicobject.uicontent.service;

import java.util.List;
import tr.com.havelsan.dynamicobject.uicontent.api.dto.UiContentRequestDTO;
import tr.com.havelsan.dynamicobject.uicontent.domain.UiContent;

public interface UiContentService {
    UiContent createUiContent(UiContentRequestDTO dto);

    UiContent getUiContentById(String name);

    UiContent updateUiContent(String name, UiContentRequestDTO dto);

    void deleteUiContent(String name);

    List<UiContent> getAllUiContents();
}
