package com.aiep.evsum2.domain;

import static com.aiep.evsum2.domain.DepartamentosTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aiep.evsum2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepartamentosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Departamentos.class);
        Departamentos departamentos1 = getDepartamentosSample1();
        Departamentos departamentos2 = new Departamentos();
        assertThat(departamentos1).isNotEqualTo(departamentos2);

        departamentos2.setId(departamentos1.getId());
        assertThat(departamentos1).isEqualTo(departamentos2);

        departamentos2 = getDepartamentosSample2();
        assertThat(departamentos1).isNotEqualTo(departamentos2);
    }
}
