package com.aiep.evsum2.repository;

import com.aiep.evsum2.domain.DepartamentosJefes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the DepartamentosJefes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartamentosJefesRepository
    extends ReactiveCrudRepository<DepartamentosJefes, Long>, DepartamentosJefesRepositoryInternal {
    @Override
    Mono<DepartamentosJefes> findOneWithEagerRelationships(Long id);

    @Override
    Flux<DepartamentosJefes> findAllWithEagerRelationships();

    @Override
    Flux<DepartamentosJefes> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM departamentos_jefes entity WHERE entity.departamentos_id = :id")
    Flux<DepartamentosJefes> findByDepartamentos(Long id);

    @Query("SELECT * FROM departamentos_jefes entity WHERE entity.departamentos_id IS NULL")
    Flux<DepartamentosJefes> findAllWhereDepartamentosIsNull();

    @Query("SELECT * FROM departamentos_jefes entity WHERE entity.jefes_id = :id")
    Flux<DepartamentosJefes> findByJefes(Long id);

    @Query("SELECT * FROM departamentos_jefes entity WHERE entity.jefes_id IS NULL")
    Flux<DepartamentosJefes> findAllWhereJefesIsNull();

    @Override
    <S extends DepartamentosJefes> Mono<S> save(S entity);

    @Override
    Flux<DepartamentosJefes> findAll();

    @Override
    Mono<DepartamentosJefes> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DepartamentosJefesRepositoryInternal {
    <S extends DepartamentosJefes> Mono<S> save(S entity);

    Flux<DepartamentosJefes> findAllBy(Pageable pageable);

    Flux<DepartamentosJefes> findAll();

    Mono<DepartamentosJefes> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DepartamentosJefes> findAllBy(Pageable pageable, Criteria criteria);

    Mono<DepartamentosJefes> findOneWithEagerRelationships(Long id);

    Flux<DepartamentosJefes> findAllWithEagerRelationships();

    Flux<DepartamentosJefes> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
