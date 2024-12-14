package com.aiep.evsum2.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EmpleadosDepartamentosAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadosDepartamentosAllPropertiesEquals(EmpleadosDepartamentos expected, EmpleadosDepartamentos actual) {
        assertEmpleadosDepartamentosAutoGeneratedPropertiesEquals(expected, actual);
        assertEmpleadosDepartamentosAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadosDepartamentosAllUpdatablePropertiesEquals(
        EmpleadosDepartamentos expected,
        EmpleadosDepartamentos actual
    ) {
        assertEmpleadosDepartamentosUpdatableFieldsEquals(expected, actual);
        assertEmpleadosDepartamentosUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadosDepartamentosAutoGeneratedPropertiesEquals(
        EmpleadosDepartamentos expected,
        EmpleadosDepartamentos actual
    ) {
        assertThat(expected)
            .as("Verify EmpleadosDepartamentos auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadosDepartamentosUpdatableFieldsEquals(EmpleadosDepartamentos expected, EmpleadosDepartamentos actual) {
        // empty method

    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmpleadosDepartamentosUpdatableRelationshipsEquals(
        EmpleadosDepartamentos expected,
        EmpleadosDepartamentos actual
    ) {
        assertThat(expected)
            .as("Verify EmpleadosDepartamentos relationships")
            .satisfies(e -> assertThat(e.getEmpleados()).as("check empleados").isEqualTo(actual.getEmpleados()))
            .satisfies(e -> assertThat(e.getDepartamentos()).as("check departamentos").isEqualTo(actual.getDepartamentos()));
    }
}