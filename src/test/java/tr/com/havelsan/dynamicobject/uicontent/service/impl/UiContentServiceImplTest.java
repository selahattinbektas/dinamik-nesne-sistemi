package tr.com.havelsan.dynamicobject.uicontent.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import tr.com.havelsan.dynamicobject.common.enums.EUiContentType;
import tr.com.havelsan.dynamicobject.uicontent.api.dto.UiContentRequestDTO;
import tr.com.havelsan.dynamicobject.uicontent.api.mapper.UiContentMapper;
import tr.com.havelsan.dynamicobject.uicontent.domain.UiContent;
import tr.com.havelsan.dynamicobject.uicontent.repository.UiContentRepository;

@ExtendWith(MockitoExtension.class)
class UiContentServiceImplTest {

    @Mock
    private UiContentRepository uiContentRepository;

    @Mock
    private UiContentMapper uiContentMapper;

    @Captor
    private ArgumentCaptor<UiContent> uiContentCaptor;

    private UiContentServiceImpl uiContentService;

    @BeforeEach
    void setUp() {
        uiContentService = new UiContentServiceImpl(uiContentRepository, uiContentMapper);
    }

    @Test
    void createUiContent_whenNameExists_throwsConflict() {
        UiContentRequestDTO dto = buildDto("existing", "btn", EUiContentType.DEFAULT, List.of(1, 2));
        when(uiContentRepository.existsById("existing")).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> uiContentService.createUiContent(dto));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("UI content already exists", exception.getReason());
        verify(uiContentRepository, never()).save(any());
        verify(uiContentMapper, never()).toEntity(any());
    }

    @Test
    void createUiContent_whenNameDoesNotExist_mapsAndSaves() {
        UiContentRequestDTO dto = buildDto("content", "card", EUiContentType.CONTROL_DRIVING, List.of(10, 20));
        UiContent mapped = buildContent(dto.getName(), dto.getCssClassName(), dto.getType(), dto.getItemIdList());
        when(uiContentRepository.existsById("content")).thenReturn(false);
        when(uiContentMapper.toEntity(dto)).thenReturn(mapped);
        when(uiContentRepository.save(any(UiContent.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UiContent saved = uiContentService.createUiContent(dto);

        verify(uiContentRepository).save(uiContentCaptor.capture());
        verify(uiContentMapper).toEntity(dto);
        UiContent captured = uiContentCaptor.getValue();
        assertEquals(mapped.getName(), captured.getName());
        assertEquals(mapped.getCssClassName(), captured.getCssClassName());
        assertEquals(mapped.getType(), captured.getType());
        assertEquals(mapped.getItemIdList(), captured.getItemIdList());
        assertEquals("content", saved.getName());
        assertEquals("card", saved.getCssClassName());
        assertEquals(EUiContentType.CONTROL_DRIVING, saved.getType());
        assertEquals(List.of(10, 20), saved.getItemIdList());
    }

    @Test
    void getUiContentById_whenFound_returnsContent() {
        UiContent content = buildContent("content", "card", EUiContentType.DEFAULT, List.of(3));
        when(uiContentRepository.findById("content")).thenReturn(Optional.of(content));

        UiContent result = uiContentService.getUiContentById("content");

        assertSame(content, result);
    }

    @Test
    void getUiContentById_whenMissing_throwsNotFound() {
        when(uiContentRepository.findById("missing")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> uiContentService.getUiContentById("missing"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("UI content not found", exception.getReason());
    }

    @Test
    void updateUiContent_whenDtoFieldsNull_keepsExistingValues() {
        UiContent existing = buildContent("content", "original", EUiContentType.DEFAULT, List.of(1, 2));
        UiContentRequestDTO dto = new UiContentRequestDTO();
        dto.setItemIdList(null);
        when(uiContentRepository.findById("content")).thenReturn(Optional.of(existing));
        when(uiContentRepository.save(existing)).thenReturn(existing);

        UiContent result = uiContentService.updateUiContent("content", dto);

        assertSame(existing, result);
        assertEquals("original", existing.getCssClassName());
        assertEquals(EUiContentType.DEFAULT, existing.getType());
        assertEquals(List.of(1, 2), existing.getItemIdList());
        verify(uiContentRepository).save(existing);
    }

    @Test
    void updateUiContent_whenDtoHasValues_updatesFieldsAndItemList() {
        UiContent existing = buildContent("content", "original", EUiContentType.DEFAULT, List.of(1, 2));
        UiContentRequestDTO dto = buildDto(null, "updated", EUiContentType.AREA_RULE_PROPERTIES, List.of(7, 8, 9));
        when(uiContentRepository.findById("content")).thenReturn(Optional.of(existing));
        when(uiContentRepository.save(existing)).thenReturn(existing);

        UiContent result = uiContentService.updateUiContent("content", dto);

        assertSame(existing, result);
        assertEquals("updated", existing.getCssClassName());
        assertEquals(EUiContentType.AREA_RULE_PROPERTIES, existing.getType());
        assertEquals(List.of(7, 8, 9), existing.getItemIdList());
        verify(uiContentRepository).save(existing);
    }

    @Test
    void updateUiContent_whenMissing_throwsNotFound() {
        UiContentRequestDTO dto = buildDto(null, "updated", EUiContentType.AREA_RULE_PROPERTIES, List.of(7));
        when(uiContentRepository.findById("missing")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> uiContentService.updateUiContent("missing", dto));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("UI content not found", exception.getReason());
        verify(uiContentRepository, never()).save(any());
    }

    @Test
    void deleteUiContent_whenFound_deletesContent() {
        UiContent existing = buildContent("content", "css", EUiContentType.DEFAULT, List.of());
        when(uiContentRepository.findById("content")).thenReturn(Optional.of(existing));

        uiContentService.deleteUiContent("content");

        verify(uiContentRepository).delete(existing);
    }

    @Test
    void deleteUiContent_whenMissing_throwsNotFound() {
        when(uiContentRepository.findById("missing")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> uiContentService.deleteUiContent("missing"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("UI content not found", exception.getReason());
        verify(uiContentRepository, never()).delete(any());
    }

    @Test
    void getAllUiContents_returnsRepositoryResults() {
        List<UiContent> contents = List.of(
                buildContent("one", "c1", EUiContentType.DEFAULT, List.of(1)),
                buildContent("two", "c2", EUiContentType.GROUP, List.of(2, 3)));
        when(uiContentRepository.findAll()).thenReturn(contents);

        List<UiContent> result = uiContentService.getAllUiContents();

        assertEquals(contents, result);
        verify(uiContentRepository).findAll();
    }

    private UiContentRequestDTO buildDto(String name, String cssClassName, EUiContentType type, List<Integer> itemIds) {
        UiContentRequestDTO dto = new UiContentRequestDTO();
        dto.setName(name);
        dto.setCssClassName(cssClassName);
        dto.setType(type);
        dto.setItemIdList(itemIds);
        return dto;
    }

    private UiContent buildContent(String name, String cssClassName, EUiContentType type, List<Integer> itemIds) {
        UiContent content = new UiContent();
        content.setName(name);
        content.setCssClassName(cssClassName);
        content.setType(type);
        content.setItemIdList(itemIds);
        return content;
    }
}
