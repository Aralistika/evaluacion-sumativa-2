package com.aiep.evsum2.service.impl;

import com.aiep.evsum2.domain.Departamentos;
import com.aiep.evsum2.repository.DepartamentosRepository;
import com.aiep.evsum2.service.DepartamentosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.aiep.evsum2.domain.Departamentos}.
 */
@Service
@Transactional
public class DepartamentosServiceImpl implements DepartamentosService {

    private static final Logger LOG = LoggerFactory.getLogger(DepartamentosServiceImpl.class);

    private final DepartamentosRepository departamentosRepository;

    public DepartamentosServiceImpl(DepartamentosRepository departamentosRepository) {
        this.departamentosRepository = departamentosRepository;
    }

    @Override
    public Mono<Departamentos> save(Departamentos departamentos) {
        LOG.debug("Request to save Departamentos : {}", departamentos);
        return departamentosRepository.save(departamentos);
    }

    @Override
    public Mono<Departamentos> update(Departamentos departamentos) {
        LOG.debug("Request to update Departamentos : {}", departamentos);
        return departamentosRepository.save(departamentos);
    }

    @Override
    public Mono<Departamentos> partialUpdate(Departamentos departamentos) {
        LOG.debug("Request to partially update Departamentos : {}", departamentos);

        return departamentosRepository
            .findById(departamentos.getId())
            .map(existingDepartamentos -> {
                if (departamentos.getNombredepartamento() != null) {
                    existingDepartamentos.setNombredepartamento(departamentos.getNombredepartamento());
                }
                if (departamentos.getUbicaciondepartamento() != null) {
                    existingDepartamentos.setUbicaciondepartamento(departamentos.getUbicaciondepartamento());
                }
                if (departamentos.getPresupuestodepartamento() != null) {
                    existingDepartamentos.setPresupuestodepartamento(departamentos.getPresupuestodepartamento());
                }

                return existingDepartamentos;
            })
            .flatMap(departamentosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Departamentos> findAll() {
        LOG.debug("Request to get all Departamentos");
        return departamentosRepository.findAll();
    }

    public Mono<Long> countAll() {
        return departamentosRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Departamentos> findOne(Long id) {
        LOG.debug("Request to get Departamentos : {}", id);
        return departamentosRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Departamentos : {}", id);
        return departamentosRepository.deleteById(id);
    }
}
