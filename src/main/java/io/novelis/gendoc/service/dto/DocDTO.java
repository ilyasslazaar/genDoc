package io.novelis.gendoc.service.dto;
import java.io.IOException;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.novelis.gendoc.domain.enumeration.DocTypes;
import org.apache.poi.ss.formula.functions.T;

/**
 * A DTO for the {@link io.novelis.gendoc.domain.Doc} entity.
 */
public class DocDTO<T> implements Serializable {

    private Long id;

    @NotNull
    private String doc;

    @NotNull
    private DocTypes type;

    @NotNull
    private Boolean signed;

    @NotNull
    private ZonedDateTime createdAt;

    private ObjectMapper objectMapper;

    private Map<String, T> jsonMap;
    private DocDTO docDTO;

    public DocDTO(String json) throws IOException {
        objectMapper=new ObjectMapper();
        docDTO=objectMapper.readValue(json,DocDTO.class);
    }
    public DocDTO(){
        jsonMap=new HashMap<>();
    }

    @JsonAnySetter
    public void setProperties(String key, T value) {
        jsonMap.put(key, value);
    }
    @JsonAnyGetter
    public Map<String, T> getProperties() {
        return jsonMap;
    }
    public DocDTO getDTO() {
        return docDTO;
    }

    public void seDTO(DocDTO<T> docDTO) {
        this.docDTO = docDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }


    public DocTypes getType() {
        return type;
    }

    public void setType(DocTypes type) {
        this.type = type;
    }

    public Boolean isSigned() {
        return signed;
    }

    public void setSigned(Boolean signed) {
        this.signed = signed;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DocDTO docDTO = (DocDTO) o;
        if (docDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), docDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DocDTO{" +
            "id=" + getId() +
            ", doc='" + getDoc() + "'" +
            ", type='" + getType() + "'" +
            ", signed='" + isSigned() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
