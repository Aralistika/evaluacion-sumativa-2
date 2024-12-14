package com.aiep.evsum2.domain;

import static com.aiep.evsum2.domain.DepartamentosJefesTestSamples.*;
import static com.aiep.evsum2.domain.DepartamentosTestSamples.*;
import static com.aiep.evsum2.domain.JefesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aiep.evsum2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepartamentosJefesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepartamentosJefes.class);
        DepartamentosJefes departamentosJefes1 = getDepartamentosJefesSample1();
        DepartamentosJefes departamentosJefes2 = new DepartamentosJefes();
        assertThat(departamentosJefes1).isNotEqualTo(departamentosJefes2);

        departamentosJefes2.setId(departamentosJefes1.getId());
        assertThat(departamentosJefes1).isEqualTo(departamentosJefes2);

        departamentosJefes2 = getDepartamentosJefesSample2();
        assertThat(departamentosJefes1).isNotEqualTo(departamentosJefes2);
    }

    @Test
    void departamentosTest() {
        DepartamentosJefes departamentosJefes = getDepartamentosJefesRandomSampleGenerator();
        Departamentos departamentosBack = getDepartamentosRandomSampleGenerator();

        departamentosJefes.setDepartamentos(departamentosBack);
        assertThat(departamentosJefes.getDepartamentos()).isEqualTo(departamentosBack);

        departamentosJefes.departamentos(null);
        assertThat(departamentosJefes.getDepartamentos()).isNull();
    }

    @Test
    void jefesTest() {
        DepartamentosJefes departamentosJefes = getDepartamentosJefesRandomSampleGenerator();
        Jefes jefesBack = getJefesRandomSampleGenerator();

        departamentosJefes.setJefes(jefesBack);
        assertThat(departamentosJefes.getJefes()).isEqualTo(jefesBack);

        departamentosJefes.jefes(null);
        assertThat(departamentosJefes.getJefes()).isNull();
    }
}
