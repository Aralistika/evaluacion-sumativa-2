package com.aiep.evsum2.service;

import com.aiep.evsum2.domain.DepartamentosJefes;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.aiep.evsum2.domain.DepartamentosJefes}.
 */
public interface DepartamentosJefesService {
    /**
     * Save a departamentosJefes.
     *
     * @param departamentosJefes the entity to save.
     * @return the persisted entity.
     */
    Mono<DepartamentosJefes> save(DepartamentosJefes departamentosJefes);

    /**
     * Updates a departamentosJefes.
     *
     * @param departamentosJefes the entity to update.
     * @return the persisted entity.
     */
    Mono<DepartamentosJefes> update(DepartamentosJefes departamentosJefes);

    /**
     * Partially updates a departamentosJefes.
     *
     * @param departamentosJefes the entity to update partially.
     * @return the persisted entity.
     */
    Mono<DepartamentosJefes> partialUpdate(DepartamentosJefes departamentosJefes);

    /**
     * Get all the departamentosJefes.
     *
     * @return the list of entities.
     */
    Flux<DepartamentosJefes> findAll();

    /**
     * Get all the departamentosJefes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<DepartamentosJefes> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of departamentosJefes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" departamentosJefes.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<DepartamentosJefes> findOne(Long id);

    /**
     * Delete the "id" departamentosJefes.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
