package io.novelis.gendoc.web.rest;

import io.novelis.gendoc.service.DocService;
import io.novelis.gendoc.service.DownloadFile;
import io.novelis.gendoc.web.rest.errors.BadRequestAlertException;
import io.novelis.gendoc.service.dto.DocDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.novelis.gendoc.domain.Doc}.
 */
@RestController
@RequestMapping("/api")
public class DocResource {

    private final Logger log = LoggerFactory.getLogger(DocResource.class);
    private static final String ENTITY_NAME = "gendocDoc";
    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final DocService docService;

    public DocResource(DocService docService) {
        this.docService = docService;
    }
    private final String OUTPUT_DIR="src/main/resources/generated-documents/";
    @PostMapping(value = "/docs/generate")
    public String  generateDocument(@RequestParam("data") DocDTO docDTO)  {
        File PDFFile=null;
        String PDFFileName="";
        try {
            PDFFile = docService.generateDoc(docDTO.getDTO());
            PDFFileName=PDFFile.getName();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        }
        return PDFFileName;
    }
    /**
     * Download the generated PDF File from the FS
     * @param response HttpServletResponse object
     * @param fileName File name to download
     * @throws IOException Throw IOE if the file doesn't exists in the FS
     */
    @GetMapping("/docs/download/{fileName:.+}")
    public void downloadPDFResource(HttpServletResponse response,
                                    @PathVariable("fileName") String fileName) throws IOException {
        DownloadFile.downloadFile(OUTPUT_DIR,fileName,response);
    }
    /**
     * {@code POST  /docs} : Create a new doc.
     *
     * @param docDTO the docDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new docDTO, or with status {@code 400 (Bad Request)} if the doc has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/docs")
    public ResponseEntity<DocDTO> createDoc(@Valid @RequestBody DocDTO docDTO) throws URISyntaxException {
        log.debug("REST request to save Doc : {}", docDTO);
        if (docDTO.getId() != null) {
            throw new BadRequestAlertException("A new doc cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocDTO result = docService.save(docDTO);
        return ResponseEntity.created(new URI("/api/docs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /docs} : Updates an existing doc.
     *
     * @param docDTO the docDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated docDTO,
     * or with status {@code 400 (Bad Request)} if the docDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the docDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/docs")
    public ResponseEntity<DocDTO> updateDoc(@Valid @RequestBody DocDTO docDTO) throws URISyntaxException {
        log.debug("REST request to update Doc : {}", docDTO);
        if (docDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DocDTO result = docService.save(docDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, docDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /docs} : get all the docs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of docs in body.
     */
    @GetMapping("/docs")
    public List<DocDTO> getAllDocs() {
        log.debug("REST request to get all Docs");
        return docService.findAll();
    }

    /**
     * {@code GET  /docs/:id} : get the "id" doc.
     *
     * @param id the id of the docDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the docDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/docs/{id}")
    public ResponseEntity<DocDTO> getDoc(@PathVariable Long id) {
        log.debug("REST request to get Doc : {}", id);
        Optional<DocDTO> docDTO = docService.findOne(id);
        return ResponseUtil.wrapOrNotFound(docDTO);
    }

    /**
     * {@code DELETE  /docs/:id} : delete the "id" doc.
     *
     * @param id the id of the docDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/docs/{id}")
    public ResponseEntity<Void> deleteDoc(@PathVariable Long id) {
        log.debug("REST request to delete Doc : {}", id);
        docService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
