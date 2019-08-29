package io.novelis.gendoc.service.impl;
import com.google.common.io.ByteStreams;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ByteArrayImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour;
import io.novelis.gendoc.service.DocService;
import io.novelis.gendoc.domain.Doc;
import io.novelis.gendoc.repository.DocRepository;
import io.novelis.gendoc.service.TypeService;
import io.novelis.gendoc.service.dto.DocDTO;
import io.novelis.gendoc.service.dto.TypeDTO;
import io.novelis.gendoc.service.mapper.DocMapper;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    private final TypeService typeService;
    private final Logger log = LoggerFactory.getLogger(DocServiceImpl.class);
    private final DocRepository docRepository;
    private final String OUTPUT_DIR="src/main/resources/generated-documents/";
    private final String INPUT_DIR="src/main/resources/velocity-templates/";
    private OutputStream out;
    private ZonedDateTime createdAt;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss'Z'");
    private final DocMapper docMapper;

    public DocServiceImpl(DocRepository docRepository, DocMapper docMapper,TypeService typeService) {
        this.docRepository = docRepository;
        this.docMapper = docMapper;
        this.typeService = typeService;
    }
    /**
     * Generate DOCX from the given Template and DTO using XDOCREPORT and convert it to PDF file
     * @param docDTO the document DTO
     * @return the generated PDF
     */
    @Override
    public File generateDoc(DocDTO docDTO){
        IXDocReport report;
        IContext context;
        File docxFile=null;
        File tmpFile=null;
        String templateName=getTemplateName(docDTO.getTypeName());
        String formattedDateString = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC")).format(formatter);
        createdAt=ZonedDateTime.parse(formattedDateString);

        String docxFileName=docDTO.getTypeName()+"_"+ createdAt +".docx";

        tmpFile=new File(INPUT_DIR+templateName);
        File PDFFile=null;
        try {
            //Load Docx file by filling Velocity template engine and cache it to the registry.
            report= XDocReportRegistry.getRegistry().loadReport(new FileInputStream(tmpFile), TemplateEngineKind.Velocity);
            //Create data model context .
            context = report.createContext();
            context.put("doc",docDTO.getProperties());
            //Configure the behaviour of the null image.
            FieldsMetadata metadata = new FieldsMetadata();
            metadata.addFieldAsImage( "signature", NullImageBehaviour.RemoveImageTemplate);
            report.setFieldsMetadata(metadata);

            byte[] signature=null;

            if(docDTO.isSigned())
                signature= ByteStreams.toByteArray(new FileInputStream(new File(INPUT_DIR+"signature.JPG")));

            IImageProvider sign = new ByteArrayImageProvider(signature);
            //Create signature image context
            context.put("signature", sign);
            docxFile =new File(docxFileName);
            out= new FileOutputStream(docxFile);
            //Generate report by merging contexts with the Docx template.
            report.process(context, out);
            //converting the generated Docx to PDF
            PDFFile=convertDOCXToPDF(docxFile,docDTO.getTypeName());
            //delete the generated Docx file.
            docxFile.delete();
            docDTO.setCreatedAt(createdAt);
            docDTO.setDoc(docDTO.getTypeName()+"_"+createdAt+".pdf");
            docDTO.setTypeId(getTypeId(docDTO.getTypeName()));
            save(docDTO);
        } catch (XDocReportException xdoce) {
            log.error(xdoce.getMessage());
        }  catch (IOException ioe) {
            log.error(ioe.getMessage());
        }
        finally {
            if(docxFile!=null)
                docxFile.delete();
            if(out!=null) {
                try {
                    out.close();
                }
                catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return PDFFile;
    }


    /**
     * Convert the given DOCX file to PDF.
     * @param docxFile the DOCX file to convert
     * @param docType   Document Type ( CV, Attestation de stage... )
     * @return the converted PDF.
     */

    @Override
    public File convertDOCXToPDF(File docxFile, String docType) {
        PdfOptions options = PdfOptions.create();
        File PDFFile=null;
        try {
            //Load docx with POI XWPFDocument
            XWPFDocument document = new XWPFDocument(new FileInputStream(docxFile));
            String PDFPath=OUTPUT_DIR+docType+"_"+ createdAt+".pdf";
            PDFFile=new File(PDFPath);
            out = new FileOutputStream(PDFFile);
            //Convert POI XWPFDocument to PDF
            PdfConverter.getInstance().convert(document,out,options);
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
        }
        return PDFFile;
    }

    /**
     * Get the template document name from the DB.
     * @param typeName the document type name.
     * @return Template document name
     */
    public String getTemplateName(String typeName){
        List<TypeDTO> typesList =typeService.findAll();
        String templateName="";
        for(TypeDTO type : typesList){
            if(typeName.equals(type.getName())) {
                templateName=type.getTemplate();
                break;
            }
        }
        return templateName;
    }
    /**
     * Get the type id from the DB.
     * @param typeName the document type name.
     * @return the document type id
     */
    public long getTypeId(String typeName){
        List<TypeDTO> typesList =typeService.findAll();
        long id=0l;
        for(TypeDTO type : typesList){
            if(typeName.equals(type.getName())) {
                id=type.getId();
                break;
            }
        }
        return id;
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
