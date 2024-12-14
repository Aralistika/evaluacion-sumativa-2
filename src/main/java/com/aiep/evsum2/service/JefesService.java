package com.aiep.evsum2.service;

import com.aiep.evsum2.domain.Jefes;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.aiep.evsum2.domain.Jefes}.
 */
public interface JefesService {
    /**
     * Save a jefes.
     *
     * @param jefes the entity to save.
     * @return the persisted entity.
     */
    Mono<Jefes> save(Jefes jefes);

    /**
     * Updates a jefes.
     *
     * @param jefes the entity to update.
     * @return the persisted entity.
     */
    Mono<Jefes> update(Jefes jefes);

    /**
     * Partially updates a jefes.
     *
     * @param jefes the entity to update partially.
     * @return the persisted entity.
     */
    Mono<Jefes> partialUpdate(Jefes jefes);

    /**
     * Get all the jefes.
     *
     * @return the list of entities.
     */
    Flux<Jefes> findAll();

    /**
     * Returns the number of jefes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" jefes.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<Jefes> findOne(Long id);

    /**
     * Delete the "id" jefes.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
