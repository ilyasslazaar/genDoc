package io.novelis.gendoc.service.mapper;

import io.novelis.gendoc.domain.Type;
import io.novelis.gendoc.service.dto.TypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Type} and its DTO {@link TypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeMapper extends EntityMapper<TypeDTO, Type> {


    @Mapping(target = "docs", ignore = true)
    @Mapping(target = "removeDoc", ignore = true)
    Type toEntity(TypeDTO typeDTO);

    default Type fromId(Long id) {
        if (id == null) {
            return null;
        }
        Type type = new Type();
        type.setId(id);
        return type;
    }
}
