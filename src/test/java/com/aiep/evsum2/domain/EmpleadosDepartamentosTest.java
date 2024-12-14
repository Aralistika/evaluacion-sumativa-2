package com.aiep.evsum2.domain;

import static com.aiep.evsum2.domain.DepartamentosTestSamples.*;
import static com.aiep.evsum2.domain.EmpleadosDepartamentosTestSamples.*;
import static com.aiep.evsum2.domain.EmpleadosTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aiep.evsum2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmpleadosDepartamentosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmpleadosDepartamentos.class);
        EmpleadosDepartamentos empleadosDepartamentos1 = getEmpleadosDepartamentosSample1();
        EmpleadosDepartamentos empleadosDepartamentos2 = new EmpleadosDepartamentos();
        assertThat(empleadosDepartamentos1).isNotEqualTo(empleadosDepartamentos2);

        empleadosDepartamentos2.setId(empleadosDepartamentos1.getId());
        assertThat(empleadosDepartamentos1).isEqualTo(empleadosDepartamentos2);

        empleadosDepartamentos2 = getEmpleadosDepartamentosSample2();
        assertThat(empleadosDepartamentos1).isNotEqualTo(empleadosDepartamentos2);
    }

    @Test
    void empleadosTest() {
        EmpleadosDepartamentos empleadosDepartamentos = getEmpleadosDepartamentosRandomSampleGenerator();
        Empleados empleadosBack = getEmpleadosRandomSampleGenerator();

        empleadosDepartamentos.setEmpleados(empleadosBack);
        assertThat(empleadosDepartamentos.getEmpleados()).isEqualTo(empleadosBack);

        empleadosDepartamentos.empleados(null);
        assertThat(empleadosDepartamentos.getEmpleados()).isNull();
    }

    @Test
    void departamentosTest() {
        EmpleadosDepartamentos empleadosDepartamentos = getEmpleadosDepartamentosRandomSampleGenerator();
        Departamentos departamentosBack = getDepartamentosRandomSampleGenerator();

        empleadosDepartamentos.setDepartamentos(departamentosBack);
        assertThat(empleadosDepartamentos.getDepartamentos()).isEqualTo(departamentosBack);

        empleadosDepartamentos.departamentos(null);
        assertThat(empleadosDepartamentos.getDepartamentos()).isNull();
    }
}
