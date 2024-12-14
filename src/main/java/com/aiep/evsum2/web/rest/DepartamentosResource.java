package com.aiep.evsum2.web.rest;

import com.aiep.evsum2.domain.Departamentos;
import com.aiep.evsum2.repository.DepartamentosRepository;
import com.aiep.evsum2.service.DepartamentosService;
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
 * REST controller for managing {@link com.aiep.evsum2.domain.Departamentos}.
 */
@RestController
@RequestMapping("/api/departamentos")
public class DepartamentosResource {

    private static final Logger LOG = LoggerFactory.getLogger(DepartamentosResource.class);

    private static final String ENTITY_NAME = "departamentos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartamentosService departamentosService;

    private final DepartamentosRepository departamentosRepository;

    public DepartamentosResource(DepartamentosService departamentosService, DepartamentosRepository departamentosRepository) {
        this.departamentosService = departamentosService;
        this.departamentosRepository = departamentosRepository;
    }

    /**
     * {@code POST  /departamentos} : Create a new departamentos.
     *
     * @param departamentos the departamentos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departamentos, or with status {@code 400 (Bad Request)} if the departamentos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Departamentos>> createDepartamentos(@RequestBody Departamentos departamentos) throws URISyntaxException {
        LOG.debug("REST request to save Departamentos : {}", departamentos);
        if (departamentos.getId() != null) {
            throw new BadRequestAlertException("A new departamentos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return departamentosService
            .save(departamentos)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/departamentos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /departamentos/:id} : Updates an existing departamentos.
     *
     * @param id the id of the departamentos to save.
     * @param departamentos the departamentos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departamentos,
     * or with status {@code 400 (Bad Request)} if the departamentos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departamentos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Departamentos>> updateDepartamentos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Departamentos departamentos
    ) throws URISyntaxException {
        LOG.debug("REST request to update Departamentos : {}, {}", id, departamentos);
        if (departamentos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departamentos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return departamentosRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return departamentosService
                    .update(departamentos)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /departamentos/:id} : Partial updates given fields of an existing departamentos, field will ignore if it is null
     *
     * @param id the id of the departamentos to save.
     * @param departamentos the departamentos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departamentos,
     * or with status {@code 400 (Bad Request)} if the departamentos is not valid,
     * or with status {@code 404 (Not Found)} if the departamentos is not found,
     * or with status {@code 500 (Internal Server Error)} if the departamentos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Departamentos>> partialUpdateDepartamentos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Departamentos departamentos
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Departamentos partially : {}, {}", id, departamentos);
        if (departamentos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departamentos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return departamentosRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Departamentos> result = departamentosService.partialUpdate(departamentos);

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
     * {@code GET  /departamentos} : get all the departamentos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departamentos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Departamentos>> getAllDepartamentos() {
        LOG.debug("REST request to get all Departamentos");
        return departamentosService.findAll().collectList();
    }

    /**
     * {@code GET  /departamentos} : get all the departamentos as a stream.
     * @return the {@link Flux} of departamentos.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Departamentos> getAllDepartamentosAsStream() {
        LOG.debug("REST request to get all Departamentos as a stream");
        return departamentosService.findAll();
    }

    /**
     * {@code GET  /departamentos/:id} : get the "id" departamentos.
     *
     * @param id the id of the departamentos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departamentos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Departamentos>> getDepartamentos(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Departamentos : {}", id);
        Mono<Departamentos> departamentos = departamentosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departamentos);
    }

    /**
     * {@code DELETE  /departamentos/:id} : delete the "id" departamentos.
     *
     * @param id the id of the departamentos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDepartamentos(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Departamentos : {}", id);
        return departamentosService
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
