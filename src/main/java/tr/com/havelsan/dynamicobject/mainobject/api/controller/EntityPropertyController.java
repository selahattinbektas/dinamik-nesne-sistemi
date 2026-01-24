package tr.com.havelsan.dynamicobject.mainobject.api.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.havelsan.dynamicobject.mainobject.api.dto.EntityPropertyValueDTO;
import tr.com.havelsan.dynamicobject.mainobject.api.dto.EntityDataResponseDTO;
import tr.com.havelsan.dynamicobject.mainobject.api.mapper.EntityDataMapper;
import tr.com.havelsan.dynamicobject.mainobject.service.EntityPropertyService;

@RestController
@RequestMapping("/api/entities/{entityId}/properties")
public class EntityPropertyController {
    private final EntityPropertyService entityPropertyService;
    private final EntityDataMapper entityDataMapper;

    public EntityPropertyController(EntityPropertyService entityPropertyService, EntityDataMapper entityDataMapper) {
        this.entityPropertyService = entityPropertyService;
        this.entityDataMapper = entityDataMapper;
    }

    @PostMapping
    @Operation(summary = "Assign property value to entity")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Entity property value payload",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "EntityPropertyValueCreate",
                            value = """
                                    [
                                      {
                                        "propertyId": 10,
                                        "value": 120
                                      }
                                    ]
                                    """
                    )
            )
    )
    public ResponseEntity<EntityDataResponseDTO> addProperty(
            @PathVariable String entityId,
            @RequestBody List<EntityPropertyValueDTO> dtoList
    ) {
        return ResponseEntity.ok(entityDataMapper.toResponse(entityPropertyService.addPropertyValuesToEntity(entityId, dtoList)));
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<Void> removeProperty(
            @PathVariable String entityId,
            @PathVariable Integer propertyId
    ) {
        entityPropertyService.removePropertyFromEntity(entityId, propertyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getProperties(@PathVariable String entityId) {
        return ResponseEntity.ok(entityPropertyService.getPropertiesOfEntity(entityId));
    }
}
