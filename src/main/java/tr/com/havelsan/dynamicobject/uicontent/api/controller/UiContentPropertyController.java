package tr.com.havelsan.dynamicobject.uicontent.api.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.havelsan.dynamicobject.uicontent.service.UiContentPropertyService;

@RestController
@RequestMapping("/api/ui-contents/{uiContentName}/properties")
public class UiContentPropertyController {
    private final UiContentPropertyService uiContentPropertyService;

    public UiContentPropertyController(UiContentPropertyService uiContentPropertyService) {
        this.uiContentPropertyService = uiContentPropertyService;
    }

    @PostMapping("/{propertyId}")
    public ResponseEntity<List<Integer>> addProperty(
            @PathVariable String uiContentName,
            @PathVariable Integer propertyId
    ) {
        return ResponseEntity.ok(uiContentPropertyService.addPropertyToUiContent(uiContentName, propertyId));
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<Void> removeProperty(
            @PathVariable String uiContentName,
            @PathVariable Integer propertyId
    ) {
        uiContentPropertyService.removePropertyFromUiContent(uiContentName, propertyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Integer>> getProperties(@PathVariable String uiContentName) {
        return ResponseEntity.ok(uiContentPropertyService.getPropertiesOfUiContent(uiContentName));
    }
}
