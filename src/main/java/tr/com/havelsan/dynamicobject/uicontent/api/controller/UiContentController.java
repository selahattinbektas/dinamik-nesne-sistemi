package tr.com.havelsan.dynamicobject.uicontent.api.controller;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.havelsan.dynamicobject.uicontent.api.dto.UiContentRequestDTO;
import tr.com.havelsan.dynamicobject.uicontent.api.dto.UiContentResponseDTO;
import tr.com.havelsan.dynamicobject.uicontent.api.mapper.UiContentMapper;
import tr.com.havelsan.dynamicobject.uicontent.service.UiContentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ui-contents")
public class UiContentController {
    private final UiContentService uiContentService;
    private final UiContentMapper uiContentMapper;

    @PostMapping
    @Operation(summary = "Create UI content definition")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "UI content payload",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "UiContentCreate",
                            value = """
                                    {
                                      "name": "IHA-DETAIL",
                                      "cssClassName": "iha-detail",
                                      "type": "DEFAULT",
                                      "itemIdList": [1, 10]
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<UiContentResponseDTO> createUiContent(@RequestBody UiContentRequestDTO dto) {
        return ResponseEntity.ok(uiContentMapper.toResponse(uiContentService.createUiContent(dto)));
    }

    @GetMapping("/{name}")
    public ResponseEntity<UiContentResponseDTO> getUiContent(@PathVariable String name) {
        return ResponseEntity.ok(uiContentMapper.toResponse(uiContentService.getUiContentById(name)));
    }

    @PutMapping("/{name}")
    public ResponseEntity<UiContentResponseDTO> updateUiContent(@PathVariable String name, @RequestBody UiContentRequestDTO dto) {
        return ResponseEntity.ok(uiContentMapper.toResponse(uiContentService.updateUiContent(name, dto)));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteUiContent(@PathVariable String name) {
        uiContentService.deleteUiContent(name);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UiContentResponseDTO>> getAllUiContents() {
        return ResponseEntity.ok(uiContentMapper.toResponseList(uiContentService.getAllUiContents()));
    }
}
