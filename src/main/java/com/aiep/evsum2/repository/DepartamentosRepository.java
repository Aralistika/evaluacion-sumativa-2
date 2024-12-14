package com.aiep.evsum2.repository;

import com.aiep.evsum2.domain.Departamentos;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Departamentos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartamentosRepository extends ReactiveCrudRepository<Departamentos, Long>, DepartamentosRepositoryInternal {
    @Override
    <S extends Departamentos> Mono<S> save(S entity);

    @Override
    Flux<Departamentos> findAll();

    @Override
    Mono<Departamentos> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DepartamentosRepositoryInternal {
    <S extends Departamentos> Mono<S> save(S entity);

    Flux<Departamentos> findAllBy(Pageable pageable);

    Flux<Departamentos> findAll();

    Mono<Departamentos> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Departamentos> findAllBy(Pageable pageable, Criteria criteria);
}
