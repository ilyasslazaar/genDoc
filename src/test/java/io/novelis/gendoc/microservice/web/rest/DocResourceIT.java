package io.novelis.gendoc.microservice.web.rest;

import io.novelis.gendoc.microservice.MicroserviceApp;
import io.novelis.gendoc.microservice.domain.Doc;
import io.novelis.gendoc.microservice.repository.DocRepository;
import io.novelis.gendoc.microservice.service.DocService;
import io.novelis.gendoc.microservice.service.dto.DocDTO;
import io.novelis.gendoc.microservice.service.mapper.DocMapper;
import io.novelis.gendoc.microservice.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static io.novelis.gendoc.microservice.web.rest.TestUtil.sameInstant;
import static io.novelis.gendoc.microservice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.novelis.gendoc.microservice.domain.enumeration.DocTypes;
/**
 * Integration tests for the {@Link DocResource} REST controller.
 */
@SpringBootTest(classes = MicroserviceApp.class)
public class DocResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_DOC = "AAAAAAAAAA";
    private static final String UPDATED_DOC = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final DocTypes DEFAULT_TYPE = DocTypes.CV;
    private static final DocTypes UPDATED_TYPE = DocTypes.ATTESTATION_STAGE;

    @Autowired
    private DocRepository docRepository;

    @Autowired
    private DocMapper docMapper;

    @Autowired
    private DocService docService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDocMockMvc;

    private Doc doc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocResource docResource = new DocResource(docService);
        this.restDocMockMvc = MockMvcBuilders.standaloneSetup(docResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doc createEntity(EntityManager em) {
        Doc doc = new Doc()
            .email(DEFAULT_EMAIL)
            .doc(DEFAULT_DOC)
            .createdAt(DEFAULT_CREATED_AT)
            .type(DEFAULT_TYPE);
        return doc;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doc createUpdatedEntity(EntityManager em) {
        Doc doc = new Doc()
            .email(UPDATED_EMAIL)
            .doc(UPDATED_DOC)
            .createdAt(UPDATED_CREATED_AT)
            .type(UPDATED_TYPE);
        return doc;
    }

    @BeforeEach
    public void initTest() {
        doc = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoc() throws Exception {
        int databaseSizeBeforeCreate = docRepository.findAll().size();

        // Create the Doc
        DocDTO docDTO = docMapper.toDto(doc);
        restDocMockMvc.perform(post("/api/docs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docDTO)))
            .andExpect(status().isCreated());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeCreate + 1);
        Doc testDoc = docList.get(docList.size() - 1);
        assertThat(testDoc.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDoc.getDoc()).isEqualTo(DEFAULT_DOC);
        assertThat(testDoc.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testDoc.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createDocWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = docRepository.findAll().size();

        // Create the Doc with an existing ID
        doc.setId(1L);
        DocDTO docDTO = docMapper.toDto(doc);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocMockMvc.perform(post("/api/docs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = docRepository.findAll().size();
        // set the field null
        doc.setEmail(null);

        // Create the Doc, which fails.
        DocDTO docDTO = docMapper.toDto(doc);

        restDocMockMvc.perform(post("/api/docs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docDTO)))
            .andExpect(status().isBadRequest());

        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDocIsRequired() throws Exception {
        int databaseSizeBeforeTest = docRepository.findAll().size();
        // set the field null
        doc.setDoc(null);

        // Create the Doc, which fails.
        DocDTO docDTO = docMapper.toDto(doc);

        restDocMockMvc.perform(post("/api/docs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docDTO)))
            .andExpect(status().isBadRequest());

        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = docRepository.findAll().size();
        // set the field null
        doc.setCreatedAt(null);

        // Create the Doc, which fails.
        DocDTO docDTO = docMapper.toDto(doc);

        restDocMockMvc.perform(post("/api/docs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docDTO)))
            .andExpect(status().isBadRequest());

        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = docRepository.findAll().size();
        // set the field null
        doc.setType(null);

        // Create the Doc, which fails.
        DocDTO docDTO = docMapper.toDto(doc);

        restDocMockMvc.perform(post("/api/docs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docDTO)))
            .andExpect(status().isBadRequest());

        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocs() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get all the docList
        restDocMockMvc.perform(get("/api/docs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doc.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].doc").value(hasItem(DEFAULT_DOC.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getDoc() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        // Get the doc
        restDocMockMvc.perform(get("/api/docs/{id}", doc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doc.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.doc").value(DEFAULT_DOC.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDoc() throws Exception {
        // Get the doc
        restDocMockMvc.perform(get("/api/docs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoc() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        int databaseSizeBeforeUpdate = docRepository.findAll().size();

        // Update the doc
        Doc updatedDoc = docRepository.findById(doc.getId()).get();
        // Disconnect from session so that the updates on updatedDoc are not directly saved in db
        em.detach(updatedDoc);
        updatedDoc
            .email(UPDATED_EMAIL)
            .doc(UPDATED_DOC)
            .createdAt(UPDATED_CREATED_AT)
            .type(UPDATED_TYPE);
        DocDTO docDTO = docMapper.toDto(updatedDoc);

        restDocMockMvc.perform(put("/api/docs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docDTO)))
            .andExpect(status().isOk());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeUpdate);
        Doc testDoc = docList.get(docList.size() - 1);
        assertThat(testDoc.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDoc.getDoc()).isEqualTo(UPDATED_DOC);
        assertThat(testDoc.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testDoc.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingDoc() throws Exception {
        int databaseSizeBeforeUpdate = docRepository.findAll().size();

        // Create the Doc
        DocDTO docDTO = docMapper.toDto(doc);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocMockMvc.perform(put("/api/docs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Doc in the database
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDoc() throws Exception {
        // Initialize the database
        docRepository.saveAndFlush(doc);

        int databaseSizeBeforeDelete = docRepository.findAll().size();

        // Delete the doc
        restDocMockMvc.perform(delete("/api/docs/{id}", doc.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Doc> docList = docRepository.findAll();
        assertThat(docList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Doc.class);
        Doc doc1 = new Doc();
        doc1.setId(1L);
        Doc doc2 = new Doc();
        doc2.setId(doc1.getId());
        assertThat(doc1).isEqualTo(doc2);
        doc2.setId(2L);
        assertThat(doc1).isNotEqualTo(doc2);
        doc1.setId(null);
        assertThat(doc1).isNotEqualTo(doc2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocDTO.class);
        DocDTO docDTO1 = new DocDTO();
        docDTO1.setId(1L);
        DocDTO docDTO2 = new DocDTO();
        assertThat(docDTO1).isNotEqualTo(docDTO2);
        docDTO2.setId(docDTO1.getId());
        assertThat(docDTO1).isEqualTo(docDTO2);
        docDTO2.setId(2L);
        assertThat(docDTO1).isNotEqualTo(docDTO2);
        docDTO1.setId(null);
        assertThat(docDTO1).isNotEqualTo(docDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(docMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(docMapper.fromId(null)).isNull();
    }
}
