package tr.com.havelsan.dynamicobject.metadata.api.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tr.com.havelsan.dynamicobject.common.enums.EEntityType;

@Data
public class MetaDataDTO {

    @NotBlank(message = "Metadata name boş olamaz")
    @Size(max = 100, message = "Metadata name en fazla 100 karakter olabilir")
    private String name;

    @NotNull(message = "EntityType zorunludur")
    private EEntityType entityType;
    private List<PropertyItemDTO> propertyItemList = new ArrayList<>();

    public MetaDataDTO() {
    }

}
