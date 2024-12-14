package com.aiep.evsum2.web.rest;

import static com.aiep.evsum2.domain.EmpleadosDepartamentosAsserts.*;
import static com.aiep.evsum2.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.aiep.evsum2.IntegrationTest;
import com.aiep.evsum2.domain.EmpleadosDepartamentos;
import com.aiep.evsum2.repository.EmpleadosDepartamentosRepository;
import com.aiep.evsum2.repository.EntityManager;
import com.aiep.evsum2.service.EmpleadosDepartamentosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link EmpleadosDepartamentosResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EmpleadosDepartamentosResourceIT {

    private static final String ENTITY_API_URL = "/api/empleados-departamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmpleadosDepartamentosRepository empleadosDepartamentosRepository;

    @Mock
    private EmpleadosDepartamentosRepository empleadosDepartamentosRepositoryMock;

    @Mock
    private EmpleadosDepartamentosService empleadosDepartamentosServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private EmpleadosDepartamentos empleadosDepartamentos;

    private EmpleadosDepartamentos insertedEmpleadosDepartamentos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmpleadosDepartamentos createEntity() {
        return new EmpleadosDepartamentos();
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmpleadosDepartamentos createUpdatedEntity() {
        return new EmpleadosDepartamentos();
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(EmpleadosDepartamentos.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        empleadosDepartamentos = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmpleadosDepartamentos != null) {
            empleadosDepartamentosRepository.delete(insertedEmpleadosDepartamentos).block();
            insertedEmpleadosDepartamentos = null;
        }
        deleteEntities(em);
    }

    @Test
    void createEmpleadosDepartamentos() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmpleadosDepartamentos
        var returnedEmpleadosDepartamentos = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(empleadosDepartamentos))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(EmpleadosDepartamentos.class)
            .returnResult()
            .getResponseBody();

        // Validate the EmpleadosDepartamentos in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmpleadosDepartamentosUpdatableFieldsEquals(
            returnedEmpleadosDepartamentos,
            getPersistedEmpleadosDepartamentos(returnedEmpleadosDepartamentos)
        );

        insertedEmpleadosDepartamentos = returnedEmpleadosDepartamentos;
    }

    @Test
    void createEmpleadosDepartamentosWithExistingId() throws Exception {
        // Create the EmpleadosDepartamentos with an existing ID
        empleadosDepartamentos.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(empleadosDepartamentos))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmpleadosDepartamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEmpleadosDepartamentosAsStream() {
        // Initialize the database
        empleadosDepartamentosRepository.save(empleadosDepartamentos).block();

        List<EmpleadosDepartamentos> empleadosDepartamentosList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(EmpleadosDepartamentos.class)
            .getResponseBody()
            .filter(empleadosDepartamentos::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(empleadosDepartamentosList).isNotNull();
        assertThat(empleadosDepartamentosList).hasSize(1);
        EmpleadosDepartamentos testEmpleadosDepartamentos = empleadosDepartamentosList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertEmpleadosDepartamentosAllPropertiesEquals(empleadosDepartamentos, testEmpleadosDepartamentos);
        assertEmpleadosDepartamentosUpdatableFieldsEquals(empleadosDepartamentos, testEmpleadosDepartamentos);
    }

    @Test
    void getAllEmpleadosDepartamentos() {
        // Initialize the database
        insertedEmpleadosDepartamentos = empleadosDepartamentosRepository.save(empleadosDepartamentos).block();

        // Get all the empleadosDepartamentosList
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
            .value(hasItem(empleadosDepartamentos.getId().intValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmpleadosDepartamentosWithEagerRelationshipsIsEnabled() {
        when(empleadosDepartamentosServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(empleadosDepartamentosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmpleadosDepartamentosWithEagerRelationshipsIsNotEnabled() {
        when(empleadosDepartamentosServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(empleadosDepartamentosRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getEmpleadosDepartamentos() {
        // Initialize the database
        insertedEmpleadosDepartamentos = empleadosDepartamentosRepository.save(empleadosDepartamentos).block();

        // Get the empleadosDepartamentos
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, empleadosDepartamentos.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(empleadosDepartamentos.getId().intValue()));
    }

    @Test
    void getNonExistingEmpleadosDepartamentos() {
        // Get the empleadosDepartamentos
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEmpleadosDepartamentos() throws Exception {
        // Initialize the database
        insertedEmpleadosDepartamentos = empleadosDepartamentosRepository.save(empleadosDepartamentos).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleadosDepartamentos
        EmpleadosDepartamentos updatedEmpleadosDepartamentos = empleadosDepartamentosRepository
            .findById(empleadosDepartamentos.getId())
            .block();

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedEmpleadosDepartamentos.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedEmpleadosDepartamentos))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmpleadosDepartamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmpleadosDepartamentosToMatchAllProperties(updatedEmpleadosDepartamentos);
    }

    @Test
    void putNonExistingEmpleadosDepartamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadosDepartamentos.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, empleadosDepartamentos.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(empleadosDepartamentos))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmpleadosDepartamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEmpleadosDepartamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadosDepartamentos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(empleadosDepartamentos))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmpleadosDepartamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEmpleadosDepartamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadosDepartamentos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(empleadosDepartamentos))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EmpleadosDepartamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEmpleadosDepartamentosWithPatch() throws Exception {
        // Initialize the database
        insertedEmpleadosDepartamentos = empleadosDepartamentosRepository.save(empleadosDepartamentos).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleadosDepartamentos using partial update
        EmpleadosDepartamentos partialUpdatedEmpleadosDepartamentos = new EmpleadosDepartamentos();
        partialUpdatedEmpleadosDepartamentos.setId(empleadosDepartamentos.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmpleadosDepartamentos.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEmpleadosDepartamentos))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmpleadosDepartamentos in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpleadosDepartamentosUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmpleadosDepartamentos, empleadosDepartamentos),
            getPersistedEmpleadosDepartamentos(empleadosDepartamentos)
        );
    }

    @Test
    void fullUpdateEmpleadosDepartamentosWithPatch() throws Exception {
        // Initialize the database
        insertedEmpleadosDepartamentos = empleadosDepartamentosRepository.save(empleadosDepartamentos).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empleadosDepartamentos using partial update
        EmpleadosDepartamentos partialUpdatedEmpleadosDepartamentos = new EmpleadosDepartamentos();
        partialUpdatedEmpleadosDepartamentos.setId(empleadosDepartamentos.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmpleadosDepartamentos.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEmpleadosDepartamentos))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmpleadosDepartamentos in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpleadosDepartamentosUpdatableFieldsEquals(
            partialUpdatedEmpleadosDepartamentos,
            getPersistedEmpleadosDepartamentos(partialUpdatedEmpleadosDepartamentos)
        );
    }

    @Test
    void patchNonExistingEmpleadosDepartamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadosDepartamentos.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, empleadosDepartamentos.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(empleadosDepartamentos))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmpleadosDepartamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEmpleadosDepartamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadosDepartamentos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(empleadosDepartamentos))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmpleadosDepartamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEmpleadosDepartamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empleadosDepartamentos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(empleadosDepartamentos))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EmpleadosDepartamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEmpleadosDepartamentos() {
        // Initialize the database
        insertedEmpleadosDepartamentos = empleadosDepartamentosRepository.save(empleadosDepartamentos).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the empleadosDepartamentos
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, empleadosDepartamentos.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return empleadosDepartamentosRepository.count().block();
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

    protected EmpleadosDepartamentos getPersistedEmpleadosDepartamentos(EmpleadosDepartamentos empleadosDepartamentos) {
        return empleadosDepartamentosRepository.findById(empleadosDepartamentos.getId()).block();
    }

    protected void assertPersistedEmpleadosDepartamentosToMatchAllProperties(EmpleadosDepartamentos expectedEmpleadosDepartamentos) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEmpleadosDepartamentosAllPropertiesEquals(expectedEmpleadosDepartamentos, getPersistedEmpleadosDepartamentos(expectedEmpleadosDepartamentos));
        assertEmpleadosDepartamentosUpdatableFieldsEquals(
            expectedEmpleadosDepartamentos,
            getPersistedEmpleadosDepartamentos(expectedEmpleadosDepartamentos)
        );
    }

    protected void assertPersistedEmpleadosDepartamentosToMatchUpdatableProperties(EmpleadosDepartamentos expectedEmpleadosDepartamentos) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEmpleadosDepartamentosAllUpdatablePropertiesEquals(expectedEmpleadosDepartamentos, getPersistedEmpleadosDepartamentos(expectedEmpleadosDepartamentos));
        assertEmpleadosDepartamentosUpdatableFieldsEquals(
            expectedEmpleadosDepartamentos,
            getPersistedEmpleadosDepartamentos(expectedEmpleadosDepartamentos)
        );
    }
}
