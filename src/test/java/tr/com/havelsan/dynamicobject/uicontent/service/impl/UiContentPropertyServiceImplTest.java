package tr.com.havelsan.dynamicobject.uicontent.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import tr.com.havelsan.dynamicobject.uicontent.domain.UiContent;
import tr.com.havelsan.dynamicobject.uicontent.repository.UiContentRepository;
import tr.com.havelsan.dynamicobject.uicontent.service.UiContentService;

@ExtendWith(MockitoExtension.class)
class UiContentPropertyServiceImplTest {
    @Mock
    private UiContentService uiContentService;

    @Mock
    private UiContentRepository uiContentRepository;

    @InjectMocks
    private UiContentPropertyServiceImpl service;

    private UiContent uiContent;

    @BeforeEach
    void setUp() {
        uiContent = new UiContent();
        uiContent.setName("content-1");
        uiContent.setItemIdList(new ArrayList<>(Arrays.asList(1, 2)));
    }

    @Test
    void addPropertyToUiContentAddsAndReturnsUpdatedList() {
        when(uiContentService.getUiContentById("content-1")).thenReturn(uiContent);

        List<Integer> result = service.addPropertyToUiContent("content-1", 3);

        assertThat(result).containsExactly(1, 2, 3);
        verify(uiContentRepository).save(uiContent);
    }

    @Test
    void removePropertyFromUiContentRemovesAndPersists() {
        when(uiContentService.getUiContentById("content-1")).thenReturn(uiContent);

        service.removePropertyFromUiContent("content-1", 2);

        assertThat(uiContent.getItemIdList()).containsExactly(1);
        verify(uiContentRepository).save(uiContent);
    }

    @Test
    void removePropertyFromUiContentThrowsWhenMissing() {
        when(uiContentService.getUiContentById("content-1")).thenReturn(uiContent);

        assertThatThrownBy(() -> service.removePropertyFromUiContent("content-1", 99))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException statusException = (ResponseStatusException) ex;
                    assertThat(statusException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                });
    }

    @Test
    void getPropertiesOfUiContentReturnsCopy() {
        when(uiContentService.getUiContentById("content-1")).thenReturn(uiContent);

        List<Integer> result = service.getPropertiesOfUiContent("content-1");

        assertThat(result).containsExactly(1, 2);
        result.add(3);
        assertThat(uiContent.getItemIdList()).containsExactly(1, 2);
    }
}
