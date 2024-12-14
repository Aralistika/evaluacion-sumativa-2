package com.aiep.evsum2.repository;

import com.aiep.evsum2.domain.Empleados;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Empleados entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpleadosRepository extends ReactiveCrudRepository<Empleados, Long>, EmpleadosRepositoryInternal {
    @Override
    <S extends Empleados> Mono<S> save(S entity);

    @Override
    Flux<Empleados> findAll();

    @Override
    Mono<Empleados> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EmpleadosRepositoryInternal {
    <S extends Empleados> Mono<S> save(S entity);

    Flux<Empleados> findAllBy(Pageable pageable);

    Flux<Empleados> findAll();

    Mono<Empleados> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Empleados> findAllBy(Pageable pageable, Criteria criteria);
}
