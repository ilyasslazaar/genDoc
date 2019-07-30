package io.novelis.gendoc.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import io.novelis.gendoc.domain.enumeration.DocTypes;

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
    @Column(name = "doc", nullable = false)
    private String doc;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DocTypes type;

    @NotNull
    @Column(name = "signed", nullable = false)
    private Boolean signed;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean isSigned() {
        return signed;
    }

    public Doc signed(Boolean signed) {
        this.signed = signed;
        return this;
    }

    public void setSigned(Boolean signed) {
        this.signed = signed;
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
            ", doc='" + getDoc() + "'" +
            ", type='" + getType() + "'" +
            ", signed='" + isSigned() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}