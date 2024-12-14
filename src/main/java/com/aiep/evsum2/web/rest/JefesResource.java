package com.aiep.evsum2.web.rest;

import com.aiep.evsum2.domain.Jefes;
import com.aiep.evsum2.repository.JefesRepository;
import com.aiep.evsum2.service.JefesService;
import com.aiep.evsum2.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.aiep.evsum2.domain.Jefes}.
 */
@RestController
@RequestMapping("/api/jefes")
public class JefesResource {

    private static final Logger LOG = LoggerFactory.getLogger(JefesResource.class);

    private static final String ENTITY_NAME = "jefes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JefesService jefesService;

    private final JefesRepository jefesRepository;

    public JefesResource(JefesService jefesService, JefesRepository jefesRepository) {
        this.jefesService = jefesService;
        this.jefesRepository = jefesRepository;
    }

    /**
     * {@code POST  /jefes} : Create a new jefes.
     *
     * @param jefes the jefes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jefes, or with status {@code 400 (Bad Request)} if the jefes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Jefes>> createJefes(@RequestBody Jefes jefes) throws URISyntaxException {
        LOG.debug("REST request to save Jefes : {}", jefes);
        if (jefes.getId() != null) {
            throw new BadRequestAlertException("A new jefes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return jefesService
            .save(jefes)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/jefes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /jefes/:id} : Updates an existing jefes.
     *
     * @param id the id of the jefes to save.
     * @param jefes the jefes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jefes,
     * or with status {@code 400 (Bad Request)} if the jefes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jefes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Jefes>> updateJefes(@PathVariable(value = "id", required = false) final Long id, @RequestBody Jefes jefes)
        throws URISyntaxException {
        LOG.debug("REST request to update Jefes : {}, {}", id, jefes);
        if (jefes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jefes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return jefesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return jefesService
                    .update(jefes)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /jefes/:id} : Partial updates given fields of an existing jefes, field will ignore if it is null
     *
     * @param id the id of the jefes to save.
     * @param jefes the jefes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jefes,
     * or with status {@code 400 (Bad Request)} if the jefes is not valid,
     * or with status {@code 404 (Not Found)} if the jefes is not found,
     * or with status {@code 500 (Internal Server Error)} if the jefes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Jefes>> partialUpdateJefes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Jefes jefes
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Jefes partially : {}, {}", id, jefes);
        if (jefes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jefes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return jefesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Jefes> result = jefesService.partialUpdate(jefes);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /jefes} : get all the jefes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jefes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Jefes>> getAllJefes() {
        LOG.debug("REST request to get all Jefes");
        return jefesService.findAll().collectList();
    }

    /**
     * {@code GET  /jefes} : get all the jefes as a stream.
     * @return the {@link Flux} of jefes.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Jefes> getAllJefesAsStream() {
        LOG.debug("REST request to get all Jefes as a stream");
        return jefesService.findAll();
    }

    /**
     * {@code GET  /jefes/:id} : get the "id" jefes.
     *
     * @param id the id of the jefes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jefes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Jefes>> getJefes(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Jefes : {}", id);
        Mono<Jefes> jefes = jefesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jefes);
    }

    /**
     * {@code DELETE  /jefes/:id} : delete the "id" jefes.
     *
     * @param id the id of the jefes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteJefes(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Jefes : {}", id);
        return jefesService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
