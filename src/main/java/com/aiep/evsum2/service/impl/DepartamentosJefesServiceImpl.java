package com.aiep.evsum2.service.impl;

import com.aiep.evsum2.domain.DepartamentosJefes;
import com.aiep.evsum2.repository.DepartamentosJefesRepository;
import com.aiep.evsum2.service.DepartamentosJefesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.aiep.evsum2.domain.DepartamentosJefes}.
 */
@Service
@Transactional
public class DepartamentosJefesServiceImpl implements DepartamentosJefesService {

    private static final Logger LOG = LoggerFactory.getLogger(DepartamentosJefesServiceImpl.class);

    private final DepartamentosJefesRepository departamentosJefesRepository;

    public DepartamentosJefesServiceImpl(DepartamentosJefesRepository departamentosJefesRepository) {
        this.departamentosJefesRepository = departamentosJefesRepository;
    }

    @Override
    public Mono<DepartamentosJefes> save(DepartamentosJefes departamentosJefes) {
        LOG.debug("Request to save DepartamentosJefes : {}", departamentosJefes);
        return departamentosJefesRepository.save(departamentosJefes);
    }

    @Override
    public Mono<DepartamentosJefes> update(DepartamentosJefes departamentosJefes) {
        LOG.debug("Request to update DepartamentosJefes : {}", departamentosJefes);
        return departamentosJefesRepository.save(departamentosJefes);
    }

    @Override
    public Mono<DepartamentosJefes> partialUpdate(DepartamentosJefes departamentosJefes) {
        LOG.debug("Request to partially update DepartamentosJefes : {}", departamentosJefes);

        return departamentosJefesRepository.findById(departamentosJefes.getId()).flatMap(departamentosJefesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<DepartamentosJefes> findAll() {
        LOG.debug("Request to get all DepartamentosJefes");
        return departamentosJefesRepository.findAll();
    }

    public Flux<DepartamentosJefes> findAllWithEagerRelationships(Pageable pageable) {
        return departamentosJefesRepository.findAllWithEagerRelationships(pageable);
    }

    public Mono<Long> countAll() {
        return departamentosJefesRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<DepartamentosJefes> findOne(Long id) {
        LOG.debug("Request to get DepartamentosJefes : {}", id);
        return departamentosJefesRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete DepartamentosJefes : {}", id);
        return departamentosJefesRepository.deleteById(id);
    }
}
