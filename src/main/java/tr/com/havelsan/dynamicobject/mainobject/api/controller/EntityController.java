package tr.com.havelsan.dynamicobject.mainobject.api.controller;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.havelsan.dynamicobject.mainobject.api.dto.EntityDataDTO;
import tr.com.havelsan.dynamicobject.mainobject.api.dto.EntityDataResponseDTO;
import tr.com.havelsan.dynamicobject.mainobject.api.mapper.EntityDataMapper;
import tr.com.havelsan.dynamicobject.mainobject.service.EntityService;

@RestController
@RequestMapping("/api/entities")
public class EntityController {
    private final EntityService entityService;
    private final EntityDataMapper entityDataMapper;

    public EntityController(EntityService entityService, EntityDataMapper entityDataMapper) {
        this.entityService = entityService;
        this.entityDataMapper = entityDataMapper;
    }

    @PostMapping("/{metaDataName}")
    @Operation(summary = "Create entity instance")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Entity payload",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "EntityCreate",
                            value = """
                                    {
                                      "id": "IHA-001",
                                      "metaDataName": "IHA",
                                      "properties": {
                                        "altitude": 120,
                                        "speed": 80
                                      }
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<EntityDataResponseDTO> createEntity(
            @PathVariable String metaDataName,
            @RequestBody EntityDataDTO dto
    ) {
        return ResponseEntity.ok(entityDataMapper.toResponse(entityService.createEntity(metaDataName, dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityDataResponseDTO> getEntity(@PathVariable String id) {
        return ResponseEntity.ok(entityDataMapper.toResponse(entityService.getEntityById(id)));
    }

    @GetMapping
    public ResponseEntity<List<EntityDataResponseDTO>> getEntities() {
        return ResponseEntity.ok(entityDataMapper.toResponseList(entityService.getAllEntities()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityDataResponseDTO> updateEntity(@PathVariable String id, @RequestBody EntityDataDTO dto) {
        return ResponseEntity.ok(entityDataMapper.toResponse(entityService.updateEntity(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable String id) {
        entityService.deleteEntity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/metadata/{metaDataName}")
    public ResponseEntity<List<EntityDataResponseDTO>> getEntitiesByMetaData(@PathVariable String metaDataName) {
        return ResponseEntity.ok(entityDataMapper.toResponseList(entityService.getAllEntitiesByMetaData(metaDataName)));
    }
}
