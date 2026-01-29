package tr.com.havelsan.dynamicobject.uicontent.api.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import tr.com.havelsan.dynamicobject.uicontent.api.dto.UiContentRequestDTO;
import tr.com.havelsan.dynamicobject.uicontent.api.dto.UiContentResponseDTO;
import tr.com.havelsan.dynamicobject.uicontent.domain.UiContent;

@Mapper(componentModel = "spring")
public interface UiContentMapper {
    UiContent toEntity(UiContentRequestDTO dto);

    UiContentResponseDTO toResponse(UiContent content);

    List<UiContentResponseDTO> toResponseList(List<UiContent> contents);
}
