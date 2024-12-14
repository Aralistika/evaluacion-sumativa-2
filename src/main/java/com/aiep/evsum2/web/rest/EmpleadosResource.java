package com.aiep.evsum2.web.rest;

import com.aiep.evsum2.domain.Empleados;
import com.aiep.evsum2.repository.EmpleadosRepository;
import com.aiep.evsum2.service.EmpleadosService;
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
 * REST controller for managing {@link com.aiep.evsum2.domain.Empleados}.
 */
@RestController
@RequestMapping("/api/empleados")
public class EmpleadosResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmpleadosResource.class);

    private static final String ENTITY_NAME = "empleados";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmpleadosService empleadosService;

    private final EmpleadosRepository empleadosRepository;

    public EmpleadosResource(EmpleadosService empleadosService, EmpleadosRepository empleadosRepository) {
        this.empleadosService = empleadosService;
        this.empleadosRepository = empleadosRepository;
    }

    /**
     * {@code POST  /empleados} : Create a new empleados.
     *
     * @param empleados the empleados to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new empleados, or with status {@code 400 (Bad Request)} if the empleados has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Empleados>> createEmpleados(@RequestBody Empleados empleados) throws URISyntaxException {
        LOG.debug("REST request to save Empleados : {}", empleados);
        if (empleados.getId() != null) {
            throw new BadRequestAlertException("A new empleados cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return empleadosService
            .save(empleados)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/empleados/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /empleados/:id} : Updates an existing empleados.
     *
     * @param id the id of the empleados to save.
     * @param empleados the empleados to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empleados,
     * or with status {@code 400 (Bad Request)} if the empleados is not valid,
     * or with status {@code 500 (Internal Server Error)} if the empleados couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Empleados>> updateEmpleados(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Empleados empleados
    ) throws URISyntaxException {
        LOG.debug("REST request to update Empleados : {}, {}", id, empleados);
        if (empleados.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empleados.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return empleadosRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return empleadosService
                    .update(empleados)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /empleados/:id} : Partial updates given fields of an existing empleados, field will ignore if it is null
     *
     * @param id the id of the empleados to save.
     * @param empleados the empleados to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empleados,
     * or with status {@code 400 (Bad Request)} if the empleados is not valid,
     * or with status {@code 404 (Not Found)} if the empleados is not found,
     * or with status {@code 500 (Internal Server Error)} if the empleados couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Empleados>> partialUpdateEmpleados(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Empleados empleados
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Empleados partially : {}, {}", id, empleados);
        if (empleados.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empleados.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return empleadosRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Empleados> result = empleadosService.partialUpdate(empleados);

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
     * {@code GET  /empleados} : get all the empleados.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of empleados in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Empleados>> getAllEmpleados() {
        LOG.debug("REST request to get all Empleados");
        return empleadosService.findAll().collectList();
    }

    /**
     * {@code GET  /empleados} : get all the empleados as a stream.
     * @return the {@link Flux} of empleados.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Empleados> getAllEmpleadosAsStream() {
        LOG.debug("REST request to get all Empleados as a stream");
        return empleadosService.findAll();
    }

    /**
     * {@code GET  /empleados/:id} : get the "id" empleados.
     *
     * @param id the id of the empleados to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the empleados, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Empleados>> getEmpleados(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Empleados : {}", id);
        Mono<Empleados> empleados = empleadosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(empleados);
    }

    /**
     * {@code DELETE  /empleados/:id} : delete the "id" empleados.
     *
     * @param id the id of the empleados to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEmpleados(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Empleados : {}", id);
        return empleadosService
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
