package com.aiep.evsum2.service.impl;

import com.aiep.evsum2.domain.Empleados;
import com.aiep.evsum2.repository.EmpleadosRepository;
import com.aiep.evsum2.service.EmpleadosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.aiep.evsum2.domain.Empleados}.
 */
@Service
@Transactional
public class EmpleadosServiceImpl implements EmpleadosService {

    private static final Logger LOG = LoggerFactory.getLogger(EmpleadosServiceImpl.class);

    private final EmpleadosRepository empleadosRepository;

    public EmpleadosServiceImpl(EmpleadosRepository empleadosRepository) {
        this.empleadosRepository = empleadosRepository;
    }

    @Override
    public Mono<Empleados> save(Empleados empleados) {
        LOG.debug("Request to save Empleados : {}", empleados);
        return empleadosRepository.save(empleados);
    }

    @Override
    public Mono<Empleados> update(Empleados empleados) {
        LOG.debug("Request to update Empleados : {}", empleados);
        return empleadosRepository.save(empleados);
    }

    @Override
    public Mono<Empleados> partialUpdate(Empleados empleados) {
        LOG.debug("Request to partially update Empleados : {}", empleados);

        return empleadosRepository
            .findById(empleados.getId())
            .map(existingEmpleados -> {
                if (empleados.getNombreempleado() != null) {
                    existingEmpleados.setNombreempleado(empleados.getNombreempleado());
                }
                if (empleados.getApellidoempleado() != null) {
                    existingEmpleados.setApellidoempleado(empleados.getApellidoempleado());
                }
                if (empleados.getTelefonoempleado() != null) {
                    existingEmpleados.setTelefonoempleado(empleados.getTelefonoempleado());
                }
                if (empleados.getCorreoempleado() != null) {
                    existingEmpleados.setCorreoempleado(empleados.getCorreoempleado());
                }

                return existingEmpleados;
            })
            .flatMap(empleadosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Empleados> findAll() {
        LOG.debug("Request to get all Empleados");
        return empleadosRepository.findAll();
    }

    public Mono<Long> countAll() {
        return empleadosRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Empleados> findOne(Long id) {
        LOG.debug("Request to get Empleados : {}", id);
        return empleadosRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Empleados : {}", id);
        return empleadosRepository.deleteById(id);
    }
}
