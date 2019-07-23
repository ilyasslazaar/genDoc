package io.novelis.gendoc.microservice.service.dto;
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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.novelis.gendoc.microservice.domain.enumeration.DocTypes;
import io.novelis.gendoc.microservice.service.util.CustomJsonDateDeserializer;

/**
 * A DTO for the {@link io.novelis.gendoc.microservice.domain.Doc} entity.
 */
public class DocDTO<T> implements Serializable {

    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String doc;

    @NotNull
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private ZonedDateTime createdAt;

    @NotNull
    private DocTypes type;

    private Map<String, T> jsonMap;
    private DocDTO<T> docDTO;

    public DocDTO(String json) throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
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

    public DocDTO<T> getDTO() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DocTypes getType() {
        return type;
    }

    public void setType(DocTypes type) {
        this.type = type;
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
            ", email='" + getEmail() + "'" +
            ", doc='" + getDoc() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
