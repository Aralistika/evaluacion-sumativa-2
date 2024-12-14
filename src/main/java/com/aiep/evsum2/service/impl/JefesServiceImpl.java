package com.aiep.evsum2.service.impl;

import com.aiep.evsum2.domain.Jefes;
import com.aiep.evsum2.repository.JefesRepository;
import com.aiep.evsum2.service.JefesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.aiep.evsum2.domain.Jefes}.
 */
@Service
@Transactional
public class JefesServiceImpl implements JefesService {

    private static final Logger LOG = LoggerFactory.getLogger(JefesServiceImpl.class);

    private final JefesRepository jefesRepository;

    public JefesServiceImpl(JefesRepository jefesRepository) {
        this.jefesRepository = jefesRepository;
    }

    @Override
    public Mono<Jefes> save(Jefes jefes) {
        LOG.debug("Request to save Jefes : {}", jefes);
        return jefesRepository.save(jefes);
    }

    @Override
    public Mono<Jefes> update(Jefes jefes) {
        LOG.debug("Request to update Jefes : {}", jefes);
        return jefesRepository.save(jefes);
    }

    @Override
    public Mono<Jefes> partialUpdate(Jefes jefes) {
        LOG.debug("Request to partially update Jefes : {}", jefes);

        return jefesRepository
            .findById(jefes.getId())
            .map(existingJefes -> {
                if (jefes.getNombrejefe() != null) {
                    existingJefes.setNombrejefe(jefes.getNombrejefe());
                }
                if (jefes.getTelefonojefe() != null) {
                    existingJefes.setTelefonojefe(jefes.getTelefonojefe());
                }

                return existingJefes;
            })
            .flatMap(jefesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Jefes> findAll() {
        LOG.debug("Request to get all Jefes");
        return jefesRepository.findAll();
    }

    public Mono<Long> countAll() {
        return jefesRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Jefes> findOne(Long id) {
        LOG.debug("Request to get Jefes : {}", id);
        return jefesRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Jefes : {}", id);
        return jefesRepository.deleteById(id);
    }
}
