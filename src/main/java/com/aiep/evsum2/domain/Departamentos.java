package com.aiep.evsum2.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Departamentos.
 */
@Table("departamentos")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Departamentos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("nombredepartamento")
    private String nombredepartamento;

    @Column("ubicaciondepartamento")
    private String ubicaciondepartamento;

    @Column("presupuestodepartamento")
    private BigDecimal presupuestodepartamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Departamentos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombredepartamento() {
        return this.nombredepartamento;
    }

    public Departamentos nombredepartamento(String nombredepartamento) {
        this.setNombredepartamento(nombredepartamento);
        return this;
    }

    public void setNombredepartamento(String nombredepartamento) {
        this.nombredepartamento = nombredepartamento;
    }

    public String getUbicaciondepartamento() {
        return this.ubicaciondepartamento;
    }

    public Departamentos ubicaciondepartamento(String ubicaciondepartamento) {
        this.setUbicaciondepartamento(ubicaciondepartamento);
        return this;
    }

    public void setUbicaciondepartamento(String ubicaciondepartamento) {
        this.ubicaciondepartamento = ubicaciondepartamento;
    }

    public BigDecimal getPresupuestodepartamento() {
        return this.presupuestodepartamento;
    }

    public Departamentos presupuestodepartamento(BigDecimal presupuestodepartamento) {
        this.setPresupuestodepartamento(presupuestodepartamento);
        return this;
    }

    public void setPresupuestodepartamento(BigDecimal presupuestodepartamento) {
        this.presupuestodepartamento = presupuestodepartamento != null ? presupuestodepartamento.stripTrailingZeros() : null;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Departamentos)) {
            return false;
        }
        return getId() != null && getId().equals(((Departamentos) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Departamentos{" +
            "id=" + getId() +
            ", nombredepartamento='" + getNombredepartamento() + "'" +
            ", ubicaciondepartamento='" + getUbicaciondepartamento() + "'" +
            ", presupuestodepartamento=" + getPresupuestodepartamento() +
            "}";
    }
}
