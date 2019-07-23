package io.novelis.gendoc.microservice.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import io.novelis.gendoc.microservice.domain.enumeration.DocTypes;

/**
 * A Doc.
 */
@Entity
@Table(name = "doc")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Doc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "doc", nullable = false)
    private String doc;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DocTypes type;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public Doc email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDoc() {
        return doc;
    }

    public Doc doc(String doc) {
        this.doc = doc;
        return this;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Doc createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DocTypes getType() {
        return type;
    }

    public Doc type(DocTypes type) {
        this.type = type;
        return this;
    }

    public void setType(DocTypes type) {
        this.type = type;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Doc)) {
            return false;
        }
        return id != null && id.equals(((Doc) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Doc{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", doc='" + getDoc() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
