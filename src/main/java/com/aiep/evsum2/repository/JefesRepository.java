package com.aiep.evsum2.repository;

import com.aiep.evsum2.domain.Jefes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Jefes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JefesRepository extends ReactiveCrudRepository<Jefes, Long>, JefesRepositoryInternal {
    @Override
    <S extends Jefes> Mono<S> save(S entity);

    @Override
    Flux<Jefes> findAll();

    @Override
    Mono<Jefes> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface JefesRepositoryInternal {
    <S extends Jefes> Mono<S> save(S entity);

    Flux<Jefes> findAllBy(Pageable pageable);

    Flux<Jefes> findAll();

    Mono<Jefes> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Jefes> findAllBy(Pageable pageable, Criteria criteria);
}
