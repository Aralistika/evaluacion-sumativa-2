package com.aiep.evsum2.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class JefesAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertJefesAllPropertiesEquals(Jefes expected, Jefes actual) {
        assertJefesAutoGeneratedPropertiesEquals(expected, actual);
        assertJefesAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertJefesAllUpdatablePropertiesEquals(Jefes expected, Jefes actual) {
        assertJefesUpdatableFieldsEquals(expected, actual);
        assertJefesUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertJefesAutoGeneratedPropertiesEquals(Jefes expected, Jefes actual) {
        assertThat(expected)
            .as("Verify Jefes auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertJefesUpdatableFieldsEquals(Jefes expected, Jefes actual) {
        assertThat(expected)
            .as("Verify Jefes relevant properties")
            .satisfies(e -> assertThat(e.getNombrejefe()).as("check nombrejefe").isEqualTo(actual.getNombrejefe()))
            .satisfies(e -> assertThat(e.getTelefonojefe()).as("check telefonojefe").isEqualTo(actual.getTelefonojefe()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertJefesUpdatableRelationshipsEquals(Jefes expected, Jefes actual) {
        // empty method
    }
}
