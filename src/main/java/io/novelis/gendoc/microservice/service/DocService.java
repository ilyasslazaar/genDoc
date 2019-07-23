package io.novelis.gendoc.microservice.service;

import io.novelis.gendoc.microservice.service.dto.DocDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.novelis.gendoc.microservice.domain.Doc}.
 */
public interface DocService {

    /**
     * Save a doc.
     *
     * @param docDTO the entity to save.
     * @return the persisted entity.
     */
    DocDTO save(DocDTO docDTO);

    /**
     * Get all the docs.
     *
     * @return the list of entities.
     */
    List<DocDTO> findAll();


    /**
     * Get the "id" doc.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocDTO> findOne(Long id);

    /**
     * Delete the "id" doc.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
