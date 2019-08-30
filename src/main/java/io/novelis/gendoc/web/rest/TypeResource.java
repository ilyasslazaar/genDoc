package io.novelis.gendoc.web.rest;
import io.novelis.gendoc.util.DownloadFile;
import io.novelis.gendoc.service.TypeService;
import io.novelis.gendoc.web.rest.errors.BadRequestAlertException;
import io.novelis.gendoc.service.dto.TypeDTO;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.novelis.gendoc.domain.Type}.
 */
@RestController
@RequestMapping("/api")
public class TypeResource {
    private final String INPUT_DIR="src/main/resources/velocity-templates/";
    private final Logger log = LoggerFactory.getLogger(TypeResource.class);
    private static final String ENTITY_NAME = "gendocType";
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeService typeService;

    public TypeResource(TypeService typeService) {
        this.typeService = typeService;
    }

    /**
     * create a new type.
     * @param typeDTO the typeDTO to create
     * @param template the type template
     * @return true if the type created successfully otherwise returns false
     */
    @PostMapping(value = "/type/create")
    public boolean createType(@RequestParam("data") TypeDTO typeDTO,@RequestPart MultipartFile template) {
        return typeService.saveFile(template,typeDTO.getDTO());
    }
    /**
     * {@code POST  /types} : Create a new type.
     *
     * @param typeDTO the typeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeDTO, or with status {@code 400 (Bad Request)} if the type has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/types")
    public ResponseEntity<TypeDTO> createType(@Valid @RequestBody TypeDTO typeDTO) throws URISyntaxException {
        log.debug("REST request to save Type : {}", typeDTO);
        if (typeDTO.getId() != null) {
            throw new BadRequestAlertException("A new type cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeDTO result = typeService.save(typeDTO);
        return ResponseEntity.created(new URI("/api/types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /types} : Updates an existing type.
     *
     * @param typeDTO the typeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeDTO,
     * or with status {@code 400 (Bad Request)} if the typeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeDTO couldn't be updated.
     */
    @PutMapping("/types")
    public ResponseEntity<TypeDTO> updateType(@Valid @RequestBody TypeDTO typeDTO)  {
        log.debug("REST request to update Type : {}", typeDTO);
        if (typeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeDTO result = typeService.save(typeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeDTO.getId().toString()))
            .body(result);
    }

    /**
     * Download the template from the FS
     * @param response HttpServletResponse object
     * @param fileName File name to download
     * @throws IOException Throw IOE if the file doesn't exists in the FS
     */
    @GetMapping("/types/download/{fileName:.+}")
    public void downloadTemplateResource(HttpServletResponse response,
                                    @PathVariable("fileName") String fileName) throws IOException {
        DownloadFile.downloadFile(INPUT_DIR,fileName,response);
    }
    /**
     * {@code GET  /types} : get all the types.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of types in body.
     */
    @GetMapping("/types")
    public List<TypeDTO> getAllTypes() {
        log.debug("REST request to get all Types");
        return typeService.findAll();
    }

    /**
     * {@code GET  /types/:id} : get the "id" type.
     *
     * @param id the id of the typeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/types/{id}")
    public ResponseEntity<TypeDTO> getType(@PathVariable Long id) {
        log.debug("REST request to get Type : {}", id);
        Optional<TypeDTO> typeDTO = typeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeDTO);
    }

    /**
     * {@code DELETE  /types/:id} : delete the "id" type.
     *
     * @param id the id of the typeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/types/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        log.debug("REST request to delete Type : {}", id);
        typeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
