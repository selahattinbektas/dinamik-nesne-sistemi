package tr.com.havelsan.dynamicobject.metadata.api.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import tr.com.havelsan.dynamicobject.metadata.api.dto.MetaDataClassificationDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.MetaDataClassificationResponseDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.MetaDataDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.MetaDataResponseDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.OptionDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.OptionResponseDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.PropertyItemDTO;
import tr.com.havelsan.dynamicobject.metadata.api.dto.PropertyItemResponseDTO;
import tr.com.havelsan.dynamicobject.metadata.api.mapper.MetaDataMapper;
import tr.com.havelsan.dynamicobject.metadata.service.MetaDataService;

@RestController
@RequestMapping("/api/metadata")
public class MetaDataController {
    private final MetaDataService metaDataService;
    private final MetaDataMapper metaDataMapper;

    public MetaDataController(MetaDataService metaDataService, MetaDataMapper metaDataMapper) {
        this.metaDataService = metaDataService;
        this.metaDataMapper = metaDataMapper;
    }

    @PostMapping
    @Operation(summary = "Create metadata definition")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Metadata definition payload",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "MetaDataCreate",
                            value = """
                                    {
                                      "name": "IHA",
                                      "entityType": "CGF",
                                      "propertyItemList": [
                                        {
                                          "type": "TEXT",
                                          "id": 1,
                                          "itemName": "altitude",
                                          "title": "Altitude",
                                          "unit": "m",
                                          "min": 0,
                                          "max": 10000,
                                          "defaultValue": "100",
                                          "groupProperty": false,
                                          "readOnly": false,
                                          "showContextMenu": true,
                                          "propertyItemType": "DEFAULT",
                                          "selectedVisible": true,
                                          "options": []
                                        }
                                      ]
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<MetaDataResponseDTO> createMetaData(@Valid @RequestBody MetaDataDTO dto) {
        return ResponseEntity.ok(metaDataMapper.toMetaDataResponse(metaDataService.createMetaData(dto)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MetaDataResponseDTO>> searchMetaData(@RequestParam String name) {
        return ResponseEntity.ok(metaDataMapper.toMetaDataResponseList(metaDataService.searchMetaDataByName(name)));
    }

    @GetMapping("/{name}")
    public ResponseEntity<MetaDataResponseDTO> getMetaData(@PathVariable String name) {
        return ResponseEntity.ok(metaDataMapper.toMetaDataResponse(metaDataService.getMetaDataByName(name)));
    }

    @PutMapping("/{name}")
    public ResponseEntity<MetaDataResponseDTO> updateMetaData(@PathVariable String name, @RequestBody MetaDataDTO dto) {
        return ResponseEntity.ok(metaDataMapper.toMetaDataResponse(metaDataService.updateMetaData(name, dto)));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteMetaData(@PathVariable String name) {
        metaDataService.deleteMetaData(name);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MetaDataResponseDTO>> getAllMetaDatas() {
        return ResponseEntity.ok(metaDataMapper.toMetaDataResponseList(metaDataService.getAllMetaDatas()));
    }

    //property-item
    @PostMapping("/property-items/{metaDataName}")
    @Operation(summary = "Create property item for metadata")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Property item payload",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "PropertyItemCreate",
                            value = """
                                    {
                                      "type": "TEXT",
                                      "id": 10,
                                      "itemName": "speed",
                                      "title": "Speed",
                                      "unit": "km/h",
                                      "min": 0,
                                      "max": 500,
                                      "defaultValue": "120",
                                      "groupProperty": false,
                                      "readOnly": false,
                                      "showContextMenu": true,
                                      "propertyItemType": "DEFAULT",
                                      "selectedVisible": true,
                                      "options": []
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<PropertyItemResponseDTO> createPropertyItem(
            @PathVariable String metaDataName,
            @RequestBody PropertyItemDTO dto
    ) {
        return ResponseEntity.ok(metaDataMapper.toPropertyItemResponse(metaDataService.createPropertyItem(metaDataName, dto)));
    }

    @GetMapping("/property-items/{id}")
    public ResponseEntity<PropertyItemResponseDTO> getPropertyItem(@PathVariable Integer id) {
        return ResponseEntity.ok(metaDataMapper.toPropertyItemResponse(metaDataService.getPropertyItemById(id)));
    }

    @PutMapping("/property-items/{id}")
    public ResponseEntity<PropertyItemResponseDTO> updatePropertyItem(@PathVariable Integer id, @RequestBody PropertyItemDTO dto) {
        return ResponseEntity.ok(metaDataMapper.toPropertyItemResponse(metaDataService.updatePropertyItem(id, dto)));
    }

    @DeleteMapping("/property-items/{id}")
    public ResponseEntity<Void> deletePropertyItem(@PathVariable Integer id) {
        metaDataService.deletePropertyItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/property-items/metadata/{metaDataName}")
    public ResponseEntity<List<PropertyItemResponseDTO>> getPropertyItemsByMetaData(@PathVariable String metaDataName) {
        return ResponseEntity.ok(metaDataMapper.toPropertyItemResponseList(
                metaDataService.getAllPropertyItemsByMetaData(metaDataName)
        ));
    }


    //options
    @PostMapping("/options")
    @Operation(summary = "Create option")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Option payload",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "OptionCreate",
                            value = """
                                    {
                                      "value": "TEAM_ALPHA",
                                      "label": "Team Alpha",
                                      "optionsPropertyItemType": "teamType"
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<OptionResponseDTO> createOption(@RequestBody OptionDTO dto) {
        return ResponseEntity.ok(metaDataMapper.toOptionResponse(metaDataService.createOption(dto)));
    }

    @GetMapping("/options/{value}")
    public ResponseEntity<OptionResponseDTO> getOption(@PathVariable String value) {
        return ResponseEntity.ok(metaDataMapper.toOptionResponse(metaDataService.getOptionById(value)));
    }

    @PutMapping("/options/{value}")
    public ResponseEntity<OptionResponseDTO> updateOption(@PathVariable String value, @RequestBody OptionDTO dto) {
        return ResponseEntity.ok(metaDataMapper.toOptionResponse(metaDataService.updateOption(value, dto)));
    }

    @DeleteMapping("/options/{value}")
    public ResponseEntity<Void> deleteOption(@PathVariable String value) {
        metaDataService.deleteOption(value);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/options")
    public ResponseEntity<List<OptionResponseDTO>> getAllOptions() {
        return ResponseEntity.ok(metaDataMapper.toOptionResponseList(metaDataService.getAllOptions()));
    }

    //metadataproperty
    @PostMapping("/{metaDataName}/properties")
    public ResponseEntity<PropertyItemResponseDTO> addProperty(
            @PathVariable String metaDataName,
            @RequestBody PropertyItemDTO dto
    ) {
        return ResponseEntity.ok(metaDataMapper.toPropertyItemResponse(metaDataService.addPropertyToMetaData(metaDataName, dto)));
    }

    @DeleteMapping("/{metaDataName}/properties/{propertyId}")
    public ResponseEntity<Void> removeProperty(
            @PathVariable String metaDataName,
            @PathVariable Integer propertyId
    ) {
        metaDataService.removePropertyFromMetaData(metaDataName, propertyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{metaDataName}/properties")
    public ResponseEntity<List<PropertyItemResponseDTO>> getProperties(@PathVariable String metaDataName) {
        return ResponseEntity.ok(metaDataMapper.toPropertyItemResponseList(
                metaDataService.getPropertiesOfMetaData(metaDataName)
        ));
    }

    //classsification

    @PostMapping("/classifications")
    @Operation(summary = "Create metadata classification")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Classification payload",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "ClassificationCreate",
                            value = """
                                    {
                                      "drawClassification": "TECH",
                                      "classification": "UAV",
                                      "metaDataName": "IHA",
                                      "metaDataCode": "IHA",
                                      "symbolCode": "UAV-001",
                                      "unitType": "metric",
                                      "undeletable": false
                                    }
                                    """
                    )
            )
    )
    public ResponseEntity<MetaDataClassificationResponseDTO> createClassification(@RequestBody MetaDataClassificationDTO dto) {
        return ResponseEntity.ok(metaDataMapper.toClassificationResponse(metaDataService.createClassification(dto)));
    }

    @GetMapping("/classifications/{id}")
    public ResponseEntity<MetaDataClassificationResponseDTO> getClassification(@PathVariable String id) {
        return ResponseEntity.ok(metaDataMapper.toClassificationResponse(metaDataService.getClassificationById(id)));
    }

    @PutMapping("/classifications/{id}")
    public ResponseEntity<MetaDataClassificationResponseDTO> updateClassification(
            @PathVariable String id,
            @RequestBody MetaDataClassificationDTO dto
    ) {
        return ResponseEntity.ok(metaDataMapper.toClassificationResponse(metaDataService.updateClassification(id, dto)));
    }

    @GetMapping("/classifications")
    public ResponseEntity<List<MetaDataClassificationResponseDTO>> getAllClassifications() {
        return ResponseEntity.ok(metaDataMapper.toClassificationResponseList(metaDataService.getAllClassifications()));
    }

    @GetMapping("/classifications/filter/{classification}")
    public ResponseEntity<List<MetaDataClassificationResponseDTO>> filterByClassification(
            @PathVariable String classification
    ) {
        return ResponseEntity.ok(metaDataMapper.toClassificationResponseList(
                metaDataService.filterByClassification(classification)
        ));
    }

    @DeleteMapping("/classifications/{id}")
    public ResponseEntity<Void> deleteClassification(@PathVariable String id) {
        metaDataService.deleteClassification(id);
        return ResponseEntity.noContent().build();
    }

}
