package io.novelis.gendoc.microservice.service.impl;

import io.novelis.gendoc.microservice.service.DocService;
import io.novelis.gendoc.microservice.domain.Doc;
import io.novelis.gendoc.microservice.repository.DocRepository;
import io.novelis.gendoc.microservice.service.dto.DocDTO;
import io.novelis.gendoc.microservice.service.mapper.DocMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Doc}.
 */
@Service
@Transactional
public class DocServiceImpl implements DocService {

    private final Logger log = LoggerFactory.getLogger(DocServiceImpl.class);

    private final DocRepository docRepository;

    private final DocMapper docMapper;

    public DocServiceImpl(DocRepository docRepository, DocMapper docMapper) {
        this.docRepository = docRepository;
        this.docMapper = docMapper;
    }

    /**
     * Save a doc.
     *
     * @param docDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DocDTO save(DocDTO docDTO) {
        log.debug("Request to save Doc : {}", docDTO);
        Doc doc = docMapper.toEntity(docDTO);
        doc = docRepository.save(doc);
        return docMapper.toDto(doc);
    }

    /**
     * Get all the docs.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DocDTO> findAll() {
        log.debug("Request to get all Docs");
        return docRepository.findAll().stream()
            .map(docMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one doc by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DocDTO> findOne(Long id) {
        log.debug("Request to get Doc : {}", id);
        return docRepository.findById(id)
            .map(docMapper::toDto);
    }

    /**
     * Delete the doc by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Doc : {}", id);
        docRepository.deleteById(id);
    }
}
