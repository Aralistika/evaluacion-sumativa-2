package com.aiep.evsum2.domain;

import static com.aiep.evsum2.domain.EmpleadosTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aiep.evsum2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmpleadosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Empleados.class);
        Empleados empleados1 = getEmpleadosSample1();
        Empleados empleados2 = new Empleados();
        assertThat(empleados1).isNotEqualTo(empleados2);

        empleados2.setId(empleados1.getId());
        assertThat(empleados1).isEqualTo(empleados2);

        empleados2 = getEmpleadosSample2();
        assertThat(empleados1).isNotEqualTo(empleados2);
    }
}
