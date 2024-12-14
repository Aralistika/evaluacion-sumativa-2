package com.aiep.evsum2.web.rest;

import static com.aiep.evsum2.domain.DepartamentosAsserts.*;
import static com.aiep.evsum2.web.rest.TestUtil.createUpdateProxyForBean;
import static com.aiep.evsum2.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.aiep.evsum2.IntegrationTest;
import com.aiep.evsum2.domain.Departamentos;
import com.aiep.evsum2.repository.DepartamentosRepository;
import com.aiep.evsum2.repository.EntityManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link DepartamentosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DepartamentosResourceIT {

    private static final String DEFAULT_NOMBREDEPARTAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBREDEPARTAMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_UBICACIONDEPARTAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_UBICACIONDEPARTAMENTO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRESUPUESTODEPARTAMENTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRESUPUESTODEPARTAMENTO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/departamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DepartamentosRepository departamentosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Departamentos departamentos;

    private Departamentos insertedDepartamentos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departamentos createEntity() {
        return new Departamentos()
            .nombredepartamento(DEFAULT_NOMBREDEPARTAMENTO)
            .ubicaciondepartamento(DEFAULT_UBICACIONDEPARTAMENTO)
            .presupuestodepartamento(DEFAULT_PRESUPUESTODEPARTAMENTO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departamentos createUpdatedEntity() {
        return new Departamentos()
            .nombredepartamento(UPDATED_NOMBREDEPARTAMENTO)
            .ubicaciondepartamento(UPDATED_UBICACIONDEPARTAMENTO)
            .presupuestodepartamento(UPDATED_PRESUPUESTODEPARTAMENTO);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Departamentos.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        departamentos = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDepartamentos != null) {
            departamentosRepository.delete(insertedDepartamentos).block();
            insertedDepartamentos = null;
        }
        deleteEntities(em);
    }

    @Test
    void createDepartamentos() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Departamentos
        var returnedDepartamentos = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(departamentos))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Departamentos.class)
            .returnResult()
            .getResponseBody();

        // Validate the Departamentos in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDepartamentosUpdatableFieldsEquals(returnedDepartamentos, getPersistedDepartamentos(returnedDepartamentos));

        insertedDepartamentos = returnedDepartamentos;
    }

    @Test
    void createDepartamentosWithExistingId() throws Exception {
        // Create the Departamentos with an existing ID
        departamentos.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(departamentos))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Departamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDepartamentosAsStream() {
        // Initialize the database
        departamentosRepository.save(departamentos).block();

        List<Departamentos> departamentosList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Departamentos.class)
            .getResponseBody()
            .filter(departamentos::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(departamentosList).isNotNull();
        assertThat(departamentosList).hasSize(1);
        Departamentos testDepartamentos = departamentosList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertDepartamentosAllPropertiesEquals(departamentos, testDepartamentos);
        assertDepartamentosUpdatableFieldsEquals(departamentos, testDepartamentos);
    }

    @Test
    void getAllDepartamentos() {
        // Initialize the database
        insertedDepartamentos = departamentosRepository.save(departamentos).block();

        // Get all the departamentosList
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
            .value(hasItem(departamentos.getId().intValue()))
            .jsonPath("$.[*].nombredepartamento")
            .value(hasItem(DEFAULT_NOMBREDEPARTAMENTO))
            .jsonPath("$.[*].ubicaciondepartamento")
            .value(hasItem(DEFAULT_UBICACIONDEPARTAMENTO))
            .jsonPath("$.[*].presupuestodepartamento")
            .value(hasItem(sameNumber(DEFAULT_PRESUPUESTODEPARTAMENTO)));
    }

    @Test
    void getDepartamentos() {
        // Initialize the database
        insertedDepartamentos = departamentosRepository.save(departamentos).block();

        // Get the departamentos
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, departamentos.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(departamentos.getId().intValue()))
            .jsonPath("$.nombredepartamento")
            .value(is(DEFAULT_NOMBREDEPARTAMENTO))
            .jsonPath("$.ubicaciondepartamento")
            .value(is(DEFAULT_UBICACIONDEPARTAMENTO))
            .jsonPath("$.presupuestodepartamento")
            .value(is(sameNumber(DEFAULT_PRESUPUESTODEPARTAMENTO)));
    }

    @Test
    void getNonExistingDepartamentos() {
        // Get the departamentos
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDepartamentos() throws Exception {
        // Initialize the database
        insertedDepartamentos = departamentosRepository.save(departamentos).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departamentos
        Departamentos updatedDepartamentos = departamentosRepository.findById(departamentos.getId()).block();
        updatedDepartamentos
            .nombredepartamento(UPDATED_NOMBREDEPARTAMENTO)
            .ubicaciondepartamento(UPDATED_UBICACIONDEPARTAMENTO)
            .presupuestodepartamento(UPDATED_PRESUPUESTODEPARTAMENTO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDepartamentos.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedDepartamentos))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Departamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDepartamentosToMatchAllProperties(updatedDepartamentos);
    }

    @Test
    void putNonExistingDepartamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentos.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, departamentos.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(departamentos))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Departamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDepartamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(departamentos))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Departamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDepartamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(departamentos))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Departamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDepartamentosWithPatch() throws Exception {
        // Initialize the database
        insertedDepartamentos = departamentosRepository.save(departamentos).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departamentos using partial update
        Departamentos partialUpdatedDepartamentos = new Departamentos();
        partialUpdatedDepartamentos.setId(departamentos.getId());

        partialUpdatedDepartamentos.nombredepartamento(UPDATED_NOMBREDEPARTAMENTO).presupuestodepartamento(UPDATED_PRESUPUESTODEPARTAMENTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDepartamentos.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedDepartamentos))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Departamentos in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartamentosUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDepartamentos, departamentos),
            getPersistedDepartamentos(departamentos)
        );
    }

    @Test
    void fullUpdateDepartamentosWithPatch() throws Exception {
        // Initialize the database
        insertedDepartamentos = departamentosRepository.save(departamentos).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departamentos using partial update
        Departamentos partialUpdatedDepartamentos = new Departamentos();
        partialUpdatedDepartamentos.setId(departamentos.getId());

        partialUpdatedDepartamentos
            .nombredepartamento(UPDATED_NOMBREDEPARTAMENTO)
            .ubicaciondepartamento(UPDATED_UBICACIONDEPARTAMENTO)
            .presupuestodepartamento(UPDATED_PRESUPUESTODEPARTAMENTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDepartamentos.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedDepartamentos))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Departamentos in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartamentosUpdatableFieldsEquals(partialUpdatedDepartamentos, getPersistedDepartamentos(partialUpdatedDepartamentos));
    }

    @Test
    void patchNonExistingDepartamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentos.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, departamentos.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(departamentos))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Departamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDepartamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(departamentos))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Departamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDepartamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(departamentos))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Departamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDepartamentos() {
        // Initialize the database
        insertedDepartamentos = departamentosRepository.save(departamentos).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the departamentos
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, departamentos.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return departamentosRepository.count().block();
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

    protected Departamentos getPersistedDepartamentos(Departamentos departamentos) {
        return departamentosRepository.findById(departamentos.getId()).block();
    }

    protected void assertPersistedDepartamentosToMatchAllProperties(Departamentos expectedDepartamentos) {
        // Test fails because reactive api returns an empty object instead of null
        // assertDepartamentosAllPropertiesEquals(expectedDepartamentos, getPersistedDepartamentos(expectedDepartamentos));
        assertDepartamentosUpdatableFieldsEquals(expectedDepartamentos, getPersistedDepartamentos(expectedDepartamentos));
    }

    protected void assertPersistedDepartamentosToMatchUpdatableProperties(Departamentos expectedDepartamentos) {
        // Test fails because reactive api returns an empty object instead of null
        // assertDepartamentosAllUpdatablePropertiesEquals(expectedDepartamentos, getPersistedDepartamentos(expectedDepartamentos));
        assertDepartamentosUpdatableFieldsEquals(expectedDepartamentos, getPersistedDepartamentos(expectedDepartamentos));
    }
}
