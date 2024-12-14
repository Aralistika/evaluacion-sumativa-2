package com.aiep.evsum2.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DepartamentosTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Departamentos getDepartamentosSample1() {
        return new Departamentos().id(1L).nombredepartamento("nombredepartamento1").ubicaciondepartamento("ubicaciondepartamento1");
    }

    public static Departamentos getDepartamentosSample2() {
        return new Departamentos().id(2L).nombredepartamento("nombredepartamento2").ubicaciondepartamento("ubicaciondepartamento2");
    }

    public static Departamentos getDepartamentosRandomSampleGenerator() {
        return new Departamentos()
            .id(longCount.incrementAndGet())
            .nombredepartamento(UUID.randomUUID().toString())
            .ubicaciondepartamento(UUID.randomUUID().toString());
    }
}
