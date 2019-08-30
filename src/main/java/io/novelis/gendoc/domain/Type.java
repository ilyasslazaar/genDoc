package io.novelis.gendoc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Type.
 */
@Entity
@Table(name = "type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Type implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "template", nullable = false)
    private String template;

    @OneToMany(mappedBy = "type")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Doc> docs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Type name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public Type template(String template) {
        this.template = template;
        return this;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Set<Doc> getDocs() {
        return docs;
    }

    public Type docs(Set<Doc> docs) {
        this.docs = docs;
        return this;
    }

    public Type addDoc(Doc doc) {
        this.docs.add(doc);
        doc.setType(this);
        return this;
    }

    public Type removeDoc(Doc doc) {
        this.docs.remove(doc);
        doc.setType(null);
        return this;
    }

    public void setDocs(Set<Doc> docs) {
        this.docs = docs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Type)) {
            return false;
        }
        return id != null && id.equals(((Type) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Type{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", template='" + getTemplate() + "'" +
            "}";
    }
}
