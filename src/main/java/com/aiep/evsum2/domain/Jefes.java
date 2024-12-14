package com.aiep.evsum2.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Jefes.
 */
@Table("jefes")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Jefes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("nombrejefe")
    private String nombrejefe;

    @Column("telefonojefe")
    private String telefonojefe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Jefes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrejefe() {
        return this.nombrejefe;
    }

    public Jefes nombrejefe(String nombrejefe) {
        this.setNombrejefe(nombrejefe);
        return this;
    }

    public void setNombrejefe(String nombrejefe) {
        this.nombrejefe = nombrejefe;
    }

    public String getTelefonojefe() {
        return this.telefonojefe;
    }

    public Jefes telefonojefe(String telefonojefe) {
        this.setTelefonojefe(telefonojefe);
        return this;
    }

    public void setTelefonojefe(String telefonojefe) {
        this.telefonojefe = telefonojefe;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Jefes)) {
            return false;
        }
        return getId() != null && getId().equals(((Jefes) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Jefes{" +
            "id=" + getId() +
            ", nombrejefe='" + getNombrejefe() + "'" +
            ", telefonojefe='" + getTelefonojefe() + "'" +
            "}";
    }
}
