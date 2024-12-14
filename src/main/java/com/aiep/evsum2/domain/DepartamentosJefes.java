package com.aiep.evsum2.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DepartamentosJefes.
 */
@Table("departamentos_jefes")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepartamentosJefes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @org.springframework.data.annotation.Transient
    private Departamentos departamentos;

    @org.springframework.data.annotation.Transient
    private Jefes jefes;

    @Column("departamentos_id")
    private Long departamentosId;

    @Column("jefes_id")
    private Long jefesId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DepartamentosJefes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Departamentos getDepartamentos() {
        return this.departamentos;
    }

    public void setDepartamentos(Departamentos departamentos) {
        this.departamentos = departamentos;
        this.departamentosId = departamentos != null ? departamentos.getId() : null;
    }

    public DepartamentosJefes departamentos(Departamentos departamentos) {
        this.setDepartamentos(departamentos);
        return this;
    }

    public Jefes getJefes() {
        return this.jefes;
    }

    public void setJefes(Jefes jefes) {
        this.jefes = jefes;
        this.jefesId = jefes != null ? jefes.getId() : null;
    }

    public DepartamentosJefes jefes(Jefes jefes) {
        this.setJefes(jefes);
        return this;
    }

    public Long getDepartamentosId() {
        return this.departamentosId;
    }

    public void setDepartamentosId(Long departamentos) {
        this.departamentosId = departamentos;
    }

    public Long getJefesId() {
        return this.jefesId;
    }

    public void setJefesId(Long jefes) {
        this.jefesId = jefes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartamentosJefes)) {
            return false;
        }
        return getId() != null && getId().equals(((DepartamentosJefes) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartamentosJefes{" +
            "id=" + getId() +
            "}";
    }
}
