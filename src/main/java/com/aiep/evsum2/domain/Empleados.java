package com.aiep.evsum2.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Empleados.
 */
@Table("empleados")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Empleados implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("nombreempleado")
    private String nombreempleado;

    @Column("apellidoempleado")
    private String apellidoempleado;

    @Column("telefonoempleado")
    private String telefonoempleado;

    @Column("correoempleado")
    private String correoempleado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empleados id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreempleado() {
        return this.nombreempleado;
    }

    public Empleados nombreempleado(String nombreempleado) {
        this.setNombreempleado(nombreempleado);
        return this;
    }

    public void setNombreempleado(String nombreempleado) {
        this.nombreempleado = nombreempleado;
    }

    public String getApellidoempleado() {
        return this.apellidoempleado;
    }

    public Empleados apellidoempleado(String apellidoempleado) {
        this.setApellidoempleado(apellidoempleado);
        return this;
    }

    public void setApellidoempleado(String apellidoempleado) {
        this.apellidoempleado = apellidoempleado;
    }

    public String getTelefonoempleado() {
        return this.telefonoempleado;
    }

    public Empleados telefonoempleado(String telefonoempleado) {
        this.setTelefonoempleado(telefonoempleado);
        return this;
    }

    public void setTelefonoempleado(String telefonoempleado) {
        this.telefonoempleado = telefonoempleado;
    }

    public String getCorreoempleado() {
        return this.correoempleado;
    }

    public Empleados correoempleado(String correoempleado) {
        this.setCorreoempleado(correoempleado);
        return this;
    }

    public void setCorreoempleado(String correoempleado) {
        this.correoempleado = correoempleado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empleados)) {
            return false;
        }
        return getId() != null && getId().equals(((Empleados) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empleados{" +
            "id=" + getId() +
            ", nombreempleado='" + getNombreempleado() + "'" +
            ", apellidoempleado='" + getApellidoempleado() + "'" +
            ", telefonoempleado='" + getTelefonoempleado() + "'" +
            ", correoempleado='" + getCorreoempleado() + "'" +
            "}";
    }
}
