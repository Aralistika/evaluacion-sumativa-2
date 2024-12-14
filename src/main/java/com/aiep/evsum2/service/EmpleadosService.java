package com.aiep.evsum2.service;

import com.aiep.evsum2.domain.Empleados;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.aiep.evsum2.domain.Empleados}.
 */
public interface EmpleadosService {
    /**
     * Save a empleados.
     *
     * @param empleados the entity to save.
     * @return the persisted entity.
     */
    Mono<Empleados> save(Empleados empleados);

    /**
     * Updates a empleados.
     *
     * @param empleados the entity to update.
     * @return the persisted entity.
     */
    Mono<Empleados> update(Empleados empleados);

    /**
     * Partially updates a empleados.
     *
     * @param empleados the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Empleados> partialUpdate(Empleados empleados);

    /**
     * Get all the empleados.
     *
     * @return the list of entities.
     */
    Flux<Empleados> findAll();

    /**
     * Returns the number of empleados available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" empleados.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Empleados> findOne(Long id);

    /**
     * Delete the "id" empleados.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
