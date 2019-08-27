package io.novelis.gendoc.service.mapper;

import io.novelis.gendoc.domain.*;
import io.novelis.gendoc.service.dto.DocDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Doc} and its DTO {@link DocDTO}.
 */
@Mapper(componentModel = "spring", uses = {TypeMapper.class})
public interface DocMapper extends EntityMapper<DocDTO, Doc> {

    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "type.name", target = "typeName")
    DocDTO toDto(Doc doc);

    @Mapping(source = "typeId", target = "type")
    Doc toEntity(DocDTO docDTO);

    default Doc fromId(Long id) {
        if (id == null) {
            return null;
        }
        Doc doc = new Doc();
        doc.setId(id);
        return doc;
    }
}
