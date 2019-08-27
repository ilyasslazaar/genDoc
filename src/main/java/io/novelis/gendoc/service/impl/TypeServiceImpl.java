package io.novelis.gendoc.service.impl;

import io.novelis.gendoc.service.TypeService;
import io.novelis.gendoc.domain.Type;
import io.novelis.gendoc.repository.TypeRepository;
import io.novelis.gendoc.service.dto.TypeDTO;
import io.novelis.gendoc.service.mapper.TypeMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Type}.
 */
@Service
@Transactional
public class TypeServiceImpl implements TypeService {

    private final Logger log = LoggerFactory.getLogger(TypeServiceImpl.class);
    private final String INPUT_DIR="src/main/resources/velocity-templates/";
    private final TypeRepository typeRepository;

    private final TypeMapper typeMapper;

    public TypeServiceImpl(TypeRepository typeRepository, TypeMapper typeMapper) {
        this.typeRepository = typeRepository;
        this.typeMapper = typeMapper;
    }
    /**
     * @param template .docx/.doc template file
     * @return true if the file is saved in the FS otherwise returns false.
     */
    @Override
    public boolean saveFile(MultipartFile template,TypeDTO typeDTO) {
        boolean result=false;
        boolean fileNameExists=false;
        List<TypeDTO> types=findAll();
        for (TypeDTO type:types) {
        if(type.getName().equals(typeDTO.getName())){
            fileNameExists=true;
            break;
        }}
        if(!fileNameExists){
        save(typeDTO);
        File templateFile=new File(INPUT_DIR+template.getOriginalFilename());
        try {
            FileUtils.writeByteArrayToFile(templateFile,template.getBytes());
            result=true;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        }
        return result;
    }

    /**
     * Save a type.
     *
     * @param typeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TypeDTO save(TypeDTO typeDTO) {
        log.debug("Request to save Type : {}", typeDTO);
        Type type = typeMapper.toEntity(typeDTO);
        type = typeRepository.save(type);
        return typeMapper.toDto(type);
    }

    /**
     * Get all the types.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TypeDTO> findAll() {
        log.debug("Request to get all Types");
        return typeRepository.findAll().stream()
            .map(typeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one type by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TypeDTO> findOne(Long id) {
        log.debug("Request to get Type : {}", id);
        return typeRepository.findById(id)
            .map(typeMapper::toDto);
    }

    /**
     * Delete the type by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Type : {}", id);
        typeRepository.deleteById(id);
    }
}
