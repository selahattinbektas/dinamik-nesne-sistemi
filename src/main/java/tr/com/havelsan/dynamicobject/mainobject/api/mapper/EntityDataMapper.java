package tr.com.havelsan.dynamicobject.mainobject.api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import tr.com.havelsan.dynamicobject.mainobject.api.dto.EntityDataResponseDTO;
import tr.com.havelsan.dynamicobject.mainobject.domain.EntityData;

@Mapper(componentModel = "spring")
public interface EntityDataMapper {
    EntityDataResponseDTO toResponse(EntityData entityData);

    List<EntityDataResponseDTO> toResponseList(List<EntityData> entityDataList);
}
