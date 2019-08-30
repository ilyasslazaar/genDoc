package io.novelis.gendoc.service.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.novelis.gendoc.domain.Type} entity.
 */
public class TypeDTO implements Serializable {

    private Long id;

    private ObjectMapper objectMapper;

    private TypeDTO typeDTO;

    @NotNull
    private String name;

    @NotNull
    private String template;

    public TypeDTO(String json) throws IOException {
        objectMapper = new ObjectMapper();
        typeDTO = objectMapper.readValue(json, TypeDTO.class);
    }

    public TypeDTO getDTO() {
        return typeDTO;
    }

    public TypeDTO() {
    }

    /* getters & setters */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TypeDTO typeDTO = (TypeDTO) o;
        if (typeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), typeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", template='" + getTemplate() + "'" +
            "}";
    }
}
