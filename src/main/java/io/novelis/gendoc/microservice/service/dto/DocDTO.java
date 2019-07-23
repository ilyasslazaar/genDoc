package io.novelis.gendoc.microservice.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import io.novelis.gendoc.microservice.domain.enumeration.DocTypes;

/**
 * A DTO for the {@link io.novelis.gendoc.microservice.domain.Doc} entity.
 */
public class DocDTO implements Serializable {

    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String doc;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private DocTypes type;


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
