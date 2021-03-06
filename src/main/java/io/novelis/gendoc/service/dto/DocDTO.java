package io.novelis.gendoc.service.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A DTO for the {@link io.novelis.gendoc.domain.Doc} entity.
 */
public class DocDTO<T> implements Serializable {

    private Long id;

    @NotNull
    private String doc;

    @NotNull
    private Boolean signed;

    @NotNull
    private ZonedDateTime createdAt;

    private Long typeId;

    private String typeName;

    private ObjectMapper objectMapper;

    private Map<String, T> jsonMap;

    private DocDTO docDTO;

    public DocDTO(String json) throws IOException {
        objectMapper = new ObjectMapper();
        docDTO = objectMapper.readValue(json, DocDTO.class);
    }

    public DocDTO() {
        jsonMap = new HashMap<>();
    }

    /* getters & setters */

    @JsonAnyGetter
    public Map<String, T> getProperties() {
        return jsonMap;
    }
    @JsonAnySetter
    public void setProperties(String key, T value) {
        jsonMap.put(key, value);}
    public DocDTO getDTO() {
        return docDTO;
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

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
            ", signed='" + isSigned() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", type=" + getTypeId() +
            ", type='" + getTypeName() + "'" +
            "}";
    }
}
