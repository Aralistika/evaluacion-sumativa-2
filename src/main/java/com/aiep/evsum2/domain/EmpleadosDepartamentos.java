package com.aiep.evsum2.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A EmpleadosDepartamentos.
 */
@Table("empleados_departamentos")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmpleadosDepartamentos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @org.springframework.data.annotation.Transient
    private Empleados empleados;

    @org.springframework.data.annotation.Transient
    private Departamentos departamentos;

    @Column("empleados_id")
    private Long empleadosId;

    @Column("departamentos_id")
    private Long departamentosId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmpleadosDepartamentos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empleados getEmpleados() {
        return this.empleados;
    }

    public void setEmpleados(Empleados empleados) {
        this.empleados = empleados;
        this.empleadosId = empleados != null ? empleados.getId() : null;
    }

    public EmpleadosDepartamentos empleados(Empleados empleados) {
        this.setEmpleados(empleados);
        return this;
    }

    public Departamentos getDepartamentos() {
        return this.departamentos;
    }

    public void setDepartamentos(Departamentos departamentos) {
        this.departamentos = departamentos;
        this.departamentosId = departamentos != null ? departamentos.getId() : null;
    }

    public EmpleadosDepartamentos departamentos(Departamentos departamentos) {
        this.setDepartamentos(departamentos);
        return this;
    }

    public Long getEmpleadosId() {
        return this.empleadosId;
    }

    public void setEmpleadosId(Long empleados) {
        this.empleadosId = empleados;
    }

    public Long getDepartamentosId() {
        return this.departamentosId;
    }

    public void setDepartamentosId(Long departamentos) {
        this.departamentosId = departamentos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmpleadosDepartamentos)) {
            return false;
        }
        return getId() != null && getId().equals(((EmpleadosDepartamentos) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpleadosDepartamentos{" +
            "id=" + getId() +
            "}";
    }
}
