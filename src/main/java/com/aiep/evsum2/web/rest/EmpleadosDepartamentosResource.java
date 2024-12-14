package com.aiep.evsum2.web.rest;

import com.aiep.evsum2.domain.EmpleadosDepartamentos;
import com.aiep.evsum2.repository.EmpleadosDepartamentosRepository;
import com.aiep.evsum2.service.EmpleadosDepartamentosService;
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
 * REST controller for managing {@link com.aiep.evsum2.domain.EmpleadosDepartamentos}.
 */
@RestController
@RequestMapping("/api/empleados-departamentos")
public class EmpleadosDepartamentosResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmpleadosDepartamentosResource.class);

    private static final String ENTITY_NAME = "empleadosDepartamentos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmpleadosDepartamentosService empleadosDepartamentosService;

    private final EmpleadosDepartamentosRepository empleadosDepartamentosRepository;

    public EmpleadosDepartamentosResource(
        EmpleadosDepartamentosService empleadosDepartamentosService,
        EmpleadosDepartamentosRepository empleadosDepartamentosRepository
    ) {
        this.empleadosDepartamentosService = empleadosDepartamentosService;
        this.empleadosDepartamentosRepository = empleadosDepartamentosRepository;
    }

    /**
     * {@code POST  /empleados-departamentos} : Create a new empleadosDepartamentos.
     *
     * @param empleadosDepartamentos the empleadosDepartamentos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new empleadosDepartamentos, or with status {@code 400 (Bad Request)} if the empleadosDepartamentos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<EmpleadosDepartamentos>> createEmpleadosDepartamentos(
        @RequestBody EmpleadosDepartamentos empleadosDepartamentos
    ) throws URISyntaxException {
        LOG.debug("REST request to save EmpleadosDepartamentos : {}", empleadosDepartamentos);
        if (empleadosDepartamentos.getId() != null) {
            throw new BadRequestAlertException("A new empleadosDepartamentos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return empleadosDepartamentosService
            .save(empleadosDepartamentos)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/empleados-departamentos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /empleados-departamentos/:id} : Updates an existing empleadosDepartamentos.
     *
     * @param id the id of the empleadosDepartamentos to save.
     * @param empleadosDepartamentos the empleadosDepartamentos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empleadosDepartamentos,
     * or with status {@code 400 (Bad Request)} if the empleadosDepartamentos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the empleadosDepartamentos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EmpleadosDepartamentos>> updateEmpleadosDepartamentos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmpleadosDepartamentos empleadosDepartamentos
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmpleadosDepartamentos : {}, {}", id, empleadosDepartamentos);
        if (empleadosDepartamentos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empleadosDepartamentos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return empleadosDepartamentosRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return empleadosDepartamentosService
                    .update(empleadosDepartamentos)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /empleados-departamentos/:id} : Partial updates given fields of an existing empleadosDepartamentos, field will ignore if it is null
     *
     * @param id the id of the empleadosDepartamentos to save.
     * @param empleadosDepartamentos the empleadosDepartamentos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empleadosDepartamentos,
     * or with status {@code 400 (Bad Request)} if the empleadosDepartamentos is not valid,
     * or with status {@code 404 (Not Found)} if the empleadosDepartamentos is not found,
     * or with status {@code 500 (Internal Server Error)} if the empleadosDepartamentos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EmpleadosDepartamentos>> partialUpdateEmpleadosDepartamentos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmpleadosDepartamentos empleadosDepartamentos
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmpleadosDepartamentos partially : {}, {}", id, empleadosDepartamentos);
        if (empleadosDepartamentos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empleadosDepartamentos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return empleadosDepartamentosRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EmpleadosDepartamentos> result = empleadosDepartamentosService.partialUpdate(empleadosDepartamentos);

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
     * {@code GET  /empleados-departamentos} : get all the empleadosDepartamentos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of empleadosDepartamentos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<EmpleadosDepartamentos>> getAllEmpleadosDepartamentos(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all EmpleadosDepartamentos");
        return empleadosDepartamentosService.findAll().collectList();
    }

    /**
     * {@code GET  /empleados-departamentos} : get all the empleadosDepartamentos as a stream.
     * @return the {@link Flux} of empleadosDepartamentos.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<EmpleadosDepartamentos> getAllEmpleadosDepartamentosAsStream() {
        LOG.debug("REST request to get all EmpleadosDepartamentos as a stream");
        return empleadosDepartamentosService.findAll();
    }

    /**
     * {@code GET  /empleados-departamentos/:id} : get the "id" empleadosDepartamentos.
     *
     * @param id the id of the empleadosDepartamentos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the empleadosDepartamentos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmpleadosDepartamentos>> getEmpleadosDepartamentos(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmpleadosDepartamentos : {}", id);
        Mono<EmpleadosDepartamentos> empleadosDepartamentos = empleadosDepartamentosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(empleadosDepartamentos);
    }

    /**
     * {@code DELETE  /empleados-departamentos/:id} : delete the "id" empleadosDepartamentos.
     *
     * @param id the id of the empleadosDepartamentos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEmpleadosDepartamentos(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmpleadosDepartamentos : {}", id);
        return empleadosDepartamentosService
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
