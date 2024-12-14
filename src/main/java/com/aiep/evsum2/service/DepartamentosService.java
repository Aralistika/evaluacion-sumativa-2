package com.aiep.evsum2.service;

import com.aiep.evsum2.domain.Departamentos;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.aiep.evsum2.domain.Departamentos}.
 */
public interface DepartamentosService {
    /**
     * Save a departamentos.
     *
     * @param departamentos the entity to save.
     * @return the persisted entity.
     */
    Mono<Departamentos> save(Departamentos departamentos);

    /**
     * Updates a departamentos.
     *
     * @param departamentos the entity to update.
     * @return the persisted entity.
     */
    Mono<Departamentos> update(Departamentos departamentos);

    /**
     * Partially updates a departamentos.
     *
     * @param departamentos the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Departamentos> partialUpdate(Departamentos departamentos);

    /**
     * Get all the departamentos.
     *
     * @return the list of entities.
     */
    Flux<Departamentos> findAll();

    /**
     * Returns the number of departamentos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" departamentos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Departamentos> findOne(Long id);

    /**
     * Delete the "id" departamentos.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
