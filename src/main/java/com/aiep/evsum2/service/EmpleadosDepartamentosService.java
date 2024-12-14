package com.aiep.evsum2.service;

import com.aiep.evsum2.domain.EmpleadosDepartamentos;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.aiep.evsum2.domain.EmpleadosDepartamentos}.
 */
public interface EmpleadosDepartamentosService {
    /**
     * Save a empleadosDepartamentos.
     *
     * @param empleadosDepartamentos the entity to save.
     * @return the persisted entity.
     */
    Mono<EmpleadosDepartamentos> save(EmpleadosDepartamentos empleadosDepartamentos);

    /**
     * Updates a empleadosDepartamentos.
     *
     * @param empleadosDepartamentos the entity to update.
     * @return the persisted entity.
     */
    Mono<EmpleadosDepartamentos> update(EmpleadosDepartamentos empleadosDepartamentos);

    /**
     * Partially updates a empleadosDepartamentos.
     *
     * @param empleadosDepartamentos the entity to update partially.
     * @return the persisted entity.
     */
    Mono<EmpleadosDepartamentos> partialUpdate(EmpleadosDepartamentos empleadosDepartamentos);

    /**
     * Get all the empleadosDepartamentos.
     *
     * @return the list of entities.
     */
    Flux<EmpleadosDepartamentos> findAll();

    /**
     * Get all the empleadosDepartamentos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EmpleadosDepartamentos> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of empleadosDepartamentos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" empleadosDepartamentos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<EmpleadosDepartamentos> findOne(Long id);

    /**
     * Delete the "id" empleadosDepartamentos.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
