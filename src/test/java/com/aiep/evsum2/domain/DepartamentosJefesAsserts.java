package com.aiep.evsum2.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class DepartamentosJefesAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDepartamentosJefesAllPropertiesEquals(DepartamentosJefes expected, DepartamentosJefes actual) {
        assertDepartamentosJefesAutoGeneratedPropertiesEquals(expected, actual);
        assertDepartamentosJefesAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDepartamentosJefesAllUpdatablePropertiesEquals(DepartamentosJefes expected, DepartamentosJefes actual) {
        assertDepartamentosJefesUpdatableFieldsEquals(expected, actual);
        assertDepartamentosJefesUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDepartamentosJefesAutoGeneratedPropertiesEquals(DepartamentosJefes expected, DepartamentosJefes actual) {
        assertThat(expected)
            .as("Verify DepartamentosJefes auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDepartamentosJefesUpdatableFieldsEquals(DepartamentosJefes expected, DepartamentosJefes actual) {
        // empty method

    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDepartamentosJefesUpdatableRelationshipsEquals(DepartamentosJefes expected, DepartamentosJefes actual) {
        assertThat(expected)
            .as("Verify DepartamentosJefes relationships")
            .satisfies(e -> assertThat(e.getDepartamentos()).as("check departamentos").isEqualTo(actual.getDepartamentos()))
            .satisfies(e -> assertThat(e.getJefes()).as("check jefes").isEqualTo(actual.getJefes()));
    }
}
