package com.aiep.evsum2.repository;

import com.aiep.evsum2.domain.EmpleadosDepartamentos;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the EmpleadosDepartamentos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpleadosDepartamentosRepository
    extends ReactiveCrudRepository<EmpleadosDepartamentos, Long>, EmpleadosDepartamentosRepositoryInternal {
    @Override
    Mono<EmpleadosDepartamentos> findOneWithEagerRelationships(Long id);

    @Override
    Flux<EmpleadosDepartamentos> findAllWithEagerRelationships();

    @Override
    Flux<EmpleadosDepartamentos> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM empleados_departamentos entity WHERE entity.empleados_id = :id")
    Flux<EmpleadosDepartamentos> findByEmpleados(Long id);

    @Query("SELECT * FROM empleados_departamentos entity WHERE entity.empleados_id IS NULL")
    Flux<EmpleadosDepartamentos> findAllWhereEmpleadosIsNull();

    @Query("SELECT * FROM empleados_departamentos entity WHERE entity.departamentos_id = :id")
    Flux<EmpleadosDepartamentos> findByDepartamentos(Long id);

    @Query("SELECT * FROM empleados_departamentos entity WHERE entity.departamentos_id IS NULL")
    Flux<EmpleadosDepartamentos> findAllWhereDepartamentosIsNull();

    @Override
    <S extends EmpleadosDepartamentos> Mono<S> save(S entity);

    @Override
    Flux<EmpleadosDepartamentos> findAll();

    @Override
    Mono<EmpleadosDepartamentos> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EmpleadosDepartamentosRepositoryInternal {
    <S extends EmpleadosDepartamentos> Mono<S> save(S entity);

    Flux<EmpleadosDepartamentos> findAllBy(Pageable pageable);

    Flux<EmpleadosDepartamentos> findAll();

    Mono<EmpleadosDepartamentos> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EmpleadosDepartamentos> findAllBy(Pageable pageable, Criteria criteria);

    Mono<EmpleadosDepartamentos> findOneWithEagerRelationships(Long id);

    Flux<EmpleadosDepartamentos> findAllWithEagerRelationships();

    Flux<EmpleadosDepartamentos> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
