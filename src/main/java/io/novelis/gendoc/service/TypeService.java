package io.novelis.gendoc.service;

import io.novelis.gendoc.service.dto.TypeDTO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.novelis.gendoc.domain.Type}.
 */
public interface TypeService {
    /**
     * @param template .docx/.doc template file
     * @return true if the file is saved in the FS otherwise returns false.
     */
    boolean saveFile(MultipartFile template,TypeDTO typeDTO);
    /**
     * Save a type.
     *
     * @param typeDTO the entity to save.
     * @return the persisted entity.
     */
    TypeDTO save(TypeDTO typeDTO);

    /**
     * Get all the types.
     *
     * @return the list of entities.
     */
    List<TypeDTO> findAll();


    /**
     * Get the "id" type.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeDTO> findOne(Long id);

    /**
     * Delete the "id" type.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
