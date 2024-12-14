package com.aiep.evsum2.web.rest;

import static com.aiep.evsum2.domain.JefesAsserts.*;
import static com.aiep.evsum2.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.aiep.evsum2.IntegrationTest;
import com.aiep.evsum2.domain.Jefes;
import com.aiep.evsum2.repository.EntityManager;
import com.aiep.evsum2.repository.JefesRepository;
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
 * Integration tests for the {@link JefesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class JefesResourceIT {

    private static final String DEFAULT_NOMBREJEFE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBREJEFE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONOJEFE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONOJEFE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/jefes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JefesRepository jefesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Jefes jefes;

    private Jefes insertedJefes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jefes createEntity() {
        return new Jefes().nombrejefe(DEFAULT_NOMBREJEFE).telefonojefe(DEFAULT_TELEFONOJEFE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jefes createUpdatedEntity() {
        return new Jefes().nombrejefe(UPDATED_NOMBREJEFE).telefonojefe(UPDATED_TELEFONOJEFE);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Jefes.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        jefes = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedJefes != null) {
            jefesRepository.delete(insertedJefes).block();
            insertedJefes = null;
        }
        deleteEntities(em);
    }

    @Test
    void createJefes() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Jefes
        var returnedJefes = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(jefes))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Jefes.class)
            .returnResult()
            .getResponseBody();

        // Validate the Jefes in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertJefesUpdatableFieldsEquals(returnedJefes, getPersistedJefes(returnedJefes));

        insertedJefes = returnedJefes;
    }

    @Test
    void createJefesWithExistingId() throws Exception {
        // Create the Jefes with an existing ID
        jefes.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(jefes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Jefes in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllJefesAsStream() {
        // Initialize the database
        jefesRepository.save(jefes).block();

        List<Jefes> jefesList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Jefes.class)
            .getResponseBody()
            .filter(jefes::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(jefesList).isNotNull();
        assertThat(jefesList).hasSize(1);
        Jefes testJefes = jefesList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertJefesAllPropertiesEquals(jefes, testJefes);
        assertJefesUpdatableFieldsEquals(jefes, testJefes);
    }

    @Test
    void getAllJefes() {
        // Initialize the database
        insertedJefes = jefesRepository.save(jefes).block();

        // Get all the jefesList
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
            .value(hasItem(jefes.getId().intValue()))
            .jsonPath("$.[*].nombrejefe")
            .value(hasItem(DEFAULT_NOMBREJEFE))
            .jsonPath("$.[*].telefonojefe")
            .value(hasItem(DEFAULT_TELEFONOJEFE));
    }

    @Test
    void getJefes() {
        // Initialize the database
        insertedJefes = jefesRepository.save(jefes).block();

        // Get the jefes
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, jefes.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(jefes.getId().intValue()))
            .jsonPath("$.nombrejefe")
            .value(is(DEFAULT_NOMBREJEFE))
            .jsonPath("$.telefonojefe")
            .value(is(DEFAULT_TELEFONOJEFE));
    }

    @Test
    void getNonExistingJefes() {
        // Get the jefes
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingJefes() throws Exception {
        // Initialize the database
        insertedJefes = jefesRepository.save(jefes).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jefes
        Jefes updatedJefes = jefesRepository.findById(jefes.getId()).block();
        updatedJefes.nombrejefe(UPDATED_NOMBREJEFE).telefonojefe(UPDATED_TELEFONOJEFE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedJefes.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedJefes))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Jefes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedJefesToMatchAllProperties(updatedJefes);
    }

    @Test
    void putNonExistingJefes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jefes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, jefes.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(jefes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Jefes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchJefes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jefes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(jefes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Jefes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamJefes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jefes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(jefes))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Jefes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateJefesWithPatch() throws Exception {
        // Initialize the database
        insertedJefes = jefesRepository.save(jefes).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jefes using partial update
        Jefes partialUpdatedJefes = new Jefes();
        partialUpdatedJefes.setId(jefes.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedJefes.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedJefes))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Jefes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJefesUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedJefes, jefes), getPersistedJefes(jefes));
    }

    @Test
    void fullUpdateJefesWithPatch() throws Exception {
        // Initialize the database
        insertedJefes = jefesRepository.save(jefes).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the jefes using partial update
        Jefes partialUpdatedJefes = new Jefes();
        partialUpdatedJefes.setId(jefes.getId());

        partialUpdatedJefes.nombrejefe(UPDATED_NOMBREJEFE).telefonojefe(UPDATED_TELEFONOJEFE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedJefes.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedJefes))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Jefes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJefesUpdatableFieldsEquals(partialUpdatedJefes, getPersistedJefes(partialUpdatedJefes));
    }

    @Test
    void patchNonExistingJefes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jefes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, jefes.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(jefes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Jefes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchJefes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jefes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(jefes))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Jefes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamJefes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        jefes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(jefes))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Jefes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteJefes() {
        // Initialize the database
        insertedJefes = jefesRepository.save(jefes).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the jefes
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, jefes.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return jefesRepository.count().block();
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

    protected Jefes getPersistedJefes(Jefes jefes) {
        return jefesRepository.findById(jefes.getId()).block();
    }

    protected void assertPersistedJefesToMatchAllProperties(Jefes expectedJefes) {
        // Test fails because reactive api returns an empty object instead of null
        // assertJefesAllPropertiesEquals(expectedJefes, getPersistedJefes(expectedJefes));
        assertJefesUpdatableFieldsEquals(expectedJefes, getPersistedJefes(expectedJefes));
    }

    protected void assertPersistedJefesToMatchUpdatableProperties(Jefes expectedJefes) {
        // Test fails because reactive api returns an empty object instead of null
        // assertJefesAllUpdatablePropertiesEquals(expectedJefes, getPersistedJefes(expectedJefes));
        assertJefesUpdatableFieldsEquals(expectedJefes, getPersistedJefes(expectedJefes));
    }
}
