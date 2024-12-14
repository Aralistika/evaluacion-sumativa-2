package com.aiep.evsum2.service.impl;

import com.aiep.evsum2.domain.EmpleadosDepartamentos;
import com.aiep.evsum2.repository.EmpleadosDepartamentosRepository;
import com.aiep.evsum2.service.EmpleadosDepartamentosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.aiep.evsum2.domain.EmpleadosDepartamentos}.
 */
@Service
@Transactional
public class EmpleadosDepartamentosServiceImpl implements EmpleadosDepartamentosService {

    private static final Logger LOG = LoggerFactory.getLogger(EmpleadosDepartamentosServiceImpl.class);

    private final EmpleadosDepartamentosRepository empleadosDepartamentosRepository;

    public EmpleadosDepartamentosServiceImpl(EmpleadosDepartamentosRepository empleadosDepartamentosRepository) {
        this.empleadosDepartamentosRepository = empleadosDepartamentosRepository;
    }

    @Override
    public Mono<EmpleadosDepartamentos> save(EmpleadosDepartamentos empleadosDepartamentos) {
        LOG.debug("Request to save EmpleadosDepartamentos : {}", empleadosDepartamentos);
        return empleadosDepartamentosRepository.save(empleadosDepartamentos);
    }

    @Override
    public Mono<EmpleadosDepartamentos> update(EmpleadosDepartamentos empleadosDepartamentos) {
        LOG.debug("Request to update EmpleadosDepartamentos : {}", empleadosDepartamentos);
        return empleadosDepartamentosRepository.save(empleadosDepartamentos);
    }

    @Override
    public Mono<EmpleadosDepartamentos> partialUpdate(EmpleadosDepartamentos empleadosDepartamentos) {
        LOG.debug("Request to partially update EmpleadosDepartamentos : {}", empleadosDepartamentos);

        return empleadosDepartamentosRepository.findById(empleadosDepartamentos.getId()).flatMap(empleadosDepartamentosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<EmpleadosDepartamentos> findAll() {
        LOG.debug("Request to get all EmpleadosDepartamentos");
        return empleadosDepartamentosRepository.findAll();
    }

    public Flux<EmpleadosDepartamentos> findAllWithEagerRelationships(Pageable pageable) {
        return empleadosDepartamentosRepository.findAllWithEagerRelationships(pageable);
    }

    public Mono<Long> countAll() {
        return empleadosDepartamentosRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<EmpleadosDepartamentos> findOne(Long id) {
        LOG.debug("Request to get EmpleadosDepartamentos : {}", id);
        return empleadosDepartamentosRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete EmpleadosDepartamentos : {}", id);
        return empleadosDepartamentosRepository.deleteById(id);
    }
}
