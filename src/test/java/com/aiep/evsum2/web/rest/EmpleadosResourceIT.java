package com.aiep.evsum2.web.rest;

import static com.aiep.evsum2.domain.EmpleadosAsserts.*;
import static com.aiep.evsum2.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.aiep.evsum2.IntegrationTest;
import com.aiep.evsum2.domain.Empleados;
import com.aiep.evsum2.repository.EmpleadosRepository;
import com.aiep.evsum2.repository.EntityManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link EmpleadosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EmpleadosResourceIT {

    private static final String DEFAULT_NOMBREEMPLEADO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBREEMPLEADO = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOEMPLEADO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOEMPLEADO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONOEMPLEADO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONOEMPLEADO = "BBBBBBBBBB";

    private static final String DEFAULT_CORREOEMPLEADO = "AAAAAAAAAA";
    private static final String UPDATED_CORREOEMPLEADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/empleados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmpleadosRepository empleadosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Empleados empleados;

    private Empleados insertedEmpleados;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empleados createEntity() {
        return new Empleados()
            .nombreempleado(DEFAULT_NOMBREEMPLEADO)
            .apellidoempleado(DEFAULT_APELLIDOEMPLEADO)
            .telefonoempleado(DEFAULT_TELEFONOEMPLEADO)
            .correoempleado(DEFAULT_CORREOEMPLEADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empleados createUpdatedEntity() {
        return new Empleados()
            .nombreempleado(UPDATED_NOMBREEMPLEADO)
            .apellidoempleado(UPDATED_APELLIDOEMPLEADO)
            .telefonoempleado(UPDATED_TELEFONOEMPLEADO)
            .correoempleado(UPDATED_CORREOEMPLEADO);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Empleados.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        empleados = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmpleados != null) {
            empleadosRepository.delete(insertedEmpleados).block();
            insertedEmpleados = null;
        }
        deleteEntities(em);
    }

    @Test
    void createEmpleados() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Empleados
        var returnedEmpleados = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(empleados))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Empleados.class)
            .returnResult()
            .getResponseBody();

        // Validate the Empleados in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmpleadosUpdatableFieldsEquals(returnedEmpleados, getPersistedEmpleados(returnedEmpleados));

        insertedEmpleados = returnedEmpleados;
    }

    @Test
    void createEmpleadosWithExistingId() throws Exception {
        // Create the Empleados with an existing ID
        empleados.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(empleados))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empleados in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEmpleadosAsStream() {
        // Initialize the database
        empleadosRepository.save(empleados).block();

        List<Empleados> empleadosList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Empleados.class)
            .getResponseBody()
            .filter(empleados::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(empleadosList).isNotNull();
        assertThat(empleadosList).hasSize(1);
        Empleados testEmpleados = empleadosList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertEmpleadosAllPropertiesEquals(empleados, testEmpleados);
        assertEmpleadosUpdatableFieldsEquals(empleados, testEmpleados);
    }

    @Test
    void getAllEmpleados() {
        // Initialize the database
        insertedEmpleados = empleadosRepository.save(empleados).block();

        // Get all the empleadosList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(empleados.getId().intValue()))
            .jsonPath("$.[*].nombreempleado")
            .value(hasItem(DEFAULT_NOMBREEMPLEADO))
            .jsonPath("$.[*].apellidoempleado")
            .value(hasItem(DEFAULT_APELLIDOEMPLEADO))
            .jsonPath("$.[*].telefonoempleado")
            .value(hasItem(DEFAULT_TELEFONOEMPLEADO))
            .jsonPath("$.[*].correoempleado")
            .value(hasItem(DEFAULT_CORREOEMPLEADO));
    }

    @Test
    void getEmpleados() {
        // Initialize the database
        insertedEmpleados = empleadosRepository.save(empleados).block();

        // Get the empleados
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, empleados.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(empleados.getId().intValue()))
            .jsonPath("$.nombreempleado")
            .value(is(DEFAULT_NOMBREEMPLEADO))
            .jsonPath("$.apellidoempleado")
            .value(is(DEFAULT_APELLIDOEMPLEADO))
            .jsonPath("$.telefonoempleado")
            .value(is(DEFAULT_TELEFONOEMPLEADO))
            .jsonPath("$.correoempleado")
            .value(is(DEFAULT_CORREOEMPLEADO));
    }

    @Test
    void getNonExistingEmpleados() {
        // Get the empleados
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEmpleados() throws Exception {
        // Initialize the database
        insertedEmpleados = empleadosRepository.save(empleados).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleados
        Empleados updatedEmpleados = empleadosRepository.findById(empleados.getId()).block();
        updatedEmpleados
            .nombreempleado(UPDATED_NOMBREEMPLEADO)
            .apellidoempleado(UPDATED_APELLIDOEMPLEADO)
            .telefonoempleado(UPDATED_TELEFONOEMPLEADO)
            .correoempleado(UPDATED_CORREOEMPLEADO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedEmpleados.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedEmpleados))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Empleados in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmpleadosToMatchAllProperties(updatedEmpleados);
    }

    @Test
    void putNonExistingEmpleados() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleados.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, empleados.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(empleados))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empleados in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEmpleados() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleados.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(empleados))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empleados in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEmpleados() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleados.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(empleados))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Empleados in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEmpleadosWithPatch() throws Exception {
        // Initialize the database
        insertedEmpleados = empleadosRepository.save(empleados).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleados using partial update
        Empleados partialUpdatedEmpleados = new Empleados();
        partialUpdatedEmpleados.setId(empleados.getId());

        partialUpdatedEmpleados.nombreempleado(UPDATED_NOMBREEMPLEADO).correoempleado(UPDATED_CORREOEMPLEADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmpleados.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEmpleados))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Empleados in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpleadosUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmpleados, empleados),
            getPersistedEmpleados(empleados)
        );
    }

    @Test
    void fullUpdateEmpleadosWithPatch() throws Exception {
        // Initialize the database
        insertedEmpleados = empleadosRepository.save(empleados).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleados using partial update
        Empleados partialUpdatedEmpleados = new Empleados();
        partialUpdatedEmpleados.setId(empleados.getId());

        partialUpdatedEmpleados
            .nombreempleado(UPDATED_NOMBREEMPLEADO)
            .apellidoempleado(UPDATED_APELLIDOEMPLEADO)
            .telefonoempleado(UPDATED_TELEFONOEMPLEADO)
            .correoempleado(UPDATED_CORREOEMPLEADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmpleados.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEmpleados))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Empleados in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpleadosUpdatableFieldsEquals(partialUpdatedEmpleados, getPersistedEmpleados(partialUpdatedEmpleados));
    }

    @Test
    void patchNonExistingEmpleados() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleados.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, empleados.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(empleados))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empleados in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEmpleados() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleados.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(empleados))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Empleados in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEmpleados() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleados.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(empleados))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Empleados in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEmpleados() {
        // Initialize the database
        insertedEmpleados = empleadosRepository.save(empleados).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the empleados
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, empleados.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return empleadosRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Empleados getPersistedEmpleados(Empleados empleados) {
        return empleadosRepository.findById(empleados.getId()).block();
    }

    protected void assertPersistedEmpleadosToMatchAllProperties(Empleados expectedEmpleados) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEmpleadosAllPropertiesEquals(expectedEmpleados, getPersistedEmpleados(expectedEmpleados));
        assertEmpleadosUpdatableFieldsEquals(expectedEmpleados, getPersistedEmpleados(expectedEmpleados));
    }

    protected void assertPersistedEmpleadosToMatchUpdatableProperties(Empleados expectedEmpleados) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEmpleadosAllUpdatablePropertiesEquals(expectedEmpleados, getPersistedEmpleados(expectedEmpleados));
        assertEmpleadosUpdatableFieldsEquals(expectedEmpleados, getPersistedEmpleados(expectedEmpleados));
    }
}
