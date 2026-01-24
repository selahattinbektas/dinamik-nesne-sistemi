package tr.com.havelsan.dynamicobject.uicontent.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tr.com.havelsan.dynamicobject.uicontent.domain.UiContent;
import tr.com.havelsan.dynamicobject.uicontent.repository.UiContentRepository;
import tr.com.havelsan.dynamicobject.uicontent.service.UiContentPropertyService;
import tr.com.havelsan.dynamicobject.uicontent.service.UiContentService;

@Service
public class UiContentPropertyServiceImpl implements UiContentPropertyService {
    private final UiContentService uiContentService;
    private final UiContentRepository uiContentRepository;

    public UiContentPropertyServiceImpl(UiContentService uiContentService, UiContentRepository uiContentRepository) {
        this.uiContentService = uiContentService;
        this.uiContentRepository = uiContentRepository;
    }

    @Override
    public List<Integer> addPropertyToUiContent(String uiContentName, Integer propertyId) {
        UiContent content = uiContentService.getUiContentById(uiContentName);
        content.getItemIdList().add(propertyId);
        uiContentRepository.save(content);
        return new ArrayList<>(content.getItemIdList());
    }

    @Override
    public void removePropertyFromUiContent(String uiContentName, Integer propertyId) {
        UiContent content = uiContentService.getUiContentById(uiContentName);
        boolean removed = content.getItemIdList().remove(propertyId);
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UI content property not found");
        }
        uiContentRepository.save(content);
    }

    @Override
    public List<Integer> getPropertiesOfUiContent(String uiContentName) {
        UiContent content = uiContentService.getUiContentById(uiContentName);
        return new ArrayList<>(content.getItemIdList());
    }
}
