package tr.com.havelsan.dynamicobject.uicontent.service.impl;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tr.com.havelsan.dynamicobject.uicontent.api.dto.UiContentRequestDTO;
import tr.com.havelsan.dynamicobject.uicontent.api.mapper.UiContentMapper;
import tr.com.havelsan.dynamicobject.uicontent.domain.UiContent;
import tr.com.havelsan.dynamicobject.uicontent.repository.UiContentRepository;
import tr.com.havelsan.dynamicobject.uicontent.service.UiContentService;

@Service
public class UiContentServiceImpl implements UiContentService {
    private final UiContentRepository uiContentRepository;
    private final UiContentMapper uiContentMapper;

    public UiContentServiceImpl(UiContentRepository uiContentRepository, UiContentMapper uiContentMapper) {
        this.uiContentRepository = uiContentRepository;
        this.uiContentMapper = uiContentMapper;
    }

    @Override
    public UiContent createUiContent(UiContentRequestDTO dto) {
        if (uiContentRepository.existsById(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "UI content already exists");
        }
        UiContent content = uiContentMapper.toEntity(dto);
        return uiContentRepository.save(content);
    }

    @Override
    public UiContent getUiContentById(String name) {
        return uiContentRepository.findById(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UI content not found"));
    }

    @Override
    public UiContent updateUiContent(String name, UiContentRequestDTO dto) {
        UiContent existing = getUiContentById(name);
        existing.setCssClassName(dto.getCssClassName() == null ? existing.getCssClassName() : dto.getCssClassName());
        existing.setType(dto.getType() == null ? existing.getType() : dto.getType());
        if (dto.getItemIdList() != null) {
            existing.setItemIdList(dto.getItemIdList());
        }
        return uiContentRepository.save(existing);
    }

    @Override
    public void deleteUiContent(String name) {
        UiContent content = getUiContentById(name);
        uiContentRepository.delete(content);
    }

    @Override
    public List<UiContent> getAllUiContents() {
        return uiContentRepository.findAll();
    }


}
