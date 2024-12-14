package com.aiep.evsum2.web.rest;

import static com.aiep.evsum2.domain.DepartamentosJefesAsserts.*;
import static com.aiep.evsum2.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.aiep.evsum2.IntegrationTest;
import com.aiep.evsum2.domain.DepartamentosJefes;
import com.aiep.evsum2.repository.DepartamentosJefesRepository;
import com.aiep.evsum2.repository.EntityManager;
import com.aiep.evsum2.service.DepartamentosJefesService;
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
 * Integration tests for the {@link DepartamentosJefesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DepartamentosJefesResourceIT {

    private static final String ENTITY_API_URL = "/api/departamentos-jefes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DepartamentosJefesRepository departamentosJefesRepository;

    @Mock
    private DepartamentosJefesRepository departamentosJefesRepositoryMock;

    @Mock
    private DepartamentosJefesService departamentosJefesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private DepartamentosJefes departamentosJefes;

    private DepartamentosJefes insertedDepartamentosJefes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartamentosJefes createEntity() {
        return new DepartamentosJefes();
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartamentosJefes createUpdatedEntity() {
        return new DepartamentosJefes();
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(DepartamentosJefes.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        departamentosJefes = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDepartamentosJefes != null) {
            departamentosJefesRepository.delete(insertedDepartamentosJefes).block();
            insertedDepartamentosJefes = null;
        }
        deleteEntities(em);
    }

    @Test
    void createDepartamentosJefes() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DepartamentosJefes
        var returnedDepartamentosJefes = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(departamentosJefes))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(DepartamentosJefes.class)
            .returnResult()
            .getResponseBody();

        // Validate the DepartamentosJefes in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDepartamentosJefesUpdatableFieldsEquals(
            returnedDepartamentosJefes,
            getPersistedDepartamentosJefes(returnedDepartamentosJefes)
        );

        insertedDepartamentosJefes = returnedDepartamentosJefes;
    }

    @Test
    void createDepartamentosJefesWithExistingId() throws Exception {
        // Create the DepartamentosJefes with an existing ID
        departamentosJefes.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(departamentosJefes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DepartamentosJefes in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDepartamentosJefesAsStream() {
        // Initialize the database
        departamentosJefesRepository.save(departamentosJefes).block();

        List<DepartamentosJefes> departamentosJefesList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DepartamentosJefes.class)
            .getResponseBody()
            .filter(departamentosJefes::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(departamentosJefesList).isNotNull();
        assertThat(departamentosJefesList).hasSize(1);
        DepartamentosJefes testDepartamentosJefes = departamentosJefesList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertDepartamentosJefesAllPropertiesEquals(departamentosJefes, testDepartamentosJefes);
        assertDepartamentosJefesUpdatableFieldsEquals(departamentosJefes, testDepartamentosJefes);
    }

    @Test
    void getAllDepartamentosJefes() {
        // Initialize the database
        insertedDepartamentosJefes = departamentosJefesRepository.save(departamentosJefes).block();

        // Get all the departamentosJefesList
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
            .value(hasItem(departamentosJefes.getId().intValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepartamentosJefesWithEagerRelationshipsIsEnabled() {
        when(departamentosJefesServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(departamentosJefesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepartamentosJefesWithEagerRelationshipsIsNotEnabled() {
        when(departamentosJefesServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(departamentosJefesRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getDepartamentosJefes() {
        // Initialize the database
        insertedDepartamentosJefes = departamentosJefesRepository.save(departamentosJefes).block();

        // Get the departamentosJefes
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, departamentosJefes.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(departamentosJefes.getId().intValue()));
    }

    @Test
    void getNonExistingDepartamentosJefes() {
        // Get the departamentosJefes
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDepartamentosJefes() throws Exception {
        // Initialize the database
        insertedDepartamentosJefes = departamentosJefesRepository.save(departamentosJefes).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departamentosJefes
        DepartamentosJefes updatedDepartamentosJefes = departamentosJefesRepository.findById(departamentosJefes.getId()).block();

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDepartamentosJefes.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedDepartamentosJefes))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DepartamentosJefes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDepartamentosJefesToMatchAllProperties(updatedDepartamentosJefes);
    }

    @Test
    void putNonExistingDepartamentosJefes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentosJefes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, departamentosJefes.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(departamentosJefes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DepartamentosJefes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDepartamentosJefes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentosJefes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(departamentosJefes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DepartamentosJefes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDepartamentosJefes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentosJefes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(departamentosJefes))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DepartamentosJefes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDepartamentosJefesWithPatch() throws Exception {
        // Initialize the database
        insertedDepartamentosJefes = departamentosJefesRepository.save(departamentosJefes).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departamentosJefes using partial update
        DepartamentosJefes partialUpdatedDepartamentosJefes = new DepartamentosJefes();
        partialUpdatedDepartamentosJefes.setId(departamentosJefes.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDepartamentosJefes.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedDepartamentosJefes))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DepartamentosJefes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartamentosJefesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDepartamentosJefes, departamentosJefes),
            getPersistedDepartamentosJefes(departamentosJefes)
        );
    }

    @Test
    void fullUpdateDepartamentosJefesWithPatch() throws Exception {
        // Initialize the database
        insertedDepartamentosJefes = departamentosJefesRepository.save(departamentosJefes).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departamentosJefes using partial update
        DepartamentosJefes partialUpdatedDepartamentosJefes = new DepartamentosJefes();
        partialUpdatedDepartamentosJefes.setId(departamentosJefes.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDepartamentosJefes.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedDepartamentosJefes))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DepartamentosJefes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartamentosJefesUpdatableFieldsEquals(
            partialUpdatedDepartamentosJefes,
            getPersistedDepartamentosJefes(partialUpdatedDepartamentosJefes)
        );
    }

    @Test
    void patchNonExistingDepartamentosJefes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentosJefes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, departamentosJefes.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(departamentosJefes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DepartamentosJefes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDepartamentosJefes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentosJefes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(departamentosJefes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DepartamentosJefes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDepartamentosJefes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departamentosJefes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(departamentosJefes))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DepartamentosJefes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDepartamentosJefes() {
        // Initialize the database
        insertedDepartamentosJefes = departamentosJefesRepository.save(departamentosJefes).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the departamentosJefes
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, departamentosJefes.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return departamentosJefesRepository.count().block();
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

    protected DepartamentosJefes getPersistedDepartamentosJefes(DepartamentosJefes departamentosJefes) {
        return departamentosJefesRepository.findById(departamentosJefes.getId()).block();
    }

    protected void assertPersistedDepartamentosJefesToMatchAllProperties(DepartamentosJefes expectedDepartamentosJefes) {
        // Test fails because reactive api returns an empty object instead of null
        // assertDepartamentosJefesAllPropertiesEquals(expectedDepartamentosJefes, getPersistedDepartamentosJefes(expectedDepartamentosJefes));
        assertDepartamentosJefesUpdatableFieldsEquals(
            expectedDepartamentosJefes,
            getPersistedDepartamentosJefes(expectedDepartamentosJefes)
        );
    }

    protected void assertPersistedDepartamentosJefesToMatchUpdatableProperties(DepartamentosJefes expectedDepartamentosJefes) {
        // Test fails because reactive api returns an empty object instead of null
        // assertDepartamentosJefesAllUpdatablePropertiesEquals(expectedDepartamentosJefes, getPersistedDepartamentosJefes(expectedDepartamentosJefes));
        assertDepartamentosJefesUpdatableFieldsEquals(
            expectedDepartamentosJefes,
            getPersistedDepartamentosJefes(expectedDepartamentosJefes)
        );
    }
}
