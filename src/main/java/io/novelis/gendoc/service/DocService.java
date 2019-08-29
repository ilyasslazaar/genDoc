package io.novelis.gendoc.service;

import io.novelis.gendoc.service.dto.DocDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link io.novelis.gendoc.domain.Doc}.
 */
public interface DocService {
    /**
     * Generate DOCX from the given DTO and Template using XDOCREPORT and convert it to PDF file
     * @param docDTO the document DTO
     * @return the generated PDF file
     */
    File generateDoc(DocDTO docDTO) throws FileNotFoundException;
    /**
     * Convert the given XDOC file to a PDF File
     * @param docxFile DOCX file to convert
     * @param docType   Document Type ( CV, Attestation de stage... )
     * @return the converted PDF file
     */
    File convertDOCXToPDF(File docxFile, String docType);
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
