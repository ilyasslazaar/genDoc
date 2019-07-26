package io.novelis.gendoc.service.mapper;

import io.novelis.gendoc.domain.*;
import io.novelis.gendoc.service.dto.DocDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Doc} and its DTO {@link DocDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DocMapper extends EntityMapper<DocDTO, Doc> {



    default Doc fromId(Long id) {
        if (id == null) {
            return null;
        }
        Doc doc = new Doc();
        doc.setId(id);
        return doc;
    }
}
