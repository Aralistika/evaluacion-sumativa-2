package com.aiep.evsum2.web.rest;

import com.aiep.evsum2.domain.DepartamentosJefes;
import com.aiep.evsum2.repository.DepartamentosJefesRepository;
import com.aiep.evsum2.service.DepartamentosJefesService;
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
 * REST controller for managing {@link com.aiep.evsum2.domain.DepartamentosJefes}.
 */
@RestController
@RequestMapping("/api/departamentos-jefes")
public class DepartamentosJefesResource {

    private static final Logger LOG = LoggerFactory.getLogger(DepartamentosJefesResource.class);

    private static final String ENTITY_NAME = "departamentosJefes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartamentosJefesService departamentosJefesService;

    private final DepartamentosJefesRepository departamentosJefesRepository;

    public DepartamentosJefesResource(
        DepartamentosJefesService departamentosJefesService,
        DepartamentosJefesRepository departamentosJefesRepository
    ) {
        this.departamentosJefesService = departamentosJefesService;
        this.departamentosJefesRepository = departamentosJefesRepository;
    }

    /**
     * {@code POST  /departamentos-jefes} : Create a new departamentosJefes.
     *
     * @param departamentosJefes the departamentosJefes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departamentosJefes, or with status {@code 400 (Bad Request)} if the departamentosJefes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<DepartamentosJefes>> createDepartamentosJefes(@RequestBody DepartamentosJefes departamentosJefes)
        throws URISyntaxException {
        LOG.debug("REST request to save DepartamentosJefes : {}", departamentosJefes);
        if (departamentosJefes.getId() != null) {
            throw new BadRequestAlertException("A new departamentosJefes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return departamentosJefesService
            .save(departamentosJefes)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/departamentos-jefes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /departamentos-jefes/:id} : Updates an existing departamentosJefes.
     *
     * @param id the id of the departamentosJefes to save.
     * @param departamentosJefes the departamentosJefes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departamentosJefes,
     * or with status {@code 400 (Bad Request)} if the departamentosJefes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departamentosJefes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<DepartamentosJefes>> updateDepartamentosJefes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepartamentosJefes departamentosJefes
    ) throws URISyntaxException {
        LOG.debug("REST request to update DepartamentosJefes : {}, {}", id, departamentosJefes);
        if (departamentosJefes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departamentosJefes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return departamentosJefesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return departamentosJefesService
                    .update(departamentosJefes)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /departamentos-jefes/:id} : Partial updates given fields of an existing departamentosJefes, field will ignore if it is null
     *
     * @param id the id of the departamentosJefes to save.
     * @param departamentosJefes the departamentosJefes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departamentosJefes,
     * or with status {@code 400 (Bad Request)} if the departamentosJefes is not valid,
     * or with status {@code 404 (Not Found)} if the departamentosJefes is not found,
     * or with status {@code 500 (Internal Server Error)} if the departamentosJefes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DepartamentosJefes>> partialUpdateDepartamentosJefes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepartamentosJefes departamentosJefes
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DepartamentosJefes partially : {}, {}", id, departamentosJefes);
        if (departamentosJefes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departamentosJefes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return departamentosJefesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DepartamentosJefes> result = departamentosJefesService.partialUpdate(departamentosJefes);

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
     * {@code GET  /departamentos-jefes} : get all the departamentosJefes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departamentosJefes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<DepartamentosJefes>> getAllDepartamentosJefes(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all DepartamentosJefes");
        return departamentosJefesService.findAll().collectList();
    }

    /**
     * {@code GET  /departamentos-jefes} : get all the departamentosJefes as a stream.
     * @return the {@link Flux} of departamentosJefes.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<DepartamentosJefes> getAllDepartamentosJefesAsStream() {
        LOG.debug("REST request to get all DepartamentosJefes as a stream");
        return departamentosJefesService.findAll();
    }

    /**
     * {@code GET  /departamentos-jefes/:id} : get the "id" departamentosJefes.
     *
     * @param id the id of the departamentosJefes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departamentosJefes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DepartamentosJefes>> getDepartamentosJefes(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DepartamentosJefes : {}", id);
        Mono<DepartamentosJefes> departamentosJefes = departamentosJefesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departamentosJefes);
    }

    /**
     * {@code DELETE  /departamentos-jefes/:id} : delete the "id" departamentosJefes.
     *
     * @param id the id of the departamentosJefes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDepartamentosJefes(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DepartamentosJefes : {}", id);
        return departamentosJefesService
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
